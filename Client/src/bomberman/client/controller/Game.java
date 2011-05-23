package bomberman.client.controller;

import bomberman.client.gui.Board;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener {

    private static Game instance;
    private Board board;

    private Game() {
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

    public void newGame() throws Exception {
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
            System.out.println("FIRE IN THE HOLE !");
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            System.out.println("UP");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            System.out.println("DOWN");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            System.out.println("LEFT");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            System.out.println("RIGHT");
        }
    }

    public void keyReleased(KeyEvent e) {
    }
}
