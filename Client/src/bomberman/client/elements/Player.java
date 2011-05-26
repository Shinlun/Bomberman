package bomberman.client.elements;

import bomberman.client.gui.Window;
import bomberman.client.model.Client;
import java.awt.Image;

public class Player {

    protected Image image;
    /**
     * Target position of the player
     */
    private int x;
    private int y;
    /**
     * Old position of the player
     */
    private int old_x;
    private int old_y;
    /**
     * Progression of the position change. 0 to 1
     */
    private double move_progression = 1;
    /**
     * Squares per second
     */
    private double velocity = 3;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = Window.getInstance().getToolkit().getImage("images/player.png");
    }

    public Image getImage() {
        return this.image;
    }

    public void progressMove(int period) {
        if (this.move_progression != 1) {
            this.move_progression += this.velocity * period / 1000;
            if (this.move_progression > 1) {
                this.move_progression = 1;
            }
        }
    }

    public void startMoveRelative(int diff_x, int diff_y) {
        this.old_x = this.x;
        this.old_y = this.y;
        this.x += diff_x;
        this.y += diff_y;
        this.move_progression = 0;
        Client.getInstance().movePlayer(diff_x, diff_y);
    }

    public void startMove(int x, int y) {
        this.old_x = this.x;
        this.old_y = this.y;
        this.x = x;
        this.y = y;
        this.move_progression = 0;
    }

    public void reposition(int x, int y) {
        this.x = x;
        this.y = y;
        this.old_x = x;
        this.old_y = y;
        this.move_progression = 1;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the old_x
     */
    public int getOldX() {
        return old_x;
    }

    /**
     * @param old_x the old_x to set
     */
    public void setOldX(int old_x) {
        this.old_x = old_x;
    }

    /**
     * @return the old_y
     */
    public int getOldY() {
        return old_y;
    }

    /**
     * @param old_y the old_y to set
     */
    public void setOldY(int old_y) {
        this.old_y = old_y;
    }

    /**
     * @return the velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getMovePogression() {
        return this.move_progression;
    }
}
