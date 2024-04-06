import java.util.Set;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

//objeto que representa un cliente o usuario o persona en el chat
public class Person {
    private String name; //nombre de usuario
    PrintWriter out;    //canal para enviarle mensajes a ese usuario
    private String lastPrivateMessage; //ultimo mensaje privado recibido
    private String lastPrivateMessageSender; //nombre del usuario que envio el ultimo mensaje privado
    /*Nuevo */
    private DataOutputStream dos; //Canal para enviar audios
    private DataInputStream dis; //canal para recibir audios
    

    public Person(String name, PrintWriter out,DataOutputStream don, DataInputStream din){
        this.name = name;
        this.out  = out;
    }
   
    public String getName() {
        return name;
    }
    
    public PrintWriter getOut() {
        return out;
    }

    public void setLastPrivateMessage(String sender, String message) {
        this.lastPrivateMessageSender = sender;
        this.lastPrivateMessage = message;
    }

    public String getLastPrivateMessageSender() {
        return lastPrivateMessageSender;
    }

    public String getLastPrivateMessage() {
        return lastPrivateMessage;
    }
    public DataInputStream getDis(){
        return dis;
    }
    public DataOutputStream getDos(){
        return dos;
    }

    
}