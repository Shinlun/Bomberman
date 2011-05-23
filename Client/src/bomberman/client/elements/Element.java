package bomberman.client.elements;

import java.awt.Image;
import java.util.Map;

public abstract class Element {

    protected Image image;

    public static Element factory(Map data) throws Exception {
        if (data == null) {
            return null;
        }
        Element element = null;

        if (data.get("type").equals("wall")) {
            element = new Wall();
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
