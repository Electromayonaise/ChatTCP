package RecordAndReproduceImmediately.RecordAndReproduceInThreads;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Reproducer implements Runnable{
    private AudioFormat format;
    private SourceDataLine speakers;
    private Recorder recorder;
    
    Reproducer(Recorder recorder){
        format = new AudioFormat(44100, 16, 2, true, true);
        this.recorder=recorder;
        try {
            speakers = AudioSystem.getSourceDataLine(format);
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            speakers=null;
        }
    }
    public void run(){
        try {
            speakers.open(format);
            speakers.start();
            while (true) {
                speakers.write(recorder.getBuffer(), 0, recorder.getBuffer().length);
            }
        } catch (LineUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
