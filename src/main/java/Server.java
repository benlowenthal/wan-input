import redlaboratory.jvjoyinterface.VJoy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final VJoy vj = new VJoy();
    public static final int rID = 1;

    public static void main(String[] args){
        try {
            if (!vj.driverMatch() || !vj.vJoyEnabled()){
                System.out.println("vJoy driver mismatch or incorrectly set up, exiting.");
                System.exit(1);
            }

            if (vj.acquireVJD(rID)){
                System.out.println("Successfully acquired vJoy Device");
            } else {
                System.out.println("Failed to acquire vJoy Device");
                System.exit(1);
            }

            ServerSocket server = new ServerSocket(Protocol.PORT);

            System.out.println("Waiting for connections...");
            Socket client = server.accept();
            System.out.println("Connection found");

            DataInputStream reader = new DataInputStream(client.getInputStream());
            while (client.isConnected())
                parse(reader.readUTF());

            reader.close();
            server.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Connection interrupted");
        }
    }

    public static void parse(String msg){
        System.out.println("[Server<-Client] " + msg);

        String[] split = msg.split(" ", 2);
        String code = split[0];
        float value = Float.parseFloat(split[1]);

        if (Protocol.BINDINGS.containsKey(code)){   //axis
            vj.setAxis((long) value, rID, Protocol.BINDINGS.get(code));
        } else if (code.equals("pov")){
            vj.setContPov((long) value, rID, 0);
        } else try {
            vj.setBtn((value==1), rID, Integer.parseInt(split[0]));
        } catch (NumberFormatException ignored){
            System.out.println("Failed to parse message: " + msg);
        }
    }
}
