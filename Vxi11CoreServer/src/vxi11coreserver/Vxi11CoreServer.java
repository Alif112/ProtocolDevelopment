/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vxi11coreserver;

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
public class Vxi11CoreServer {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    static String msg;
    static String ip="65.99.254.85";
    static int serversocketport=1024;
    public static int offset,len;
    static int sendCount=1;
    
    static Vxi11CoreImplementation vx11=new Vxi11CoreImplementation();
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
                InputStream is = s.getInputStream();
                OutputStream os = s.getOutputStream();
                byte [] data = new byte[2048];

//                br=new BufferedReader(isr);
                len=100;
                offset=0;
                int index=offset;
                while(true){
//                    Functions.ignoreByte(is, 80);
//                    int createLen=Utility.buildLen4(is);
//                    if(createLen<-1) {
//                        System.out.println("------------------> "+createLen);
//                        break;
//                    }
//                    is.read(data, offset, createLen);
//                    String d=Utility.bytesToHex(data, offset, createLen);
//                    System.out.println(d);
                        
                    
                    
                    len=vx11.decodePacket(data, offset, is);
                    if(len<0){
                        System.out.println("================================> "+len);
                        break;
                    } 
                    System.out.println("================================> "+len);
                    String d=Utility.bytesToHex(data, offset, len);
                    System.out.println(d);
                    
                    offset=0;
                    len=0;
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    //                    Vx11
//                    String m="800000280f5a27360000000100000000000000000000000000000000"
//                            + "0000000001cc83780000032d00004000"
//                            + ""+hexdata2;
                    

                    
                    offset=0;
                    len=300;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
                    System.out.println("--------------> ");
                    System.out.println(m1);
//                    
                    len2=vx11.createPacketAtServer(newdata, offset, len);
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
