package Audio;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;

public class AudioManager {
    private DataInputStream din;
    private DataOutputStream don;
    public AudioManager(DataInputStream din, DataOutputStream don){
        this.din=din;
        this.don=don;
    }
    

}
