import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Set;

public class CallParticipants {
    private HashMap<String,CallParticipant> callers;
    private HashMap<String,Set<CallParticipant>> groups;

    public CallParticipants(){
        this.callers=new HashMap<>();
        this.groups=new HashMap<>();
    }
    public boolean addCallParticipant(String username, DataInputStream dis, DataOutputStream dos){
        if(isACallParticipant(username)){
            return false;
        }
        CallParticipant callParticipant=new CallParticipant(username, dis, dos);
        callers.put(username,callParticipant);
        return true;
    }
    public boolean isACallParticipant(String username){
        return callers.containsKey(username);
    }
    public CallParticipant getCallParticipant(String username){
        if(!isACallParticipant(username)){
            return null;
        }
        return callers.get(username);
    }
    public boolean removeCallParticipant(String username){
        if(!isACallParticipant(username)){
            return false;
        }
        callers.remove(username);
        return true;
    }
    
}
