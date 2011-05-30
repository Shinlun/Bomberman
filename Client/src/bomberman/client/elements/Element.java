package bomberman.client.elements;

import bomberman.client.model.Client;
import java.awt.Image;
import java.util.Map;

public abstract class Element {

    private int index;
    protected Image image;
    protected boolean breakable = true;
    protected boolean walkable = false;

    public static Element factory(Map data) throws Exception {
        if (data == null) {
            return null;
        }
        Element element = null;

        if (data.get("type").equals("wall")) {
            element = new Wall();
        } else if (data.get("type").equals("bomb")) {
            element = new Bomb();
        } else if (data.get("type").equals("bonus")) {
            element = new Bonus();
            ((Bonus) element).setType(Client.convertToInt(data.get("bonus_type")));
        }

        if (element == null) {
            throw new Exception("Unknown Element");
        }

        element.setIndex(Client.convertToInt(data.get("index")));
        if (data.containsKey("breakable")) {
            element.setBreakable((Boolean) data.get("breakable"));
        }
        if (data.containsKey("walkable")) {
            element.setWalkable((Boolean) data.get("walkable"));
        }
        return element;

    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return this.image;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    public boolean isWalkable() {
        return this.walkable;
    }
}
