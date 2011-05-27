package bomberman.server.elements;

import java.util.HashMap;
import java.util.Map;

public abstract class Element {

    public static Map export(Element element) throws Exception {
        if (element == null) {
            return null;
        }
        Map data = new HashMap();

        if (element instanceof Wall) {
            data.put("type", "wall");
        } else if (element instanceof Bomb) {
            data.put("type", "bomb");
        }

        if (data.isEmpty()) {
            throw new Exception("Unknown Element");
        }
        return data;

    }
}
