package messengerclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

public class MessengerClient {
    static int clientToServerPort=1027;
    static int numberOfPackets=4;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
    static CoAPImplementation dspv2=new CoAPImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Test udp Client Started...........");
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
                    
//                    messenger
//                    String m="040028001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fcb05b8b3905acb97415260f7f7096c2210000000001000000000000000000ffffffff660100000000";
//                            + "10000000000000001000000053454355524954590000000000000000100000000000000010000000414c45525400000000000000000000002201000000000000220100004d6963726f736f66742057696e646f77732068617320646574656374656420796f757220696e7465726e65742062726f777365720a697320696e666563746564207769746820537079776172652c2041642d776172652c20616e64205468696566776172652e0a0a596f7572207072697661637920697320696e2064616e6765722e0a5765207265636f6d6d656e6420616e20696d6d6564696174652073797374656d207363616e2e20506c656173652076697369740a0a687474703a2f2f53776970655370792e636f6d0a0a4661696c75726520746f20646973696e6665637420636f756c6420616c6c6f772074686972642070617274696573206163636573730a746f20796f757220706572736f6e616c20696e666f726d6174696f6e2e0a00";
                    
                    String m="040128001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fcb05b8b3905acb97415260f7f7096c2210000000001000000000000000000ffffffff66010000000010000000000000001000000053454355524954590000000000000000100000000000000010000000414c4552540000000000000000000000220100000000000022010000098234e47bb4ffc11f2ca2bece202705491b1ceaf4a614db1d1f3fb770c78c20c10188135693cb612c770da2e4596d937fe0417867a903a742b221f4e879f26a2ca2e008bee6fc3a8f078d5c21f4a248587c509b12db81409f025583e2f812707dd52a28";
                    
                    
                    
                    
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
//                   System.out.println(m);
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
                    Thread.sleep(300);
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
                    int ll=dspv2.decodePacket(b1, 0, dp1.getLength());
//                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);
                    
                    
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
