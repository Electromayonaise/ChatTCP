package tcpStream;

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
            while (1==1){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Check if there is data available to read
                int availableBytes = dataInputStream.available();
                System.out.println("Available bytes to read: " + availableBytes);

                // Receive bytes if available
                if (availableBytes > 0) {
                    int length = dataInputStream.readInt(); // Read the length first
                    byte[] receivedBytes = new byte[length];
                    dataInputStream.readFully(receivedBytes); // Read the bytes

                    // Print received bytes
                    System.out.println("Received bytes:");
                    for (byte b : receivedBytes) {
                        System.out.print((char) b + " "); // Convert bytes to characters
                    }
                    System.out.println();
                } else {
                    System.out.println("No data available to read.");
                }
            }

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
