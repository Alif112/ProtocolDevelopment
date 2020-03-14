package uftpserver;


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
public class UFTPServer {
    static int ServerPort=1044;
    static int ServerToClientPort=43266;
    public static int countsend=0;
    public static int countreceive=0;
    
    static UFTPImplementation uftp= new UFTPImplementation();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("UDP UFTP Server Started Successfully");
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
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("191.101.189.93"));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
//                    System.out.println(message);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    int offset=0;
                    int ll=uftp.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);

                    System.out.println("==========>"+ack.length());
                    System.out.println(ack);
                    
                    
                    countreceive+=1;
                    System.out.println("Received at server---------> "+countreceive);
//                   int len=200;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
//                    data=Utility.getRandomData(data, len);
//                    String hexdata1=Utility.bytesToHex(data);
//                    data=Utility.getRandomData(data, len);
//                    String hexdata2=Utility.bytesToHex(data);
//                    
//                    int idint=i%255+1;
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
////                    System.out.println("id ----> "+id );
//                    
//                    String m="310800d48987cb7e0a000001e6050519080000010100000100000000"+hexdata;
                    int len=ll;
                    byte[] newdata=new byte[offset+len+61];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> ");
                    System.out.println(m1);
                    InetAddress ia=InetAddress.getByName("191.101.189.93");
                    InetAddress sia=InetAddress.getByName("10.0.0.2");
                    
                    len2=uftp.createPacket(newdata, offset, len,ia,sia);
//                    System.out.println("================================>          "+ len2);
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
                    i++;
                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}