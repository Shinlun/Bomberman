package bomberman.client.controller;

import bomberman.client.elements.Player;
import bomberman.client.gui.Board;
import bomberman.client.gui.Window;
import bomberman.client.model.Client;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game extends Thread implements KeyListener {

    private static Game instance;
    private Board board;
    private Map<Integer, Player> players = new HashMap();
    private int player_id;
    private boolean started = false;
    private int fps = 25;
    private int last_pressed_key = 0;

    private Game() {
        this.start();
    }

    /**
     * Creates a unique instance of Game (Singleton)
     *
     * @return Instance of Game
     */
    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void newGame() {
        this.setBoard(new Board());
        Window window = Window.getInstance();
        window.showBoard();
        window.addKeyListener(this);
    }

    @Override
    public void run() {
        int period = 1000 / this.fps;

        while (true) {
            try {
                if (!this.started) {
                    Thread.sleep(1000);
                    continue;
                }

                this.move();

                for (Player player : this.players.values()) {
                    player.progressMove(period);
                }

                this.board.repaint(period);

                Thread.sleep(period);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCurrentPlayerId() {
        return this.player_id;
    }

    public void setCurrentPlayerId(int player_id) {
        this.player_id = player_id;
    }

    public Player getCurrentPlayer() {
        return this.players.get(this.player_id);
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Map> data) throws Exception {
        this.players = new HashMap();
        for (Map<String, Object> player_data : data) {
            Player player = Player.factory(player_data);
            this.players.put(player.getId(), player);
        }
        this.started = true;
    }

    public Player getPlayer(int player_id) {
        return this.players.get(player_id);
    }

    public void addPlayer(Map<String, Object> player_data) throws Exception {
        Player player = Player.factory(player_data);
        this.players.put(player.getId(), player);
    }

    public void delPlayer(int player_id) {
        if (this.players.containsKey(player_id)) {
            this.players.remove(player_id);
        }
    }

    private void move() {
        Player player = this.getCurrentPlayer();
        if (player.getMovePogression() == 1) {
            int up_target = player.getIndex() - this.board.getCols();
            int down_target = player.getIndex() + this.board.getCols();
            int left_target = player.getIndex() + 1;
            int right_target = player.getIndex() - 1;
            if (this.last_pressed_key == KeyEvent.VK_UP && this.board.isSquareWalkable(up_target)) {
                player.startMove(up_target, true);
            } else if (this.last_pressed_key == KeyEvent.VK_DOWN && this.board.isSquareWalkable(down_target)) {
                player.startMove(down_target, true);
            } else if (this.last_pressed_key == KeyEvent.VK_LEFT && this.board.isSquareWalkable(left_target)) {
                player.startMove(left_target, true);
            } else if (this.last_pressed_key == KeyEvent.VK_RIGHT && this.board.isSquareWalkable(right_target)) {
                player.startMove(right_target, true);
            }
        }
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            Client.getInstance().dropBomb();
        }
    }

    public void keyPressed(KeyEvent e) {
        int key_code = e.getKeyCode();
        if (key_code == KeyEvent.VK_UP || key_code == KeyEvent.VK_DOWN || key_code == KeyEvent.VK_LEFT || key_code == KeyEvent.VK_RIGHT) {
            this.last_pressed_key = key_code;
        }
        this.move();
    }

    public void keyReleased(KeyEvent e) {
        if (this.last_pressed_key == e.getKeyCode()) {
            this.last_pressed_key = 0;
        }
    }
}
