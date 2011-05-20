package bomberman.gui;

import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class Board extends JPanel {

    private List<List> data;

    public Board() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (List square : this.data) {
            // TODO
        }
    }

    public void setData(List<List> data) {
        this.data = data;
    }
}
