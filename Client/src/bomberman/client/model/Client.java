package bomberman.client.model;

import bomberman.client.controller.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
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
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                JOptionPane.showMessageDialog(null, "Connexion perdue !");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void execute(String command, Object obj) {
        try {
            if (command.equals("board_cols")) {
                Game.getInstance().getBoard().setCols((Integer) obj);
            } else if (command.equals("board")) {
                Game.getInstance().getBoard().setData((List<Map>) obj);
            }
        } catch (Exception e) {
        }
    }

    private void send(String command, Object obj) {
        try {
            this.out.println(command + " " + this.encodeData(obj));
        } catch (Exception e) {
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

    public List getBoard() throws Exception {
        // TODO
        return null;
    }
}
