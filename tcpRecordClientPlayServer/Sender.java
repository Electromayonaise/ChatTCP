package tcpRecordClientPlayServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;

public class Sender {
    private static  int SAMPLE_RATE = 16000; // Frecuencia de muestreo en Hz
    private static  int SAMPLE_SIZE_IN_BITS = 16; // Tamaño de muestra en bits
    private static  int CHANNELS = 1; // Mono
    private static  boolean SIGNED = true; // Muestras firmadas
    private static  boolean BIG_ENDIAN = false; // Little-endian
    public static void main(String[] args) {
        try {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
            // Connect to the receiver
            Socket socket = new Socket("localhost", 12345);

            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            Recorder recorder=new Recorder(dataOutputStream,format);
            
            Thread recorderThread= new Thread(recorder);
            recorderThread.start();

            /*  Close streams and socket
            dataOutputStream.close();
            socket.close();
            */

            System.out.println("Bytes sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



    

