/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import static serversetuptcp.Constants.logger;
import tcpprotocols.BGPImplementation;
import tcpprotocols.BasicTcpImplementation;
import tcpprotocols.COPSImplementation;
import tcpprotocols.CQLImplementation;
import tcpprotocols.EXECImplementation;
import tcpprotocols.IMAPImplementation;
import tcpprotocols.IPAImplementation;
import tcpprotocols.NineP2000Implementation;
import tcpprotocols.SMTPImplementation;


/**
 *
 * @author User
 */
public class Constants {
    public static final String SERVER_VERSION="1.2";
    public static final String fileName="serverTcpConfig.txt";
    
    
    public static final String inputConfigFromServer="inputConfigFromServer";
    public static final String protocolType="protocolType";
    public static final String protocolNumber="protocolNumber";
    public static final String fixedConfigIP="fixedConfigIP";
    public static final String fixedConfigPort="fixedConfigPort";
    public static final String dataLen="dataLen";
    public static final String rtpHeader="rtpHeader";
    public static final String serverIP="serverIP";
    public static final String enableFixedPort="enableFixedPort";
    public static final String fixedPortForServer="fixedPortForServer";
    public static final String enableSocialBypass="enableSocialBypass";
    public static final String socialPacketCount="socialPakcetCount";
    public static final String minimumPortRange="minimumPortRange";
    public static final String maximumPortRange="maximumPortRange";
    
    
    
    public static String[] protocolNameListTCP={"UDP 100","NineP2000","COPS","EXEC","BasicTcp","IMAP",
                                            "SMTP","IPA","CQL","BGP"
                                              };
    
    public static String[] protocolNameListUDP={"UDP 100",
                                            "UFTP","CIGI","NFS","NTP","SNMP",
                                            "CLDAP","L2TP","BFD","WSP","MOUNT",
                                            "STAT","ICMPv6","6LoWPAN","DSPv2","TEPv1",
                                            "DPPv2", "CoAP",  "TFTP", "IPv6", "LTPSegment",
                                            "XTACACS", "ISAKMP","BVLC", "MMSE","Slimp3",
                                            "AutoRP", "MIOP","eDonkey", "UAUDP","Dropbox",
                                            "RDT", "MACTelnet","DCP-PFT"
                                              };
    
    /** All UDP protocols***/
    public static final int UDP_100=1000;
    public static final int UFTP=1001;
    public static final int CIGI=1002;
    public static final int NFS=1003;
    public static final int NTP=1004;
    public static final int SNMP=1005;
    public static final int CLDAP=1006;
    public static final int L2TP=1007;
    public static final int BFD=1008;
    public static final int WSP=1009;
    public static final int MOUNT=1010;
    public static final int STAT=1011;
    public static final int ICMPv6=1012;
    public static final int LoWPAN=1013;
    public static final int DSPv2=1014;
    public static final int TEPv1=1015;
    public static final int DPPv2=1016;
    public static final int CoAP=1017;
    public static final int TFTP=1018;
    public static final int IPv6=1019;
    public static final int LTPSegment=1020;
    public static final int XTACACS=1021;
    public static final int ISAKMP=1022;
    public static final int BVLC=1023;
    public static final int MMSE=1024;
    public static final int Slimp3=1025;
    public static final int AutoRP=1026;
    public static final int MIOP=1027;
    public static final int eDonkey=1028;
    public static final int UAUDP=1029;
    public static final int Dropbox=1030;
    public static final int RDT=1031;
    public static final int MACTelnet=1032;
    public static final int DCP_PFT=1033;
    
    
    public static final String UDP="udp";
    public static final String TCP="tcp";
    public static final String TEST="test";
    
    
    /** All TCP protocols***/
    public static final int TCP_100=2000;
    public static final int NineP2000=2001;
    public static final int COPS=2002;
    public static final int EXEC=2003;
    public static final int BasicTcp=2004;
    public static final int IMAP=2005;
    public static final int SMTP=2006;
    public static final int IPA=2007;
    public static final int CQL=2008;
    public static final int BGP=2009;
    
    
    public static NineP2000Implementation nineP2000=new NineP2000Implementation(false); //201
    public static COPSImplementation cops=new COPSImplementation(); //202
    public static EXECImplementation exec=new EXECImplementation();//203
    public static BasicTcpImplementation tcp=new BasicTcpImplementation();
    public static IMAPImplementation imap=new IMAPImplementation();
    public static SMTPImplementation smtp=new SMTPImplementation();
    public static IPAImplementation ipa=new IPAImplementation();
    public static CQLImplementation cql=new CQLImplementation();
    public static BGPImplementation bgp=new BGPImplementation();

    static void logger(String config_not_loaded_properly) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public static final int NORMAL_SERVER=1;
    
    
}
