package slimp3client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Slimp3Client {
    static int delayTime=200;
    static int clientToServerPort=1069;
    static String ip="65.99.254.85";
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean checkSocket=true;
    
    static Slimp3Implementation isakmp=new Slimp3Implementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0,j=0;
        System.out.println("Udp Slimp3 Client Started...........");
        Scanner sc=new Scanner(System.in);
        int socketing=sc.nextInt();
        if(socketing==-1){
            checkSocket=false;
            numberOfPackets=10000;
            DatagramSocket ds=new DatagramSocket();
            MySender mySender=new MySender(ds);
            mySender.init();
        }
        else{
            numberOfPackets=socketing;
            while(true){
                DatagramSocket ds=new DatagramSocket();
                MySender mySender=new MySender(ds);
                mySender.init();
            }
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
                Thread myReceiver=new MyReceiver(ds);
                myReceiver.start();
                while(i<numberOfPackets){
                    int len=230;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=108;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
                    //Slimp3
//                    String m="69f902010004067075626c6963a2ec0202140f0201"
//                            +hexdata;
//                            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                    
                    int offset=0;
                    len=161;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=isakmp.createPacket(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                  
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName(ip);
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
//                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("---Total Packet Send---------------------------------> "+ totalSend);
                    Thread.sleep(delayTime);
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
                    if(checkSocket && checkTime>=receiverTime || countreceive==numberOfPackets ){
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
                    int ll=isakmp.decodePacket(b1, 0, dp1.getLength());
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b1, 0, ll);                   
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
