package tftpserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import sun.audio.AudioPlayer;

/**
 *
 * @author REVE
 */
public class TFTPServer {
    static int ServerPort=161;
    static int ServerToClientPort=15916;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("UDP SNMP Server Started Successfully");
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
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("65.99.254.85"));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                SNMPImplementationServer snmp=new SNMPImplementationServer();
                
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
                    countreceive+=1;
                    
                    int ll=snmp.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    
                    
                    System.out.println("Received at server--------> "+countreceive);
//                    int len=78;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);

//                    
//                    int len2=4;
//                    byte[] data2 = new byte[4];
//                    data2=Utility.getRandomData(data2, len2);
//                    String hexdata2=Utility.bytesToHex(data2);
//                    
//                    System.out.println(hexdata2);
//                    //Backend coding
//                    
////                   String m="302602010004067075626c6963a019020126020100020100300e300c06082b0601020101020005000000"+ hexdata +"0002450000690d880000401100007f0000017f000001c4df00a10055fe68304b0201033011020430f6f3d4020300ffe30401040201030410300e04000201000201000400040004003021040d80001f888059dc486145a263220400a00e02047d0e082e0201000201003000";
////                    String m="303802010004067075626c6963a22b0201260201000201003020301e06082b0601020101020006122b060104018f5101010182295d011b020201";
////                   String m="307902010004067075626c6963a26c0201290201000201003061"+"30210612"+hexdata+"040b3137322e33312e31392e3230230612"+hexdata1+"040d3235352e3235352e3235352e3030170612"+hexdata2+"020101";
////                    String m="303202010004067075626c6963a225020117020100020100301a301806082b06010201010500040c6f6f662e7a696e672e6f726705000000"+hexdata;
////                    String m="307f"+"02010004067075626c6963a22102013402010002010030163014060a2b060102010202010601040608003715e6bc0000000"+ hexdata;
//                    String m="307f"+"02010004067075626c6963a22102013402010002010030163014060a2b06010201020201060104060800"+hexdata2+"000000"+hexdata;
                    
                    int offset=0,len=72;
                    byte[] newdata=new byte[offset+len+len+1];
                    int backlen=Utility.getRandomData(newdata, offset, len);
                    
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("---sending from server    ---------------------------------> "+m1.length());
//                    System.out.println(m1);
                    
                    backlen=snmp.createPacket(newdata, offset, len);
                    
                            
                    String m=Utility.bytesToHex(newdata,offset,backlen);
                    
                    
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
