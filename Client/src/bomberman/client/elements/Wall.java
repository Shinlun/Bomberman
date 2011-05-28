package bomberman.client.elements;

import bomberman.client.gui.Window;

public class Wall extends Element {

    public Wall() {
        this.setBreakable(this.breakable);
    }

    @Override
    public void setBreakable(boolean breakable) {
        if (breakable) {
            this.image = Window.getInstance().getToolkit().getImage("images/sprite_wall1.png");
        } else {
            this.image = Window.getInstance().getToolkit().getImage("images/sprite_wall2.png");
        }
        this.breakable = breakable;
    }
}
