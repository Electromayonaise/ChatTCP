package Audio;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class AudioSocketConnector extends Thread{

    private static final String SERVER_IP = "172.20.10.11";
    private static final int PORT = 6790;
    private String username;
    private AudioManager audioManager;
    
    public AudioSocketConnector(String username){
        this.username=username;
    }
    public AudioManager getAudioManager(){
        return this.audioManager;
    }
    public void run(){        
        try {
                /*NUEVO AUDIO */
            DataInputStream din; //del servidor al cliente
            DataOutputStream dos;
            
            Socket socket;
            socket = new Socket(SERVER_IP,PORT);
            
            /*NUEVO */
            din=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());



            /*PRIMERO ENVIAMOS el nombre de usuario que se nos acepto */
            PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
            out.println(username);
            /*Y luego establecemos el audio managaer */
            this.audioManager=new AudioManager(din, dos);
            /*Iniciamos el hilo que solo recibe audio y lo reproduce*/
            audioManager.initPlayer();
            
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

         

    }
    
}
