package tcpRecordClientPlayServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Sender {
    public static void main(String[] args) {
        try {
            // Connect to the receiver
            Socket socket = new Socket("localhost", 12345);

            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            Recorder recorder=new Recorder(dataOutputStream);
            
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



    

