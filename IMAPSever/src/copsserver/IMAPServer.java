/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copsserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author User
 */
public class IMAPServer {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    static String msg;
    static String ip="65.99.254.85";
    static int serversocketport=143;
    public static int offset,len;
    static int sendCount=1;
    
    static IMAPImplementation imap=new IMAPImplementation();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Byte Server started here");
        
        
        try{
            InetAddress addr = InetAddress.getByName(ip);

            ss= new ServerSocket(serversocketport,50, addr);
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
                boolean isHandShake=imap.imapHandshakeAtServer(s);
                if(!isHandShake) throw new Exception("HandShaking Failed!!!");
                
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                byte [] data = new byte[2048];

//                br=new BufferedReader(isr);
                len=100;
                offset=0;
                int index=offset;
                while(true){
                    len=imap.decodePacketAtServer(data, offset, is);
                    System.out.println("==========================================> "+len);
                    if(len==-1) break;
                    String d=Utility.bytesToHex(data, offset, len);
//                    System.out.println(d);
                    
                    offset=0;
                    len=0;
                    len=1000;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
//                    String m="10070000"
//                            + "00000154"
//                            + "00080a010004000a"
//                            + "0144"
//                            + "10010000000100000000"+hexdata2;
                    
//                    System.out.println("-------------------->");
//                    System.out.println(hexdata2);
                    
                    offset=0;
                    len=500;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
                    len2=imap.createPacketAtServer(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] senddata=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
                    os.write(senddata);
                    
                    System.out.println("-> "+len+"----------sending number of data "+ sendCount++);
                }
                
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
