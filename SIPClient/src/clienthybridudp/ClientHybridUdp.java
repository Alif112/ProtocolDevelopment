/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthybridudp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author User
 */
public class ClientHybridUdp {
    static int clientPort=5060;
    static int clientToServerPort=5060;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        System.out.println("Udp Client Started...........");
        DatagramSocket ds=new DatagramSocket(clientPort);
        Thread mySender=new MySender(ds);
        mySender.start();
        Thread myReceiver=new MyReceiver(ds);
        myReceiver.start();
       
    }

    private static class MySender extends Thread {
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        @Override
        public void run() {
            try {
                int i=0;
                while(i<20){
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                
//                    String m="323030203331363536383630206f6b"+  hexdata  +"0d0a0d0a";
//                    String m="32303020333136353638363020"+  hexdata  +"0d0a0d0a";
                    String m="5245474953544552207369703a3139322e3136382e3130352e313035205349502f322e300d0a5669613a205349502f322e302f554450203139322e3136382e3130352e3131303a353036303b6272616e63683d7a39684734624b32303733370d0a526f7574653a203c7369703a3139322e3136382e3130352e3130353a353036303b6c723e0d0a46726f6d3a2032353033203c7369703a32353033403139322e3136382e3130352e3130353e3b7461673d31313131330d0a546f3a2032353033203c7369703a32353033403139322e3136382e3130352e3130353e0d0a43616c6c2d49443a2033303730403139322e3136382e3130352e3130350d0a435365713a20312052454749535445520d0a436f6e746163743a20223235303322203c7369703a32353033403139322e3136382e3130352e3131303a353036303b7472616e73706f72743d7564703e0d0a457870697265733a20333630300d0a4d61782d466f7277617264733a2037300d0a537570706f727465643a207265706c616365730d0a557365722d4167656e743a205349502049502d4445435420676174657761792c204e4543205068696c69707320556e696669656420536f6c7574696f6e7320200d0a416c6c6f773a20494e564954452c2041434b2c2043414e43454c2c204259452c2052454645522c204f5054494f4e532c20494e464f0d0a4163636570743a206170706c69636174696f6e2f7364700d0a436f6e74656e742d4c656e6774683a20300d0a0d0a";
                    byte[] b1=Utility.hexStringToByteArray(m);
//                    byte[] c = new byte[b1.length];
                    
                    for(int j=0;j<b1.length;j++){
                        System.out.print(j+"-> "+b1[j]+" ");
                    }
                    System.out.println();
                    
//                    byte[] c = new byte[b.length + b1.length];
//                    System.arraycopy(b, 0, c, 0, b.length);
//                    System.arraycopy(b1, 0, c, b.length, b1.length);
                    
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
                    
                    System.out.println(message.length()+" Send from client---> : "+message);
                    
                    Thread.sleep(500);
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
                while(true){
                    byte[] b1= new byte[2048];
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
                    ds.receive(dp1);
                    
                    String received= new String(dp1.getData(),0,b1.length);
                    System.out.println("Received at client:-->  "+ received);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
