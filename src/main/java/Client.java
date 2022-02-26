import net.java.games.input.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private static DataOutputStream writer;
    private static final Socket socket = new Socket();

    private static final int DELAY = 1000;
    private static final float EPSILON = 0.1f;

    public static void main(String[] args){
        Controller[] devices = ControllerEnvironment.getDefaultEnvironment().getControllers();
        Controller controller = null;
        for (Controller d : devices){
            if (d.getType().toString().equals("Gamepad")){
                controller = d;
            }
        }
        if (controller == null)
            throw new NullPointerException("Controller not connected");

        try {
            socket.connect(new InetSocketAddress(Protocol.IP, Protocol.PORT), 1000);
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Could not connect to host " + Protocol.IP + ":" + Protocol.PORT);
            System.exit(1);
        }

        while (socket.isConnected()){
            controller.poll();
            Event event = new Event();
            EventQueue queue = controller.getEventQueue();
            while (queue.getNextEvent(event)){
                if (event.getValue() > EPSILON || event.getValue() < -EPSILON){
                    Component comp = event.getComponent();
                    try {
                        send(comp.getIdentifier() + " " + event.getValue());
                    } catch (IOException e) {
                        System.out.println(comp.getIdentifier() + " " + event.getValue() + " failed");
                    }
                }
            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            writer.close();
            socket.close();
        } catch (IOException ignored) {}
    }

    public static void send(String msg) throws IOException {
        writer.writeUTF(msg);
        writer.flush();
        System.out.println("[Client->Server] " + msg);
    }
}
