package bomberman.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

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
                System.out.println("Attente de joueur via le nem vapeur " + port + "...");
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
}
