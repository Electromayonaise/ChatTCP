import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Clase que maneja la comunicacion con un cliente
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private Chatters clientes;
    private String currentGroup; 

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

            // El estado default del cliente es el chat global
            out.println("Ahora estás en el chat global. ");
            out.println("1. Usa /group [nombre_grupo] para cambiar a un grupo, o /creategroup [nombre_grupo] para crearlo.");
            out.println("2. Usa /msg [nombre_usuario] para enviar un mensaje privado a un usuario.");
            out.println("3. Usa /r para responder a tu ultimo mensaje privado sin necesidad de escribir el nombre de usuario.");
            out.println("4. Usa /exitgroup para salir del grupo actual y volver al chat global.");
            out.println("5. Usa /exit para salir del chat.");

            while (true) {
                message = in.readLine();
                if (message.startsWith("/group")) {
                    handleGroupCommand(message);
                } else if (message.startsWith("/creategroup")) {
                    handleCreateGroupCommand(message);
                } else if (message.equals("/exitgroup")) {
                    handleExitGroupCommand();
                }else if (message.startsWith("/msg")) {
                    handlePrivateMessageCommand(message);
                } else if (message.startsWith("/r")) {
                    handleReplyCommand(message);
                } else if (message.equals("/exit")) {
                    break;
                } else {
                    if (currentGroup == null) {
                        // Mandar mensaje a todos los clientes, si no esta en un grupo
                        clientes.sendToAll(clientName + ": " + message);
                    } else {
                        // Mandar mensaje a un grupo
                        clientes.sendToGroup(currentGroup, clientName + " (" + currentGroup + "): " + message);
                    }
                }
            }

            if (currentGroup != null) {
                // Quitarlo del grupo actual si esta en uno
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
                    // Quitarlo del grupo actual si esta en uno, antes de unirse a otro
                    clientes.removeUserFromGroup(currentGroup, new Person(clientName, out));
                    out.println("Saliste del grupo '" + currentGroup + "'.");
                }
                // Agregarlo al grupo
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

    private void handleExitGroupCommand() {
        if (currentGroup != null) {
            // Quitarlo del grupo actual si esta en uno
            clientes.removeUserFromGroup(currentGroup, new Person(clientName, out));
            out.println("Has salido del grupo '" + currentGroup + "'.");
            currentGroup = null; // Setiar el grupo actual a null, para volver al chat global
        } else {
            out.println("No estás en ningún grupo.");
        }
    }

    private void handlePrivateMessageCommand(String message) {
        String[] parts = message.split("\\s+", 3);
        if (parts.length == 3) {
            String recipient = parts[1];
            String privateMessage = parts[2];
            if (clientes.exists(recipient)) {
                // Enviar mensaje privado
                clientes.sendPrivateMessage(recipient, clientName, clientName + " (privado): " + privateMessage);
            } else {
                out.println("El usuario '" + recipient + "' no existe o no está en línea.");
            }
        } else {
            out.println("Uso: /msg [nombre_usuario] [mensaje]");
        }
    }
    
    // aqui cabe aclarar que el handleReply depende de una comunicacion 1 a 1, es decir, que solo se puede usar si se ha recibido un mensaje privado que aun no ha sido respondido
    private void handleReplyCommand(String message) {
        // Obtener el ultimo mensaje privado y su remitente
        String lastSender = clientes.getLastPrivateMessageSender(clientName);
        String lastMessage = clientes.getLastPrivateMessage(clientName);
        
        if (lastSender != null && message != null) {
            // Mandar mensaje privado al remitente del ultimo mensaje privado recibido por el usuario
            clientes.sendPrivateMessage(lastSender, clientName, clientName + " (respuesta): " + message);
        } else {
            out.println("No hay mensajes privados anteriores para responder.");
        }
    }

}
