package tcpConnection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.midi.Soundbank;

public class Client {
    private static int PORT=6789;
    private static String IP_SERVER="localhost";

    public static void main(String[] args) {
        try {
            
            Socket socket=new Socket(IP_SERVER,PORT);
            
            System.out.println("conectado");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
}
