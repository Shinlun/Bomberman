package bomberman.server.elements;

import java.awt.Image;
import java.util.Map;

public abstract class Element {

    protected Image image;

    public static Element factory(Map data) throws Exception {
        Element element = null;
        if (data.get("type") == "wall") {
            element = new Wall();
            return element;
        }
        if (element == null) {
            throw new Exception("Unknown Element");
        }
        return element;

    }

    public Image getImage() {
        return this.image;
    }
}
