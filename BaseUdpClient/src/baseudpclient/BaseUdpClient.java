package baseudpclient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class BaseUdpClient {

    static int numberOfPackets;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean check=true;
    
    static int protocolNumber;
    static int serverSocketPort;
    static int dataLen,offset=0;
    static String ip;
    static int socketInput;
    static int delay;
    static InetAddress sia,ia;
    
    static UFTPImplementation uftp=new UFTPImplementation();
    static CIGIImplementation cigi=new CIGIImplementation();
    static NFSImplementation nfs=new NFSImplementation();
    static NTPImplementation ntp=new NTPImplementation();
    static SNMPImplementation snmp=new SNMPImplementation(true);
    static CLDAPImplementation cldap=new CLDAPImplementation();
    static L2TPImplementation l2tp=new L2TPImplementation();
    static BFDImplementation bfd=new BFDImplementation();
    static WSPImplementation wsp=new WSPImplementation();
    static MOUNTImplementation mount=new MOUNTImplementation();
    static STATImplementation stat=new STATImplementation();
    static ICMPv6Implementation icmpv6=new ICMPv6Implementation();
    static LoWPANImplementation lowpan=new LoWPANImplementation();
    static DSPv2Implementation dspv2=new DSPv2Implementation();
    static TEPv1Implementation tepv1=new TEPv1Implementation();
    static DPPv2Implementation dppv2=new DPPv2Implementation();
    static CoAPImplementation coap=new CoAPImplementation();
    static TFTP2Implementation tftp2=new TFTP2Implementation();
    static IPv6Implementation ipv6=new IPv6Implementation();
    static LTPSegmentImplementation ltp=new LTPSegmentImplementation();
    static XTACACSImplementation xtacacs=new XTACACSImplementation(true);
    static ISAKMPImplementation isakmp=new ISAKMPImplementation(true);
    static BVLCImplementation bvlc=new BVLCImplementation();
    static MMSEImplementation mmse= new MMSEImplementation();
    static Slimp3Implementation slimp3=new Slimp3Implementation();
    static AutoRPImplementation autorp=new AutoRPImplementation();
    static MIOPImplementation miop=new MIOPImplementation();
    static EDonkeyImplementation edonkey=new EDonkeyImplementation();
    static UAUDPImplementation uaudp=new UAUDPImplementation(true);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int i=0;
        System.out.println("Udp Client Started...........");
                try{
            BufferedReader br = new BufferedReader(new FileReader("udpClientSetup.txt"));
            if(br== null){
                throw new FileNotFoundException();
            }
            ArrayList<String> list=new ArrayList<String>(); 
            String line;
            while((line=br.readLine())!=null){
                list.add(line);
            }

            
            protocolNumber=Integer.parseInt(list.get(0));
            ip=list.get(1);
            serverSocketPort=Integer.parseInt(list.get(2));
            dataLen=Integer.parseInt(list.get(3));
            delay=Integer.parseInt(list.get(4));
            socketInput=Integer.parseInt(list.get(5));
            
        }catch(Exception e){e.printStackTrace();}
       
        ia=InetAddress.getByName(ip);
        sia=InetAddress.getByName("10.0.0.2");
        check=true;
       if(socketInput==-1){
           check=false;
           numberOfPackets=99999;
           DatagramSocket ds=new DatagramSocket();

           MySender mySender=new MySender(ds);
           mySender.init();
           i++;
        
       }
       else{
           while(true){
               numberOfPackets=socketInput;
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

                    offset=0;
                    
                    byte[] newdata=new byte[offset+dataLen+100];
                    int len2=Utility.getRandomData(newdata, offset, dataLen);
                    String m1=Utility.bytesToHex(newdata,offset,dataLen);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    switch(protocolNumber){
                        case 1001:
                            len2=uftp.createPacket(newdata, offset, dataLen,ia,sia);
                            break;
                        case 1002:
                            len2=cigi.createPacket(newdata, offset, dataLen);
                            break;
                        case 1003:
                            len2=nfs.createPacket(newdata, offset, dataLen);
                            break;
                        case 1004:
                            len2=ntp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1005:
                            len2=snmp.createPacket(newdata, offset, dataLen);
                            break;
                            case 1006:
                            len2=cldap.createPacket(newdata, offset, dataLen);
                            break;
                        case 1007:
                            len2=l2tp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1008:
                            len2=bfd.createPacket(newdata, offset, dataLen);
                            break;
                        case 1009:
                            len2=wsp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1010:
                            len2=mount.createPacket(newdata, offset, dataLen);
                            break;
                        case 1011:
                            len2=stat.createPacket(newdata, offset, dataLen);
                            break;
                        case 1012:
                            len2=icmpv6.createPacket(newdata, offset, dataLen);
                            break;
                        case 1013:
                            len2=lowpan.createPacket(newdata, offset, dataLen);
                            break;
                        case 1014:
                            len2=dspv2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1015:
                            len2=tepv1.createPacket(newdata, offset, dataLen);
                            break;
                        case 1016:
                            len2=dppv2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1017:
                            len2=coap.createPacket(newdata, offset, dataLen);
                            break;
                        case 1018:
                            len2=tftp2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1019:
                            len2=ipv6.createPacket(newdata, offset, dataLen);
                            break;
                        case 1020:
                            len2=ltp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1021:
                            len2=xtacacs.createPacket(newdata, offset, dataLen, (Inet4Address) ia,49);
                            break;
                        case 1022:
                            len2=isakmp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1023:
                            len2=bvlc.createPacket(newdata, offset, dataLen);
                            break;
                        case 1024:
                            len2=mmse.createPacket(newdata, offset, dataLen);
                            break;
                        case 1025:
                            len2=slimp3.createPacket(newdata, offset, dataLen);
                            break;
                        case 1026:
                            len2=autorp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1027:
                            len2=miop.createPacket(newdata, offset, dataLen);
                            break;
                        case 1028:
                            len2=edonkey.createPacket(newdata, offset, dataLen);
                            break;
                        case 1029:
                            len2=uaudp.createPacket(newdata, offset, dataLen);
                            break;
                    }
                    
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                  
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName(ip);
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,serverSocketPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
                    totalSend+=1;
                    System.out.println("Total Packet Send-----------------> "+ totalSend);
                    Thread.sleep(delay);
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
                int countreceive=0,len2=0;

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
                    switch(protocolNumber){
                        case 1001:
                            len2=uftp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1002:
                            len2=cigi.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1003:
                            len2=nfs.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1004:
                            len2=ntp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1005:
                            len2=snmp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1007:
                            len2=l2tp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1008:
                            len2=bfd.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1009:
                            len2=wsp.decodePacket(b1, 0, dp1.getLength());
                            break;   
                        case 1010:
                            len2=mount.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1011:
                            len2=stat.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1012:
                            len2=icmpv6.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1013:
                            len2=lowpan.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1014:
                            len2=dspv2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1015:
                            len2=tepv1.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1016:
                            len2=dppv2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1017:
                            len2=coap.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1018:
                            len2=tftp2.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1019:
                            len2=ipv6.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1020:
                            len2=ltp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1021:
                            len2=xtacacs.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1022:
                            len2=isakmp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1023:
                            len2=bvlc.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1024:
                            len2=mmse.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1025:
                            len2=slimp3.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1026:
                            len2=autorp.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1027:
                            len2=miop.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1028:
                            len2=edonkey.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1029:
                            len2=uaudp.decodePacket(b1, 0, dp1.getLength());
                            break;
                    }
                    
                    
                    System.out.println("=======================> "+len2);
//                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);
                    
                    
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received at client:--->>>----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
