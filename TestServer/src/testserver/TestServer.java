package testserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class TestServer {
    static int ServerPort=3544;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("Test server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        CoAPImplementation dspv2=new CoAPImplementation();
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("191.96.12.12"));
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
//                    int ll=dspv2.decodePacket(b, 0, dp.getLength());
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("=====received len=====>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=100;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=16;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);                   
                    
                    Random rand=new Random();
                    int idint=rand.nextInt();
                    byte bid=(byte) idint;
                    String id=Utility.byteToHex(bid);
                    System.out.println("id ----> "+id );
                    
                     String m="3082008c"
                            + "02820064"+hexdata
//                            + "00fefefefe14fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe04fefefefefefefefefefefefefefefefefefe257300fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe4afefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe257300fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe31fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe97fefefefefefcfefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefec7fefefefefefefeeefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe41fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe6cfefefefefefefefefefefefefefefefefefefefefe7efefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefe51fefefefefefefefefefefefefeb3fefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefeeefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefefedefefefefefefefefefefefefefefefe"
                            + "04067075626c6963"
                             + "a21a020200de020100020100300e300c06082b060102012573000500";

                    
                    
//                    teredo
//                    String m="00007f7bac55fed9600000000000"+id
//                            + "00fe80000000000000708dfe834114a5122001000041379e508000f12ab9c82815"
//                            + ""+hexdata;
                    
//                    pimv2
//                    String m="600000000049"
//                            + "67403ffe802000000001026097fffe0769ea3ffe050100001c010200f8fffe03d9c0"
////                            + "0201003014020202000000642e2800000000000000000000ffffff00000a020100000028d2000002d200000111030303"
//                            + "2100e85900000000";
//                            + "600000000019673f3ffe050700000001020086fffe0580faff050000000000000000000000009999cd60270f0019b35b5b69746f6a756e5d20686f6765686f6765";
                    
//                    pim v2
//                    String m="60000000002a"
//                            + "67403ffe802000000001026097fffe0769ea3ffe050100001c010200f8fffe03d9c0"
//                            + "2100e859";
                    
                    
                    
//                    int offset=0;
//                    len=100;
//                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
////                    System.out.println("--------------> ");
////                    System.out.println(m1);
//                    
//                    len2=dspv2.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    
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