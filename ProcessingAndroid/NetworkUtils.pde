import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public static class NetworkUtils {
    public static Socket socket = null;
    public static Scanner socketReader;
    public static PrintWriter socketWriter;
    public static int socketID;
    public static byte movementData;

    public static void initializeSocket(String ip, int port, String name){
        if(socket != null){
            return;
        }
        try{
            socket = new Socket(ip, port);
            socketReader = new Scanner(socket.getInputStream());
            socketWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioe){
            System.out.println(ioe);
        }
        socketWriter.println(name);
        String xd = socketReader.nextLine(); 
        print(xd);
        int ID = Integer.parseInt(xd);
        println(ID);
        if(ID < 0) {
            try {
                socket.close();
            }catch(IOException ioe){}
        } else{
            socketID = ID;
            movementData = 0;
        }
        System.out.println("done");
    }

    public static void sendUpdate(int id, boolean click){
        if(click){
            movementData = byte(movementData | (1 << id));
        } else{
            movementData = byte(movementData ^ (1 << id));
        }
        println(socketID, movementData);
        socketWriter.print(socketID + "");
        socketWriter.println(Byte.toString(movementData));
    }

}
