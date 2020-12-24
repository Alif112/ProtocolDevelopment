package baseclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BaseClient {
    static int delayTime=200;
    static int clientToServerPort=5007;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean check=true;
    static int socketClosedCount=0;
    
    
    static String ip="65.99.254.85";
    
    static BVLCImplementation isakmp=new BVLCImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Udp Base Client Started...........");
        Scanner sc=new Scanner(System.in);
        int fixed=sc.nextInt();
        check=true;
       if(fixed==-1){
           check=false;
           numberOfPackets=99999;
            DatagramSocket ds=new DatagramSocket();

            MySender mySender=new MySender(ds);
            mySender.init();
            i++;
        
       }
       else{
           while(true){
               numberOfPackets=fixed;
                DatagramSocket ds=new DatagramSocket();

                MySender mySender=new MySender(ds);
                mySender.init();

                i++;
             }
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
                
                Thread myReceiver=new MyReceiver(ds);
                myReceiver.start();
                while(i<numberOfPackets){
                    int len=200;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=108;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    


//                    STUN
                    String m="000100542112a442fd35d5ae3101f6a0b71c2d05002400046e001eff8029000828ee15f9825e68a70006000b64667074663a39696868340080220009696365346a9b6f726700000000080014ba84e711110be2f48b3521ed72d96d1f987f33e6802800040b1af628"
                            + "";

//                    SSDP
//                    String m="4d2d534541524348202a20485454502f312e310d0a486f73743a5b464630323a3a435d3a313930300d0a53543a75726e3a4d6963726f736f66742057696e646f77732050656572204e616d65205265736f6c7574696f6e2050726f746f636f6c3a2056343a495056363a4c696e6b4c6f63616c0d0a4d616e3a22737364703a646973636f766572220d0a4d583a330d0a0d0a"
//                            + ""+hexdata;

                    //                    Time 
//                    String m="9b8201100001000000080000204641464345504545434e464445464643464745464643444543414341434943410000200001"
//                            + ""+hexdata;
//                    SIP
//                    String m="5349502f322e302031303020aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
//                    LDSS
//                    String m="000101001ed7928c69b4a82c3209b3dd210c6d1e43965468000000000000000000000000000000000000000000000a060000000000000a06000000000000000017c7000000000000"
//                            + "";
                            

//                    NTP
//                    String m="19030bfa00003000000029740a023719cf46ac056c428332cf46ac054a894a96cf46ac0568428184cf46b406564eb9f85224000000000000000000000000000000000000"
//                            + "000000"+hexdata;

//                    String m="040000000000000000000000000000000000"+hexdata;

//                    BVLC
//                    String m="810200760108000106c0a80018bac020a201"
//                            + ""+hexdata;
                    
//                    int offset=0;
//                    len=100;
                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
//                    len2=isakmp.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                  
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName(ip);
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
//                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("Total Packet Send---------------> "+ totalSend);
                    delayTime=m.length();
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
                        System.out.println("--> "+ ++socketClosedCount +" --------------------------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,dp1.getLength());
//                    int ll=isakmp.decodePacket(b1, 0, dp1.getLength());
                    System.out.println("==============================================> "+dp1.getLength());
//                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);

                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
