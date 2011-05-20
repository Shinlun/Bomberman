
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ProtocolThread extends Thread {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ProtocolThread(Socket socket) {
        System.out.println("Accès à la cuisine autorisé pour " + socket.getInetAddress());
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        
    }
}
