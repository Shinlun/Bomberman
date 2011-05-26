package bomberman.server.elements;

public class Wall extends Element {

    private boolean breakable = true;

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isBreakable() {
        return this.breakable;
    }
}
