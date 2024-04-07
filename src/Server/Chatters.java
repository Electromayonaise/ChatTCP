import java.util.Set;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Chatters {
    // tendra una lista de personas que seran nuestros clientes
    // cada persona tiene un nombre y un canal para enviarle mensajes

    private Set<Person> clientes = new HashSet<>();
    private Map<String, Set<Person>> groups = new HashMap<>();

    public Chatters() {
        // inicializar la lista de clientes

    }

    // metodo para verificar si un usuario existe, retorna true si existe
    public boolean exists(String name) {
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // metodo para revisar si un grupo existe
    public boolean groupExists(String groupName) {
        return groups.containsKey(groupName);
    }

    // metodo para agregar un usuario nuevo
    public boolean add(String name, PrintWriter out) {
        if (!exists(name)) {
            clientes.add(new Person(name, out));
            return true;
        }
        return false;
    }

    // metodo para eliminar un usuario
    public void remove(String name) {
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                clientes.remove(p);
                break;
            }
        }
    }

    // metodo para enviar un mensaje a todos los usuarios
    public void sendToAll(String name, String message) {
        Set<Person> allExceptSelf = new HashSet<>(clientes);
        allExceptSelf.removeIf(p -> p.getName().equals(name));
        for (Person p : allExceptSelf) {
            p.getOut().println(message);
        }
    }

    // metodo para enviar un mensaje a un grupo
    public void sendToGroup(String groupName, String message) {
        if (groups.containsKey(groupName)) {
            for (Person p : groups.get(groupName)) {
                p.getOut().println(message);
            }
        }
    }

    // metodo para enviar un mensaje a un usuario
    public void sendPrivateMessage(String recieverName, String senderName, String message) {
        for (Person p : clientes) {
            if (p.getName().equals(recieverName)) {
                p.getOut().println(message);
                p.setLastPrivateMessage(senderName, message);
            }
        }
    }

    // Method to get the last private message sender for a specific user
    public String getLastPrivateMessageSender(String name) {
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                return p.getLastPrivateMessageSender();
            }
        }
        return null;
    }

    // Method to get the last private message for a specific user
    public String getLastPrivateMessage(String name) {
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                return p.getLastPrivateMessage();
            }
        }
        return null;
    }

    // Method to create a group
    public void createGroup(String groupName) {
        groups.put(groupName, new HashSet<>());
    }

    // Method to add a user to a group
    public void addUserToGroup(String groupName, Person person) {
        if (groups.containsKey(groupName)) {
            groups.get(groupName).add(person);
        }
    }

    // Method to remove a user from a group
    public void removeUserFromGroup(String groupName, Person person) {
        if (groups.containsKey(groupName)) {
            groups.get(groupName).remove(person);
        }
    }

    // Method to get users in a group
    public Set<Person> getUsersInGroup(String groupName) {
        return groups.getOrDefault(groupName, new HashSet<>());
    }

    // Method to get the group a user is in
    public String getGroupForUser(String name) {
        for (Map.Entry<String, Set<Person>> entry : groups.entrySet()) {
            for (Person p : entry.getValue()) {
                if (p.getName().equals(name)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    /*EXTRA */
    public Person getPerson(String name) {
        Person person=null;
        for (Person p : clientes) {
            if (p.getName().equals(name)) {
                person=p;
                return person;
            }
        }
        return person;
    }

}