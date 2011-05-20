
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.List;
import org.json.simple.JSONValue;


public class ServerThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int client_id;

    public ServerThread(Socket socket, int nb_clients) {
        this.client_id = nb_clients;
        System.out.println("Accès à la cuisine autorisé pour " + socket.getInetAddress());
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                System.out.println("Viré du resto !");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(String command, Object obj) {
        try {
            if (command.equals("moveBitch")) {
                // TODO
            } else if (command.equals("dropBomb")){
                // TODO
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
}
