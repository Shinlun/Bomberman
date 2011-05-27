package bomberman.client.elements;

import bomberman.client.gui.Window;

public class Wall extends Element {

    public Wall() {
        this.image = Window.getInstance().getToolkit().getImage("images/sprite_wall1.png");
    }

    public void setBreakable(boolean breakable) {
        if (!breakable) {
            this.image = Window.getInstance().getToolkit().getImage("images/sprite_wall2.png");
        }
        this.breakable = breakable;
    }
}
