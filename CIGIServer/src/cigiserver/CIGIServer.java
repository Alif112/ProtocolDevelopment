
package cigiserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class CIGIServer {
    static int ServerPort=8004;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("CIGI Server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        CIGIImplementation cigi=new CIGIImplementation();
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
                    
                    
                    int ll=cigi.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
                    System.out.println("==========>"+ack.length());
                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
//                    int len=46;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
//                    len=6;
//                    byte[] data1 = new byte[len];
//                    data1=Utility.getRandomData(data1, len);
//                    String hexdata1=Utility.bytesToHex(data1);
//                    System.out.println("--------> "+hexdata1);
//                    Random rand=new Random();
//                    
//                    byte shift = (byte) rand.nextInt(255);
//                    shift=(byte) (shift<<3);
//                    String s=Utility.byteToHex(shift);
//                    int minisize=7;
//                    byte[] b3=new byte[minisize];
//                    byte[] b2=new byte[minisize];
//                    b3=Utility.getRandomData(b3, minisize);
//                    b2=b3;
//                    for(int j=0;j<minisize;j++){
//                        b3[j]=(byte) (b3[j]<<3);
//                        b2[j]=(byte) (b2[j]>>5);
//                        b3[j]=(byte) (b3[j]|b2[j]);
//                    }
//                    String latitude=Utility.bytesToHex(b3);
//                    
//                    System.out.println("---> "+latitude);
//                    
////                    int idint=i%256;
////                    byte bid=(byte) idint;
////                    String id=Utility.byteToHex(bid);
////                    System.out.println("id ----> "+id );
//                    
////                    String m="0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b300015428ed4c7be152f99000000000000000043a875ec40341de6e8db8bad403a58a30e8293d9405ff0113d6bf064";
////                    String m="0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b30"+hexdata;
//                    
//                    
                    
//                    String m="01100200400000001a2c7a6w000000002c180018"+"00000000"+      "40"+s+hexdata1 +   "40"+latitude  +"2b30"+hexdata;
                    int offset=0,len=dp.getLength();
                   byte[] newdata=new byte[offset+len+61];
                    
                    int len2=cigi.createPacket(b, offset, len);
                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(b,offset,len2);
                   System.out.println(m);
                    
                    byte[] b1=Utility.hexStringToByteArray(m);
              

                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(dp.getPort());
                    ds.send(dp2);

//                    System.out.println("------sending from server to ip:port: "+dp.getAddress()+":"+ServerToClientPort);
                    countsend+=1;
                    System.out.println("------------------->  "+countsend);
                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}