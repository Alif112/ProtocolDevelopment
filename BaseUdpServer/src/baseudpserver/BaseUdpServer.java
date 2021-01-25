package baseudpserver;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class BaseUdpServer {
    
    static int protocolNumber;
    static int serverSocketPort;
    static int dataLen=0,offset=0;
    public static String ip;
    public static InetAddress ia,sia;
    public static String serverVersion="1.1";
    static byte[] header;
    static int headerLen;
    static String protocolName;
    
    
    public static int countsend=0;
    public static int countreceive=0;
    
    
    static UFTPImplementation uftp=new UFTPImplementation();
    static CIGIImplementation cigi=new CIGIImplementation();
    static NFSImplementation nfs=new NFSImplementation();
    static NTPImplementation ntp=new NTPImplementation();
    static SNMPImplementation snmp=new SNMPImplementation(false);
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
    static XTACACSImplementation xtacacs=new XTACACSImplementation(false);
    static ISAKMPImplementation isakmp=new ISAKMPImplementation(false);
    static BVLCImplementation bvlc=new BVLCImplementation();
    static MMSEImplementation mmse= new MMSEImplementation();
    static Slimp3Implementation slimp3=new Slimp3Implementation();
    static AutoRPImplementation autorp=new AutoRPImplementation();
    static MIOPImplementation miop=new MIOPImplementation();
    static EDonkeyImplementation edonkey=new EDonkeyImplementation();
    static UAUDPImplementation uaudp=new UAUDPImplementation(false);
    static DropboxImplementation dropbox=new DropboxImplementation();
    static UDP100Implementation udp100=new UDP100Implementation();
    static RDTImplementation rdt=new RDTImplementation();
    
    
    public static String[] protocolNameList={"UDP 100","UFTP","CIGI","NFS","NTP","SNMP",
                                            "CLDAP","L2TP","BFD","WSP","MOUNT",
                                            "STAT","ICMPv6","6LoWPAN","DSPv2","TEPv1",
                                            "DPPv2", "CoAP",  "TFTP", "IPv6", "LTPSegment",
                                            "XTACACS", "ISAKMP","BVLC", "MMSE","Slimp3",
                                            "AutoRP", "MIOP","eDonkey", "UAUDP","Dropbox",
                                            "RDT"
                                              };
    
    public static String search(String value, ArrayList<String> prodNames) {
        value=value+"=";
        for (String name : prodNames) {
            if (name.contains(value)) {
                return name = name.replaceAll(".*=", "");
            }
        }
        return null;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        
        
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("udpServerSetup.txt"));
            if(br== null){
                throw new FileNotFoundException();
            }
            ArrayList<String> list=new ArrayList<String>(); 
            String line;
            while((line=br.readLine())!=null){
                list.add(line);
            }

            
            protocolNumber=Integer.parseInt(search("protocolNumber", list));
            ip=search("fixedClientIP", list);
            serverSocketPort=Integer.parseInt(search("fixedClientPort", list));
            dataLen=Integer.parseInt(search("dataLen", list));
            header=Utility.hexStringToByteArray(search("rtpHeader", list));
            headerLen=header.length;
            
            
            ia=InetAddress.getByName(ip);
            sia=InetAddress.getByName("10.0.0.2");
        }catch(Exception e){e.printStackTrace();}
        
        int position=protocolNumber-1000;
        protocolName=protocolNameList[position];
        System.out.println(protocolName+" server version "+serverVersion+" Started Successfully.... ");
        
        Thread t=new MyThread();
        t.start();

    }

    private static class MyThread extends Thread {
        
        public MyThread() {
        }

        @Override
        public void run() {
            try{
//                DatagramSocket ds=new DatagramSocket(ServerPort, InetAddress.getByName("localhost"));
                DatagramSocket ds=new DatagramSocket(serverSocketPort, InetAddress.getByName(ip));
                byte[] b1=new byte[2048];

                DatagramPacket dp1=new DatagramPacket(b1, b1.length);
                DatagramPacket dp2 = new DatagramPacket(b1, b1.length);
                int i=0;
                while(true){
                    ds.receive(dp1);
                    String message=new String(dp1.getData(),0,dp1.getLength());
//                    System.out.println(message);

                    int len1=0,len2=0;
                    
                    byte[] newdata=new byte[offset+dataLen+1000];
                    len2=Utility.getRandomData(newdata, offset, dataLen);
                    String m1=Utility.bytesToHex(newdata,offset,dataLen);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    switch(protocolNumber){
                        case 1000:
                            len1=udp100.decodePacket(b1, 0, dp1.getLength(),headerLen);
                            len2=udp100.createPacket(newdata, offset, dataLen,header,headerLen);
                            break;
                        case 1001:
                            len1=uftp.decodePacket(b1, 0, dp1.getLength());
                            len2=uftp.createPacket(newdata, offset, dataLen,ia,sia);
                            break;
                        case 1002:
                            len1=cigi.decodePacket(b1, 0, dp1.getLength());
                            len2=cigi.createPacket(newdata, offset, dataLen);
                            break;
                        case 1003:
                            len1=nfs.decodePacket(b1, 0, dp1.getLength());
                            len2=nfs.createPacket(newdata, offset, dataLen);
                            break;
                        case 1004:
                            len1=ntp.decodePacket(b1, 0, dp1.getLength());
                            len2=ntp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1005:
                            len1=snmp.decodePacket(b1, 0, dp1.getLength());
                            len2=snmp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1006:
                            len1=cldap.decodePacket(b1, 0, dp1.getLength());
                            len2=cldap.createPacket(newdata, offset, dataLen);
                        case 1007:
                            len1=l2tp.decodePacket(b1, 0, dp1.getLength());
                            len2=l2tp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1008:
                            len1=bfd.decodePacket(b1, 0, dp1.getLength());
                            len2=bfd.createPacket(newdata, offset, dataLen);
                            break;
                        case 1009:
                            len1=wsp.decodePacket(b1, 0, dp1.getLength());
                            len2=wsp.createPacket(newdata, offset, dataLen);
                            break;   
                        case 1010:
                            len1=mount.decodePacket(b1, 0, dp1.getLength());
                            len2=mount.createPacket(newdata, offset, dataLen);
                            break;
                        case 1011:
                            len1=stat.decodePacket(b1, 0, dp1.getLength());
                            len2=stat.createPacket(newdata, offset, dataLen);
                            break;
                        case 1012:
                            len1=icmpv6.decodePacket(b1, 0, dp1.getLength());
                            len2=icmpv6.createPacket(newdata, offset, dataLen);
                            break;
                        case 1013:
                            len1=lowpan.decodePacket(b1, 0, dp1.getLength());
                            len2=lowpan.createPacket(newdata, offset, dataLen);
                            break;
                        case 1014:
                            len1=dspv2.decodePacket(b1, 0, dp1.getLength());
                            len2=dspv2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1015:
                            len1=tepv1.decodePacket(b1, 0, dp1.getLength());
                            len2=tepv1.createPacket(newdata, offset, dataLen);
                            break;
                        case 1016:
                            len1=dppv2.decodePacket(b1, 0, dp1.getLength());
                            len2=dppv2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1017:
                            len1=coap.decodePacket(b1, 0, dp1.getLength());
                            len2=coap.createPacket(newdata, offset, dataLen);
                            break;
                        case 1018:
                            len1=tftp2.decodePacket(b1, 0, dp1.getLength());
                            len2=tftp2.createPacket(newdata, offset, dataLen);
                            break;
                        case 1019:
                            len1=ipv6.decodePacket(b1, 0, dp1.getLength());
                            len2=ipv6.createPacket(newdata, offset, dataLen);
                            break;
                        case 1020:
                            len1=ltp.decodePacket(b1, 0, dp1.getLength());
                            len2=ltp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1021:
                            len1=xtacacs.decodePacket(b1, 0, dp1.getLength());
                            len2=xtacacs.createPacket(newdata, offset, dataLen, (Inet4Address) ia,serverSocketPort);
                            break;
                        case 1022:
                            len1=isakmp.decodePacket(b1, 0, dp1.getLength());
                            len2=isakmp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1023:
                            len1=bvlc.decodePacket(b1, 0, dp1.getLength());
                            len2=bvlc.createPacket(newdata, offset, dataLen);
                            break;
                        case 1024:
                            len1=mmse.decodePacket(b1, 0, dp1.getLength());
                            len2=mmse.createPacket(newdata, offset, dataLen);
                            break;
                        case 1025:
                            len1=slimp3.decodePacket(b1, 0, dp1.getLength());
                            len2=slimp3.createPacket(newdata, offset, dataLen);
                            break;
                        case 1026:
                            len1=autorp.decodePacket(b1, 0, dp1.getLength());
                            len2=autorp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1027:
                            len1=miop.decodePacket(b1, 0, dp1.getLength());
                            len2=miop.createPacket(newdata, offset, dataLen);
                            break;
                        case 1028:
                            len1=edonkey.decodePacket(b1, 0, dp1.getLength());
                            len2=edonkey.createPacket(newdata, offset, dataLen);
                            break;
                        case 1029:
                            len1=uaudp.decodePacket(b1, 0, dp1.getLength());
                            len2=uaudp.createPacket(newdata, offset, dataLen);
                            break;
                        case 1030:
                            len1=dropbox.decodePacket(b1, 0, dp1.getLength());
                            len2=dropbox.createPacket(newdata, offset, dataLen,serverSocketPort);
                            break;
                        case 1031:
                            len1=rdt.decodePacket(b1, 0, dp1.getLength());
                            len2=rdt.createPacket(newdata, offset, dataLen);
                            break;
                    }
                    
                    /**show decoded message**/
                    System.out.println(protocolName+" Received ---------------> "+len1);
                    String ack=Utility.bytesToHex(b1, 0, len1);                   
//                    System.out.println(ack);

                  
                    /**show new encoded message**/
                    System.out.println("Sending from server --------->   "+ dataLen);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println(m);
                    
                    byte[] b2=Utility.hexStringToByteArray(m);
                    

                    dp2.setData(b2);
                    dp2.setAddress(dp1.getAddress());
                    dp2.setPort(dp1.getPort());
                    ds.send(dp2);

//                    System.out.println("------sending from server to ip:port: "+dp.getAddress()+":"+ServerToClientPort);
                    countsend+=1;
                    System.out.println(ds+"Total Count ------------------->  "+countsend);
                }



            }catch(Exception e){

                e.printStackTrace();
            }
        }
        
        
    }
    
}