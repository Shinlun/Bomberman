package bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
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

    public static void sendAllBut(String command, Object obj, ArrayList<Integer> exceptions) {
        for (ServerThread player_thread : players_threads.values()) {
            if (!exceptions.contains(player_thread.getClientId())) {
                player_thread.send(command, obj);
            }
        }
    }

    public static Map<Integer, Map> getPlayersList(int client_id) {
        Map<Integer, Map> players = new HashMap<Integer, Map>();

        for (ServerThread player_thread : players_threads.values()) {
            Boolean client = client_id == player_thread.getClientId();
            if (!player_thread.isInitialized() && !client) {
                continue;
            }
            Map player_infos = new HashMap();
            player_infos.put("x", player_thread.getPostionX());
            player_infos.put("y", player_thread.getPositionY());
            player_infos.put("client", client);

            players.put(player_thread.getClientId(), player_infos);
        }
        return players;
    }

    public static Map<Integer, Integer> getPlayersPositions() {
        Map<Integer, Integer> positions = new HashMap<Integer, Integer>();

        for (ServerThread player_thread : players_threads.values()) {
            positions.put(player_thread.getPostionX(), player_thread.getPositionY());
        }

        return positions;
    }

    public static void delPlayer(int client_id) {
        players_threads.remove(client_id);
        sendAll("del_player", client_id);
    }
}
