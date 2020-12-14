package mihclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MIHClient {
    static int delayTime=200;
    static int clientToServerPort=4551;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
    static Slimp3Implementation isakmp=new Slimp3Implementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Udp Slimp3 Client Started...........");
//        Scanner sc=new Scanner(System.in);
//        int fixed=sc.nextInt();
//       if(fixed==-1){
//            DatagramSocket ds=new DatagramSocket();
//
//            MySender mySender=new MySender(ds);
//            mySender.init();
//            Thread myReceiver=new MyReceiver(ds);
//            myReceiver.start();
//            i++;
//        
//       }
//       else{
           while(true){
            DatagramSocket ds=new DatagramSocket();

            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
            i++;
        }
//       }
        
       
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
                    int len=230;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=108;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
                    
//                    MIH
                    String m="3c3133333e4a756c"
                            + "2031392031313a30313a3335204150502d444f544e45542d342045766e74534c6f653a203c6576436e743e3c6975743e6e74656c723c2f6975743e3c6d73676e756d3e35353538333333393c2f6d73676e756d2e3cf3657665726974793e5b4155535d3c2f73657665726874793e3c757365723e4e5420415554484f524954595c53597754454d3c2f757365723e3c6c6f67747970653e53656375726974793c2f6c6f67747970653e3c736f757263653e4150502d444f544e45542d343c2f736f757263653e3c736f7572636570726f633e53656375726974793c2f736f7572636536726f633e3c69643e3536303c2f69643e3c6d61673e4f626a656374204f70656e3a204f626a656374205365727665723a205365637572697479204f626a65637420547970653a2046696c65204f626a656374204e616d653a20443a5c496e65747075625c777777726f6f745c50504c5c244e54415050535c53656341646d5c42696e2048616e646c652049443a2032343434204f7065726174696f6e2049443a204a312c323034313637353539397d2050726f636573732049443a203134383020496d6167652046696c65204e616d653a20443a5c50726f6772616d2046696c65735c4d6963726f736f6674204170706c69636174696f6e2043656e7465725c6163737265706c2e657865205072696d6172792055736572204e616d653a204150502d444f544e45542d3424205072696d61727920426f6d61696e3a2050504c574542535256205072696d617279204c6f676f6e2049443a20283078302c30783345372920436c69656e742055736572204e616d653a202d20436c69656e7420446f6d61696e3a202d20436c69656e74204c6f676f6e2049443a202d2041636365737365733a20524541445f434f4e54524f4c2053f54e4348524f4e495a45204143434553535f5359535f53454320526561644461746120286f72204c6973744469726563746f7279292052656164454120457865637574652f54726176657273653352657164417474726962757465732050726976696c656765733a2053654261636b757050726976316c65676520526573747269637465642053696420436f756e7468663020416363657373204d61736b3a20307831313230304139203c2f6d73673e7763617465676f72793e333c2f63617465676f72793e3c2f6576656e743e";
                    
                    int offset=0;
                    len=200;
                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
//                    len2=isakmp.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                  
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
                    Thread.sleep(delayTime);
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
//                    String received= new String(dp1.getData(),0,b1.length);
//                    int ll=isakmp.decodePacket(b1, 0, dp1.getLength());
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);
                    
                    
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
