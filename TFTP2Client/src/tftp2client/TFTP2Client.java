package tftp2client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TFTP2Client {
    static int clientToServerPort=69;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
    static TFTP2Implementation tftp2=new TFTP2Implementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("CoAP udp Client Started...........");
//        Scanner sc=new Scanner(System.in);
//        int fixed=sc.nextInt();
//       if(fixed==-1){
//            DatagramSocket ds=new DatagramSocket();
//
//            MySender mySender=new MySender(ds);
//            mySender.init();
//            Thread myReceiver=new MyReceiver(ds);
//            myReceiver.start();
//            i++;
//        
//       }
//       else{
           while(true){
            
            DatagramSocket ds=new DatagramSocket();

            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
            i++;
        }
//       }
        
       
    }

    private static class MySender {
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0,j=0;
                int countsend=0;
                while(i<numberOfPackets){
                    int len=200;
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
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
//                    tftp
//                    String m="000162696e38303638532d686561646572006f63746574007473697a65003000626c6b73697a6500313432380074696d656f7574003100"
//                            + "ff00"+hexdata+"00";
//                    
//                    String m="000162696e38303638532d686561646572006f63746574007473697a65003000626c6b73697a65003134323800"
//                            +hexdata+ "74696d656f7574003100";

//                    String m="00030001"
//                            + "0c1f00000000000090855f022d922141405b6e6f6538307838535f352e33302e30355f323846656231385f30306830380046520000000000494c4c4b4952434872727272727272727272727272727272727272727272727272727272727272006e6f6538307838535f52785f5f5f5f5f4244455f5f5f5f5f5f5f5f5f5f5f5f5f636363636363636363636363636363006e6f6538307838535f52785f5f5f5f5f707070707070707070707070707070000000aa00855f02220a791c352e33302e3035323031383032323830303038000000001100855f0200010000446000000000865f02ee0000000060865f0200000000000000000032303739000000000053";
//                            +hexdata;
                    int offset=0;
                    len=100;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> ");
                    System.out.println(m1);
                    
                    len2=tftp2.createPacket(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
                    System.out.println("================================>          "+ len2);
                   System.out.println(m);
//                   
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
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
//                    String received= new String(dp1.getData(),0,b1.length);
                    int ll=tftp2.decodePacket(b1, 0, dp1.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b1, 0, ll);                   
                    System.out.println(ack);
//                    
                    
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
