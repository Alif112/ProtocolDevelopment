
package uftpclient;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author User
 */
public class UFTPClient {
    static int clientPort=43266;
    static int clientToServerPort=1044;
    public static int countsend=0;
    public static int countreceive=0;
    public static long timeDelayAtReceiver=(long)5e9;
    public static int numberOfPackets=3;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        while(true){
            System.out.println("Udp UFTP Client Started...........");
            DatagramSocket ds=new DatagramSocket();
            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start(); 
            
            
        }

       
    }

    private static class MySender{
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0;
                
                while(i<numberOfPackets){
                    int len=200;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    data=Utility.getRandomData(data, len);
                    String hexdata1=Utility.bytesToHex(data);
                    data=Utility.getRandomData(data, len);
                    String hexdata2=Utility.bytesToHex(data);
                    
                    int idint=i%255+1;
                    byte bid=(byte) idint;
                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
                    String m="310800d48987cb7e0a000001e6050519080000"+id+""+id+"00000100000000"+hexdata;
//                    String m="310800d48987cb7e0a000001e6050519080000010100000100000000"+hexdata;

                    byte[] b1=Utility.hexStringToByteArray(m);

                    
                    InetAddress ia=InetAddress.getByName("191.101.189.93");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());

                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    
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
                long startTIme=(long)System.nanoTime();
                int packetreceived=0;
                while(true){
                    long currentTIme=(long)System.nanoTime();
                    long checkTime=currentTIme-startTIme;
                    if(checkTime>=timeDelayAtReceiver || packetreceived==numberOfPackets){
                        System.out.println("-Socket closed-------------------------------------> "+ds);
                        ds.close();
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
                    ds.receive(dp1);
                    countreceive+=1;
                    packetreceived+=1;
                    String received= new String(dp1.getData(),0,b1.length);
                    System.out.println("Received at client:----------------------> "+ countreceive);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
