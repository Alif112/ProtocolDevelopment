package clientbase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ClientBase {

    static int numberOfPackets;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 10e100;
    static boolean check=true;
    
    static int protocolNumber;
    static int serverSocketPort;
    static int dataLen,offset=0;
    static String ip;
    static int socketType;
    static int delay;
    static InetAddress sia,ia;
    static int minimumPortRange=1, maximumPortRange=65000;
    static byte[] header;
    static String headerString;
    static int headerLen,multiUser,numberOfUser,protocolType;
    static String protocolName;
    static int sequenceNumber=0;
    
    
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
    static DropboxImplementation dropbox=new DropboxImplementation();
    static UDP100Implementation udp100=new UDP100Implementation();
    static RDTImplementation rdt=new RDTImplementation();
    static MACTelnetImplementation macTelnet=new MACTelnetImplementation(); 
    
    
    public static String[] protocolNameListUDP={"UDP 100","UFTP","CIGI","NFS","NTP","SNMP",
                                            "CLDAP","L2TP","BFD","WSP","MOUNT",
                                            "STAT","ICMPv6","6LoWPAN","DSPv2","TEPv1",
                                            "DPPv2", "CoAP",  "TFTP", "IPv6", "LTPSegment",
                                            "XTACACS", "ISAKMP","BVLC", "MMSE","Slimp3",
                                            "AutoRP", "MIOP","eDonkey", "UAUDP","Dropbox",
                                            "RDT", "MACTelnet"
                                              };
    
    static NineP2000Implementation nineP2000=new NineP2000Implementation(false); //201
    static COPSImplementation cops=new COPSImplementation(); //202
    static EXECImplementation exec=new EXECImplementation();//203
    static BasicTcpImplementation tcp=new BasicTcpImplementation();
    static IMAPImplementation imap=new IMAPImplementation();
    static SMTPImplementation smtp=new SMTPImplementation();
    static IPAImplementation ipa=new IPAImplementation();
    static CQLImplementation cql=new CQLImplementation();
    static BGPImplementation bgp=new BGPImplementation();
    
    public static String[] protocolNameListTCP={"TCP 100","NineP2000","COPS","EXEC","BasicTcp","IMAP",
                                            "SMTP","IPA","CQL","BGP"
                                              };
    
    
    /**
     * @param value
     * @param prodNames
     
     * @return 
     */
    
    public static String search(String value, ArrayList<String> prodNames) {
        value=value+"=";
        for (String name : prodNames) {
            if (name.contains(value)) {
                return name = name.replaceAll(".*=", "");
            }
        }
        return null;
    }
    
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException, IOException {
        
        int i=0;
        System.out.println("Udp Client Started...........");
        ArrayList<String> list=new ArrayList<String>(); 
        try{
            BufferedReader br = new BufferedReader(new FileReader("udpClientSetup.txt"));
            if(br== null){
                throw new FileNotFoundException();
            }
            
            String line;
            while((line=br.readLine())!=null){
                if(line.charAt(0)!='#')
                        list.add(line);
            }
            
            protocolType=Integer.parseInt(search("protocolType", list));
            protocolNumber=Integer.parseInt(search("protocolNumber", list));
            ip=search("fixedClientIP", list);
            serverSocketPort=Integer.parseInt(search("fixedClientPort", list));
            dataLen=Integer.parseInt(search("dataLen", list));
            delay=Integer.parseInt(search("dealy", list));
            socketType=Integer.parseInt(search("socketType", list));
            numberOfPackets=Integer.parseInt(search("numberOfPacketsPerSocket", list));
            header=Utility.hexStringToByteArray(search("rtpHeader", list));
            headerLen=header.length;
            multiUser=Integer.parseInt(search("multiUser", list));
            numberOfUser=Integer.parseInt(search("numberOfUser", list));
            if(protocolType==0) protocolName=protocolNameListUDP[protocolNumber-1000];
            else protocolName=protocolNameListTCP[protocolNumber-2000];
            
        }catch(Exception e){e.printStackTrace();}
       
        ia=InetAddress.getByName(ip);
        sia=InetAddress.getByName("10.0.0.2");
        check=true;
        DatagramSocket ds;
        Socket skt;
        MySenderTCP mySenderTcp;
        if(protocolType==0){
            switch (socketType) {
                case -1:
                    if(multiUser==1){
                        check=false;
                        numberOfPackets=99999;
                        ds=new DatagramSocket();
                        MySenderUDP mySender=new MySenderUDP(ds);
                        mySender.init();
                    }else{
                        check=false;
                        numberOfPackets=99999;
                        ds=new DatagramSocket();
                        MySenderUDP mySender=new MySenderUDP(ds);
                        mySender.init();
                    }
                    i++;
                    break;
                case 0:
                    minimumPortRange=Integer.parseInt(search("minimumPortRange=", list));
                    maximumPortRange=Integer.parseInt(search("maximumPortRange=", list));;
                    serverSocketPort=minimumPortRange-1;
                    while(true){
                        ds=new DatagramSocket();
                        MySenderUDP mySender = new MySenderUDP(ds);
                        mySender.init();
                    }    

                default:
                    while(true){

                        ds=new DatagramSocket();
                        MySenderUDP mySender = new MySenderUDP(ds);
                        mySender.init();
                        i++;
                    }  
                
                }
        }else if(protocolType==1){
            switch(socketType){
                case -1:
                    numberOfPackets=99999;
                    skt=new Socket(ip,serverSocketPort);
                    mySenderTcp=new MySenderTCP(skt);
                    mySenderTcp.init();
                    break;
                default:
                    while(true){
                        skt=new Socket(ip,serverSocketPort);
                        mySenderTcp=new MySenderTCP(skt);
                        mySenderTcp.init();
                    }
                    
            }
        }

        
       
    }

    private static class MySenderUDP{
        DatagramSocket ds;
        public MySenderUDP(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0,j=0;
                
                Thread myReceiver=new MyReceiverUDP(ds);
                myReceiver.start();
                int len2=0;
                while(i<numberOfPackets){

                    offset=0;
                    
                    byte[] newdata=new byte[offset+dataLen+1000];
                    int sendDataLen=Utility.getRandomData(newdata, offset, dataLen);
                    if(sequenceNumber==256) sequenceNumber=0;
                    newdata[sendDataLen]=(byte) sequenceNumber++;
//                    String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
//                    System.out.println("--------------> "+(sendDataLen+1));
//                    System.out.println(m1);
                    
                    

                    switch(protocolNumber){
                        case 1000:
                            len2=udp100.createPacket(newdata, offset, sendDataLen+1, header, headerLen);
                            break;
                        case 1001:
                            len2=uftp.createPacket(newdata, offset, sendDataLen+1,ia,sia);
                            break;
                        case 1002:
                            len2=cigi.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1003:
                            len2=nfs.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1004:
                            len2=ntp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1005:
                            len2=snmp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                            case 1006:
                            len2=cldap.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1007:
                            len2=l2tp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1008:
                            len2=bfd.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1009:
                            len2=wsp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1010:
                            len2=mount.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1011:
                            len2=stat.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1012:
                            len2=icmpv6.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1013:
                            len2=lowpan.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1014:
                            len2=dspv2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1015:
                            len2=tepv1.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1016:
                            len2=dppv2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1017:
                            len2=coap.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1018:
                            len2=tftp2.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1019:
                            len2=ipv6.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1020:
                            len2=ltp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1021:
                            len2=xtacacs.createPacket(newdata, offset, sendDataLen+1, (Inet4Address) ia,serverSocketPort);
                            break;
                        case 1022:
                            len2=isakmp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1023:
                            len2=bvlc.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1024:
                            len2=mmse.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1025:
                            len2=slimp3.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1026:
                            len2=autorp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1027:
                            len2=miop.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1028:
                            len2=edonkey.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1029:
                            len2=uaudp.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1030:
                            len2=dropbox.createPacket(newdata, offset, sendDataLen+1,serverSocketPort);
                            break;
                        case 1031:
                            len2=rdt.createPacket(newdata, offset, sendDataLen+1);
                            break;
                        case 1032:
                                len2=macTelnet.createPacket(newdata, offset, sendDataLen+1);
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
                    String message=new String(dp.getData(),0,dp.getLength());
                    
                    totalSend+=1;
                    System.out.println(protocolName+"  Total Packet Sent --------------> "+ totalSend);
                    Thread.sleep(delay);
                    i++;
                    
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static class MyReceiverUDP extends Thread {
        DatagramSocket ds;
        public MyReceiverUDP(DatagramSocket ds) {
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
                        System.out.println("--------------------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
//                    String received= new String(dp1.getData(),0,b1.length);
                    switch(protocolNumber){
                        case 1000:
                            len2=udp100.decodePacket(b1, 0, dp1.getLength(),headerLen);
                            break;
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
                        case 1030:
                            len2=dropbox.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1031:
                            len2=rdt.decodePacket(b1, 0, dp1.getLength());
                            break;
                        case 1032:
                            len2=macTelnet.decodePacket(b1, 0, dp1.getLength());
                            break;
                    }
                    
                    
//                    System.out.println("=======================> "+len2);
//                    String ack=Utility.bytesToHex(b1, 0, len2);                   
//                    System.out.println(ack);
//                    
                    
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received Len= "+len2+" at client -----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
    public static class MySenderTCP{
        Socket skt;
        
        public MySenderTCP(Socket skt) {
            this.skt=skt;
        }
        
        
            
            public void init() {
                try {
                    
                    /***
                     * These swtich are for handshaking checking
                     * **/
                    boolean isHandShake=false,handShakeReq=false;
                    switch (protocolNumber){

                        case 2005:
                            isHandShake=imap.imapHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case 2006:
                            isHandShake=smtp.smtpHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case 2007:
                            isHandShake=ipa.ipaHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                        case 2008:
                            isHandShake=cql.cqlHandshakeAtClient(skt);
                            handShakeReq=true;
                            break;
                    }

                    if(handShakeReq && !isHandShake) throw new Exception("HandShaking Failed!!!");

                    OutputStream os = skt.getOutputStream();
                    InputStream is = skt.getInputStream();

                    Thread myReceiver=new MyReceiverTCP(skt,is);
                    myReceiver.start();

                    int i=0,len2=0,sendDataLen;
                    int countsend=0;
                    while (i<numberOfPackets){
                        byte[] newdata=new byte[offset+dataLen+500];
                        sendDataLen=Utility.getRandomData(newdata, offset, dataLen);
                        if(sequenceNumber==256) sequenceNumber=0;
                        newdata[sendDataLen]=(byte) sequenceNumber++;
                        
                        String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
//                        System.out.println("---> sendting multi pkt --> \n"+m1);
                        
                        switch (protocolNumber){
                            case  2001:
                                len2=nineP2000.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  2002:
                                len2=cops.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  2003:
                                len2=exec.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  2004:
                                len2=tcp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  2005:
                                len2=imap.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  2006:
                                len2=smtp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                            case  2007:
                                len2=ipa.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  2008:
                                len2=cql.createPacketAtClient(newdata, offset, sendDataLen+1);
                                break;
                            case  2009:
                                len2=bgp.createPacket(newdata, offset, sendDataLen+1);
                                break;
                        }

                        String m=Utility.bytesToHex(newdata,offset,len2);
//                        System.out.println(m);
                        byte[] data=Utility.hexStringToByteArray(m);
                        os.write(data);

                        countsend++;
                        
                        totalSend+=1;
                        System.out.println(protocolName+ " Packet Send ------------------------> "+ totalSend);

                        Thread.sleep(delay);
                        i++;

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    public static class MyReceiverTCP extends Thread{
            Socket skt;
            InputStream is;
            MyReceiverTCP(Socket skt, InputStream is){
                this.skt=skt;
                this.is=is;
            }

            @Override
            public void run() {
                try {
                    int startTime=(int)System.nanoTime();
                    int countreceive=0;
                    byte[] data= new byte[2048];
                    while(true){
                        int currentTime=(int)System.nanoTime();
                        int checkTime=currentTime-startTime;
                        if((checkTime>=receiverTime || countreceive==numberOfPackets) && socketType!=-1){
                            skt.close();
                            System.out.println("-------------------Socket closed-----> "+skt);
                            break;
                        }

                        countreceive+=1;
                        int len2=0;
                        switch (protocolNumber){
                            case 2001:
                                len2=nineP2000.decodePacket(data,offset,is);
                                break;
                            case 2002:
                                len2=cops.decodePacket(data,offset,is);
                                break;
                            case 2003:
                                len2=exec.decodePacket(data,offset,is);
                                break;
                            case 2004:
                                len2=tcp.decodePacket(data,offset,is);
                                break;
                            case 2005:
                                len2=imap.decodePakcetAtClient(data,offset,is);
                                break;
                            case 2006:
                                len2=smtp.decodePacket(data,offset,is);
                                break;
                            case 2007:
                                len2=ipa.decodePacketAtClient(data,offset,is);
                                break;
                            case 2008:
                                len2=cql.decodePacketAtClient(data,offset,is);
                                break;
                            case 2009:
                                len2=bgp.decodePacket(data,offset,is);
                                break;

                        }
                        if(len2<0){
                            System.out.println("-------------------------------------------> "+len2);
                            break;
                        }
//                        System.out.println("==============================> "+len2);
//                        String ack=Utility.bytesToHex(data, offset, len2);
//                        System.out.println(ack);
                        
                        totalReceive+=1;
                        
                        System.out.println("Total Received Len= "+len2+" at client ---------------> "+ totalReceive);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    
}
