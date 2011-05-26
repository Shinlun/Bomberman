package bomberman.client.controller;

import bomberman.client.elements.Player;
import bomberman.client.gui.Board;
import bomberman.client.gui.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends Thread implements KeyListener {

    private static Game instance;
    private Board board;
    private Player player;
    private boolean started = false;
    private int fps = 50;
    private boolean key_up = false;
    private boolean key_down = false;
    private boolean key_left = false;
    private boolean key_right = false;

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
        this.setPlayer(new Player());
        Window window = Window.getInstance();
        window.showBoard();
        window.addKeyListener(this);
        this.started = true;
    }

    @Override
    public void run() {
        while (this.started) {
            try {
                System.out.println("Arrows:" + (this.key_up ? " UP" : "") + (this.key_down ? " DOWN" : "") + (this.key_left ? " LEFT" : "") + (this.key_right ? " RIGHT" : ""));
                Thread.sleep(1000 / this.fps);
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

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            System.out.println("FIRE IN THE HOLE !");
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.key_up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.key_down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.key_left = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.key_right = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            this.key_up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            this.key_down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            this.key_left = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            this.key_right = false;
        }
    }
}
