package bomberman.client.elements;

import java.awt.Image;
import java.util.Map;

public abstract class Element {

    protected Image image;
    protected boolean walkable = false;

    public static Element factory(Map data) throws Exception {
        if (data == null) {
            return null;
        }
        Element element = null;

        if (data.get("type").equals("wall")) {
            element = new Wall();
        } else if (data.get("type").equals("unbreakable_wall")) {
            Wall wall = new Wall();
            wall.setBreakable(false);
            element = wall;
        } else if (data.get("type").equals("bomb")) {
            element = new Bomb();
        } else if (data.get("type").equals("bonus")) {
            element = new Bonus();
        }

        if (element == null) {
            throw new Exception("Unknown Element");
        }
        return element;

    }

    public Image getImage() {
        return this.image;
    }

    public boolean isWalkable() {
        return this.walkable;
    }
}
