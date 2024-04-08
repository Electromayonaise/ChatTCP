import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Objects;

public class CallParticipant {
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;
    public CallParticipant(String username, DataInputStream dis, DataOutputStream dos) {
        this.username = username;
        this.dis = dis;
        this.dos = dos;
    }
    public String getUsername(){
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallParticipant that = (CallParticipant) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    
    
}
