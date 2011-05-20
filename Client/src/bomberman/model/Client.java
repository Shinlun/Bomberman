package bomberman.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Client {

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
    }

    public Object encodeData(Object data) throws Exception {
        StringWriter jsonOut = new StringWriter();
        JSONValue.writeJSONString(data, jsonOut);

        return jsonOut.toString();
    }
}
