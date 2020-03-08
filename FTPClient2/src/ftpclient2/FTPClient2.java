/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ftpclient2;

import java.io.InputStream;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;



/**
 *
 * @author User
 */
public class FTPClient2 {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            
            Socket socket=new Socket("191.96.12.12",20);
//            Socket socket=new Socket("localhost",2222);
            Thread mythread=new MyThread(socket);
            mythread.start();


            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        
        
        
    }
    

    private static class MyReceiver extends Thread {
        InputStream is;
        public MyReceiver(InputStream is) {
            this.is=is;
        }

        @Override
        public void run() {
            byte[] data=new byte[2048];
            try {
                while (true) {
                    int firstByte = (int) is.read();
                    int secondByte = (int) is.read();
                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = firstByte << 8 | secondByte;
                    System.out.println("Receiving data of length: " + length);
                    if(length<0) break;
                    is.read(data, 0, length);

                    
                    String message = new String(data, 0, length);
                    System.out.println(message);
                    

                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
        }
        
    }

    private static class MyThread extends Thread {
        Socket socket;
        OutputStream os;
        InputStream is;
        public MyThread(Socket socket) {
            this.socket=socket;
        }

        @Override
        public void run() {
            try {
                os=socket.getOutputStream();
                is=socket.getInputStream();
                Thread myreceiver=new MyReceiver(is);
                myreceiver.start();
                Scanner sc=new Scanner(System.in);
                while(true){
                    System.out.println("Input Something to send ......");
                    String message=sc.nextLine();
                    
//                    message+="\r\n";
                    int len=message.length();
                    byte [] data = new byte[len + 2];
                    data[0] = (byte)((len >> 8) & 0xff);
                    data[1] = (byte)((len & 0xff));
                    System.arraycopy(message.getBytes(), 0, data, 2, message.length());
                    System.out.println("This message has been sent from client to server-->");
                    System.out.println(message);

                    os.write(data, 0, len+2);
                    os.flush();
                    System.out.println("Sending Done at client!!!!!!!!!!");
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
                        
            }

            
            
        }
        
        
    }
    
}
