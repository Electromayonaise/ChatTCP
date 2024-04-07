import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenerCallPort extends Thread{
    private Chatters clientes;
    private static int CALL_PORT= 6790;
    public ListenerCallPort(Chatters clientes){
        this.clientes=clientes;
    }
    public void run(){

        try{
            ServerSocket serverSocket = new ServerSocket(CALL_PORT);
            System.out.println("Servidor de llamadas iniciado. Esperando clientes...");
            while(true){
                Socket socket=serverSocket.accept();
                System.out.println("Nuevo cliente conectado: al canal de llamadas " + socket);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientName=in.readLine();
                Person person=clientes.getPerson(clientName);
                person.setDis(new DataInputStream(socket.getInputStream()));
                person.setDos(new DataOutputStream(socket.getOutputStream()));
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}