package Audio;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class AudioSocketConnector extends Thread{

    private static final String SERVER_IP = "localhost";
    private static final int PORT = 6790;
    private String username;
    public AudioSocketConnector(String username){
        this.username=username;
    }
    public void run(){        
        try {
                /*NUEVO AUDIO */
            DataInputStream din; //del servidor al cliente
            DataOutputStream dos;
            AudioManager audioManager;
            Socket socket;
            socket = new Socket(SERVER_IP,PORT);
            System.out.println("CONECTADO al socket de audio");
            /*NUEVO */
            din=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());



            /*PRIMERO ENVIAMOS el nombre de usuario que se nos acepto */
            PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
            out.println(username);
            /*Y luego establecemos el audio managaer */
            audioManager=new AudioManager(din, dos);
            /*Iniciamos hilos que envian y reciben audio */
            audioManager.initPlayer();
            audioManager.initRecorder();

          

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         

    }
    
}
