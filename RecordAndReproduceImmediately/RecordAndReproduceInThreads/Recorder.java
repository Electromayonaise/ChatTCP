package RecordAndReproduceImmediately.RecordAndReproduceInThreads;

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
   
    
    
    public Recorder() {
        format = new AudioFormat(44100, 16, 2, true, true);
        info = new DataLine.Info(TargetDataLine.class, format);
        buffer=new byte[4096];
        
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            line = null;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public synchronized byte[] getBuffer(){
        return buffer;
    }
    public synchronized int getBytesRead(){
        return bytesRead;
    }

    public void run() {
        try {
            line.open(format);
            line.start();
            while (true) {
                bytesRead = line.read(buffer, 0, buffer.length);
               
            }
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }



}