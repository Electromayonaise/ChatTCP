
import java.util.ArrayList;
import java.util.List;

public class ServerAudioManager {
    
    private Chatters chatters;
    private CallParticipants callParticipants;

    private List<Thread> currentCalls;
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
        Call call=new Call(callParticipant1.getDis(), callParticipant2.getDis(), callParticipant1.getDos(), callParticipant2.getDos());
        Thread threadCall=new Thread(call);
        threadCall.start();
        currentCalls.add(threadCall);
        return true;
    }
    

    

}
