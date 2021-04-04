/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import tcpserver.TCPServer;

import utils.SetupLogger;

/**
 *
 * @author User
 */


public class ServerSetupTCP {
    public static Logger logger = Logger.getLogger(ServerSetupTCP.class);

    
    static Socket sendSocket,receiveConfigReqSocket;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static InetAddress ia;
    static ServerSocket serverReceivedSocket,serverSenderSocket;
    
    
    public static int offset=0;
    public static int len2=0;

    
    static String msg;
    
    
    
    static int sendCount=1,sequenceNumber=0;
    

    

    private static void sendConfig(int checkPort, String ip) throws IOException {
        
        receiveConfigReqSocket=serverReceivedSocket.accept();
        
        InputStream is=receiveConfigReqSocket.getInputStream();
        OutputStream os=receiveConfigReqSocket.getOutputStream();
        
        is.read();
        
        
        
    }

    private static void startServer() throws FileNotFoundException {
        SetupLogger.startLogging();
        logger.debug("logger done ");
        boolean loaded=Configuration.readConfigFile();
        if(!loaded) logger.debug("------------Config Load Failed!!!!");
        
        logger.debug("------------Config Loaded----------");
        
        if(null != Configuration.protocolType)switch (Configuration.protocolType) {
            case "tcp":
                new TCPServer(Configuration.addr,Configuration.serverSocketPort).start();
                
                break;
            case "udp":
                
                break;
            case "test":
                
                break;
            default:
                break;
        }
        
        
        
        System.out.println(Configuration.protocolName+" server version "+Constants.SERVER_VERSION+" started successfully!!!");
    }

    

