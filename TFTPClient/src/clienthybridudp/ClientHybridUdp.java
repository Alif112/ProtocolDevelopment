package clienthybridudp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 *
 * @author User
 */
public class ClientHybridUdp {
    static int clientPort=15916;
    static int clientToServerPort=161;
    public static int countsend=0;
    public static int countreceive=0;
    public static int numberOfPackets=5;
    public static long timeDelayAtReceiver=(long) 1e9;
    static SNMPImplementation snmp=new SNMPImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        while(true){
            System.out.println("Udp SNMP Client Started...........");

            DatagramSocket ds=new DatagramSocket();
            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
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
                
                while(i<numberOfPackets){
//                    int len=55;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
//                    
//                    int len1=6;
//                    byte[] data1 = new byte[len1];
//                    Random rnd=new Random();
//                    for(int j=0;j<6;j++){
//                        data1[j]=(byte)rnd.nextInt(10);
//                    }
//                    String hexdata1=Utility.bytesToHex(data1);
//                    
////                  String m="302602010004067075626c6963a019020126020100020100300e300c06082b0601020101020005000000"+ hexdata;  
////                    String m="302602010004067075626c6963a0"+  "1c" +"020126020100020100" +"0201"+ hexdata + "300e300c06082b060102010102000500";
////                    String m="3042"+ "02010004067075626c6963" +"a035"+ "020126020100020100" + "302a" +"300c0608"+hexdata+"0500"+"300c0608"+hexdata+"0500"+"300c0608"+hexdata+"0500";                 
////                    String m="3081cd02010330110204009e5d1b020300ffe3040105020103042f302d040d80001f888059dc486145a2632202010802020ab90405706970706f040c055b0aa218fd325bbd0dead604003084"+"040d80001f888059dc486145a263220418"+hexdata+"a15902042c180dbd020100020100304b300d06092b06010201020201080500300d06092b060102010202010b0500300d06092b060102010202010c0500300d06092b06010201020201110500300d06092b06010201020201120500";
////                  Working 54 bits SNMP Packets  
////                    String m="3060" + "02010004067075626c6963a053" + "0201290201000201003048" + "30160612"+hexdata+"050030160612"+hexdata1+"0500"+"30160612"+hexdata2+"0500";
//                    
//                    byte totallen=(byte) ((byte)len+40);
//                    String s=Utility.bytesToHex(totallen);
//                    
//                    String m="30"+s+"02010004067075626c6963a019020126020100020100300e300c06082b06"+hexdata1+"05000000"+ hexdata;
//                    
                    int offset=0,len=72;
                    byte[] newdata=new byte[offset+len+len+1];
                    int backlen=Utility.getRandomData(newdata, offset, len);
                    
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> "+m1.length());
                    System.out.println(m1);
                    
                    backlen=snmp.createPacket(newdata, offset, len);
                    
                            
                    String m=Utility.bytesToHex(newdata,offset,backlen);
                    byte[] b1=Utility.hexStringToByteArray(m);
//                    System.out.println(m);
//                    byte[] c = new byte[b1.length];
                    
//                    for(int j=0;j<b1.length;j++){
//                        System.out.print(j+"-> "+b1[j]+" ");
//                    }
//                    System.out.println();
                    
//                    byte[] c = new byte[b.length + b1.length];
//                    System.arraycopy(b, 0, c, 0, b.length);
//                    System.arraycopy(b1, 0, c, b.length, b1.length);
                    
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    
                    Thread.sleep(len);
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
                int packetReceived=0;
                int startTime=(int) System.nanoTime();
                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(packetReceived==numberOfPackets || checkTime>=timeDelayAtReceiver){
                        System.out.println("Socket closed-------------------------------------------------------------->  "+ds);
                        ds.close();
                        break;
                    }
                    byte[] b1= new byte[2048];
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
                    ds.receive(dp1);
                    
                    int ll=snmp.decodePacket(b1, 0, dp1.getLength());
                    
                    String ack=Utility.bytesToHex(b1, 0, ll);
                    
                    System.out.println("============Received at client          ==================================> "+ll);         
                    System.out.println("==========>"+ack.length());
                    System.out.println(ack); 
                    
                    
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,b1.length);
                    
                    System.out.println("Received at client:-->--------> "+ countreceive);
                    packetReceived+=1;
                    
                    
                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
