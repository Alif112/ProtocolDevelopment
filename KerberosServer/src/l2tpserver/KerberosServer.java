package l2tpserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class KerberosServer {
    static int ServerPort=88;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("L2TP server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        L2TPImplementation l2tp=new L2TPImplementation();
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
                    
                    
                     
                    int ll=l2tp.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
//                    
//                            
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
                    
                    String m="6d8201b2308201aea003020105a10302010da3091b07554d522e454455a4123010a003020101a10930071b056e6e65756ca581d16181ce3081cba003020105a1091b07554d522e454455a210300ea003020100a10730051b03616673a381a63081a3a003020101a2819b04819848217864b46571a0942bfd36be763afe68480aeeb770e898117eec5261355a67792207a93d655c053961c664989ba61a98d0bd8de977cf2f373a4c986255a125a69147821d6517e341fad3fe6e7b0098e5c8894240d0905fe4c4385841a539caf40ad9d63169b1c5ba904704786249be33c54e6cd77915d525b076ec4fb547416d1d0e274e27c163178693232f9f2654a045df488a095caba681ae3081aba003020101a281a30481a0e89f7d3961bff1b488a23a8970e2a69d6d554978a56209eeae7f13056e1a4ab4b5550749e60ff3682ae4f14c929f89f285b9e2fcae7a3a35b4bc1eaf6b91ef1942c5dc2c624b0318da3d1bdaacf3ea65a965803978adf728130a058d4975ab2879a7ba7773853ddce4dc974693e640f229f0d67688262766f2164b4301ae22c7b434ec8bdf16b5852d57ac5cccf8550c66f3116d092b9c82b603f412f6ad2a7b";
                    
//                    int offset=0;
//                    len=100;
//                    
//                    byte[] newdata=new byte[offset+len+30];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
////                    System.out.println("--------------> ");
////                    System.out.println(m1);
//                    
//                    len2=l2tp.createPacket(newdata, offset, len);
////                    System.out.println("================================>          "+ len2);
//                   String m=Utility.bytesToHex(newdata,offset,len2);
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