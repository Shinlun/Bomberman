package bomberman.server.elements;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {

    private boolean active = true;
    private boolean breakable = true;

    public static Map export(Element element) throws Exception {
        if (element == null) {
            return null;
        }
        Map data = new HashMap();

        if (element instanceof Wall && element.active) {
            data.put("type", "wall");
        } else if (element instanceof Bomb) {
            data.put("type", "bomb");
        }

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

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }
}
