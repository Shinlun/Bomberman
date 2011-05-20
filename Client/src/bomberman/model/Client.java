package bomberman.model;

import bomberman.controller.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.simple.JSONValue;

public class Client extends Thread {

    private static Client instance;
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

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
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        this.start();
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                int space_pos = line.indexOf(" ");
                if (space_pos != -1) {
                    try {
                        Object obj = this.decodeData(line.substring(space_pos + 1));
                        this.executeCommand(line.substring(0, space_pos), obj);
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

    private void executeCommand(String command, Object obj) {
        try {
            if (command.equals("board")) {
                Game.getInstance().getBoard().setData((List<List>) obj);
            }
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
