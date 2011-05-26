package bomberman.server.elements;

public class Bomb extends Element {

    private int x;
    private int y;
    private int sleeping_time = 4000;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSleepingTime(int sleeping_time) {
        this.sleeping_time = sleeping_time;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getSleepingTime() {
        return this.sleeping_time;
    }
}
