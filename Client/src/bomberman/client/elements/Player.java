package bomberman.client.elements;

import bomberman.client.gui.Window;
import java.awt.Image;

public class Player {

    protected Image image;

    public Player() {
        this.image = Window.getInstance().getToolkit().getImage("images/player.png");
    }

    public Image getImage() {
        return this.image;
    }
}
