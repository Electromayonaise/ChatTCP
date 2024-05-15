import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;

public class Server {
    

     public static void main(String[] args) {
        
        int PORT = 6789;
        Chatters clientes = new Chatters(); //lista de clientes
        /*NUEVO */
        CallParticipants callParticipants=new CallParticipants(); //listas de participante de llamda
        ServerAudioManager serverAudioManager= new ServerAudioManager(clientes,callParticipants);
        Thread listenerCallPort= new ListenerCallPort(clientes,callParticipants);
        listenerCallPort.start();
        /*NUEVO */



        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciado. Esperando clientes...");
            
            
            

            while (true) {
                Socket clientSocket = serverSocket.accept();
                
                
                
                System.out.println("Nuevo cliente conectado: " + clientSocket);
                //crea el objeto para gestionar al cliente y le envia la informacion necesaria
                //inicia el hilo para ese cliente
                ClientHandler clientThread = new ClientHandler(clientSocket,clientes,serverAudioManager);
                Thread thread = new Thread(clientThread);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

   
}
    

   


