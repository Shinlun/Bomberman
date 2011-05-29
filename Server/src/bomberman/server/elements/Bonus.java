package bomberman.server.elements;

import bomberman.server.Server;

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

    @Override
    public void burn() {
        try {
            Server.board.delElement(this.index);
            Server.sendAll("del_element", this.index);
            Wall wall = new Wall();
            wall.setIndex(this.index);
            wall.setBonusProbability(0);
            Server.board.setElement(wall);
            Server.sendAll("add_element", Element.export(wall));
            wall.burn();
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
