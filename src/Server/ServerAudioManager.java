
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServerAudioManager {
    
    private Chatters chatters;
    private CallParticipants callParticipants;

    private List<Thread> currentCalls;
    private HashMap<CallParticipant,Call> callMap;
    /*AHORA se debe crear un objeto llamada que
     * constantemente reciba de los bytes del audio de la persona  A
     * que graba y los reenvie al  cliente B. De igual forma
     * se debe hacer que los bytes de B le lleguen al dataInpustream de A.
     * se deben ejecutar en hils porque pueden haber multiples llamdas a la vez
     * y ademas se deben almacenartodos esos hilos, para no perderlos.
     * Con el metodo .isAlive nos damos cuenta si un hilo sirve o no
     * esto lo p
     */
/* */
    public ServerAudioManager(Chatters chatters,CallParticipants callParticipants) {
        this.chatters=chatters;
        this.currentCalls=new ArrayList<>();
        this.callParticipants=callParticipants;
        this.callMap=new HashMap<>();
    }
    public boolean endCall(String username){
        if(!callParticipants.isACallParticipant(username)){
            System.out.println("No se pudo finalizar llamada");
        }
        return endCall(callParticipants.getCallParticipant(username));
    }
    private boolean endCall(CallParticipant callParticipant){
        if(!callMap.containsKey(callParticipant)){
            return false;
        }
        Call call=callMap.get(callParticipant);
        call.setIsRunnig(false);
        Set<CallParticipant> participantsInCall=call.getParticipants();
        for (CallParticipant participant : participantsInCall) {
            callMap.remove(participant);
            PrintWriter pw= (chatters.getPerson(participant.getUsername())).getOut();
            pw.println("Llamada actual finalizada");
        }
        return true;
    }

    public boolean addCall(String username1, String username2){
        CallParticipant callParticipant1=callParticipants.getCallParticipant(username1);
        CallParticipant callParticipant2=callParticipants.getCallParticipant(username2);
        System.out.println("LINEA 28");
        if(callParticipant1==null){
            System.out.println("person1 no esta"+callParticipant1);
            return false;
        }
        if(callParticipant2==null){
            System.out.println("person2 no esta"+callParticipant2);
            return false;
        }
        System.out.println("linea 36");
        Set<CallParticipant> callParticipantsSet = new HashSet<>();
        
        callParticipantsSet.add(callParticipant1);
        callParticipantsSet.add(callParticipant2);

        Call call=new Call(callParticipantsSet);
        
        
        Thread threadCall=new Thread(call);
        threadCall.start();
        System.out.println("linea 52");
        callMap.put(callParticipant1,call);
        callMap.put(callParticipant2,call);
        return true;
    }
    

    

}
