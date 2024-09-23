import java.io.*;
import java.net.*;

public class ExternalPlayerController {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ExternalPlayerController(String host, int port) throws IOException {
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Connection to external player failed.");
            throw e;
        }
    }

    public String receiveCommand() throws IOException {
        return in.readLine(); // Reading command from server
    }

    public void sendState(String state) {
        out.println(state); // Updating server with current game state
    }

    public void closeConnection() throws IOException {
        if (socket != null) socket.close();
    }
}
