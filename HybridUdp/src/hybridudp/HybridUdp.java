/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hybridudp;



/**
 *
 * @author User
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
public class HybridUdp {
    static int ServerPort=2427;
    static int ServerToClientPort=2427;
    static int countsend=0;
    static int countreceive=0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("UDP Server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {

        public MyThread() {
        }

        @Override
        public void run() {
            try{
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("191.96.12.103"));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);

                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
                    System.out.println(message);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    //Backend coding
                    int len=61;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                
//                    String m="323030203331363536383630206f6b"+  hexdata  +"0d0a0d0a";
                    String m="32303020333136353638363020"+  hexdata  +"0d0a0d0a";
                   
                   byte[] b1=Utility.hexStringToByteArray(m);

                    

                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(ServerToClientPort);
                    ds.send(dp2);
                    countsend+=1;
                    System.out.println("------sending from server to ip:port: "+dp.getAddress()+":"+ServerToClientPort);
                    
                    System.out.println("----------> "+countsend);

                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}
