package bomberman.client.gui;

import bomberman.client.elements.Element;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class Board extends JPanel {

    private List<List> data;
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
                    List<Element> elements = this.data.get(i * this.cols + j);
                    for (Element element : elements) {
                        g.drawImage(element.getImage(), this.getPosX(i, j), this.getPosY(i, j), this);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public void setData(List<List> data) {
        this.data = data;
        this.rows = data.size() / this.cols;

        for (int i = data.size(); i >= 0; i--) {
            List<Object> elements = this.data.get(i);
            for (int j = elements.size(); j >= 0; j--) {
                try {
                    if (elements.get(j) instanceof Map) {
                        elements.set(j, Element.factory((Map) elements.get(j)));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
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
