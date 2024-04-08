import java.io.*;
import java.net.*;

import Audio.AudioManager;
import Audio.AudioSocketConnector;

public class Client {
    private static final String SERVER_IP = "172.20.10.11";
    private static final int PORT = 6789;
    

    public static void main(String[] args) {
        BufferedReader in; //del servidor al cliente
        PrintWriter out; //del cliente al servidor
        
        
        
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
            String input;
            do {
                
                //solicitar al usuario un alias, o nombre y enviarlo al servidor
                msg=in.readLine();
                System.out.println(msg);

                
                //no debe salir de este bloque hasta que el nombre no sea aceptado
                //al ser aceptado notificar, de lo contrario seguir pidiendo un alias
                input=userInput.readLine();
                out.println(input);
                msg=in.readLine();
                System.out.println(msg);
                
            } while (msg.equals("El nombre de usuario ya existe"));

            /*NUEVO una vez ya se tiene un nombre de usuario nos podemos conectar al flujo de lllamadas */
            AudioSocketConnector audioSocketConnector=new AudioSocketConnector(input);
            Thread AudioSocketConnector= (Thread) audioSocketConnector;
            AudioSocketConnector.start();
            try {
                audioSocketConnector.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AudioManager audioManager=audioSocketConnector.getAudioManager();
        

            /*NUEVO */
                 
            //creamos el objeto Lector e iniciamos el hilo que nos permitira estar atentos a los mensajes
            //que llegan del servidor
            //inicar el hilo
            
            Lector lector=new Lector(in);
            lector.setAudioManager(audioManager);
            Thread theadDeLectura=new Thread(lector);
            try {
                theadDeLectura.start();
            } catch (Exception threadException) {
                threadException.printStackTrace();
            }
           
            
           
            
            


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

