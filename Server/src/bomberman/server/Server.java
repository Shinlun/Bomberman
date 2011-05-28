package bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    final static int port = 1337;
    private static int nb_players = 1;
    private static HashMap<Integer, ServerThread> players_threads = new HashMap<Integer, ServerThread>();
    public static Board board;

    public static void main(String[] args) {
        board = new Board();
        board.generate();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                System.out.println("Attente de joueur via le port " + port + "...");
                ServerThread player_thread = new ServerThread(serverSocket.accept(), nb_players);
                players_threads.put(nb_players, player_thread);
                player_thread.start();
                nb_players++;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendAll(String command, Object obj) {
        for (ServerThread player_thread : players_threads.values()) {
            player_thread.send(command, obj);
        }
    }

    public static void sendAllBut(String command, Object obj, int exception_client_id) {
        for (ServerThread player_thread : players_threads.values()) {
            if (player_thread.getClientId() != exception_client_id) {
                player_thread.send(command, obj);
            }
        }
    }

    public static HashMap<Integer, ServerThread> getPlayersThreads() {
        return players_threads;
    }

    public static List<Map> getPlayersList(int client_id) {
        List<Map> players = new ArrayList<Map>();

        for (ServerThread player_thread : players_threads.values()) {
            boolean client = client_id == player_thread.getClientId();
            if (!player_thread.isInitialized() && !client) {
                continue;
            }
            Map<String, Object> player_data = player_thread.exportPlayerData();
            if (client) {
                player_data.put("client", client);
            }

            players.add(player_data);
        }
        return players;
    }

    public static List<Integer> getPlayersPositions() {
        List<Integer> positions = new ArrayList<Integer>();
        for (ServerThread player_thread : players_threads.values()) {
            positions.add(player_thread.getPostionX() + board.getCols() * player_thread.getPositionY());
        }
        return positions;
    }

    public static void delPlayer(int client_id) {
        players_threads.remove(client_id);
        sendAll("del_player", client_id);
    }

    public static void killPlayer(int client_id) {
        ArrayList<Integer> position = new ArrayList<Integer>();
        players_threads.get(client_id).setRandomPosition();
        position.add(client_id);
        position.add(players_threads.get(client_id).getPostionX());
        position.add(players_threads.get(client_id).getPositionY());
        sendAll("reposition", position);
    }
}
