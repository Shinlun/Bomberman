package bomberman.client.controller;

import bomberman.client.gui.Board;

public class Game {

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
}
