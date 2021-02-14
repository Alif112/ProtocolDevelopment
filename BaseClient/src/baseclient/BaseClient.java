package baseclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class BaseClient {
    static int delayTime=300;
    static int clientToServerPort=496;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean check=true;
    static int socketClosedCount=0;
    
    
//    static String ip="207.210.233.111";
    static String ip="65.99.254.78";
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

                    
                    String m="ffb99e024fef78c0bdb37708386c25540b1005804b000000000000280000000c000000010100000e0000003ee00000f8cafc3c57a9d5cf2afef876fbc6118fda71b578f05769de7a7ec35abedcf8567f35c0cfd4b97e4690cfe91583d9faabe07ff846b03b1d5a6f1606ff50";
                    //AYIYA
//                    String m="0bd429c6018000237067079a0cf11e2c9ccd8536645b4d333ce8666c86651fa78223ca0811765c22850819720000"+hexdata;
//                    Mac-telnet
//                    String m="01010018f391bcec000c424360ce002f001500000009563412ff0200000011003b89fdc4f60a50482dee2203c5aafe09560812ff030000000561646d696e563412ff04000000056c696e7578d93412ff05000000250000563412ff06000000021800"+hexdata;
//                    /cigi
//                    String s="e7e4df7b1fe706e36fef26839a493d3b4400003a000000054c494d4500005b0c7075600bcbdc89190c4de84853fab6e8b7f10497d5e7e90045010100006d81efa0884f2a57aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                    
//                    System.out.println(s.toUpperCase());
                    
                    
//                    String m=s+hexdata;
                    
//                    RTP
//                    String m="8012001a00232580aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
                    //                    DB-LSP-DISC
//                    String m="7b2276657273696f6e223a205b322c20305d2c2022706f7274223a2030373530302c2022686f73745f696e74223a203233323337323832393631373331313737313038333130363530383730343534363130303234352c2022646973706c61796e616d65223a2022222c20226e616d65737061636573223a205b344441313039393230302c20363339383532313030382c20363837313835373032342c20313330343734393431362c20343334373238303230382c20333930393138393638302c203935353738353635362c20363837303637303431362c20383637323236383838305d7d";
//                    Dppv2
//                    String m="810c020c82806480"
//                            + "000000"+hexdata;
//                    GSM RLC/MAC
//                    String m="0204010027000000ffffffff0b00ff00400ea0c968422b2b742b2b2b2b2b2b2b2b2b892b2b2b2b2b"+hexdata;
//                    GTP
//                    String m="3210008800000000000200000222880143658709f20e010ffd10201d0c0c11201d0c0c14051a0800800002f1aa83000803617031036e673184002e80c0231101010011056669736e360673656372654f802116010000160306000000008106000000008306000000008500040a668e1b8500040a668e1b860007910300250500f187000c0123711f9296686874056868";
                    

//                    STUN
//                    String m="000100542112a442fd35d5ae3101f6a0b71c2d05002400046e001eff8029000828ee15f9825e68a70006000b64667074663a39696868340080220009696365346a9b6f726700000000080014ba84e711110be2f48b3521ed72d96d1f987f33e6802800040b1af628"
//                            + "";

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
                    System.out.println("Total Packet Sent -----------> "+ totalSend);
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
//                    System.out.println("==============================================> "+dp1.getLength());
//                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);

                    totalReceive+=1;
                    System.out.println("Total Received: "+dp1.getLength()+" at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
