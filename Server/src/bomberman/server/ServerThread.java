package bomberman.server;

import bomberman.server.elements.Bomb;
import bomberman.server.elements.Bonus;
import bomberman.server.elements.Element;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private int bomb_sleeping_time = 2000;
    private int burning_length = 1;
    private boolean moving = false;
    /**
     * Position of the player
     */
    private int board_index = 0;
    /**
     * Squares per second
     */
    private double velocity = 4;

    public ServerThread(Socket socket, int client_id) {
        this.client_id = client_id;
        System.out.println("Le joueur " + client_id + " a rejoint la partie!");
        this.socket = socket;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public int getClientId() {
        return this.client_id;
    }

    public int getBombsAllowed() {
        return this.bombs_allowed;
    }

    public void setBombsAllowed(int bombs_allowed) {
        this.bombs_allowed = bombs_allowed;
    }

    public int getBurningLength() {
        return this.burning_length;
    }

    public void setBurningLength(int length) {
        this.burning_length = length;
    }

    public void increaseBurningLength() {
        if (this.burning_length < Server.board.getCols()) {
            this.burning_length++;
        }
    }

    @Override
    public void run() {
        this.sendBoardCols();
        this.sendBoard();
        this.setRandomPosition();
        this.addPlayer();
        this.sendPlayersList();
        this.initialized = true;

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
                System.out.println("Le joueur " + client_id + " a quitté la partie");
                socket.close();
                Server.delPlayer(this.client_id);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void execute(String command, Object obj) throws Exception {
        System.out.println(command + " " + obj);
        try {
            if (command.equals("move")) {
                this.move(convertToInt(obj));

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

    private static int convertToInt(Object n) throws Exception {
        if (n instanceof Integer) {
            return (Integer) n;
        } else if (n instanceof Long) {
            return ((Long) n).intValue();
        }
        throw new Exception(n + " not an Integer");
    }

    public int getBoardIndex() {
        return this.board_index;
    }

    private void sendBoardCols() {
        this.send("board_cols", Server.board.getCols());
    }

    private void sendBoard() {
        this.send("board", Server.board.getData());
    }

    public void setRandomPosition() {
        double nb_cases = Server.board.getCols() * Server.board.getRows();
        int i;
        List<Integer> players_positions = Server.getPlayersPositions();

        Element element;
        do {
            i = (int) Math.round(Math.random() * nb_cases);
            element = Server.board.getElement(i);
        } while ((element != null && !element.isWalkable())
                || players_positions.contains(i)
                || Server.board.isSquareOnFire(i));

        this.board_index = i;
    }

    private void sendPlayersList() {
        this.send("players", Server.getPlayersList(this.client_id));
    }

    private void addPlayer() {
        Server.sendAllBut("add_player", this.exportPlayerData(), this.client_id);
    }

    public Map<String, Object> exportPlayerData() {
        Map<String, Object> player_data = new HashMap<String, Object>();
        player_data.put("id", this.client_id);
        player_data.put("index", this.board_index);
        player_data.put("velocity", this.velocity);
        return player_data;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    private void move(final int target_index) {
        boolean moving_allowed = true;

        if (this.moving) {
            moving_allowed = false;

        } else if (target_index < 0 || target_index >= Server.board.getSize()) {
            moving_allowed = false;

        } else if (Math.abs(board_index - target_index) != 1 && Math.abs(board_index - target_index) != Server.board.getCols()) {
            moving_allowed = false;

        } else if (!Server.board.isSquareWalkable(target_index)) {
            moving_allowed = false;
        }

        if (moving_allowed) {
            this.moving = true;

            ArrayList<Integer> move = new ArrayList<Integer>();
            move.add(this.client_id);
            move.add(target_index);
            Server.sendAllBut("move", move, this.client_id);

            new Thread(new Runnable() {

                public void run() {
                    int move_duration = (int) (1000 / velocity);
                    try {
                        Thread.sleep(move_duration / 2);

                        board_index = target_index;
                        if (Server.board.isSquareOnFire(board_index)) {
                            Server.killPlayer(client_id);
                        } else if (Server.board.getElement(board_index) instanceof Bonus) {
                            ((Bonus) Server.board.getElement(board_index)).applyBonus(client_id);
                            Server.board.delElement(board_index);
                            Server.sendAll("del_element", board_index);
                        }

                        Thread.sleep(move_duration / 2);
                        moving = false;

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }).start();

        } else {
            ArrayList<Integer> position = new ArrayList<Integer>();
            position.add(this.client_id);
            position.add(this.board_index);
            this.send("reposition", position);
        }
    }

    private void dropBomb() throws Exception {
        this.nb_bombs++;
        Bomb bomb = new Bomb();
        bomb.setIndex(board_index);
        bomb.setSleepingTime(this.bomb_sleeping_time);
        bomb.setClientId(this.client_id);
        bomb.delayBurst();

        Server.board.setElement(bomb);
        Server.sendAll("add_element", Element.export(bomb));
    }

    public void decreaseNbBombs() {
        if (this.nb_bombs > 0) {
            this.nb_bombs--;
        }
    }
}
