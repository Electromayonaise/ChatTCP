import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

// Clase que maneja la comunicacion con un cliente
class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String clientName;
    private Chatters clientes;
    private String currentGroup;
    private boolean isRecording = false;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;
    private ByteArrayOutputStream byteOutputStream;

    /* EXTRA PARA AUDIO*/
    private DataInputStream din;
    private DataOutputStream don;
    private ServerAudioManager serverAudioManager;
    

    public ClientHandler(Socket socket, Chatters clientes, ServerAudioManager serverAudioManager) {
        clientSocket = socket;
        this.clientes = clientes;
        this.serverAudioManager=serverAudioManager;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            bufferedOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            bufferedInputStream = new BufferedInputStream(clientSocket.getInputStream());
            byteOutputStream = new ByteArrayOutputStream();

            /*Extra para llamadas */
            din= new DataInputStream(clientSocket.getInputStream());
            don= new DataOutputStream(clientSocket.getOutputStream());
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
                    clientes.sendToAll("System", clientName + " se ha unido al chat");
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
            out.println("6. Usa /voicenote [duracion] [nombre_usuario] para enviar una nota de voz a un usuario");
            out.println("7. Usa /gvoicenote [duracion]  para enviar una nota de voz al grupo actual");
            out.println("8. Usa /call [nombre_usuario]  para iniciar una llamada con un usuario");
            out.println("9. Usa /gcall  para unirse una llamada con el grupo actual");
            out.println("10. Usa /endcall para finalizar la llamada actual");
            while (true) {
                message = in.readLine();
                if (message.startsWith("/group")) {
                    handleGroupCommand(message);
                } else if (message.startsWith("/creategroup")) {
                    handleCreateGroupCommand(message);
                } else if (message.equals("/exitgroup")) {
                    handleExitGroupCommand();
                } else if (message.startsWith("/msg")) {
                    handlePrivateMessageCommand(message);
                } else if (message.startsWith("/r")) {
                    handleReplyCommand(message);
                } else if (message.startsWith("/voicenote")) {
                    handleVoiceNote(message);
                } else if(message.startsWith("/gvoicenote")){
                    handleGrupalVoiceNote(message);
                }else if(message.startsWith("/call")){
                    handleCall(message);
                }else if(message.startsWith("/gcall")){
                    handleGrupalCall();
                }else if(message.startsWith("/endcall")){
                    handleEndCall();
                }
                else if (message.equals("/exit")) {
                    break;
                } else {
                    if (currentGroup == null) {
                        // Mandar mensaje a todos los clientes, si no esta en un grupo
                        clientes.sendToAll(clientName, clientName + ": " + message);
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
            clientes.sendToAll("System", clientName + " ha salido del chat");
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

    // aqui cabe aclarar que el handleReply depende de una comunicacion 1 a 1, es
    // decir, que solo se puede usar si se ha recibido un mensaje privado, y solo se
    // puede responder a el remitente del ultimo mensaje privado recibido
    private void handleReplyCommand(String message) {
        // Obtener el ultimo mensaje privado y su remitente
        String lastSender = clientes.getLastPrivateMessageSender(clientName);
        String lastMessage = clientes.getLastPrivateMessage(clientName);
        message = message.substring(2).trim(); // Eliminar el comando /r del mensaje
        if (lastSender != null && message != null) {
            // Mandar mensaje privado al remitente del ultimo mensaje privado recibido por
            // el usuario
            clientes.sendPrivateMessage(lastSender, clientName, clientName + " (respuesta): " + message);
        } else {
            out.println("No hay mensajes privados anteriores para responder.");
        }
    }

    private void handleAudioCommand() {
        try {
            if (!isRecording) {
                startRecording();
                out.println("Audio recording started. Press enter again to stop recording.");
            } else {
                stopRecording();
                byte[] audioData = byteOutputStream.toByteArray();
                sendAudioData(audioData);
                out.println("Audio sent successfully. Enter /play to listen.");
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            out.println("Error occurred while handling audio command: LineUnavailableException");
        }
    }

    private void startRecording() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(info);
        targetLine.open(format);
        targetLine.start();
        isRecording = true;
    
        Thread recordThread = new Thread(() -> {
            try {
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while (isRecording && (bytesRead = targetLine.read(buffer, 0, buffer.length)) != -1) {
                    byteOutputStream.write(buffer, 0, bytesRead);
                }
                byte[] audioData = byteOutputStream.toByteArray();
                AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), format, audioData.length);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, bufferedOutputStream);
                targetLine.stop();
                targetLine.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        recordThread.start();
    }
     

    private void stopRecording() {
        isRecording = false;
    }

    private void sendAudioData(byte[] audioData) {
        try {
           // Write the length of audio data to the output stream
            bufferedOutputStream.write(ByteBuffer.allocate(4).putInt(audioData.length).array());

            // Write audio data to the output stream
            bufferedOutputStream.write(audioData);
            bufferedOutputStream.flush(); // Flush the output stream
        } catch (IOException e) {
            e.printStackTrace();
            out.println("Error occurred while sending audio data.");
        }
    }
    

    private void handlePlayCommand() {
        try {
            // Receive audio data from the server and play it
            // Read the length of audio data from the input stream
            byte[] lengthBuffer = new byte[4];
            bufferedInputStream.read(lengthBuffer);
            int audioLength = ByteBuffer.wrap(lengthBuffer).getInt();
    
            // Read audio data from the input stream
            byte[] audioData = new byte[audioLength];
            bufferedInputStream.read(audioData, 0, audioLength);
    
            // Play the audio
            playAudio(audioData);
        } catch (IOException e) {
            e.printStackTrace();
            out.println("Error occurred while handling play command: IOException");
        }
    }

    private void playAudio(byte[] audioData) {
        try {
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
            AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioData), format, audioData.length);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(format);
            sourceLine.start();

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                sourceLine.write(buffer, 0, bytesRead);
            }

            sourceLine.drain();
            sourceLine.close();
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
            out.println("Error occurred while playing audio.");
        }
    }




    /*PARTE NUEVA NUEVA NUEVA */
    /*posibles erorres, numeros negativos */
    private void handleVoiceNote(String message){
        try {
            String[] parts = message.split("\\s+", 3);
            int duration=Integer.parseInt(parts[1]);
            String targetUser=parts[2];
            out.println("Nota de voz dirigida a "+targetUser+" iniciada."+"Tiene una duracion de "+ duration+" segundos");
            
        } catch (IndexOutOfBoundsException e) {
            out.println("ERROR en los argumentos del comando");
        } catch (NumberFormatException e){
            out.println("ERROR: La duracion no es valida");
        }
        
    }

    private void handleGrupalVoiceNote(String message){
        String[] parts = message.split("\\s+", 2);
        int duration=Integer.parseInt(parts[1]);
        out.println("Nota de voz con una duracion de "+duration+" iniciada");
        
    }

    private void handleCall(String message){
        System.out.println("356");
        String[] parts = message.split("\\s+", 2);
        String targetUser=parts[1];
        System.out.println("358");
        boolean flag=serverAudioManager.addCall(clientName,targetUser);
        if(flag){
            out.println("Llamada dirigida a "+targetUser+" iniciada");
        }else{
            out.println("Llamada no iniciada, algo fallo");
        }
        

        


    }
    private void handleGrupalCall(){
        out.println("Llamada grupal iniciada");
        
    }
    private void handleEndCall(){
        out.println("llamada actual finalizada");
    }

}
