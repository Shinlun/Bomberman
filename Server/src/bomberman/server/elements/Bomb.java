package bomberman.server.elements;

import bomberman.server.Server;
import bomberman.server.ServerThread;
import java.util.ArrayList;
import java.util.HashMap;

public class Bomb extends Element {

    private int sleeping_time = 4000;
    private int client_id;
    private boolean burst_ok = false;

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
    }

    public void delayBurst() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(sleeping_time);
                    burst();
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

        ArrayList<Integer> bomb_position = new ArrayList<Integer>();
        bomb_position.add(this.x);
        bomb_position.add(this.y);

        HashMap<Integer, ServerThread> players_threads = Server.getPlayersThreads();

        int index = this.x + Server.board.getCols() * this.y;
        Server.board.setElement(index, null);
        for (ServerThread thread : players_threads.values()) {
            if (thread.getPostionX() == this.x && thread.getPositionY() == this.y) {
                Server.killPlayer(thread.getClientId());
            }
        }

        for (int i = this.x + 1; i < Server.board.getCols(); i++) {
            index = i + Server.board.getCols() * this.y;
            Element element = Server.board.getElements().get(index);
            if (element != null) {
                if (element.isBreakable()) {
                    element.burn();
                }
                break;
            }
            for (ServerThread thread : players_threads.values()) {
                if (thread.getPostionX() == i && thread.getPositionY() == this.y) {
                    Server.killPlayer(thread.getClientId());
                }
            }
        }
        for (int i = this.x - 1; i >= 0; i--) {
            index = i + Server.board.getCols() * this.y;
            Element element = Server.board.getElements().get(index);
            if (element != null) {
                if (element.isBreakable()) {
                    element.burn();
                }
                break;
            }
            for (ServerThread thread : players_threads.values()) {
                if (thread.getPostionX() == i && thread.getPositionY() == this.y) {
                    Server.killPlayer(thread.getClientId());
                }
            }
        }
        for (int i = this.y + 1; i < Server.board.getRows(); i++) {
            index = this.x + Server.board.getCols() * i;
            Element element = Server.board.getElements().get(index);
            if (element != null) {
                if (element.isBreakable()) {
                    element.burn();
                }
                break;
            }
            for (ServerThread thread : players_threads.values()) {
                if (thread.getPostionX() == this.x && thread.getPositionY() == i) {
                    Server.killPlayer(thread.getClientId());
                }
            }
        }
        for (int i = this.y - 1; i >= 0; i--) {
            index = this.x + Server.board.getCols() * i;
            Element element = Server.board.getElements().get(index);
            if (element != null) {
                if (element.isBreakable()) {
                    element.burn();
                }
                break;
            }
            for (ServerThread thread : players_threads.values()) {
                if (thread.getPostionX() == this.x && thread.getPositionY() == i) {
                    Server.killPlayer(thread.getClientId());
                }
            }
        }

        Server.sendAll("burst_bomb", bomb_position);
        Server.getPlayersThreads().get(this.client_id).decreaseNbBombs();
    }
}
