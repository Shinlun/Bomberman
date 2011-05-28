package bomberman.server.elements;

import bomberman.server.Server;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Element {

    protected int index;
    protected boolean active = true;
    protected boolean breakable = true;
    protected boolean walkable = false;
    private int rebirth_delay = 20000;

    public static Map export(Element element) throws Exception {
        if (element == null || !element.active) {
            return null;
        }
        Map data = new HashMap();

        if (element instanceof Wall) {
            data.put("type", "wall");
        } else if (element instanceof Bomb) {
            data.put("type", "bomb");
        }

        if (data.isEmpty()) {
            throw new Exception("Unknown Element");
        }

        data.put("index", element.getIndex());
        data.put("breakable", element.isBreakable());
        data.put("walkable", element.isWalkable());

        return data;

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    public void setBreakable(boolean breakable) {
        this.breakable = breakable;
    }

    public boolean isBreakable() {
        return this.breakable;
    }

    public boolean isWalkable() {
        return this.walkable;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public void burn() {
        if (this.active) {
            this.setActive(false);
            this.delayRebirth();
            Server.sendAll("del_element", this.index);
        }
    }

    public void setRebirthDelay(int delay) {
        this.rebirth_delay = delay;
    }

    public int getRebirthDelay() {
        return this.rebirth_delay;
    }

    public void delayRebirth() {
        final Element element = this;
        new Thread(new Runnable() {

            public void run() {
                try {
                    if (!active) {
                        Thread.sleep(rebirth_delay);
                        List<Integer> positions = Server.getPlayersPositions();

                        Element board_element = Server.board.getElement(index);

                        if (!positions.contains(index)
                                && !Server.board.isSquareOnFire(index)
                                && (element.equals(board_element) || board_element == null)) {
                            if (board_element == null) {
                                Server.board.setElement(element);
                            }
                            setActive(true);
                            Server.sendAll("add_element", Element.export(element));
                        } else {
                            element.delayRebirth();
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
}
