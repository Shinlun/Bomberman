package bomberman.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;

public class Window extends JFrame {

    private static Window instance;
    private Settings settings;
    private Board board;

    private Window() {
        super();

        this.setTitle("Bomberman");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(400, 200);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.settings = new Settings();
        this.add(this.settings);

        this.setVisible(true);
    }

    /**
     * Creates a unique instance of Window (Singleton)
     *
     * @return Instance of Window
     */
    public static synchronized Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }
        return instance;
    }

    
    /**
     * Affiche le plateau de jeu
     */
    public void showBoard() {
        this.remove(this.settings);
        this.board = new Board();
        this.add(this.board);
        this.setVisible(true);
    }

}