    public ServerSetupTCP() {
    }

    
    public String buildConfig(){
       String line=null;
        try {
            BufferedReader br = new BufferedReader(new FileReader("config.txt"));
            if(br==null) throw new FileNotFoundException();
            ArrayList<String> list=new ArrayList<String>(); 
            
            int listsize=0;
            while((line=br.readLine())!=null){
                if(line.charAt(0)!='#'){
                    list.add(line);
                    listsize++;
                }
            }
            
            line=null;
            for(int i=0;i<listsize;i++){
                line="%"+line+list.get(i).toString();
            }
            line+="\r\n";
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            startServer();
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerSetupTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
//        try{
//            InetAddress addr = InetAddress.getByName(Configuration.ip);
//            if(Configuration.enableFixedPort==0){
//                for(int checkPort=Configuration.minimumPortRange;checkPort<=Configuration.maximumPortRange;checkPort++){
//                    try {
//                        ss=new ServerSocket(checkPort,50, addr);
//                        
////                        sendConfig(checkPort,Configuration.ip);
//                        
//                        
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        continue;
//                    }
//                    
//                    
//                    
//                }
//                
//            }else{
//                serverSenderSocket= new ServerSocket(Configuration.serverSocketPort,50, addr);
//                while(true){
//                    sendSocket=serverSenderSocket.accept();
//                    Thread myThread=new MyNewThread(sendSocket);
//                    myThread.start();
//                }
//            }
//            
//            
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        
//    }
//
//    static class MyNewThread extends Thread {
//        Socket s;
//        public MyNewThread(Socket s) {
//            this.s=s;
//        }
//
//        @Override
//        public void run() {
//            try{
//                s.setTcpNoDelay(true);
////                isr=new InputStreamReader(s.getInputStream());
//                boolean isHandShake=false,handShakeReq=false;
//                switch (Configuration.protocolNumber){
//                    case 2005:
//                        isHandShake=Constants.imap.imapHandshakeAtServer(s);
//                        handShakeReq=true;
//                        break;
//                    case 2006:
//                        isHandShake=Constants.smtp.smtpHandshakeAtServer(s);
//                        handShakeReq=true;
//                        break;
//                    case 2007:
//                        isHandShake=Constants.ipa.ipaHandshakeAtServer(s);
//                        handShakeReq=true;
//                        break;
//                    case 2008:
//                        isHandShake=Constants.cql.cqlHandshakeAtServer(s);
//                        handShakeReq=true;
//                        break;
//                }
//
//                if(handShakeReq && !isHandShake) throw new Exception("HandShaking Failed!!!");
//                
//                InputStream is = s.getInputStream();
//                OutputStream os = s.getOutputStream();
//                byte [] data = new byte[offset+Configuration.dataLen+2048];
////                br=new BufferedReader(isr);
//                
//                while(true){
//                    switch(Configuration.protocolNumber){
//                    case Constants.NineP2000:
//                        len2=Constants.nineP2000.decodePacket(data, offset, is);
//                        break;
//                    case Constants.COPS:
//                        len2=Constants.cops.decodePacket(data, offset, is);
//                        break;
//                    case Constants.EXEC:
//                        len2=Constants.exec.decodePacket(data, offset, is);
//                        break;
//                    case Constants.BasicTcp:
//                        len2=Constants.tcp.decodePacket(data, offset, is);
//                        break;
//                    case Constants.IMAP:
//                        len2=Constants.imap.decodePacketAtServer(data, offset, is);
//                        break;
//                    case Constants.SMTP:
//                        len2=Constants.smtp.decodePacket(data, offset, is);
//                        break;
//                    case Constants.IPA:
//                        len2=Constants.ipa.decodePacketAtServer(data, offset, is);
//                        break;
//                    case Constants.CQL:
//                        len2=Constants.cql.decodePacketAtServer(data, offset, is);
//                        break;
//                    case Constants.BGP:
//                        len2=Constants.bgp.decodePacket(data, offset, is);
//                        break;
//                        
//                }
//                    
//                    if(len2<0){
//                        System.out.println("---------------------------------------> "+len2);
//                        break;
//                    }
//                    
//                    String ack=Utility.bytesToHex(data, offset, len2);
////                    System.out.println(ack);
//                    System.out.println("Received at Server=================> "+len2);
//
//                    byte[] newdata=new byte[offset+Configuration.dataLen+500];
//                    int sendDataLen=Utility.getRandomData(newdata, offset, Configuration.dataLen);
//                    if(sequenceNumber==256) sequenceNumber=0;
//                    newdata[sendDataLen]=(byte) sequenceNumber++;
//                    
//                    String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
////                    System.out.println("--------------> ");
////                    System.out.println(m1);
//                    
//                    
//                    switch(Configuration.protocolNumber){
//                    case Constants.NineP2000:
//                        len2=Constants.nineP2000.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.COPS:
//                        len2=Constants.cops.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.EXEC:
//                        len2=Constants.exec.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.BasicTcp:
//                        len2=Constants.tcp.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.IMAP:
//                        len2=Constants.imap.createPacketAtServer(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.SMTP:
//                        len2=Constants.smtp.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.IPA:
//                        len2=Constants.ipa.createPacketAtServer(newdata, offset, sendDataLen+1);
//                        break;
//                    case Constants.CQL:
//                        len2=Constants.cql.createPacketAtServer(newdata, offset, sendDataLen+1);
//                        break;    
//                    case Constants.BGP:
//                        len2=Constants.bgp.createPacket(newdata, offset, sendDataLen+1);
//                        break;
//                        
//                }
//                    
//                    
//                    String m=Utility.bytesToHex(newdata,offset,len2);
////                    System.out.println("================================>          "+ len2);
////                    System.out.println(m);
//                    byte[] senddata=Utility.hexStringToByteArray(m); 
//                    
////                    os.write(message);
//                    os.write(senddata);
//                    
//                    System.out.println("--> "+ Configuration.dataLen+" Bytes of "+Configuration.protocolName+" data sending -->number of packet is "+ sendCount++);
//                }
//                
//            
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//        
    }
    
}
