/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircserver;

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
public class IRCServer {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    static String msg;
    static String ip="72.249.184.22";
    static int serversocketport=6667;
    public static int offset,len;
    static int sendCount=1;
    
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
                
                while(true){
                    
                    int firstByte = is.read();
                    int secondByte = is.read();
                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = firstByte << 8 | secondByte;
//                    System.out.println("Receiving data of length: " + length);
                    is.read(data, 0, length);
                    
                    if(length==-1) break;
                    msg=Utility.bytesToHex(data, 0, length);
//                    System.out.println(msg);
                    
//                    msg = new String(data, 0, length);
//                    
//                    
//                    msg = msg.toUpperCase();
//                    System.out.println(msg);
                            
                    
//                    data[0] = (byte)firstByte;
//                    data[1] = (byte)secondByte;
//                    System.arraycopy(message.getBytes(), 0, data, 2, message.length());
//                    os.write(data, 0, length + 2);
                    
                    offset=0;
                    len=0;
                    len=1024;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    
                    String m="49534f4e205468756e666973636820536d696c657920536d696c6579470a"+hexdata2;
                    byte[] message = Utility.hexStringToByteArray(m);
                    String show = Utility.bytesToHex(message);
//                    System.out.println(show);
                    
                    
//                    int size=Utility.getRandomData(message,offset,len);
                    len=message.length;
                    
                    byte [] data1 = new byte[len + 2];
                    data1[0] = (byte)((len >> 8) & 0xff);
                    data1[1] = (byte)((len & 0xff));

                    System.arraycopy(message, 0, data1, 2, len);
                    
                    os.write(data1);

                    
                    System.out.println("----> "+len+"----------sending number of data "+ sendCount++);
                }
                
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
