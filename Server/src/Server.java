
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashMap;

public class Server {

    final static int port = 18000;
    private static int nb_players = 1;
    private static HashMap<Integer, ServerThread> players_threads = new HashMap<Integer, ServerThread>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Attente de joueur via le nem vapeur " + port + "...");
            ServerThread player_thread = new ServerThread(serverSocket.accept(), nb_players);
            players_threads.put(nb_players, player_thread);
            player_thread.start();
            nb_players++;
        }
    }
}
