package bomberman.controller;

import bomberman.gui.Board;
import bomberman.model.Client;

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
        this.board.setData(Client.getInstance().getBoard());
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
