package bomberman.client.elements;

import bomberman.client.gui.Window;

public class Bomb extends Element {

    public Bomb() {
        this.image = Window.getInstance().getToolkit().getImage("images/sprite_bomb.png");
    }
}
