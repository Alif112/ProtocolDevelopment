package isakmpserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;

public class ISAKMPServer {
    static int ServerPort=500;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("ISAKMP server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        ISAKMPImplementation xtacacs=new ISAKMPImplementation(false);
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("65.99.254.85"));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
//                    System.out.println(message);
                    
                    int ll=xtacacs.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=100;
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
                    
                    //                    ISAKMP
//                    String m="8e7779464044673e7b452c842bc4e8912e2023200000000100000110240000f49ad912e3dce3c754c5ce92be7ba123bb5426d0e321618f99d9e7ae09d1eb82812e510e0b8acfcc68d9edc2adb23a7d97374ca219dc26cba07871944482f892837430ace5f7152bb077864dc215f02735a201107cd57bb27209e79b80844e17b26d89767d857234f1f5ceac975d80754a5cd468baee770e4b8446fc952552924eb02978f14972806b94d46903c8797715c980f4ff98c1e8ba3b9793c3d1214806beb69ef8fdeede8a43fcf2b058afed81e5dbf05c01acaa4a3d2bd9c753ed7c3c32bad034889067940ade3a86938344b04bd4abb46cd2ea62ff23ee0d6641939bbe3f98a7695ecb645caae3a8662184df";
                    
                    
                    
                    int offset=0;
                    len=100;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=xtacacs.createPacket(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    
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