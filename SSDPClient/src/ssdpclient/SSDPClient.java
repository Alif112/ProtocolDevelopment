package ssdpclient;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.IDN;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class SSDPClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;

    static int clientToServerPort=1812;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
//    static CIGIImplementation cigi=new CIGIImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        
        while(true){
            System.out.println("Udp SSDP Client Started...........");
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
                int i=0,j=0;
                int countsend=0;
                while(i<numberOfPackets){
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
//                    System.out.println(hexdata);
                    
                    int len2=4;
                    byte[] data2 = new byte[len2];
                    data=Utility.getRandomData(data2, len2);
                    String hexdata2=Utility.bytesToHex(data,0,len2);
//                    System.out.println(hexdata2);
                    
                    len2=52;
                    byte[] data3 = new byte[len2];
                    data=Utility.getRandomData(data3, len2);
                    String hexdata3=Utility.bytesToHex(data,0,len2);
                    
                    
                    int len4=4;
                    byte[] data4 = new byte[len4];
                    data=Utility.getRandomData(data4, len4);
                    String hexdata4=Utility.bytesToHex(data,0,len4);
                    
                    Random rand=new Random();
                    int idint=rand.nextInt();
                    byte bid=(byte) idint;
                    String id=Utility.byteToHex(bid);
                    System.out.println("id ----> "+id );
                    
                    
                    /** PPP MuxCP 8059, first hex20, second hex94 **/
//                    String m=hexdata+"8059010100640060"+hexdata2;
                    /** PPP OSINLCP 8059, first hex14, second hex94 **/
//                    String m=hexdata+"621b2f7e03f1"+"8023010100640060"+hexdata2;
//                    String m="5e1d0bdd0000000000000002000186a3000000030000001300000001000000343847760b00000009776572726d736368650000000000000000000001000000050000000100000000000000020000000300000011000000000000000000000020"+hexdata;
//                    String m=hexdata2+"00000000"+"00000002"+"000186a3"+"0000000300000013"+hexdata4+"00000034"+hexdata3+"0000000000000000"+"00000064"+hexdata;
                    
                    String m="01"+id+"005740b664dbf5d681b2adbd1769515118c8010773746576650212dbc6c4b758be14f005b3877c9e2fb6010406c0a8001c05060000007b50125f0f8647e8c89bd881364268fcd045324f0c0266000a0173746576650000"+hexdata;
                    
//                    int offset=0,len=61;
//                    
//                    byte[] newdata=new byte[offset+len+61];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
//                    len2=cigi.createPacket(newdata, offset, len);
////                    System.out.println("================================>          "+ len2);
//                   String m=Utility.bytesToHex(newdata,offset,len2);
//                   System.out.println(m);
//                   
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
