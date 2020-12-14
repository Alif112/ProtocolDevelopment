package statserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class STATServer {
    static int ServerPort=1011;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("STAT server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        STATImplementation stat=new STATImplementation();
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("65.99.254.85"));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
//                    System.out.println(message);
                    
//                    
//                     
                    int ll=stat.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );

//                    STAT 
//                    String m="3812451a"+"0000000000000002000186b8000000010000000200000000000000000000000000000000"
//                            + "000000096d6f6e5f6e616d6531000000000000086d795f6e616d653100030d70000000010000000131323334353637383941424344454600";
//                    
//                    String m="3812451a"+"0000000000000002000186b8000000010000000200000000000000000000000000000000"
//                            + "00000064"
//                            + hexdata
//                            + "00000008111212120000000000030d7000000001"
//                            + "0000000100000121212121212121212121221212";
                    
                    int offset=0;
                    len=52;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=stat.createPacket(newdata, offset, len);
//                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(newdata,offset,len2);
//                   System.out.println(m);
//                    
                    byte[] b1=Utility.hexStringToByteArray(m);
                    

                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(dp.getPort());
                    ds.send(dp2);

//                    System.out.println("------sending from server to ip:port: "+dp.getAddress()+":"+ServerToClientPort);
                    countsend+=1;
                    System.out.println(ds+"------------------->  "+countsend);
                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}