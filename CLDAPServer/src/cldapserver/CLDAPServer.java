package cldapserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class CLDAPServer {
    static int ServerPort=389;
    public static int countsend=0;
    public static int countreceive=0;
    public static String ip="65.99.254.113";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("CLDAP server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        CLDAPImplementation cldap=new CLDAPImplementation();
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName(ip));
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());
//                    System.out.println(message);
                    
                    
                     
                    int ll=cldap.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
//                    
//                    int len=100;
//                    byte[] data = new byte[len];
//                    data=Utility.getRandomData(data, len);
//                    String hexdata=Utility.bytesToHex(data);
////                    System.out.println(hexdata);
//                    
//                    int len2=4;
//                    byte[] data2 = new byte[len2];
//                    data=Utility.getRandomData(data2, len2);
//                    String hexdata2=Utility.bytesToHex(data2);
////                    System.out.println(hexdata2);
//                    
//                    len2=52;
//                    byte[] data3 = new byte[len2];
//                    data=Utility.getRandomData(data3, len2);
//                    String hexdata3=Utility.bytesToHex(data3);
//                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
                    
                    /** PPP MuxCP 8059, first hex20, second hex94 **/
//                    String m=hexdata+"8059010100640060"+hexdata2;
                    /** PPP OSINLCP 8059, first hex14, second hex94 **/
//                    String m=hexdata+"621b2f7e03f1"+"8023010100640060"+hexdata2;
//                    String m="5e1d0bdd0000000000000002000186a3000000030000001300000001000000343847760b00000009776572726d736368650000000000000000000001000000050000000100000000000000020000000300000011000000000000000000000020"+hexdata;
//                    String m=hexdata2+"00000000"+"00000002"+"000186a3"+"0000000300000013"+hexdata2+"00000034"+hexdata3+"0000000000000000"+"00000064"+hexdata;
                    /** RADIUS protocol**/
//                    String m="300c02010165070a012004000400";
//                    String m="307f020101637a045a6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f3d6f766572666c6f770a01020a0100020100020100010100870b6f626a656374436c6173733000";
//                    String m="300c02010161070a0100040004000000"+hexdata;
                    
                  
                    
//                    String m="01100200400000001a2c7a6w000000002c180018"+"00000000"+      "40"+s+hexdata1 +   "40"+latitude  +"2b30"+hexdata;
                    int offset=0,len=100;
                   byte[] newdata=new byte[offset+len+100];
                    
                    int len2=cldap.createPacket(b, offset, len);
//                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(b,offset,len2);
//                   System.out.println(m);
                    
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