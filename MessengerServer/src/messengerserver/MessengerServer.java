package messengerserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class MessengerServer {
    static int ServerPort=1027;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("Test server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        CoAPImplementation dspv2=new CoAPImplementation();
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
                    
//                    
//                     
//                    int ll=dspv2.decodePacket(b, 0, dp.getLength());
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("=====received len=====>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=100;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=16;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);                   
                    
                    Random rand=new Random();
                    int idint=rand.nextInt();
                    byte bid=(byte) idint;
                    String id=Utility.byteToHex(bid);
                    System.out.println("id ----> "+id );
                    
//                    messenger
                    String m="040128001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fcb05b8b3905acb97415260f7f7096c2210000000001000000000000000000ffffffff66010000000010000000000000001000000053454355524954590000000000000000100000000000000010000000414c4552540000000000000000000000"
                            + "220100000000000022010000"
                            
//                            + "4d6963726f736f66742057696e646f77732068617320646574656374656420796f757220696e7465726e65742062726f777365720a697320696e666563746564207769746820537079776172652c2041642d776172652c20616e64205468696566776172652e0a0a596f7572207072697661637920697320696e2064616e6765722e0a5765207265636f6d6d656e6420616e20696d6d6564696174652073797374656d207363616e2e20506c656173652076697369740a0a687474703a2f2f53776970655370792e636f6d0a0a4661696c75726520746f20646973696e6665637420636f756c6420616c6c6f772074686972642070617274696573206163636573730a746f20796f757220706572736f6e616c20696e666f726d6174696f6e2e0a00";
                            + ""+hexdata;
                    
                    
//                    int offset=0;
//                    len=100;
//                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
////                    System.out.println("--------------> ");
////                    System.out.println(m1);
//                    
//                    len2=dspv2.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    
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