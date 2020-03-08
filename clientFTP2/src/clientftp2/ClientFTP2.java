package clientftp2;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientFTP2 {
    public static void main(String[] args) throws Exception {
        
        try {
            
            long start = System.currentTimeMillis();

            // localhost for testing
            Socket sock = new Socket("191.101.189.91", 13267);
//            Socket sock = new Socket("localhost", 13267);
            System.out.println("Connecting...");

            // receive file
            OutputStream os = sock.getOutputStream();
            InputStream is = sock.getInputStream();
            
            Thread mySender=new MyThread(os);
            mySender.start();
            Thread myReceiver=new MyReceiver(is);
            myReceiver.start();
            
//            
//            new ClientFTP2().receiveFile(is);
//            
//            new ClientFTP2().send(os);
            
            
            long end = System.currentTimeMillis();
            System.out.println("====Time Spent=====> "+(end - start));

//            sock.close();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

//    E:/AAANowDownloaded/downloads/photos2/

    public void send(OutputStream os) throws Exception {
        // sendfile
        File myFile = new File("C:/Users/User/Documents/NetBeansProjects/clientFTP2/src/clientftp2/test2.txt");
        byte[] mybytearray = new byte[(int) myFile.length() + 1];
        System.out.println("------> "+(int)myFile.length());
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray, 0, mybytearray.length);
        System.out.println("Sending...");
        
        byte [] data = new byte[(int)mybytearray.length + 2];
        int len=(int) mybytearray.length;
        data[0] = (byte)((len >> 8) & 0xff);
        data[1] = (byte)((len & 0xff));
        System.arraycopy(mybytearray, 0, data, 2, len);
        
        
        os.write(data, 0, data.length);
        os.flush();
        System.out.println("==send from client==> "+mybytearray.length);
    }

    public void receiveFile(InputStream is) throws Exception {
        int filesize = 4096;
        int bytesRead;
        int current = 0;
        byte[] mybytearray = new byte[filesize];

        FileOutputStream fos = new FileOutputStream("def.txt");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while(true){
            int firstByte = is.read();
            int secondByte = is.read();
            System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
            int length = (firstByte << 8) | secondByte;
            if(length<0) break;
            bytesRead = is.read(mybytearray, 0, length);
            
            bos.write(mybytearray, 0, current);
            bos.flush();
            bos.close();
            System.err.println("Done at Client Received------------>");
            
        }
        
        
//        current = bytesRead;
//        System.out.println("Received at client");
//        int i=0;
        
//        do {
//            System.out.println(i+"------> current: "+current);
//            i++;
//            bytesRead = is.read(mybytearray, current,
//                    (mybytearray.length - current));
//            System.out.println(i+"------> byte arry: "+bytesRead);
//            if (bytesRead >= 0)
//                current += bytesRead;
//            System.out.println(i+"------> current: "+current);
//        } while (bytesRead>-1);

//        bos.write(mybytearray, 0, current);
//        bos.flush();
//        bos.close();
//        System.err.println("Done at Client Received------------>");
    }

    private static class MyThread extends Thread {
        OutputStream os;
        public MyThread(OutputStream os) {
            this.os=os;
        }

        @Override
        public void run() {
                        // sendfile
            File myFile = new File("C:/Users/User/Documents/NetBeansProjects/clientFTP2/src/clientftp2/test.txt");
            byte[] mybytearray = new byte[(int) myFile.length() + 1];
            System.out.println("------> "+(int)myFile.length());
            try {
                FileInputStream fis = new FileInputStream(myFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(mybytearray, 0, mybytearray.length);
                System.out.println("Sending...");

                byte [] data = new byte[(int)mybytearray.length + 2];
                int len=(int) mybytearray.length;
                data[0] = (byte)((len >> 8) & 0xff);
                data[1] = (byte)((len & 0xff));
                System.arraycopy(mybytearray, 0, data, 2, len);


                os.write(data, 0, data.length);
                os.flush();
                System.out.println("==send from client==> "+mybytearray.length);
            } catch (Exception e) {
                e.printStackTrace();
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
                FileOutputStream fos = new FileOutputStream("def.txt");
                BufferedOutputStream bos = new BufferedOutputStream(fos);
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
                    System.err.println("Done at Client Received------------>");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        
    }
} 