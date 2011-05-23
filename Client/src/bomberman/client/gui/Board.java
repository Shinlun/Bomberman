package bomberman.client.gui;

import bomberman.client.elements.Element;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class Board extends JPanel {

    private List<Element> elements;
    private int cols;
    private int rows;

    public Board() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                try {
                    Element element = this.elements.get(i * this.cols + j);
                    if (element == null) {
                        continue;
                    }
                    g.drawImage(element.getImage(), this.getPosX(i, j), this.getPosY(i, j), this);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void setData(List<Map> data) {
        this.elements = new ArrayList<Element>();
        this.rows = data.size() / this.cols;
        int size = data.size();

        for (int i = 0; i < size; i++) {
            try {
                this.elements.add(Element.factory(data.get(i)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    private int getPosX(int i, int j) {
        return 3 * i + j / 2;
    }

    private int getPosY(int i, int j) {
        return 2 * j - i / 3;
    }
}
