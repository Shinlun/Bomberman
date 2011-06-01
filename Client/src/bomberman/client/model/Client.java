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
                Game.getInstance().getBoard().setCols(convertToInt(obj));

            } else if (command.equals("board")) {
                Game.getInstance().getBoard().setData((List<Map>) obj);

            } else if (command.equals("players")) {
                Game.getInstance().setPlayers((List<Map>) obj);

            } else if (command.equals("add_player")) {
                Game.getInstance().addPlayer((Map<String, Object>) obj);

            } else if (command.equals("del_player")) {
                Game.getInstance().delPlayer(convertToInt(obj));

            } else if (command.equals("reposition")) {
                int player_id = convertToInt(((List) obj).get(0));
                int index = convertToInt(((List) obj).get(1));
                Game.getInstance().getPlayer(player_id).reposition(index);

            } else if (command.equals("move")) {
                int player_id = convertToInt(((List) obj).get(0));
                int index = convertToInt(((List) obj).get(1));
                Game.getInstance().getPlayer(player_id).startMove(index);

            } else if (command.equals("burst_bomb")) {
                ArrayList<Integer> fire = new ArrayList<Integer>();
                for (Object i : (List) obj) {
                    fire.add(convertToInt(i));
                }
                Game.getInstance().getBoard().addFire(fire);

            } else if (command.equals("add_element")) {
                Game.getInstance().getBoard().setElement(Element.factory((Map) obj));

            } else if (command.equals("del_element")) {
                Game.getInstance().getBoard().delElement(convertToInt(obj));
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

    public static int convertToInt(Object n) throws Exception {
        if (n instanceof Integer) {
            return (Integer) n;
        } else if (n instanceof Long) {
            return ((Long) n).intValue();
        }
        throw new Exception(n + " not an Integer");
    }

    public static double convertToDouble(Object n) throws Exception {
        if (n instanceof Double) {
            return (Double) n;
        } else if (n instanceof Long) {
            return ((Long) n).doubleValue();
        } else if (n instanceof Integer) {
            return ((Integer) n).doubleValue();
        }
        throw new Exception(n + " not an Double");
    }

    public void movePlayer(int index) {
        this.send("move", index);
    }

    public void dropBomb() {
        this.send("drop_bomb", "");
    }
}
