package bomberman.gui;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Board extends JPanel {

    public Board() {
        super();

        JButton button = new JButton("LOL");
        this.add(button);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(10, 10, 30, 30);
    }
}
