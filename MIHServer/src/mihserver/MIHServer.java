package mihserver;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;

public class MIHServer {
    static int ServerPort=4551;
    public static int countsend=0;
    public static int countreceive=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        System.out.println("MIH server Started Successfully");
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        Slimp3Implementation xtacacs=new Slimp3Implementation();
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
                    
//                    int ll=xtacacs.decodePacket(b, 0, dp.getLength());
//                    System.out.println("==============================================> "+ll);
//                    String ack=Utility.bytesToHex(b, 0, ll);
//                    
//                            
//                    System.out.println("==========>"+ack.length());
//                    System.out.println(ack);
//                    System.out.println(message.length()+" Received at server--> "+message);
                    
                    int len=230;
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
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
//                    MIH
                    String m="3c3133333e4a756c2031392031313a30313a3335204150502d444f544e45542d342045766e74534c6f653a203c6576436e743e3c6975743e6e74656c723c2f6975743e3c6d73676e756d3e35353538333333393c2f6d73676e756d2e3cf3657665726974793e5b4155535d3c2f73657665726874793e3c757365723e4e5420415554484f524954595c53597754454d3c2f757365723e3c6c6f67747970653e53656375726974793c2f6c6f67747970653e3c736f757263653e4150502d444f544e45542d343c2f736f757263653e3c736f7572636570726f633e53656375726974793c2f736f7572636536726f633e3c69643e3536303c2f69643e3c6d61673e4f626a656374204f70656e3a204f626a656374205365727665723a205365637572697479204f626a65637420547970653a2046696c65204f626a656374204e616d653a20443a5c496e65747075625c777777726f6f745c50504c5c244e54415050535c53656341646d5c42696e2048616e646c652049443a2032343434204f7065726174696f6e2049443a204a312c323034313637353539397d2050726f636573732049443a203134383020496d6167652046696c65204e616d653a20443a5c50726f6772616d2046696c65735c4d6963726f736f6674204170706c69636174696f6e2043656e7465725c6163737265706c2e657865205072696d6172792055736572204e616d653a204150502d444f544e45542d3424205072696d61727920426f6d61696e3a2050504c574542535256205072696d617279204c6f676f6e2049443a20283078302c30783345372920436c69656e742055736572204e616d653a202d20436c69656e7420446f6d61696e3a202d20436c69656e74204c6f676f6e2049443a202d2041636365737365733a20524541445f434f4e54524f4c2053f54e4348524f4e495a45204143434553535f5359535f53454320526561644461746120286f72204c6973744469726563746f7279292052656164454120457865637574652f54726176657273653352657164417474726962757465732050726976696c656765733a2053654261636b757050726976316c65676520526573747269637465642053696420436f756e7468663020416363657373204d61736b3a20307831313230304139203c2f6d73673e7763617465676f72793e333c2f63617465676f72793e3c2f6576656e743e";
                    
                    
                    int offset=0;
                    len=200;
                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
//                    len2=xtacacs.createPacket(newdata, offset, len);
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