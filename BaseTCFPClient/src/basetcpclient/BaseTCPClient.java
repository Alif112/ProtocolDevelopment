/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basetcpclient;

import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author User
 * hart_ip port 5094
 * imap port 143
 * exec port 512
 * 
 */
public class BaseTCPClient {
    
    public static int clientport=9991;
    public static int delayTime=300;
    
    
    public static int numberOfPackets=5000;
    public static int numberOfMultiPkt=10;
    public static long receiverTime=(long) 10e10;
    static String ip="65.99.254.85";
    public static int offset,len;
    public static boolean check=true;
    
    public static int sendcount=0;
    public static int receivecount=0;
    static int chk;
    static COPSImplementation cops=new COPSImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("======> Enter -1 ..... for fixed socket anything else for multisocket");
        chk=sc.nextInt();
        if(chk==-1){
            Thread mysender=new MySender();
            mysender.start();
        }else{
            while(true){
                try {
                    Socket socket=new Socket(ip,clientport);

    //                socket=new Socket("192.168.19.125",1212);
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    MultiSender multiSender=new MultiSender(socket, os,is);
                    multiSender.init();
                    

                    } catch (Exception e) {
                        e.printStackTrace();
                }
                
                
            }
            
        }
    }
    
    static class MySender extends Thread {


        @Override
        public void run() {

            try {
                Socket socket=new Socket(ip,clientport);
                
//                socket=new Socket("192.168.19.125",1212);
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                Thread myReceiver=new MyReceiver(socket,is);
                myReceiver.start();

                socket.setTcpNoDelay(true);
                System.out.println("----------------> Socket established");
                int i=0;
                byte[] message=new byte[4096];
                
                len=100;
                offset=0;
                
                while (i<numberOfPackets){
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
//                    System.out.println("---sending from client----------->\n"+hexdata2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
//                    pvfs
                    String m="bfca00000200000005000000000000003800000000000000d9270000020000000100000000000000e8030000e8030000be71d33f02000000b08039430100000003000080000000000100000001000000"+hexdata2;
//                    mysql
//                    String m="012c"+hexdata2;
                    //mysql
//                    String m="0f0000000373686f7720646174616261736573"+hexdata2;
//                    BGP
//                    String m="ffffffffffffffffffffffffffffffff001d0104fe0900b4c0a8000f0000"+hexdata2;
                    
                    //                    MPA
//                    String m="4d504120494420526571204672616d65400101340000001000000000"+hexdata2;
                    
//                    SMB
//                    String m="0000003bff534d422e000000001807c80000000000000000000000000008fffe0108300f0cff00dede07800000000004000400000000000400000000000000"
//                            + ""+hexdata2;
                    
//                    CQL
//                    String m="04000003070000005c0000005553454c45435420706565722c20646174615f63656e7465722c207261636b2c20746f6b656e732c207270635f616464726573732c20736368656d615f76657273696f6e2046524f4d2073797374656d2e7065657273000100"+hexdata2;
                    
//                    IPA
//                    String m="5345545f504152414d4554455220727473703a2f2f3139322e3136382e332e3130372f3334313539313037333820525453502f312e300d0a435365713a20340d0a"
//                            +hexdata2+"0d0a";
                    
                    //                    SMTP
//                    String m="41555448204c4f47494e0d0a"+hexdata2+"0d0a";
                    
//                    Vx11
//                    String m="800000400c5a27360000000000000002000607af000000010000000a00000000000000000000000000000000"
//                            + "0a03df9800000000000000000000000767706962302c3000"
//                            + ""+hexdata2;
                    
//                    Gnutella
//                    String m="000080daab8aca56fc8c15dffa343b33b8ad0711084e553c77d991dbf93e82d8522862450e40"+hexdata2;
                    
//                    IMAP
//                    String m="6130303035204e4f4f500d0a"+hexdata2;
                    
//                    Gryphon
//                    String m="030002000034010050000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"+hexdata2;
//                    String m="0000"+hexdata2;
                            
                    
                    //SMB need handshake
//                    String m="000000beff534d4272000000000801c80000000000000000000000000000091900000100009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200";
                    
                    
//                    SSL
//                    String m="1603010200010001fc03035ae206390be8019e4e6e04a76051fb2b194cb554ec1ef4cfa86cad43c698242f206e3b206fe0818e3f5ef0d17be4285bcb04b224eb7bc9edcb4a7a0cdf2bb406d900201a1a130113021303c02bc02fc02cc030cca9cca8c013c014009c009d002f0035010001935a5a0000000000180016000013636c69656e7473342e676f6f676c652e636f6d00170000ff01000100000a000a0008eaea001d00170018000b00020100002300000010000e000c02683208687474702f312e31000500050100000000000d0012001004030804040105030805050108060601001200000033002b0029eaea000100001d002070b4929d12f6148fd0c0fee53a1b505af364c375d96ae016c1c806ab0907c26b002d00020101002b000b0a1a1a0304030303020301001b0003020002caca000100001500c9000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
//                    EXEC
//                     String m="3338343000"+hexdata2;
//                    IMAP
//                    String m="6130303031204c4f47494e20226e65756c696e6765726e222022585858585858220d0a";
//                    HART_IP
//                    String m="010000000002000d0100007530"+hexdata2;
                    //                    Gryphon
//                    String m="0300020000340100500000000000000000000000"
//                            + "000000000000000000000000000000000000000"
//                            + "00000000000000000000000000000000000000000"+hexdata2;
                    
                    
//                    EXEC
//                    String m="737465666973383600";      
                    
//                    EPMD
//                    String m="013e78a8264d000005000500056e6f6465320000"+hexdata2;
                    
                    
                    
//                    offset=0;
//                    len=200;
//                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
//                    len2=cops.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] senddata=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
                    os.write(senddata);
                    sendcount+=1;
                    System.out.println("-----------> Send Data From Client<-------- "+sendcount);

                    Thread.sleep(delayTime);
                    i++;
                }

            }catch (Exception e){e.printStackTrace();}

        }


    }

    static class MyReceiver extends Thread {
        Socket socket;
        InputStream is;
        public MyReceiver(Socket socket,InputStream is) {
            this.socket=socket;
            this.is=is;
        }

        @Override
        public void run() {
            try{

                byte[] data=new byte[4096];
                int index=offset;
                while(true) {
                    Functions.ignoreByte(is, 348);
//                    EPMD
//                    Functions.ignoreByte(is, 4);
//                    int createLen=Utility.buildLen2(is);
//                    is.read(data,offset,createLen);
//                    String d=Utility.bytesToHex(data,offset,createLen);
//                    System.out.println("=============> "+createLen);
//                    System.out.println("------> \n"+d);
                    
//                    len=cops.decodePacket(data, offset, is);
//                    String msg=Utility.bytesToHex(data, offset, len);
//                    System.out.println("====received at client============> "+len);
//                    System.out.println(msg);
                    
                    
                    receivecount+=1;
                    System.out.println("--------> Received at Client -------->  "+receivecount);
                    //is.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private static class MultiSender {
        Socket skt;
        OutputStream os;
        InputStream is;
        public MultiSender(Socket skt, OutputStream os, InputStream is) {
            this.skt=skt;
            this.os=os;
            this.is=is;
        }

        public void init() {
            try {
                int i=0,j=0;
                int countsend=0;
                byte[] message=new byte[4096];
                MultiReceiver multiReceiver=new MultiReceiver(skt, is);
                multiReceiver.start();
                while(i<numberOfMultiPkt){
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
//                    
//                    len=4;
//                    byte[] data3 = new byte[len];
//                    data3=Utility.getRandomData(data3, len);
//                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
                    //                    pvfs
                    String m="bfca00000200000005000000000000003800000000000000d9270000020000000100000000000000e8030000e8030000be71d33f02000000b08039430100000003000080000000000100000001000000"+hexdata2;
//                    mysql
//                    String m="012c"+hexdata2;
                    //mysql
//                    String m="0f0000000373686f7720646174616261736573"+hexdata2;
                    //                    BGP
//                    String m="ffffffffffffffffffffffffffffffff001d0104fe0900b4c0a8000f0000"+hexdata2;
                    
                    
                    //                    MPA
//                    String m="4d504120494420526571204672616d65400101340000001000000000"+hexdata2;
                    
                    //                    SMB
//                    String m="0000003bff534d422e000000001807c80000000000000000000000000008fffe0108300f0cff00dede07800000000004000400000000000400000000000000"
//                            + ""+hexdata2;
                    //                    CQL
//                    String m="04000003070000005c0000005553454c45435420706565722c20646174615f63656e7465722c207261636b2c20746f6b656e732c207270635f616464726573732c20736368656d615f76657273696f6e2046524f4d2073797374656d2e7065657273000100"+hexdata2;
                    
                    
                    //                    IPA
//                    String m="5345545f504152414d4554455220727473703a2f2f3139322e3136382e332e3130372f3334313539313037333820525453502f312e300d0a" +
//                        "435365713a20340d0a"+hexdata2+"0d0a";
                    
//                    SMTP
//                    String m="41555448204c4f47494e0d0a"+hexdata2+"0d0a";
//                    String m="5265706c792d546f3a203c7878787878784078787878782e636f2e756b3e0d0a46726f6d3a202257536861726b205573657222203c7878787878784078787878782e636f2e756b3e0d0a546f3a203c7878787878782e787878784078787878782e636f6d3e0d0a5375626a6563743a2054657374206d65737361676520666f7220636170747572650d0a446174653a2053756e2c203234204a756e20323030372031303a35363a3033202b303230300d0a4d494d452d56657273696f6e3a20312e300d0a436f6e74656e742d547970653a206d756c7469706172742f6d697865643b0d0a09626f756e646172793d222d2d2d2d3d5f4e657874506172745f3030305f303031325f30314337423634452e3432364338313230220d0a582d4d61696c65723a204d6963726f736f6674204f6666696365204f75746c6f6f6b2c204275696c642031312e302e353531300d0a5468726561642d496e6465783a20416365324f364d30574779564a50337251437551655056484b576f3541673d3d0d0a582d4d696d654f4c453a2050726f6475636564204279204d6963726f736f6674204d696d654f4c452056362e30302e323930302e333133380d0a0d0a546869732069732061206d756c74692d70617274206d65737361676520696e204d494d4520666f726d61742e0d0a0d0a2d2d2d2d2d2d3d5f4e657874506172745f3030305f303031325f30314337423634452e34323643383132300d0a436f6e74656e742d547970653a20746578742f706c61696e3b0d0a09636861727365743d2275732d6173636969220d0a436f6e74656e742d5472616e736665722d456e636f64696e673a20376269740d0a0d0a0d0a54657374696e672c2074657374696e672c203120322033200d0a0d0a2d2d2d2d2d2d3d5f4e657874506172745f3030305f303031325f30314337423634452e34323643383132300d0a436f6e74656e742d547970653a20746578742f706c61696e3b0d0a096e616d653d22776f7264732e747874220d0a436f6e74656e742d5472616e736665722d456e636f64696e673a2071756f7465642d7072696e7461626c650d0a436f6e74656e742d446973706f736974696f6e3a206174746163686d656e743b0d0a0966696c656e616d653d22776f7264732e747874220d0a0d0a2d543d32300d0a3a3a20557365725761726e696e673a205468652073616d65206669656c64206e616d657320666f7220646966666572656e742074797065732e204578706c69636974206669656c64203d0d0a72656e616d696e67206973207265636f6d6d656e6465642e0d0a783132315f6463635f636f64650d0a20783132315f6463635f636f64655f30312020202020416464724e756d65726963537472696e67202020203d0d0a506879736963616c44656c6976657279436f756e7472794e616d652f783132312d6463632d636f64650d0a20783132315f6463635f636f646520202020202020204e756d65726963537472696e6720202020202020203d0d0a436f756e7472794e616d652f5f756e7461672f783132312d6463632d636f64650d0a0d0a783431312e636e663a3532393a20557365725761726e696e673a20556e75736564205345545f5459504520666f7220436f756e7472794e616d652f783132312d6463632d636f64650d0a2020416464724e756d65726963537472696e67202020202055736572416464726573732f783132312f783132312d61646472657373203d0d0a436f756e7472794e616d652f783132312d6463632d636f646520506879736963616c44656c6976657279436f756e7472794e616d652f783132312d6463632d636f6465203d0d0a506f7374616c436f64652f6e756d657269632d636f64650d0a41534e2e3120746f2057697265736861726b20646973736563746f7220636f6d70696c65720d0a0d0a0d0a31332520736d616c6c65720d0a0d0a727473652e636e662831313229203a206572726f722043323232303a207761726e69";
//                    Vx11
//                    String m="800000400c5a27360000000000000002000607af000000010000000a00000000000000000000000000000000"
//                            + "0a03df9800000000000000000000000767706962302c3000"
//                            + ""+hexdata2;
                    
                    //                    Gnutella
//                    String m="000080daab8aca56fc8c15dffa343b33b8ad0711084e553c77d991dbf93e82d8522862450e40"+hexdata2;

                    //                    IMAP
//                    String m="6130303035204e4f4f500d0a"+hexdata2;
                    //                    Gryphon
//                    String m="030002000034010050000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"+hexdata2;
//                    String m="0000"+hexdata2;
                    
                    //SMB need handshake
                    
//                    String m="000000beff534d4272000000000801c80000000000000000000000000000091900000100009b00025043204e4554574f524b2050524f4752414d20312e3000024d4943524f534f4654204e4554574f524b5320312e303300024d4943524f534f4654204e4554574f524b5320332e3000024c414e4d414e312e3000024c4d312e32583030320002444f53204c414e4d414e322e3100024c414e4d414e322e31000253616d626100024e54204c414e4d414e20312e3000024e54204c4d20302e313200";
                    
                    
//                    SSL
//                    String m="1603010200010001fc03035ae206390be8019e4e6e04a76051fb2b194cb554ec1ef4cfa86cad43c698242f206e3b206fe0818e3f5ef0d17be4285bcb04b224eb7bc9edcb4a7a0cdf2bb406d900201a1a130113021303c02bc02fc02cc030cca9cca8c013c014009c009d002f0035010001935a5a0000000000180016000013636c69656e7473342e676f6f676c652e636f6d00170000ff01000100000a000a0008eaea001d00170018000b00020100002300000010000e000c02683208687474702f312e31000500050100000000000d0012001004030804040105030805050108060601001200000033002b0029eaea000100001d002070b4929d12f6148fd0c0fee53a1b505af364c375d96ae016c1c806ab0907c26b002d00020101002b000b0a1a1a0304030303020301001b0003020002caca000100001500c9000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                    
                    //                    EXEC
//                     String m="3338343000"+hexdata2;
                    //                    IMAP
//                    String m="6130303031204c4f47494e20226e65756c696e6765726e222022585858585858220d0a";
                    
//                    HART_IP
//                    String m="010000000002000d0100007530"+hexdata2;
//                    Gryphon
//                    String m="0300020000340100500000000000000000000000"
//                            + "000000000000000000000000000000000000000"
//                            + "00000000000000000000000000000000000000000"+hexdata2;
                    
                    //                    EXEC
//                    String m="737465666973383600";
//                    EPMD
//                    String m="013e78a8264d000005000500056e6f6465320000"+hexdata2;
//                    System.out.println("---sending from client----------->\n"+hexdata2);
                    
                    
                    
                    offset=0;
                    len=200;
                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
//                    len2=cops.createPacket(newdata, offset, len);
//                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] sendData=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
                    os.write(sendData);
                    sendcount+=1;
                    System.out.println("-----------> Send Data From Client<-------- "+sendcount);

                    Thread.sleep(delayTime);
                    i++;
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    
    private static class MultiReceiver extends Thread {
        Socket skt;
        InputStream is;
        public MultiReceiver(Socket skt,InputStream is) {
            this.skt=skt;
            this.is=is;
        }

        @Override
        public void run() {
            try {
                int startTime=(int)System.nanoTime();
                int countreceive=0;
                byte[] data=new byte[4096];
                int index=offset;
                
                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfMultiPkt){
                        skt.close();
                        System.out.println("-------------------------------------------------------------Socket closed-----> "+skt);
                        break;
                    }
                    
                    
                    Functions.ignoreByte(is, 348);
//                    Functions.ignoreByte(is, 4);
//                    int createLen=Utility.buildLen2(is);
//                    is.read(data,offset,createLen);
//                    String d=Utility.bytesToHex(data,offset,createLen);
//                    System.out.println("=============> "+createLen);
//                    System.out.println("------> \n"+d);

                    countreceive++;
//                    len=cops.decodePacket(data, offset, is);
//                    String msg=Utility.bytesToHex(data, offset, len);
//                    System.out.println("=============================> "+len);
//                    System.out.println(msg);
                    
                    
                    receivecount+=1;
                    System.out.println("-----> Received at Client -------->  "+receivecount);
                    //is.close();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
    
    
}
