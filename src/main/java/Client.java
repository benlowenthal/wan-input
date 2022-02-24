import net.java.games.input.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Client {
    private static BufferedWriter writer;
    private static Socket server;

    private static final int DELAY = 1000;
    private static final float EPSILON = 0.1f;
    //writer = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));

    public static void main(String[] args){
        Controller[] devices = ControllerEnvironment.getDefaultEnvironment().getControllers();
        Controller controller = null;
        for (Controller d : devices){
            if (d.getType().toString().equals("Gamepad")){
                controller = d;
            }
        }

        while (true){
            assert controller != null;
            controller.poll();
            Event event = new Event();
            EventQueue queue = controller.getEventQueue();
            while (queue.getNextEvent(event)){
                if (event.getValue() > EPSILON || event.getValue() < -EPSILON){
                    Component comp = event.getComponent();
                    System.out.println(comp.getName() + " (" + comp.getIdentifier() + ") : " + event.getValue());
                }
            }

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
