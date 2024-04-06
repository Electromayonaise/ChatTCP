package tcpVoiceNoteClientPlayServer;

import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder implements Runnable{
    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine line;
    private int bytesRead;
    private byte[] buffer;
    private DataOutputStream dos;
    private int duration;
    
    
    
    public Recorder(DataOutputStream dos, int duration) {
        format = new AudioFormat(44100, 16, 2, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);
        buffer=new byte[4096];
        this.dos=dos;
        this.duration=duration;
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            line = null;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    

    public void run() {
        try {
            line.open(format);
            line.start();
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < TimeUnit.SECONDS.toMillis(duration)) {
                bytesRead = line.read(buffer, 0, buffer.length);
                if(bytesRead>0){
                    byte[] bufferCopy = Arrays.copyOfRange(buffer, 0, bytesRead);
                    dos.writeInt(bufferCopy.length);
                    dos.write(bufferCopy);
                }
                Thread.sleep(1);
               
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }



}