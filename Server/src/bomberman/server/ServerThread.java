package bomberman.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONValue;

public class ServerThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int client_id;
    private int position_x = 0;
    private int position_y = 0;

    public ServerThread(Socket socket, int client_id) {
        this.client_id = client_id;
        System.out.println("Accès à la cuisine autorisé pour " + socket.getInetAddress());
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.sendBoardCols();
            this.sendBoard();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
                System.out.println("Viré du resto !");
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void execute(String command, Object obj) throws Exception {
        try {
            if (command.equals("move")) {
                if (obj instanceof ArrayList) {
                    ArrayList<Integer> infos = (ArrayList) obj;
                    if (infos.size() == 2) {
                        ArrayList<Integer> new_position = new ArrayList<Integer>();
                        if (Math.abs(this.position_x - infos.get(0)) == 1) {
                            this.position_x += infos.get(0);
                        } else if (Math.abs(this.position_x - infos.get(1)) == 1) {
                            this.position_y += infos.get(1);
                        } else {
                            throw new Exception("Position incorrecte");
                        }
                        new_position.add(this.position_x);
                        new_position.add(this.position_y);
                        this.send("move", new_position);
                    }
                }
            } else if (command.equals("dropBomb")) {
                // TODO
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void send(String command, Object obj) {
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

    private void sendBoardCols() {
        this.send("board_cols", Server.board.getCols());
    }

    private void sendBoard() {
        this.send("board", Server.board.getData());
    }
}
