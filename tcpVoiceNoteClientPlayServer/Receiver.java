package tcpVoiceNoteClientPlayServer;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(12345);

            // Wait for a client connection
            Socket socket = serverSocket.accept();

            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            
            Reproducer reproducer =new Reproducer(dataInputStream);
            
            Thread playerThread= new Thread(reproducer);
            playerThread.start();

            

      /*       // Close streams and socket
            dataInputStream.close();
            socket.close();
            serverSocket.close();
            

            System.out.println("Receiver finished."); */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
