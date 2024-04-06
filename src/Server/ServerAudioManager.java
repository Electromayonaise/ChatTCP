
import java.util.List;

public class ServerAudioManager {
    // Private static instance variable
    private static ServerAudioManager instance;

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

    // Private constructor to prevent instantiation from outside
    private ServerAudioManager() {
        // Initialization code here
    }

    // Public static method to get the singleton instance
    public static ServerAudioManager getInstance() {
        // Lazy initialization: create the instance when it's accessed for the first time
        if (instance == null) {
            instance = new ServerAudioManager();
        }
        return instance;
    }
    

    

}
