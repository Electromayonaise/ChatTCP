package tcpRecordClientPlayServer;

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
    
    Reproducer(){
        format = new AudioFormat(44100, 16, 2, true, true);
        
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
                byte[] byteArray=queue.poll();
                if(byteArray!=null){
                    speakers.write(byteArray, 0, byteArray.length);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
                
            }
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
