package bomberman.client.elements;

import bomberman.client.gui.Window;

public class Bonus extends Element {

    private int type;

    public void setType(int type) {
        this.type = type;
        switch (this.type) {
            case 1:
                this.image = Window.getInstance().getToolkit().getImage("images/sprite_bonus1.png");
                break;
            case 2:
                this.image = Window.getInstance().getToolkit().getImage("images/sprite_bonus2.png");
                break;
            case 3:
                this.image = Window.getInstance().getToolkit().getImage("images/sprite_bonus3.png");
                break;
            default:
                this.image = Window.getInstance().getToolkit().getImage("images/sprite_bonus1.png");
                break;
        }
    }

    public int getType() {
        return this.type;
    }
}
