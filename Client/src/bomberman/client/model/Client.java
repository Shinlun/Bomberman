package bomberman.client.model;

import bomberman.client.controller.Game;
import bomberman.client.elements.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.simple.JSONValue;

public class Client extends Thread {

    private static Client instance;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private Client() {
    }

    /**
     * Creates a unique instance of Client (Singleton)
     *
     * @return Instance of Client
     */
    public static synchronized Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }
        return instance;
    }

    public void connect(String host, int port) throws Exception {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.start();
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = this.in.readLine()) != null) {
                int space_pos = line.indexOf(" ");
                if (space_pos != -1) {
                    try {
                        Object obj = this.decodeData(line.substring(space_pos + 1));
                        this.execute(line.substring(0, space_pos), obj);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                JOptionPane.showMessageDialog(null, "Connexion perdue !");
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void execute(String command, Object obj) {
        System.out.println(command + " " + obj);
        try {
            if (command.equals("board_cols")) {
                Game.getInstance().getBoard().setCols(this.convertToInt(obj));

            } else if (command.equals("board")) {
                Game.getInstance().getBoard().setData((List<Map>) obj);

            } else if (command.equals("players")) {
                Game.getInstance().setPlayers((Map<String, Map>) obj);

            } else if (command.equals("add_player")) {
                int player_id = this.convertToInt(((List) obj).get(0));
                int x = this.convertToInt(((List) obj).get(1));
                int y = this.convertToInt(((List) obj).get(2));
                Game.getInstance().addPlayer(player_id, x, y);

            } else if (command.equals("del_player")) {
                Game.getInstance().delPlayer(this.convertToInt(obj));

            } else if (command.equals("reposition")) {
                int player_id = this.convertToInt(((List) obj).get(0));
                int x = this.convertToInt(((List) obj).get(1));
                int y = this.convertToInt(((List) obj).get(2));
                Game.getInstance().getPlayer(player_id).reposition(x, y);

            } else if (command.equals("move")) {
                int player_id = this.convertToInt(((List) obj).get(0));
                int x = this.convertToInt(((List) obj).get(1));
                int y = this.convertToInt(((List) obj).get(2));
                Game.getInstance().getPlayer(player_id).startMove(x, y);
            } else if(command.equals("drop_bomb")) {
                int x = this.convertToInt(((List) obj).get(0));
                int y = this.convertToInt(((List) obj).get(1));
                Game.getInstance().dropBomb(x, y);
            } else if(command.equals("burst_bomb")) {
                int x = this.convertToInt(((List) obj).get(0));
                int y = this.convertToInt(((List) obj).get(1));
                Game.getInstance().burstBomb(x, y);
            } else if(command.equals("add_element")) {
                Element element = Element.factory((Map) ((List) obj).get(0));
                int x = this.convertToInt(((List) obj).get(1));
                int y = this.convertToInt(((List) obj).get(2));
                Game.getInstance().addElement(element, x, y);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void send(String command, Object obj) {
        try {
            this.out.println(command + " " + this.encodeData(obj));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private String encodeData(Object data) throws Exception {
        StringWriter jsonOut = new StringWriter();
        JSONValue.writeJSONString(data, jsonOut);
        return jsonOut.toString();
    }

    private Object decodeData(String data) throws Exception {
        return JSONValue.parse(data);
    }

    public int convertToInt(Object n) throws Exception {
        if (n instanceof Integer) {
            return (Integer) n;
        } else if (n instanceof Long) {
            return ((Long) n).intValue();
        }
        throw new Exception(n + " not an Integer");
    }

    public void movePlayer(int diff_x, int diff_y) {
        List<Integer> obj = new ArrayList();
        obj.add(diff_x);
        obj.add(diff_y);
        this.send("move", obj);
    }

    public void dropBomb(){
        this.send("drop_bomb", "");
    }
}
