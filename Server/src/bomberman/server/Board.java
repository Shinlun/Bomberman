package bomberman.server;

import bomberman.server.elements.Element;
import bomberman.server.elements.Wall;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {

    private List<Element> data;
    private int cols = 30;
    private int rows = 30;
    private double probability_wall = 0.3;

    public void generate() {
        this.data = new ArrayList<Element>();
        int size = this.cols * this.rows;

        for (int i = 0; i < size; i++) {
            Element element = null;

            if (Math.random() < this.probability_wall) {
                element = new Wall();
            }

            this.data.add(element);
        }
    }

    public List<Map> getData() {
        List<Map> data_export = new ArrayList<Map>();
        int size = this.data.size();

        for (int i = 0; i < size; i++) {
            try {
                data_export.add(Element.export(this.data.get(i)));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return data_export;
    }

    public int getCols() {
        return this.cols;
    }

    public int getRows() {
        return this.rows;
    }
}
