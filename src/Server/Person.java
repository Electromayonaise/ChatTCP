import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import java.io.ByteArrayInputStream;
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

    private SourceDataLine out2;  //salida a la tarjeta de audio
    private AudioInputStream in; //entrada de la tarjeta de audio

    public Person(String name, PrintWriter out){
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
    
    public void initiateAudio(AudioFormat format, byte[] audioData) {
		try {

			in = new AudioInputStream(new ByteArrayInputStream(audioData), format,
                    audioData.length / format.getFrameSize());
            // Abrir línea de salida de audio
            out2 = AudioSystem.getSourceDataLine(format);
            out2.open(format);
            out2.start(); // Comenzar la reproducción de audio
            playAudio();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void playAudio() {
		byte[] buffer = new byte[1024];
        int count;
		try {
			
            System.out.println("Reproduciendo...");
			while ((count = in.read(buffer)) != -1) {			
					out2.write(buffer, 0, count);				
			}            
            out2.drain();
            out2.stop();
            out2.close();
            in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
    
}