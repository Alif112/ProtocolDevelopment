/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ninep2000server;

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
public class NineP2000Server {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    static String msg;
    static String ip="65.99.254.85";
    static int serversocketport=564;
    public static int offset,len;
    static int sendCount=1;
    
    static NineP2000Implementation nineP2000=new NineP2000Implementation(false);
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
                byte [] message = new byte[2048];
//                br=new BufferedReader(isr);
                len=100;
                offset=0;
                
                while(true){
                    
//                    int firstByte = is.read();
//                    int secondByte = is.read();
//                    int thirdByte=is.read();
//                    int fourthByte=is.read();
//                    
//                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
//                    int length=firstByte;
//                    length=length|secondByte<<8;
//                    length=length|thirdByte<<16;
//                    length=length|fourthByte<<24;
//                    System.out.println("Receiving data of length: " + length);
//                    if(length-19==-1) continue;
//                    
//                    for(int x=0;x<15;x++)
//                        firstByte = is.read();
//                    
//                    is.read(data, offset, length-19);
//                    
//                    
//                    msg=Utility.bytesToHex(data, offset, length-19);
//                    System.out.println(msg);
                    len=nineP2000.decodePacket(data, offset, is);
                    if(len<0) {
                        System.out.println("==================================================> "+len);
                        break;
                    }
//                    msg=Utility.bytesToHex(data, offset, len);
                    System.out.println("========================================> "+len);
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
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
//                    String m="65ffff002000000600395032303030"+hexdata2;
//                    int checkLen=m.length()/2;
//                    checkLen+=4;
//                    Functions.putInt4(message, offset, checkLen);
//                    byte temp=message[offset];
//                    message[offset]=message[offset+3];
//                    message[offset+3]=temp;
//                    temp=message[offset+1];
//                    message[offset+1]=message[offset+2];
//                    message[offset+2]=temp;
//                    String m1=Utility.bytesToHex(message, offset, 4);
//                    m=m1+m;
//                    message=Utility.hexStringToByteArray(m);
//                    System.out.println("--------------Sending from Server");
//                    System.out.println(hexdata2);
//                    System.out.println(m);
                    
                    offset=0;
                    len=300;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=nineP2000.createPacket(newdata, offset, len);
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
