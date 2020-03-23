
package ssdpserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class SSDPServer {
    static int ServerPort=88;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("SSDP Started Successfully");
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
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
//                    System.out.println(hexdata);
                    
                    int len2=4;
                    byte[] data2 = new byte[len2];
                    data=Utility.getRandomData(data2, len2);
                    String hexdata2=Utility.bytesToHex(data2);
//                    System.out.println(hexdata2);
                    
                    len2=52;
                    byte[] data3 = new byte[len2];
                    data=Utility.getRandomData(data3, len2);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    Random rand=new Random();
                    int idint=rand.nextInt();
                    byte bid=(byte) idint;
                    String id=Utility.byteToHex(bid);
                    System.out.println("id ----> "+id );
                    
                    
                    /** PPP MuxCP 8059, first hex20, second hex94 **/
//                    String m=hexdata+"8059010100640060"+hexdata2;
                    /** PPP OSINLCP 8059, first hex14, second hex94 **/
//                    String m=hexdata+"621b2f7e03f1"+"8023010100640060"+hexdata2;
//                    String m="5e1d0bdd0000000000000002000186a3000000030000001300000001000000343847760b00000009776572726d736368650000000000000000000001000000050000000100000000000000020000000300000011000000000000000000000020"+hexdata;
//                    String m=hexdata2+"00000000"+"00000002"+"000186a3"+"0000000300000013"+hexdata2+"00000034"+hexdata3+"0000000000000000"+"00000064"+hexdata;
                    /** RADIUS protocol**/
//                    String m="300c02010165070a012004000400";
//                    String m="307f020101637a045a6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f753d6275666665722c6f3d6f766572666c6f770a01020a0100020100020100010100870b6f626a656374436c6173733000";
//                  /**CLDAP***/
//                    String m="300c02010161070a0100040004000000"+hexdata;
                    /**** L2TP*/
//                    String m="c80200764a3200000000000180080000000000028008000000020100800a00000003000000000008000000061130800900000007367065001900000008436973636f2053797374656d732c20496e632e80080000000905f780080000000a03e880160000000b508154fa7878436c331b3a2b11431373";
//                  SMB not working 
//                    String m="ff534d42a2000000000801c80000000000000000000000000100441c6500060018ff000000000e0000000000000000009f01020000000000000000000000000003000000010000000000000002000000001100005c007300720076007300760063000000";
//                    String m="a13ba33e5b27adcc0000000000000000000000020701000200000034000000020000000039c2f22f0f43dc289f2d47cd881bf449687a4b63ac52d65381b2f635027e85e0bd9ce0a461a0611900000000000000383ec035b2e62ff8053c824063ef08e20585b6287e17d67a110b29247bfb2cf77a5d47ecd473e0679f590f00004b0f00002a0f00000e0f0000";
                    
//                    rx not working
//                    String m="a13ba33e5b27adcc000000000000000000000001060000020000003400000002845fa3390000000000000000";
                    String m="6d8201b2308201aea003020105a10302010da3091b07554d522e454455a4123010a003020101a10930071b056e6e65756ca581d16181ce3081cba003020105a1091b07554d522e454455a210300ea003020100a10730051b03616673a381a63081a3a003020101a2819b04819848217864b46571a0942bfd36be763afe68480aeeb770e898117eec5261355a67792207a93d655c053961c664989ba61a98d0bd8de977cf2f373a4c986255a125a69147821d6517e341fad3fe6e7b0098e5c8894240d0905fe4c4385841a539caf40ad9d63169b1c5ba904704786249be33c54e6cd77915d525b076ec4fb547416d1d0e274e27c163178693232f9f2654a045df488a095caba681ae3081aba003020101a281a30481a0e89f7d3961bff1b488a23a8970e2a69d6d554978a56209eeae7f13056e1a4ab4b5550749e60ff3682ae4f14c929f89f285b9e2fcae7a3a35b4bc1eaf6b91ef1942c5dc2c624b0318da3d1bdaacf3ea65a965803978adf728130a058d4975ab2879a7ba7773853ddce4dc974693e640f229f0d67688262766f2164b4301ae22c7b434ec8bdf16b5852d57ac5cccf8550c66f3116d092b9c82b603f412f6ad2a7b";
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