package RecordAndReproduceInThreads;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.sound.sampled.*;


public class RecordAndReproduceAudioWithThreads {
    private Recorder recorder;
    private Reproducer reproducer;
    public static byte[] buffer= new byte[4096];

    public static void main(String[] args) {
        try {
            Recorder recorder=new Recorder();
            Reproducer reproducer=new Reproducer();

            Thread recorderThread= new Thread(recorder);
           
            Thread reproducerThread= new Thread(reproducer);
            
            recorder.setReproducer(reproducer);
            
            
            recorderThread.start();

            

            reproducerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}