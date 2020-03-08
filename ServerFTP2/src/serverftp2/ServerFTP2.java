/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverftp2;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFTP2 {
    public static void main(String[] args) throws Exception {
        // create socket
        try {
            ServerSocket servsock = new ServerSocket(13267);
            while (true) {
                System.out.println("Waiting...");

                Socket sock = servsock.accept();
                System.out.println("Accepted connection : " + sock);
                OutputStream os = sock.getOutputStream();
//                new ServerFTP2().send(os);
                InputStream is = sock.getInputStream();
//                new ServerFTP2().receiveFile(is);
//                sock.close();
                
                Thread mySender=new MySender(os);
                mySender.start();
                
                Thread myReceiver=new MyReceiver(is);
                myReceiver.start();
                       
                
                

            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    /**
    /root/alif/FTPTest/test.txt
    * E:/AAANowDownloaded/downloads/photos/ab.jpg
    * C:/Users/User/Documents/NetBeansProjects/ServerFTP2/src/serverftp2/test2.txt
    * /root/alif/ftpserver
**/
    public void send(OutputStream os) throws Exception {
        // sendfile
        File myFile = new File("test2.txt");
        String s=myFile.toString();
        System.out.println("========>"+s.getBytes());
        byte[] mybytearray = new byte[(int) myFile.length() + 1];
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        


        
        bis.read(mybytearray, 0, mybytearray.length);
        System.out.println("---length of send data from server side -- > "+mybytearray.length);
        System.out.println("Sending...");
        
        byte [] data = new byte[(int)mybytearray.length + 2];
        int len=(int) mybytearray.length;
        data[0] = (byte)((len >> 8) & 0xff);
        data[1] = (byte)((len & 0xff));
        System.arraycopy(mybytearray, 0, data, 2, len);
        
        os.write(data, 0, data.length);
        os.flush();
        System.err.println("Data send from Server");
        
    }

    public void receiveFile(InputStream is) throws Exception {
        int filesize = 4096;
        int bytesRead;
        int current = 0;
        byte[] mybytearray = new byte[filesize];

        FileOutputStream fos = new FileOutputStream("def");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
//        bytesRead = is.read(mybytearray, 0, mybytearray.length);
        
        while(true){
            int firstByte = is.read();
            int secondByte = is.read();
            System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
            int length = (firstByte << 8) | secondByte;
            bytesRead = is.read(mybytearray, 0, length);
            
            bos.write(mybytearray, 0, current);
            bos.flush();
            bos.close();
            System.err.println("Done at Server Received------------>");
            
        }
        
//        current = bytesRead;
//
//        do {
//            bytesRead = is.read(mybytearray, current,
//                    (mybytearray.length - current));
//            if (bytesRead >= 0)
//                current += bytesRead;
//        } while (bytesRead>0);
//
//        bos.write(mybytearray, 0, current);
//        bos.flush();
//        bos.close();
//        System.out.println("----> Received At Server");
    }

    /**
     * C:/Users/User/Documents/NetBeansProjects/ServerFTP2/src/serverftp2/
     * 
     */
    
    private static class MySender extends Thread {
        OutputStream os;
        public MySender(OutputStream os) {
            this.os=os;
        }

        @Override
        public void run() {
            
            try {
                File myFile = new File("test2.txt");
                String s=myFile.toString();
                System.out.println("========>"+s.getBytes());
                byte[] mybytearray = new byte[(int) myFile.length() + 1];
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);




                bis.read(mybytearray, 0, mybytearray.length);
                System.out.println("---length of send data from server side -- > "+mybytearray.length);
                System.out.println("Sending...");

                byte [] data = new byte[(int)mybytearray.length + 2];
                int len=(int) mybytearray.length;
                data[0] = (byte)((len >> 8) & 0xff);
                data[1] = (byte)((len & 0xff));
                System.arraycopy(mybytearray, 0, data, 2, len);

                os.write(data, 0, data.length);
                os.flush();
                System.err.println("Data send from Server");
                
            } catch (Exception e) {
            }
        }
        
    }

    private static class MyReceiver extends Thread {
        InputStream is;
        public MyReceiver(InputStream is) {
            this.is=is;
        }

        @Override
        public void run() {
            int filesize = 4096;
            int bytesRead;
            int current = 0;
            byte[] mybytearray = new byte[filesize];
            try {
                FileOutputStream fos = new FileOutputStream("def");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
        //        bytesRead = is.read(mybytearray, 0, mybytearray.length);

                while(true){
                    int firstByte =(int)is.read();
                    int secondByte =(int)is.read();
                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = (firstByte << 8) | secondByte;
                    if(length<0) break;
                    bytesRead = is.read(mybytearray, 0, length);
                    
                    bos.write(mybytearray, 0, current);
                    bos.flush();
                    bos.close();
                    System.err.println("Done at Server Received------------>");

            }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
} 