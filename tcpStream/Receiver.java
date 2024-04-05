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

            // Receive bytes
            int length = dataInputStream.readInt(); // Read the length first
            byte[] receivedBytes = new byte[length];
            dataInputStream.readFully(receivedBytes); // Read the bytes

            // Print received bytes
            System.out.println("Received bytes:");
            for (byte b : receivedBytes) {
                System.out.print((char) b + " "); // Convert bytes to characters
            }
            System.out.println();

            // Close streams and socket
            dataInputStream.close();
            socket.close();
            serverSocket.close();

            System.out.println("Bytes received successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
