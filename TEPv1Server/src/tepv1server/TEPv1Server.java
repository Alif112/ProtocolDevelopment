package tepv1server;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import sun.audio.AudioPlayer;

public class TEPv1Server {
    static int ServerPort=3567;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("TEPv2 server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        TEPv1Implementation dspv2=new TEPv1Implementation();
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("65.99.254.85"));
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
                    int ll=dspv2.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    
                            
//                    System.out.println("=====received len=====>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=50;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );

//                    tepv1
                    
//                    String m="810c0112"
//                            + "825004"
//                            + "808001"
//                            + "d47a85e073484b94239217a9d3957f920b637d659212f49387c64e68858777e8"
//                            + "b9d5ddc8ebd19697d7b780e5e3e3daae7363dbf23bf228f99a8bc05012748166"
//                            + "f49c8ba80e1649e69e96e9f0268680d9682ba36093f7117e78fbc27b09255492"
//                            + "0001890701007f03bffbff200e869b1d69c26c"
//                            + "030101008080030101"
//                            + "b401ffffffff01000100"
//                            + "0000"+hexdata;
                   
                    
                    
                    int offset=0;
                    len=200;
                    
                    byte[] newdata=new byte[offset+len+200];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=dspv2.createPacket(newdata, offset, len);
//                    System.out.println("================================>          "+ len2);
                   String m=Utility.bytesToHex(newdata,offset,len2);
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