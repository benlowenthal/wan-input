import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static BufferedReader reader;
    private static ServerSocket server;
    private static Socket client = new Socket();

    public static void main(String[] args){
        try {
            server = new ServerSocket(Integer.parseInt(args[0]));

            while (true) {
                System.out.println("Waiting for connections...");
                client = server.accept();
                System.out.println("Connection found");

                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (client.isConnected())
                    parse(reader.readLine());
            }
        } catch (Exception e) {
            System.out.println("ARGS: port (int)");
            e.printStackTrace();
        }
    }

    public static void parse(String msg){
        System.out.println(msg);
    }
}
