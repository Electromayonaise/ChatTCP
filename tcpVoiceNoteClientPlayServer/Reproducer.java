package tcpVoiceNoteClientPlayServer;

import java.io.DataInputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Reproducer implements Runnable{
    private AudioFormat format;
    private SourceDataLine speakers;
    
    private Queue<byte[]> queue;

    private DataInputStream dis;
    
    Reproducer(DataInputStream dis){
        format = new AudioFormat(44100, 16, 2, true, true);
        this.dis=dis;
        queue=new LinkedList<>();
        try {
            speakers = AudioSystem.getSourceDataLine(format);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            speakers=null;
        }
    }
    public void addBytesToQueue(byte[] bytes){
    
        queue.add(bytes);
    }

    public void run(){
        try {
            speakers.open(format);
            speakers.start();
            while (true) {
                
                int availableBytes = dis.available();

                if (availableBytes > 0) {
                    int length = dis.readInt(); // Read the length first
                    byte[] receivedBytes = new byte[length];
                    dis.readFully(receivedBytes); // Read the bytes
                    speakers.write(receivedBytes,0,receivedBytes.length);

                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
                
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        
    }
    
}
