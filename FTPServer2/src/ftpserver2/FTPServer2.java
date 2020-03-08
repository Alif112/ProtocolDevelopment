/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpserver2;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author User
 */
public class FTPServer2 {
    static Socket s;
    static ServerSocket ss;
    static InputStreamReader isr;
    static OutputStreamWriter osr;
    static BufferedReader br;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("FTP Server started here");
        
        
        try{
            ss=new ServerSocket(20);
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
                    
                        int firstByte = (int) is.read();
                        int secondByte = (int) is.read();
                        System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                        int length = firstByte << 8 | secondByte;
                        System.out.println("Receiving data of length: " + length);
                        if(length<0) break;
                        is.read(data, 0, length);

                        String message = new String(data, 0, length);

                        System.out.println(message);
                        message=message.toUpperCase();
                        message+="\r\n";
                        
                        int len=message.length();
                        byte [] data1 = new byte[len + 2];
                        data1[0] = (byte)((len >> 8) & 0xff);
                        data1[1] = (byte)((len & 0xff));
                        System.arraycopy(message.getBytes(), 0, data1, 2, message.length());
                        System.out.println(data1[0]+" This message has been sent from server to client-->"+data1[1]);
                        System.out.println(message);

                        os.write(data1, 0, len);
                        os.flush();

                        System.out.println("---------size is "+message.length()+"------------>>Finished Here");

                }
                
            
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        
    }
    
}
