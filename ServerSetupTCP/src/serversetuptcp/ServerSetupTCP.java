/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author User
 */


public class ServerSetupTCP {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static InetAddress ia;
    
    static String ip;
    static int serverSocketPort;
    static int protocolNumber;
    public static int offset=0,dataLen;
    public static int len2=0;
    static String protocolName;
    static String version="1.2";
    
    static String msg;
    
    
    
    static int sendCount=1,sequenceNumber=0;
    
    static NineP2000Implementation nineP2000=new NineP2000Implementation(false); //201
    static COPSImplementation cops=new COPSImplementation(); //202
    static EXECImplementation exec=new EXECImplementation();//203
    static BasicTcpImplementation tcp=new BasicTcpImplementation();
    static IMAPImplementation imap=new IMAPImplementation();
    static SMTPImplementation smtp=new SMTPImplementation();
    static IPAImplementation ipa=new IPAImplementation();
    static CQLImplementation cql=new CQLImplementation();
    static BGPImplementation bgp=new BGPImplementation();
    public static String[] protocolNameList={"UDP 100","NineP2000","COPS","EXEC","BasicTcp","IMAP",
                                            "SMTP","IPA","CQL","BGP"
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
    public static void main(String[] args) {
        // TODO code application logic here
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("serverTcpConfig.txt"));
            if(br== null){
                throw new FileNotFoundException();
            }
            ArrayList<String> list=new ArrayList<String>(); 
            String line;
            while((line=br.readLine())!=null){
                list.add(line);
            }

            
            protocolNumber=Integer.parseInt(search("protocolNumber", list));
            ip=search("fixedServerIP", list);
            serverSocketPort=Integer.parseInt(search("fixedServerPort", list));
            dataLen=Integer.parseInt(search("dataLen", list));
            protocolName=protocolNameList[protocolNumber-2000];
            System.out.println(protocolName+" server version "+version+" started successfully!!!");
        }catch(Exception e){e.printStackTrace();}
        
        
        try{
            InetAddress addr = InetAddress.getByName(ip);

            ss= new ServerSocket(serverSocketPort,50, addr);
            while(true){
                s=ss.accept();
                Thread myThread=new MyNewThread(s);
                myThread.start();
                
            }
          
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    static class MyNewThread extends Thread {
        Socket s;
        public MyNewThread(Socket s) {
            this.s=s;
        }

        @Override
        public void run() {
            try{
                s.setTcpNoDelay(true);
//                isr=new InputStreamReader(s.getInputStream());
                boolean isHandShake=false,handShakeReq=false;
                switch (protocolNumber){
                    case 2005:
                        isHandShake=imap.imapHandshakeAtServer(s);
                        handShakeReq=true;
                        break;
                    case 2006:
                        isHandShake=smtp.smtpHandshakeAtServer(s);
                        handShakeReq=true;
                        break;
                    case 2007:
                        isHandShake=ipa.ipaHandshakeAtServer(s);
                        handShakeReq=true;
                        break;
                    case 2008:
                        isHandShake=cql.cqlHandshakeAtServer(s);
                        handShakeReq=true;
                        break;
                }

                if(handShakeReq && !isHandShake) throw new Exception("HandShaking Failed!!!");
                
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                byte [] data = new byte[offset+dataLen+2048];
//                br=new BufferedReader(isr);
                
                while(true){
                    switch(protocolNumber){
                    case 2001:
                        len2=nineP2000.decodePacket(data, offset, is);
                        break;
                    case 2002:
                        len2=cops.decodePacket(data, offset, is);
                        break;
                    case 2003:
                        len2=exec.decodePacket(data, offset, is);
                        break;
                    case 2004:
                        len2=tcp.decodePacket(data, offset, is);
                        break;
                    case 2005:
                        len2=imap.decodePacketAtServer(data, offset, is);
                        break;
                    case 2006:
                        len2=smtp.decodePacket(data, offset, is);
                        break;
                    case 2007:
                        len2=ipa.decodePacketAtServer(data, offset, is);
                        break;
                    case 2008:
                        len2=cql.decodePacketAtServer(data, offset, is);
                        break;
                    case 2009:
                        len2=bgp.decodePacket(data, offset, is);
                        break;
                        
                            
                }
                    
                    if(len2<0){
                        System.out.println("---------------------------------------> "+len2);
                        break;
                    }
//                    String msg=Utility.bytesToHex(data, offset, len2);
//                    System.out.println(msg);
                    System.out.println("Received at Server=================> "+len2);

                    byte[] newdata=new byte[offset+dataLen+500];
                    int sendDataLen=Utility.getRandomData(newdata, offset, dataLen);
                    if(sequenceNumber==256) sequenceNumber=0;
                    newdata[sendDataLen]=(byte) sequenceNumber++;
                    
                    String m1=Utility.bytesToHex(newdata,offset,sendDataLen+1);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    
                    switch(protocolNumber){
                    case 2001:
                        len2=nineP2000.createPacket(newdata, offset, sendDataLen+1);
                        break;
                    case 2002:
                        len2=cops.createPacket(newdata, offset, sendDataLen+1);
                        break;
                    case 2003:
                        len2=exec.createPacket(newdata, offset, sendDataLen+1);
                        break;
                    case 2004:
                        len2=tcp.createPacket(newdata, offset, sendDataLen+1);
                        break;
                    case 2005:
                        len2=imap.createPacketAtServer(newdata, offset, sendDataLen+1);
                        break;
                    case 2006:
                        len2=smtp.createPacket(newdata, offset, sendDataLen+1);
                        break;
                    case 2007:
                        len2=ipa.createPacketAtServer(newdata, offset, sendDataLen+1);
                        break;
                    case 2008:
                        len2=cql.createPacketAtServer(newdata, offset, sendDataLen+1);
                        break;    
                    case 2009:
                        len2=bgp.createPacket(newdata, offset, sendDataLen+1);
                        break;
                        
                }
                    
                    
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] senddata=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
                    os.write(senddata);
                    
                    System.out.println(dataLen+" Bytes of "+protocolName+" data sending -->number of packet is "+ sendCount++);
                }
                
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
