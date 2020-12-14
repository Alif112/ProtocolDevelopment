package xtacacsserver2;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;

public class XtacacsServer2 {
    public static String[] ipArray={"65.99.254.85","65.99.254.84","65.99.254.10","65.99.254.11"};
    static String ip1="65.99.254.85",ip2="65.99.254.84";
    static int ipArrayLen=ipArray.length;
    static String ipCheck,ipCheck2;
    static int ServerPort=1069;
    static int ServerPort2=1070;
    public static int countsend=0;
    public static int countreceive=0;
    public static int i,j,k;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, InterruptedException {
        
        System.out.println("XTACACS server Started Successfully");

        Thread t=new MyThread();
        t.start();
//        Thread mySender=new MySender();
//        mySender.start();
        
    }

    private static class MyThread extends Thread {
        XTACACSImplementation xtacacs=new XTACACSImplementation(false);
        public MyThread() {
        }

        @Override
        public void run() {
            try{
                
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName(ip1));
                
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                
                while(true){
                    ds.receive(dp);
                    String message=new String(dp.getData(),0,dp.getLength());  
                    int ll=xtacacs.decodePacket(b, 0, dp.getLength());
                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b, 0, ll);
                    System.out.println("----Ttotal received---------------------------------> "+countreceive++);
                    
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=100;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    int offset=0;
                    len=108;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    Inet4Address isend=(Inet4Address) dp.getAddress();
                    int portSend=dp.getPort();
                    len2=xtacacs.createPacket(newdata, offset, len,isend,portSend);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
//                    
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(dp.getPort());
                    ds.send(dp2);
                    
                    
                    countsend+=1;
                    System.out.println(ds+"--Sending from server--------------------->  "+countsend);
                    
                }

            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
    private static class MySender extends Thread {
        XTACACSImplementation xtacacs=new XTACACSImplementation(false);
        public MySender() {
        }

        @Override
        public void run() {
            try{
                
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(ServerPort2, InetAddress.getByName(ip2));
                
                
                byte[] b=new byte[2048];

                DatagramPacket dp=new DatagramPacket(b, b.length);
                DatagramPacket dp2 = new DatagramPacket(b, b.length);
                int i=0;
                while(true){
                ds.receive(dp);
                System.out.println("Received Client address===============>"+ds);
                    
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=100;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    int offset=0;
                    len=108;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    Inet4Address isend=(Inet4Address) dp.getAddress();
                    int portSend=dp.getPort();
                    len2=xtacacs.createPacket(newdata, offset, len,isend,portSend);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
//                    
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    dp2.setData(b1);
                    dp2.setAddress(dp.getAddress());
                    dp2.setPort(dp.getPort());
                    ds.send(dp2);
                    
                    
                    countsend+=1;
                    System.out.println(ds+"--Sending from server--------------------->  "+countsend);
                    Thread.sleep(200);
                }


            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    

    
    
}