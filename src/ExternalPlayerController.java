import com.google.gson.Gson;

import java.io.*;
import java.net.*;

public class ExternalPlayerController {
    private String host;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private OpMove move;

    public ExternalPlayerController(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void establishConnection() throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public OpMove getNewMove(PureGame game) throws IOException {
        Gson gson = new Gson();
        String jsonGameState = gson.toJson(game);

        out.println(jsonGameState);
        System.out.println("Sent game state to server: " + jsonGameState);

        String response = in.readLine();
        System.out.println("Received response from server: " + response);

        move = gson.fromJson(response, OpMove.class);
        System.out.println("Optimal Move: X=" + move.getOpX() + " Rotations= " + move.getOpRotate());

        if (move.getOpX() == 0) {
            System.out.println("Place the piece at the left-most position.");
        } else {
            System.out.println("Move the piece to X = " + move.getOpX());
        }

        if (move.getOpRotate() == 0) {
            System.out.println("No rotation needed");
        } else {
            System.out.println("Rotate the piece " + move.getOpRotate() + "times");
        }

        return move;
    }
}
