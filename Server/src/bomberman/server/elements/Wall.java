package bomberman.server.elements;

import bomberman.server.Server;
import java.util.ArrayList;
import java.util.List;

public class Wall extends Element {

    private double bonus_probability = 0.2;

    @Override
    public void burn() {
        if (Math.random() < this.bonus_probability) {
            Bonus bonus = new Bonus();
            bonus.setIndex(this.index);
            bonus.setType((int) Math.ceil(Math.random() * 3));
            try {
                Server.board.setElement(bonus);
                List bonus_infos = new ArrayList();
                bonus_infos.add(bonus.index);
                bonus_infos.add(bonus.getType());
                Server.sendAll("add_bonus", bonus_infos);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            this.setActive(false);
            this.bonus_probability = Math.random() / 2;
            this.delayRebirth();

            Server.sendAll("del_element", this.index);
        }
    }

    public void setBonusProbability(double probability) {
        this.bonus_probability = probability;
    }
}
