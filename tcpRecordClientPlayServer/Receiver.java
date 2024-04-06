package tcpRecordClientPlayServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;

public class Receiver {
    private static  int SAMPLE_RATE = 16000; // Frecuencia de muestreo en Hz
    private static  int SAMPLE_SIZE_IN_BITS = 16; // Tamaño de muestra en bits
    private static  int CHANNELS = 1; // Mono
    private static  boolean SIGNED = true; // Muestras firmadas
    private static  boolean BIG_ENDIAN = false; // Little-endian
    public static void main(String[] args) {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(12345);

            // Wait for a client connection
            Socket socket = serverSocket.accept();

            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            
            Reproducer reproducer =new Reproducer(dataInputStream,format);
            
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
