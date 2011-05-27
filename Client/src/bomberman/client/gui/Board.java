package bomberman.client.gui;

import bomberman.client.controller.Game;
import bomberman.client.elements.Element;
import bomberman.client.elements.Player;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public class Board extends JPanel {

    private List<Element> elements = null;
    private int cols;
    private int rows;
    private int unit_pixels = 10;

    public Board() {
        super();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.getElements() == null) {
            return;
        }
        Collection<Player> players = Game.getInstance().getPlayers().values();

        for (int j = 0; j < this.rows; j++) {
            for (int i = 0; i < this.cols; i++) {
                try {
                    Element element = this.getElements().get(i * this.cols + j);
                    if (element == null) {
                        continue;
                    }
                    int x = this.getPosX(i, j) * this.unit_pixels;
                    int y = this.getPosY(i, j) * this.unit_pixels;
                    g.drawImage(element.getImage(), x, y, this);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

            for (Player player : players) {
                if (player.getY() == j) {
                    int x = this.getPosX(player.getX(), player.getY()) * this.unit_pixels;
                    int y = this.getPosY(player.getX(), player.getY()) * this.unit_pixels;
                    if (player.getMovePogression() != 1) {
                        int old_x = this.getPosX(player.getOldX(), player.getOldY()) * this.unit_pixels;
                        int old_y = this.getPosY(player.getOldX(), player.getOldY()) * this.unit_pixels;
                        x = (old_x + (int) ((x - old_x) * player.getMovePogression()));
                        y = (old_y + (int) ((y - old_y) * player.getMovePogression()));
                    }
                    g.drawImage(player.getImage(), x, y, this);
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
                if(i <= this.rows || i % this.rows == 0 || (i+1) % this.rows == 0 || size - this.rows < i) {
                    Map type = new HashMap();
                    type.put("type", "unbreakable_wall");
                    data.set(i, type);
                } 
                this.getElements().add(Element.factory(data.get(i)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        Dimension panel_size = new Dimension((3 * this.cols + this.rows) * this.unit_pixels, (this.cols + 2 * this.rows) * this.unit_pixels);
        this.setMinimumSize(panel_size);
        this.setPreferredSize(panel_size);
        Window.getInstance().pack();
        this.repaint();
    }

    /**
     * @return the elements
     */
    public List<Element> getElements() {
        return elements;
    }

    public void setElement(int index, Element element) {
        this.elements.set(index, element);
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    private int getPosX(int i, int j) {
        return 3 * i + j;
    }

    private int getPosY(int i, int j) {
        return 2 * j - i + this.cols;
    }
}
