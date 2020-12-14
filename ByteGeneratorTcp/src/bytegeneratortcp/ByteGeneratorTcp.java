/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bytegeneratortcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class ByteGeneratorTcp {
    public static Socket socket;

    static OutputStream os;
    static InputStream is;
    public static int offset,len;
    public static boolean check=true;
    
    public static int sendcount=0;
    public static int receivecount=0;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Thread mysender=new MySender();
        mysender.start();
        
    }
    
    static class MySender extends Thread {


        @Override
        public void run() {

            try {
                socket=new Socket("72.249.184.22",12344);
//                socket=new Socket("192.168.19.125",1212);
                os = socket.getOutputStream();
                is = socket.getInputStream();
                Thread myReceiver=new MyReceiver(socket,is);
                myReceiver.start();



                socket.setTcpNoDelay(true);
                System.out.println("----------------> Socket established");
                int i=0;
                byte[] message=new byte[2048];
                
                while (i<5){
                    len=100;
                    String m="";
                    
                    int size=Utility.getRandomData(message,0,len);
                    byte [] data = new byte[len + 2];
                    data[0] = (byte)((len >> 8) & 0xff);
                    data[1] = (byte)((len & 0xff));

                    System.arraycopy(message, 0, data, 2, len);
                    
                    os.write(data);
                    sendcount+=1;
                    System.out.println("-----------> Send Data From Client<-------- "+sendcount);

                    Thread.sleep(200);
                    i++;
                }

            }catch (Exception e){e.printStackTrace();}

        }


    }

    static class MyReceiver extends Thread {
        Socket socket;
        InputStream is;
        public MyReceiver(Socket socket,InputStream is) {
            this.socket=socket;
            this.is=is;
        }

        @Override
        public void run() {
            try{

                byte[] data=new byte[2048];
                while(true) {
                    int firstByte = is.read();
                    int secondByte = is.read();
//                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = (firstByte << 8) | secondByte;
                    System.out.println("----> Receiving at client data of length: " + length);
                    is.read(data, 0, length);
                    if(length<0) continue;


                    String message=Utility.bytesToHex(data,0,length);

//                    System.out.println(message);
                    receivecount+=1;
                    System.out.println("Received at Client<--------  "+receivecount);
                    //is.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
}
