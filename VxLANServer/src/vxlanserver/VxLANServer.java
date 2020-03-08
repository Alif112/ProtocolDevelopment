/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vxlanserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class VxLANServer {
    static int ServerPort=4789;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("CIGI VxLAN Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
//        CIGIImplementation cigi=new CIGIImplementation();
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
//                    System.out.println(message);
                    
                    
                     
//                    int ll=cigi.decodePacket(b, 0, 88);
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b, 0, ll);
//                    
//                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    int len=14;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
//                    System.out.println(hexdata);
                    
                    int len2=94;
                    byte[] data2 = new byte[len2];
                    data=Utility.getRandomData(data2, len2);
                    String hexdata2=Utility.bytesToHex(data2);
//                    System.out.println(hexdata2);
                    
                    
//                    int idint=i%256;
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
//                    String m="0110020040000000006ce72f000000002c18001800000000403a58a0902de00d405ff010624dd2f22b300015428ed4c7be152f99000000000000000043a875ec40341de6e8db8bad403a58a30e8293d9405ff0113d6bf064";
                    String m=hexdata+"b2afc242fd66"+"8038010100640060"+hexdata2;
                  
                    
//                    String m="01100200400000001a2c7a6w000000002c180018"+"00000000"+      "40"+s+hexdata1 +   "40"+latitude  +"2b30"+hexdata;
//                    int offset=0,len=61;
//                   byte[] newdata=new byte[offset+len+61];
//                    
//                    int len2=cigi.createPacket(b, offset, len);
//                    System.out.println("================================>          "+ len2);
//                   String m=Utility.bytesToHex(b,offset,len2);
//                   System.out.println(m);
//                    
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