import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// This class handles individual clients, implementing the Runnable interface.
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private Chatters clientes;
    private String currentGroup; // Track current group the client is in

    public ClientHandler(Socket socket, Chatters clientes) {
        clientSocket = socket;
        this.clientes = clientes;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while (true) {
                out.println("Ingrese su nombre de usuario");
                clientName = in.readLine();
                if (clientes.add(clientName, out)) {
                    out.println("Bienvenido " + clientName);
                    clientes.sendToAll(clientName + " se ha unido al chat");
                    break;
                } else {
                    out.println("El nombre de usuario ya existe");
                }
            }

            // Default state is global chat
            out.println("Ahora estás en el chat global. Usa /group [nombre_grupo] para cambiar a un grupo.");

            while (true) {
                message = in.readLine();
                if (message.startsWith("/group")) {
                    handleGroupCommand(message);
                } else if (message.startsWith("/creategroup")) {
                    handleCreateGroupCommand(message);
                } else if (message.equals("exit")) {
                    break;
                } else {
                    if (currentGroup == null) {
                        // Send message to global chat if not in any group
                        clientes.sendToAll(clientName + ": " + message);
                    } else {
                        // Send message to the current group
                        clientes.sendToGroup(currentGroup, clientName + " (" + currentGroup + "): " + message);
                    }
                }
            }

            if (currentGroup != null) {
                // Remove from current group if in one
                clientes.removeUserFromGroup(currentGroup, new Person(clientName, out));
            }

            clientes.remove(clientName);
            clientSocket.close();
            clientes.sendToAll(clientName + " ha salido del chat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleGroupCommand(String message) {
        String[] parts = message.split("\\s+", 2);
        if (parts.length == 2) {
            String groupName = parts[1];
            if (!clientes.groupExists(groupName)) {
                out.println("El grupo '" + groupName + "' no existe.");
            } else {
                if (currentGroup != null) {
                    // Remove from current group if in one
                    clientes.removeUserFromGroup(currentGroup, new Person(clientName, out));
                    out.println("Saliste del grupo '" + currentGroup + "'.");
                }
                // Join the new group
                clientes.addUserToGroup(groupName, new Person(clientName, out));
                currentGroup = groupName;
                out.println("Ahora estás en el grupo '" + groupName + "'.");
            }
        } else {
            out.println("Uso: /group [nombre_grupo]");
        }
    }
    
    private void handleCreateGroupCommand(String message) {
        String[] parts = message.split("\\s+", 2);
        if (parts.length == 2) {
            String groupName = parts[1];
            if (!clientes.groupExists(groupName)) {
                clientes.createGroup(groupName);
                out.println("Grupo '" + groupName + "' creado exitosamente.");
            } else {
                out.println("El grupo '" + groupName + "' ya existe.");
            }
        } else {
            out.println("Uso: /creategroup [nombre_grupo]");
        }
    }
}

