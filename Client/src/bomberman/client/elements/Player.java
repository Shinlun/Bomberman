package bomberman.client.elements;

import bomberman.client.controller.Game;
import bomberman.client.gui.Window;
import bomberman.client.model.Client;
import java.awt.Image;
import java.util.Map;

public class Player {

    protected Image image;
    private int id;
    /**
     * Target position of the player
     */
    private int index;
    /**
     * Old position of the player
     */
    private int old_index;
    /**
     * Progression of the position change. 0 to 1
     */
    private double move_progression = 1;
    /**
     * Squares per second
     */
    private double velocity = 4;

    public Player() {
        this.image = Window.getInstance().getToolkit().getImage("images/player.png");
    }

    public static Player factory(Map<String, Object> data) throws Exception {
        int id = Client.convertToInt(data.get("id"));
        Player player = new Player();
        player.setId(id);
        player.setIndex(Client.convertToInt(data.get("index")));
        player.setVelocity(Client.convertToDouble(data.get("velocity")));
        if (data.containsKey("client") && (Boolean) data.get("client")) {
            Game.getInstance().setCurrentPlayerId(id);
        }
        return player;
    }

    public Image getImage() {
        return this.image;
    }

    /**
     * @return the client_id
     */
    public int getId() {
        return id;
    }

    /**
     * @param client_id the client_id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public void progressMove(int period) {
        if (this.move_progression != 1) {
            this.move_progression += this.velocity * period / 1000;
            if (this.move_progression > 1) {
                this.move_progression = 1;
            }
        }
    }

    public void startMove(int index) {
        this.old_index = this.index;
        this.index = index;
        this.move_progression = 0;
    }

    public void startMove(int index, boolean send) {
        this.old_index = this.index;
        this.index = index;
        this.move_progression = 0;
        if (send) {
            Client.getInstance().movePlayer(index);
        }
    }

    public void reposition(int index) {
        this.index = index;
        this.old_index = index;
        this.move_progression = 1;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the old_index
     */
    public int getOldIndex() {
        return old_index;
    }

    /**
     * @param old_index the old_index to set
     */
    public void setOldIndex(int old_index) {
        this.old_index = old_index;
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
