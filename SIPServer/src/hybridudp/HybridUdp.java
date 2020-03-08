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
    static int ServerPort=5060;
    static int ServerToClientPort=5060;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("UDP SIP Server Started Successfully");
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

                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
                    System.out.println(message);
                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    //Backend coding
                    String m="5349502f322e302031303020547279696e670d0a5669613a205349502f322e302f554450203139322e3136382e3130352e3131303a353036303b6272616e63683d7a39684734624b32303733370d0a526f7574653a203c7369703a3139322e3136382e3130352e3130353a353036303b6c723e0d0a46726f6d3a2032353033203c7369703a32353033403139322e3136382e3130352e3130353e3b7461673d31313131330d0a546f3a2032353033203c7369703a32353033403139322e3136382e3130352e3130353e0d0a43616c6c2d49443a2033303730403139322e3136382e3130352e3130350d0a435365713a20312052454749535445520d0a5365727665723a204272656b656b65204f6e444f20534950205365727665722028312e322e342e332f313434290d0a436f6e74656e742d4c656e6774683a20300d0a0d0a";
                   
                   byte[] b1=Utility.hexStringToByteArray(m);

                    

                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(ServerToClientPort);
                    ds.send(dp2);

                    System.out.println("------sending from server to ip:port: "+dp.getAddress()+":"+ServerToClientPort);

                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}
