package recordTwoAudiosAndMixthemForma1;

import java.io.*;
import java.net.*;

import javax.sound.sampled.*;

public class PlayerRecording {
    
    private AudioFormat format;
    private byte[] audioData; //datos de entrada
	private SourceDataLine out;  //salida a la tarjeta de audio
    private AudioInputStream in;
    private AudioInputStream in2;

    public PlayerRecording(AudioFormat format) {
        this.format=format;       
   	}

    public void initiateAudio(byte[] audioData,byte[] audioData2) {
		try {
			in = new AudioInputStream(new ByteArrayInputStream(audioData), format,
                    audioData.length / format.getFrameSize());
            in2 = new AudioInputStream(new ByteArrayInputStream(audioData2), format,
                    audioData2.length / format.getFrameSize());
            
            // Abrir línea de salida de audio
            out = AudioSystem.getSourceDataLine(format);
            out.open(format);
            out.start(); // Comenzar la reproducción de audio
            playAudio();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void playAudio() {
		byte[] buffer = new byte[1024];
        byte[] buffer2 = new byte[1024];
        
		try {
			
            System.out.println("Reproduciendo...");
            int count= in.read(buffer);
            int count2= in2.read(buffer2);
			while (count != -1) {			
					out.write(buffer, 0, count);
                    out.write(buffer2,0,count2);
                    count= in.read(buffer);
                    count2 =in2.read(buffer2);				
			}            
            out.drain();
            out.stop();
            out.close();
            in.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}