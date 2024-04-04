package tcpStream;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    public void sendBytes(byte[] myByteArray, int start, int len,Socket socket) throws IOException {
        if (len < 0)
            throw new IllegalArgumentException("Negative length not allowed");
        if (start < 0 || start >= myByteArray.length)
            throw new IndexOutOfBoundsException("Out of bounds: " + start);
        // Other checks if needed.
    
        // May be better to save the streams in the support class;
        // just like the socket variable.
        OutputStream out = socket.getOutputStream(); 
        DataOutputStream dos = new DataOutputStream(out);
    
        dos.writeInt(len);
        if (len > 0) {
            dos.write(myByteArray, start, len);
        }
    }
    
    
    
}


    

