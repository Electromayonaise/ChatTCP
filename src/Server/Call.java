import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Call implements Runnable{
    private DataInputStream client1Dis;
    private DataInputStream client2Dis;
    private DataOutputStream client1Dos;
    private DataOutputStream client2Dos;
    public Call(DataInputStream client1Dis, DataInputStream client2Dis, 
                DataOutputStream client1Dos, DataOutputStream client2Dos) {
        this.client1Dis = client1Dis;
        this.client2Dis = client2Dis;
        this.client1Dos = client1Dos;
        this.client2Dos = client2Dos;
    }
    
    public void run(){
        while (true) {
            /*client1 recibe */
            try {

                int availableBytesFromClient1 = client1Dis.available();
                /*Recibe del client 1 y envio a client1 para probar de momento */
                if(availableBytesFromClient1>0){
                    System.out.println("recibo del client 1");
                    int length = client1Dis.readInt();
                    byte[] receivedBytes = new byte[length];
                    client1Dis.readFully(receivedBytes);
                    client1Dos.writeInt(receivedBytes.length);
                    client1Dos.write(receivedBytes,0,receivedBytes.length);
                }
                System.out.println("no recibo nada del client 1");
                /* 
                int availableBytesFromClient2 = client2Dis.available();
                /*Recibo del client2 y envio a client 1 
                if(availableBytesFromClient2>0){
                    int length2 =client2Dis.readInt();
                    byte[] receivedBytes2 =new byte[length2];
                    client2Dis.readFully(receivedBytes2);
                    client1Dos.writeInt(receivedBytes2.length);
                    client1Dos.write(receivedBytes2,0,receivedBytes2.length);
                }
                */


            } catch (IOException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
                break;
            }
        
        }
    }
    
    

    
    
}
