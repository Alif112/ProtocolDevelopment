/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byteservertcp;

import com.sun.corba.se.impl.util.Utility;
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
public class ByteServerTCP {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    static String message;
    static String ip="72.249.184.22";
    static int serversocketport=12344;
    
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

    private static class MyNewThread extends Thread {
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
                while(true){
                    
                    byte firstByte = (byte) is.read();
                    byte secondByte = (byte) is.read();
                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = firstByte << 8 | secondByte;
                    System.out.println("Receiving data of length: " + length);
                    is.read(data, 0, length);
                    
                    if(length==-1) break;
                    
                    message = new String(data, 0, length);
                    
                    System.out.println(message);
                    
                    message = message.toUpperCase();
                    System.out.println(message);

                    
                    data[0] = firstByte;
                    data[1] = secondByte;
                    System.arraycopy(message.getBytes(), 0, data, 2, message.length());
                    os.write(data, 0, length + 2);
                    
                    System.out.println("--------------------->>Finished Here");
                }
                
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
