
package ntpserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import sun.audio.AudioPlayer;

public class NTPServer {
    static int ServerPort=123;
    public static int countsend=0;
    public static int countreceive=0;
    static NTPImplementation ntp=new NTPImplementation(1);
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("UDP NTP Server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {

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
                    int offset=0;
                    
                    int ll=ntp.decodePacket(b, offset, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
                    System.out.println("==========>"+ack.length());
                    System.out.println(ack);
                    
                    
                    
                    
                    
                    
                    
                    
//                    int len=42;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
//                    len=40;
//                    byte[] data1 = new byte[len];
//                    data1=Utility.getRandomData(data1, len);                 
//                    String hexdata1=Utility.bytesToHex(data);
//
////                    int idint=i%256;
////                    byte bid=(byte) idint;
////                    String id=Utility.byteToHex(bid);
////                    System.out.println("id ----> "+id );
////                    i++;
//
//                    //Backend coding
//                    
//                    
////                    String m="1a020aef00000f7a000776dd11fe0031c4fae6e5108637bdc50204ecec42ee92c50204ebd937d1fec50204ebd93dea46";
//                    String m="1a020aef0000"+hexdata;
//                    
//                    
//                    
//                    byte[] b1=Utility.hexStringToByteArray(m);
              
                    
                    int len=42;
                    
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