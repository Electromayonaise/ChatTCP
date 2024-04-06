import java.io.*;
import java.net.*;

import Audio.AudioManager;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 6789;
    

    public static void main(String[] args) {
        BufferedReader in; //del servidor al cliente
        PrintWriter out; //del cliente al servidor
        /*NUEVO AUDIO */
        DataInputStream din; //del servidor al cliente
        DataOutputStream dos;
        AudioManager audioManager;
        
        
        try {
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("Conectado al servidor.");

            String message;
            //canal de entrada para el usuario
            
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            
            
            
            //usando el socket, crear los canales de entrada in y salida out
            in =new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out =new PrintWriter(socket.getOutputStream(),true);

            
            
            String msg;

            do {
                
                //solicitar al usuario un alias, o nombre y enviarlo al servidor
                msg=in.readLine();
                System.out.println(msg);

                
                //no debe salir de este bloque hasta que el nombre no sea aceptado
                //al ser aceptado notificar, de lo contrario seguir pidiendo un alias
                out.println(userInput.readLine());
                msg=in.readLine();
                System.out.println(msg);
                
            } while (msg.equals("El nombre de usuario ya existe"));
                 
            //creamos el objeto Lector e iniciamos el hilo que nos permitira estar atentos a los mensajes
            //que llegan del servidor
            //inicar el hilo
            
            Lector lector=new Lector(in);
            Thread theadDeLectura=new Thread(lector);
            try {
                theadDeLectura.start();
            } catch (Exception threadException) {
                threadException.printStackTrace();
            }
            /*NUEVO */
            din=new DataInputStream(socket.getInputStream());
            dos=new DataOutputStream(socket.getOutputStream());
            audioManager=new AudioManager(din, dos); 
            /*NUEVO */
            


            //estar atento a la entrada del usuario para poner los mensajes en el canal de salida out
            //en el hilo principal solo enviamos lo que escriba el usuario
            String msgFromUser;
            while(true){
                msgFromUser=userInput.readLine();
                out.println(msgFromUser);
            }


            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

