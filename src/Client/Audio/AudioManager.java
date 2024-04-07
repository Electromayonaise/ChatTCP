package Audio;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

import javax.sound.sampled.AudioFormat;

public class AudioManager {
    private DataInputStream din;
    private DataOutputStream don;
    private Player player;
    private Recorder recorder;
    private VoiceNoteRecorder voiceNoteRecorder;
    private static  int SAMPLE_RATE = 16000; // Frecuencia de muestreo en Hz
    private static  int SAMPLE_SIZE_IN_BITS = 16; // Tamaño de muestra en bits
    private static  int CHANNELS = 1; // Mono
    private static  boolean SIGNED = true; // Muestras firmadas
    private static  boolean BIG_ENDIAN = false; // Little-endian
    private AudioFormat format;

    private Thread recordThread;

    private Thread playerThread;

  
  

    public AudioManager(DataInputStream din, DataOutputStream don){
        this.format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_ENDIAN);
        this.din=din;
        this.don=don;
        this.player=new Player(din,format);
        
        this.recorder=new Recorder(don,format);
        
        this.voiceNoteRecorder=new VoiceNoteRecorder(don, 0,format);
        
    }
    public boolean initRecorder(){
        if(recordThread!=null&&recordThread.isAlive()){
            System.out.println("NO INICIE RECORDER");
            return false;
        }
        recorder.setRecord(true);
        recordThread=new Thread(recorder);
        
        recordThread.start();
        return true;
    }
    public boolean initVoiceNoteRecorder(int duration){
        if(recordThread!=null&&recordThread.isAlive()){
            return false;
        }
        voiceNoteRecorder.setDuration(duration);
        recordThread=new Thread(voiceNoteRecorder);
        recordThread.start();
        return true;
    }
    public boolean initPlayer(){
        if(playerThread!=null &&!playerThread.isAlive()){
            return false;
        }
        playerThread=new Thread(player);
        playerThread.start();
        return true;
    }
    


}
