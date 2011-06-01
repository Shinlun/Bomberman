package bomberman.server.elements;

import bomberman.server.Server;
import bomberman.server.ServerThread;

public class Bonus extends Element {

    private int type;

    public Bonus() {
        super();
        this.walkable = true;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void applyBonus(int client_id) {
        ServerThread thread = Server.getThread(client_id);
        switch (this.type) {
            case 1:
                thread.setBombsAllowed(thread.getBombsAllowed() + 1);
                break;
            case 2:
                thread.increaseBurningLength();
                break;
            default:
                break;
        }
    }

    @Override
    public void burn() {
        try {
            Server.board.delElement(this.index);
            Server.sendAll("del_element", this.index);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
