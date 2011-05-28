package bomberman.server.elements;

import bomberman.server.Server;
import bomberman.server.ServerThread;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bomb extends Element {

    private int sleeping_time = 2000;
    private int client_id;
    private boolean burst_ok = false;
    private List<Element> burning_list = new ArrayList<Element>();
    private List<Integer> fire = new ArrayList<Integer>();

    public void setSleepingTime(int sleeping_time) {
        this.sleeping_time = sleeping_time;
    }

    public int getSleepingTime() {
        return this.sleeping_time;
    }

    public void setClientId(int client_id) {
        this.client_id = client_id;
    }

    @Override
    public void burn() {
        this.burst();
        Server.sendAll("del_element", this.index);
    }

    public void delayBurst() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(sleeping_time);
                    burn();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }

    public void burst() {
        if (burst_ok) {
            return;
        }
        burst_ok = true;

        HashMap<Integer, ServerThread> players_threads = Server.getPlayersThreads();

        Server.board.delElement(this.index);
        this.fire.add(this.index);
        for (ServerThread thread : players_threads.values()) {
            if (thread.getBoardIndex() == this.index) {
                Server.killPlayer(thread.getClientId());
            }
        }

        for (int i = this.index + 1; i % Server.board.getCols() != 0; i++) {
            if (!this.checkSquare(i)) {
                break;
            }
        }
        for (int i = this.index - 1; (i + 1) % Server.board.getCols() != 0; i--) {
            if (!this.checkSquare(i)) {
                break;
            }
        }
        for (int i = this.index + Server.board.getCols(); i < Server.board.getSize(); i += Server.board.getCols()) {
            if (!this.checkSquare(i)) {
                break;
            }
        }
        for (int i = this.index - Server.board.getCols(); i >= 0; i -= Server.board.getCols()) {
            if (!this.checkSquare(i)) {
                break;
            }
        }

        for (Element element : this.burning_list) {
            element.burn();
        }

        Server.board.addFire(this.fire);
        Server.sendAll("burst_bomb", this.fire);

        Server.getPlayersThreads().get(this.client_id).decreaseNbBombs();
    }

    private boolean checkSquare(int check_index) {
        HashMap<Integer, ServerThread> players_threads = Server.getPlayersThreads();

        Element element = Server.board.getElement(check_index);
        if (element != null && element.isActive()) {
            if (!element.isBreakable()) {
                return false;
            }
            if (element instanceof Bomb) {
                element.burn();
            } else {
                this.burning_list.add(element);
                this.fire.add(check_index);
                return false;
            }
        }
        this.fire.add(check_index);

        for (ServerThread thread : players_threads.values()) {
            if (thread.getBoardIndex() == check_index) {
                Server.killPlayer(thread.getClientId());
            }
        }
        return true;
    }
}
