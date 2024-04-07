import java.io.*;
import java.net.*;

import Audio.AudioManager;

public class Lector implements Runnable{
    String message;
    BufferedReader in;
    public AudioManager audioManager;
    public Lector(BufferedReader in){
        this.in=in;
    }
    public void setAudioManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    @Override
    public void run() {
        //leer la linea que envia el servidor e imprimir en pantalla
        try { 
            
            while ((message = in.readLine()) != null) {
                System.out.println(message);
                if(message.startsWith("Llamada dirigida a")){
                    System.out.println("Intentando captura microfono");
                    if(audioManager!=null){
                        System.out.println("Captura de microfono iniciada");
                        audioManager.initRecorder();
                    }else{
                        System.out.println("Hubo problemas en la captura del microfono");
                    }
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }      
    
    }

}