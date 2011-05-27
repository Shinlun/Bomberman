package bomberman.server.elements;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {

    protected boolean active = true;
    protected boolean breakable = true;
    protected boolean walkable = false;
    private int rebirth_delay = 10000;

    public static Map export(Element element) throws Exception {
        if (element == null || !element.active) {
            return null;
        }
        Map data = new HashMap();

        if (element instanceof Wall) {
            data.put("type", "wall");
        } else if (element instanceof Bomb) {
            data.put("type", "bomb");
        }
        data.put("breakable", element.isBreakable());
        data.put("walkable", element.isWalkable());

        if (data.isEmpty()) {
            throw new Exception("Unknown Element");
        }
        return data;

    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    public boolean isWalkable() {
        return this.walkable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public void setRebirthDelay(int delay) {
        this.rebirth_delay = delay;
    }

    public int getRebirthDelay() {
        return this.rebirth_delay;
    }
}
