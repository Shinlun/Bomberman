import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    final static int port = 18000;
    private static int threadNb = 0;

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            System.out.println("Attente de joueur via le nem vapeur " + port + "...");
            new ProtocolThread(serverSocket.accept()).start();
            threadNb++;
        }
    }

}
