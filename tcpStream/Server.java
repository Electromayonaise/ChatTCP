package tcpStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static int PORT=6789;
    
    public static void main(String[] args) {
        
        ServerSocket socket;
        try {
            socket = new ServerSocket(PORT);
            System.out.println("SERVIDOR EN ESCUCHA");
            socket.accept();



        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

    }
    public static void receiveBits(){
        
    }
    
}
