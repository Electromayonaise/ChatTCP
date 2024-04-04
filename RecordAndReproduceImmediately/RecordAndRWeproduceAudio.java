package RecordAndReproduceImmediately;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;


public class RecordAndRWeproduceAudio {

    public static void main(String[] args) {
        try {
            // Open microphone line for recording
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Open speakers line for playback
            SourceDataLine speakers = AudioSystem.getSourceDataLine(format);
            speakers.open(format);
            speakers.start();

            byte[] buffer = new byte[4096];
            int bytesRead;

            while (true) {
                // Read from microphone and write to speakers
                bytesRead = line.read(buffer, 0, buffer.length);
                
                speakers.write(buffer, 0, bytesRead);
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}