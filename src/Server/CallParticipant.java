import java.io.DataInputStream;
import java.io.DataOutputStream;

public class CallParticipant {
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;
    public CallParticipant(String username, DataInputStream dis, DataOutputStream dos) {
        this.username = username;
        this.dis = dis;
        this.dos = dos;
    }
    public DataInputStream getDis() {
        return dis;
    }

    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    // Getters y setters para dos
    public DataOutputStream getDos() {
        return dos;
    }

    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }
    
    @Override
    public String toString() {
        return "CallParticipant{" +
                "username='" + username + '\'' +
                ", dis=" + dis +
                ", dos=" + dos +
                '}';
    }

    
    
}
