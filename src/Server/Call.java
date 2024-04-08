import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

public class Call implements Runnable{

    private Set<CallParticipant> currentParticipants;
    private String groupName;
    private HashMap<CallParticipant,byte[]> currentParticipantsBuffers;
    private static int SIZE=4096;

    public Call(Set<CallParticipant> currentParticipants) {
        this.currentParticipants=currentParticipants;
        groupName=null;
        this.currentParticipantsBuffers=new HashMap<>();
        initCurrentParticipantsBuffers(currentParticipants);
        
    }
    public Call(Set<CallParticipant> currentParticipants,String groupName) {
        this.currentParticipants=currentParticipants;
        this.groupName=groupName;
        this.currentParticipantsBuffers=new HashMap<>();
        initCurrentParticipantsBuffers(currentParticipants);

    }
    public void initCurrentParticipantsBuffers(Set<CallParticipant> callParticipants){
        for (CallParticipant callParticipant : callParticipants) {
            System.out.println("buffers inicializados");
            this.currentParticipantsBuffers.put(callParticipant,new byte[SIZE]);
        }
    }
    public boolean removeCurrentParticipant(CallParticipant callParticipant){
        if(!currentParticipants.contains(callParticipant)){
            return false;
        }
        return currentParticipants.remove(callParticipant);
    }
    public boolean addCurrentParticipant(CallParticipant callParticipant){
        if(currentParticipants.contains(callParticipant)){
            return false;
        }
        return currentParticipants.add(callParticipant);
    }
    public boolean isACurrentParticipant(CallParticipant callParticipant){
        return currentParticipants.contains(callParticipant);
    }
    public boolean hasMoreThanOneCurrentParticipant(){
        return currentParticipants.size()>1;
    }
    public void receiveBytesFromCurrentParticipants(){
        for (Map.Entry<CallParticipant, byte[]> entry : currentParticipantsBuffers.entrySet()) {
            CallParticipant participant = entry.getKey();
            byte[] buffer = entry.getValue();
            
            receiveBytesFromOneParticipant(participant, buffer);
        }
    }
    private void receiveBytesFromOneParticipant(CallParticipant participant, byte[] buffer){
        DataInputStream dis=participant.getDis();
        int length=0;
        Timer timer = new Timer();
        int availableBytesFromClient;
        try {
            availableBytesFromClient = dis.available();
        } catch (IOException e) {
            availableBytesFromClient =0;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(availableBytesFromClient>1){
            try {
                length=dis.readInt();
                dis.readFully(currentParticipantsBuffers.get(participant));
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        

            
            
        

    }
    private void sendBytesToOneParticipant(CallParticipant participant,byte[] buffer, int length){
        DataOutputStream dos=participant.getDos();
        try {
            System.out.println("ESCRIBO "+length);
            dos.writeInt(length);
            dos.write(buffer,0,length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void communicateTwoParticipants(){
        receiveBytesFromCurrentParticipants();
        


        List<CallParticipant> list=new ArrayList<>();
        for (Map.Entry<CallParticipant, byte[]> entry : currentParticipantsBuffers.entrySet()) {
             list.add(entry.getKey());
        }
        if(list.size()==1){
            System.out.println("AUTOLLAMADA");
           list.add(list.get(0));
        }
        System.out.println("Size de la lista"+list.size());
        byte[] participant1Buffer= currentParticipantsBuffers.get( (list.get(0)) );
        
        sendBytesToOneParticipant(list.get(1), participant1Buffer, SIZE);
        

        byte[] participant2Buffer= currentParticipantsBuffers.get( (list.get(1)) );
        sendBytesToOneParticipant(list.get(0), participant2Buffer, SIZE);
        
        
        
     //   sendBytesToOneParticipant(list.get(0), currentParticipantsBuffers.get( (list.get(1)) .getUsername() ), SIZE);
       
    }
    
    public void run1(){
        System.out.println("");
        while(true){
            System.out.println("ALO");
            communicateTwoParticipants();
            
            
        }
    }
    @Override
    public void run(){
        List<CallParticipant> list=new ArrayList<>();
        for (Map.Entry<CallParticipant, byte[]> entry : currentParticipantsBuffers.entrySet()) {
             list.add(entry.getKey());
        }
        if(list.size()==1){
            System.out.println("AUTOLLAMADA");
           list.add(list.get(0));
        }
        System.out.println("Size de la lista"+list.size());
        
        DataInputStream client1Dis=list.get(0).getDis();
        DataInputStream client2Dis=list.get(1).getDis();

        DataOutputStream client1Dos=list.get(0).getDos();
        DataOutputStream client2Dos=list.get(0).getDos();
        
        while (true) {
            /*client1 recibe */
            try {
                
                int availableBytesFromClient1 = client1Dis.available();
                /*Recibe del client 1 y envio a client1 para probar de momento */
                if(availableBytesFromClient1>0){
                    
                    int length = client1Dis.readInt();
                    byte[] receivedBytes = new byte[length];
                    client1Dis.readFully(receivedBytes);
                    client2Dos.writeInt(receivedBytes.length);
                    client2Dos.write(receivedBytes,0,receivedBytes.length);
                }
                
                 
                int availableBytesFromClient2 = client2Dis.available();
                //Recibo del client2 y envio a client 1 
                if(availableBytesFromClient2>0){
                    int length2 =client2Dis.readInt();
                    byte[] receivedBytes2 =new byte[length2];
                    client2Dis.readFully(receivedBytes2);
                    client1Dos.writeInt(receivedBytes2.length);
                    client1Dos.write(receivedBytes2,0,receivedBytes2.length);
                }
                


            } catch (IOException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
                break;
            }
        
        }
    }
  
    
    
    

    
    
}
