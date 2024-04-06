
import java.util.ArrayList;
import java.util.List;

public class ServerAudioManager {
    
    private Chatters chatters;

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

    public ServerAudioManager(Chatters chatters) {
        this.chatters=chatters;
        this.currentCalls=new ArrayList<>();
    }

    public boolean addCall(String name1, String name2){
        Person person1=chatters.getPerson(name1);
        Person person2=chatters.getPerson(name2);
        System.out.println("LINEA 28");
        if(person1==null){
            System.out.println("person1 no esta"+person1);
            return false;
        }
        if(person2==null){
            System.out.println("person2 no esta"+person2);
            return false;
        }
        System.out.println("linea 36");
        Call call=new Call(person1.getDis(), person2.getDis(), person1.getDos(), person2.getDos());
        Thread threadCall=new Thread(call);
        threadCall.start();
        currentCalls.add(threadCall);
        return true;
    }
    

    

}
