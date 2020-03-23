package ssdpclient;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.IDN;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;

public class SSDPClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;

    static int clientToServerPort=88;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
//    static CIGIImplementation cigi=new CIGIImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Udp SSDP Client Started...........");
        while(true){
            
            DatagramSocket ds=new DatagramSocket();
            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
            i++;
        }
       
    }

    private static class MySender {
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0,j=0;
                int countsend=0;
                while(i<numberOfPackets){
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
//                    System.out.println(hexdata);
                    
                    int len2=4;
                    byte[] data2 = new byte[len2];
                    data=Utility.getRandomData(data2, len2);
                    String hexdata2=Utility.bytesToHex(data,0,len2);
//                    System.out.println(hexdata2);
                    
                    len2=52;
                    byte[] data3 = new byte[len2];
                    data=Utility.getRandomData(data3, len2);
                    String hexdata3=Utility.bytesToHex(data,0,len2);
                    
                    
                    int len4=4;
                    byte[] data4 = new byte[len4];
                    data=Utility.getRandomData(data4, len4);
                    String hexdata4=Utility.bytesToHex(data,0,len4);
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
//                    String m=hexdata2+"00000000"+"00000002"+"000186a3"+"0000000300000013"+hexdata4+"00000034"+hexdata3+"0000000000000000"+"00000064"+hexdata;
                    
//                    String m="3025020101632004000a01020a0100020100020100010100870b6f626a656374436c6173733000";
//                    String m="3081a90201016381a304818264633d"+hexdata+"0a01020a0100020100020100010100870b6f626a656374436c6173733000";
                    /**working cldap**/
//                    String m="3026020101632104000a01020a0100020100020100010100a50c0403756964040541646d696e30000000"+hexdata;
                    
//                    String m="300c0201016007020103040080000000"+hexdata;
//                    String m="c80200764a3200000000000180080000000000028008000000020100800a00000003000000000008000000061130800900000007367065001900000008436973636f2053797374656d732c20496e632e80080000000905f780080000000a03e880160000000b508154fa7878436c331b3a2b11431373";
//                    /SMB not working
//                    String m="ff534d42a2000000000801c80000000000000000000000000100441c6500060018ff000000000e0000000000000000009f01020000000000000000000000000003000000010000000000000002000000001100005c007300720076007300760063000000";
//                    /rx not working
                    
//                    String m="a13ba33e5b27adcc000000000000000000000001060000020000003400000002845fa3390000000000000000";
                    String m="6c820203308201ffa103020105a20302010ca38201923082018e3082018aa103020101a28201810482017d6e82017930820175a003020105a10302010ea20703050000000000a381e26181df3081dca003020105a1091b07554d522e454455a21c301aa003020100a11330111b066b72627467741b07554d522e454455a381ab3081a8a003020101a103020172a2819b0481986cb05fd9ad09ae9c62a8f8e73b57d6ec2fe816e9b42f0f6e301b5b6dcd1434d229039f43210a08ea1e7fcffb5d3fb0d57a6da17b80f49e9938b8e16d0195fe894cd16b202fa9b6e403f21f64192d68eca1a623f3de3355be9160f8685d4139391fbfc484cca1d23f660d634a477ca7ed58ab91437f050e68f9eb7822f91df670929bda3ab4874042f6fb7bc7e87ca8af45bfbfdeeddb8873a47b3079a003020101a272047090f8782d33c4399ff546a6942b5f7e1cb5fb0e72efe6e04a702a6481550aafe4feb08e79921e15dbefb59a7116f43c595445b80ef5ea6f5632a8e6615d5e54670c90691bc3d3ed77c7a3c746ecdbeb10c7e42cf454c33208de9f68924fe1c13f478625be6f80f6465d6a55b62daf9227a45d305ba00703050040000000a2091b07554d522e454455a310300ea003020100a10730051b03616673a511180f31393939313231313231343830335aa7060204382b3993a8053003020101a911300f300da003020102a106040483972015";
                    
//                    int offset=0,len=61;
//                    
//                    byte[] newdata=new byte[offset+len+61];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
//                    len2=cigi.createPacket(newdata, offset, len);
////                    System.out.println("================================>          "+ len2);
//                   String m=Utility.bytesToHex(newdata,offset,len2);
//                   System.out.println(m);
//                   
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("---Total Packet Send---------------------------------> "+ totalSend);
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
                int startTime=(int)System.nanoTime();
                int countreceive=0;

                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfPackets){
                        ds.close();
                        System.out.println("-------------------------------------------------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,b1.length);
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
