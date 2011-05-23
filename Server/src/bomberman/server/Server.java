package bomberman.server;

import bomberman.server.elements.Element;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {

    final static int port = 18000;
    final static int board_rows = 30;
    final static int board_cols = 30;
    private static int nb_players = 1;
    private static HashMap<Integer, ServerThread> players_threads = new HashMap<Integer, ServerThread>();
    private static List<List> board;

    public static void main(String[] args) throws IOException {
        generateBoard();

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Attente de joueur via le nem vapeur " + port + "...");
            ServerThread player_thread = new ServerThread(serverSocket.accept(), nb_players);
            players_threads.put(nb_players, player_thread);
            player_thread.start();
            nb_players++;
        }
    }

    public static void sendAll(String command, Object obj) {
        for (ServerThread player_thread : players_threads.values()) {
            player_thread.send(command, obj);
        }
    }

    private static void generateBoard() {
        board = new ArrayList<List>();
        for (int i = 0; i < board_cols * board_rows; i++) {
            List<Element> elements = new ArrayList<Element>();
            board.add(elements);
        }
    }
}
