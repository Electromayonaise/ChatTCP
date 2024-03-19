import java.util.Set;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Chatters{
   //tendra una lista de personas que seran nuestros clientes
   //cada persona tiene un nombre y un canal para enviarle mensajes

    private Set<Person> clientes = new HashSet<>();
     
    public Chatters(){
        //inicializar la lista de clientes
        

    }
    //metodo para verificar si un usuario existe, retorna true si existe
    public boolean exists(String name){
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    //metodo para agregar un usuario nuevo
    public boolean add(String name, PrintWriter out){
        if (!exists(name)) {
            clientes.add(new Person(name, out));
            return true;
        }
        return false;
    }

    //metodo para eliminar un usuario
    public void remove(String name){
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                clientes.remove(p);
                break;
            }
        }
    }

    //metodo para enviar un mensaje a todos los usuarios  
    public void sendToAll(String message){
        for (Person p : clientes) {
            p.getOut().println(message);
        }
    }

}