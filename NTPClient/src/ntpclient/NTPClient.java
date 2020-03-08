package ntpclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.IDN;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;

/**
 *
 * @author User
 */
public class NTPClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;
    
    
    
    static int clientToServerPort=123;
    static int numberOfPackets=10;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 3e9;
    static NTPImplementation ntp=new NTPImplementation();
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        while(true){
            
            System.out.println("Udp NTP Client Started...........");
            DatagramSocket ds=new DatagramSocket();

            MySender mySender=new MySender(ds);
            mySender.start();
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

        public void start() {
            try {
                int i=0;
                int countsend=0;
                while(i<numberOfPackets){
//                    int len=42;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
//                    len=40;
//                    byte[] data1 = new byte[len];
//                    data1=Utility.getRandomData(data1, len);                 
//                    String hexdata1=Utility.bytesToHex(data);
////                    int idint=i%256;
////                    byte bid=(byte) idint;
////                    String id=Utility.byteToHex(bid);
////                    System.out.println("id ----> "+id );
//                    
////                    String m="d9000afa00000000"+hexdata+"000000"+hexdata1;
//                    String m="d9020afa0000"+hexdata;
//                    
////                    String m="1a020aef00000f7a"+hexdata;
////                    String m="1a020aef0000"+hexdata;
//                    
//                    byte[] b1=Utility.hexStringToByteArray(m);
//                    
                    
//                    
                    int offset=0,len=42;
                    
                    byte[] newdata=new byte[offset+len+42];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> ");
                    System.out.println(m1);
                    
                    len2=ntp.createPacket(newdata, offset, len);
                    System.out.println("================================>          "+ len2);
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
                    Thread.sleep(40);
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
                    if(currentTime-startTime>=receiverTime || countreceive==numberOfPackets){
                        ds.close();
                        System.out.println("Socket closed------------------------------> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,b1.length);
                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
//                  
                    int offset=0;
                    
                    int ll=ntp.decodePacket(b1, offset, dp1.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b1, 0, ll);
                    
                            
                    System.out.println("==========>"+ack.length());
                    System.out.println(ack);
                    
                    
                    
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
