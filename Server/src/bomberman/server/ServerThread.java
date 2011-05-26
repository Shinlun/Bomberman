package bomberman.server;

import bomberman.server.elements.Bomb;
import bomberman.server.elements.Wall;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONValue;

public class ServerThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int client_id;
    private boolean initialized = false;
    private int nb_bombs = 0;
    private int bombs_allowed = 1;
    private int bomb_sleeping_time = 4000;
    private int position_x = 0;
    private int position_y = 0;

    public ServerThread(Socket socket, int client_id) {
        this.client_id = client_id;
        System.out.println("Accès à la cuisine autorisé pour " + socket.getInetAddress());
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getPostionX() {
        return this.position_x;
    }

    public int getPositionY() {
        return this.position_y;
    }

    public int getClientId() {
        return this.client_id;
    }

    @Override
    public void run() {
        this.sendBoardCols();
        this.sendBoard();
        this.setRandomPosition();
        this.addPlayer();
        this.sendPlayersList();
        
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

    private void move(ArrayList<Integer> move_request) {
        ArrayList<Integer> new_position = new ArrayList<Integer>();
        Map<Integer, ArrayList<Integer>> server_response = new HashMap<Integer, ArrayList<Integer>>();
        Boolean moving_allowed = false;

        if (Math.abs(this.position_x - move_request.get(0)) == 1) {
            moving_allowed = true;
            this.position_x += move_request.get(0);
        } else if (Math.abs(this.position_y - move_request.get(1)) == 1 && this.position_x == move_request.get(0)) {
            moving_allowed = true;
            this.position_y += move_request.get(1);
        }

        new_position.add(this.position_x);
        new_position.add(this.position_y);
        server_response.put(this.client_id, new_position);

        if (moving_allowed) {
            Server.sendAll("move", server_response);
        } else {
            this.send("move", server_response);
        }
    }

    private void dropBomb() {
        Bomb bomb = new Bomb();
        bomb.setX(this.position_x);
        bomb.setY(this.position_y);
        bomb.setSleepingTime(this.bomb_sleeping_time);

        ArrayList<Integer> bomb_position = new ArrayList<Integer>();
        bomb_position.add(bomb.getX());
        bomb_position.add(bomb.getY());

        Server.sendAll("dropBomb", bomb_position);
        this.nb_bombs++;
        this.burst_bomb(bomb);
    }

    private void burst_bomb(final Bomb bomb) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    ArrayList<Integer> bomb_position = new ArrayList<Integer>();
                    bomb_position.add(bomb.getX());
                    bomb_position.add(bomb.getY());
                    Thread.sleep(bomb.getSleepingTime());
                    Server.sendAll("burstBomb", bomb_position);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        this.nb_bombs--;
    }

    private void execute(String command, Object obj) throws Exception {
        System.out.println(command + " " + obj);
        try {
            if (command.equals("move")) {
                if (obj instanceof ArrayList) {
                    ArrayList<Integer> move_request = (ArrayList) obj;

                    if (move_request.size() == 2) {
                        this.move(move_request);
                    }
                }
            } else if (command.equals("drop_bomb")) {
                if (this.nb_bombs < this.bombs_allowed) {
                    this.dropBomb();
                }
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

    private void setRandomPosition() {
        double nb_cases = Server.board.getCols() * Server.board.getRows();
        int i = (int) (Math.random() * nb_cases);

        while (Server.board.getElements().get(i) instanceof Wall) {
            i = (int) (Math.random() * nb_cases);
        }

        this.position_x = (int) Math.ceil(i / Server.board.getCols());
        this.position_y = i % Server.board.getCols();
    }

    private void sendPlayersList() {
        Map<Integer, Map> players_list = Server.getPlayersList(this.client_id);
        this.send("players", players_list);
    }

    private void addPlayer() {
        ArrayList<Integer> position = new ArrayList<Integer>();
        position.add(this.position_x);
        position.add(this.position_y);

        ArrayList<Integer> exceptions = new ArrayList<Integer>();
        exceptions.add(this.client_id);

        this.initialized = true;
        Server.sendAllBut("add_player", position, exceptions);
    }

    public boolean isInitialized() {
        return this.initialized;
    }
}
