package cigiclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.IDN;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

/**
 *
 * @author User
 */
public class CIGIClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;

    static int clientToServerPort=8004;
    static int numberOfPackets=3;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    static CIGIImplementation cigi=new CIGIImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        
        while(true){
            System.out.println("Udp CIGI Client Started...........");
            DatagramSocket ds=new DatagramSocket();

            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
            i++;
        }

       
    }

    private static class MySender {
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0;
                int countsend=0;
                while(i<numberOfPackets){
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
                    
//                    int idint=i%256;
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
//                    String m="0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b300015428ed4c7be152f99000000000000000043a875ec40341de6e8db8bad403a58a30e8293d9405ff0113d6bf064";
//                    String m="0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b30"+hexdata;
                    
                    
                    
//                    String m="01100200400000001a2c7a6w000000002c180018"+"00000000"+      "40"+s+hexdata1 +   "40"+latitude  +"2b30"+hexdata;
                    int offset=0,len=61;
                    
                    byte[] newdata=new byte[offset+len+61];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> ");
                    System.out.println(m1);
                    
                    len2=cigi.createPacket(newdata, offset, len);
//                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(newdata,offset,len2);
                   System.out.println(m);
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("---Total Packet Send---------------------------------> "+ totalSend);
                    Thread.sleep(50);
                    i++;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static class MyReceiver extends Thread {
        DatagramSocket ds;
        public MyReceiver(DatagramSocket ds) {
            this.ds=ds;
        }

        @Override
        public void run() {
            try {
                int startTime=(int)System.nanoTime();
                int countreceive=0;

                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfPackets){
                        ds.close();
                        System.out.println("-------------------------------------------------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,b1.length);
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
