//        data[index++]=0x68; data[index++]=0x74; data[index++]=0x74; data[index++]=0x70;
//        data[index++]=0x3a; data[index++]=0x3a; data[index++]=0x2f; data[index++]=0x2f;
//        data[index++]=0x31; data[index++]=0x32; data[index++]=0x37; data[index++]=0x2e;
//        data[index++]=0x30; data[index++]=0x2e; data[index++]=0x30; data[index++]=0x2e;
//        data[index++]=0x31; data[index++]=0x2f; data[index++]=0x69; data[index++]=0x6e;
//        data[index++]=0x64; data[index++]=0x65; data[index++]=0x78; data[index++]=0x2e;
//        data[index++]=0x77; data[index++]=0x6d; data[index++]=0x6c;
//        data[index++]=0x00; data[index++]=0x00;


package wspiclient;

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
public class WSPClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;

    static String ip="65.99.254.85";
    static int clientToServerPort=9200;
    static int numberOfPackets=10;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e10;
    static WSPImplementation wsp=new WSPImplementation();
    
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
                
                Thread myReceiver=new MyReceiver(ds);
                myReceiver.start();
                
                while(i<numberOfPackets){
//                    int len=50;
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
                    
//                    wsp working well
//                    String m="70407f68"
//                            + "7474703a3a3a3a3a"+hexdata+"3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a2f2f3132372e302e302e312f696e6465782e776d6c8080";
//                    String m="70406468"+hexdata;
//                    String m="21401b687474703a3a2f2f3132372e302e302e312f696e6465782e776d6c0000"
//                            +hexdata;
                    
                    
                    int offset=0,len=120;
                    
                    byte[] newdata=new byte[offset+len+50];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=wsp.createPacket(newdata, offset, len);
//                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(newdata,offset,len2);
//                   System.out.println(m);
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName(ip);
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("---Total Packet Send---------------------------------> "+ totalSend);
                    Thread.sleep(200);
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
                    int ll=wsp.decodePacket(b1, 0, dp1.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b1, 0, ll);
                    
                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
