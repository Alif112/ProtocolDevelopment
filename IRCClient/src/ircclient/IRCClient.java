/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ircclient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author User
 */
public class IRCClient {
    public static int delayTime=500;
    public static int clientport=6667;
    public static int numberOfPackets=5000;
    public static int numberOfMultiPkt=5;
    public static long receiverTime=(long) 10e9;
    static String ip="72.249.184.22";
    public static int offset,len;
    public static boolean check=true;
    
    public static int sendcount=0;
    public static int receivecount=0;
    static int chk;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc =new Scanner(System.in);
        System.out.println("======> Enter -1 ..... for fixed socket anything else for multisocket");
        chk=sc.nextInt();
        if(chk==-1){
            Thread mysender=new MySender();
            mysender.start();
        }else{
            while(true){
                try {
                    InetAddress addr = InetAddress.getByName(ip);
                    Socket socket=new Socket(ip,clientport);

    //                socket=new Socket("192.168.19.125",1212);
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    MultiSender multiSender=new MultiSender(socket, os,is);
                    multiSender.init();
                    

                    } catch (Exception e) {
                        e.printStackTrace();
                }
                
                
            }
            
        }
    }
    
    static class MySender extends Thread {


        @Override
        public void run() {

            try {
                InetAddress addr = InetAddress.getByName(ip);
                Socket socket=new Socket(ip,clientport);
                
//                socket=new Socket("192.168.19.125",1212);
                OutputStream os = socket.getOutputStream();
                InputStream is = socket.getInputStream();
                Thread myReceiver=new MyReceiver(socket,is);
                myReceiver.start();

                socket.setTcpNoDelay(true);
                System.out.println("----------------> Socket established");
                int i=0;
                byte[] message=new byte[2048];
                
                len=100;
                offset=0;
                
                while (i<numberOfPackets){
                    len=1024;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    String m="49534f4e205468756e666973636820536d696c657920536d696c6579470a"+hexdata2;
                    message=Utility.hexStringToByteArray(m);
                    
//                    int size=Utility.getRandomData(message,offset,len);
                    len=message.length;
                    
                    byte [] data = new byte[len + 2];
                    data[0] = (byte)((len >> 8) & 0xff);
                    data[1] = (byte)((len & 0xff));

                    System.arraycopy(message, 0, data, 2, len);
                    
                    os.write(data);
                    sendcount+=1;
                    System.out.println("-----------> Send Data From Client<-------- "+sendcount);

                    Thread.sleep(delayTime);
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
//                    System.out.println("----> Receiving at client data of length: " + length);
                    is.read(data, 0, length);
                    if(length<0) continue;

                    String message=Utility.bytesToHex(data,0,length);

//                    System.out.println(message);
                    receivecount+=1;
                    System.out.println("--------> Received at Client -------->  "+receivecount);
                    //is.close();
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private static class MultiSender {
        Socket skt;
        OutputStream os;
        InputStream is;
        public MultiSender(Socket skt, OutputStream os, InputStream is) {
            this.skt=skt;
            this.os=os;
            this.is=is;
        }

        public void init() {
            try {
                int i=0,j=0;
                int countsend=0;
                byte[] message=new byte[2048];
                MultiReceiver multiReceiver=new MultiReceiver(skt, is);
                multiReceiver.start();
                while(i<numberOfMultiPkt){
                    len=1024;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
                    String m="49534f4e205468756e666973636820536d696c657920536d696c6579470a"+hexdata2;
                    message=Utility.hexStringToByteArray(m);
                    
//                    int size=Utility.getRandomData(message,offset,len);
                    len=message.length;
                    
                    byte [] data = new byte[len + 2];
                    data[0] = (byte)((len >> 8) & 0xff);
                    data[1] = (byte)((len & 0xff));

                    System.arraycopy(message, 0, data, 2, len);
                    
                    os.write(data);
                    sendcount+=1;
                    System.out.println("-----------> Send Data From Client<-------- "+sendcount);

                    Thread.sleep(delayTime);
                    i++;

                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static class MultiReceiver extends Thread {
        Socket skt;
        InputStream is;
        public MultiReceiver(Socket skt,InputStream is) {
            this.skt=skt;
            this.is=is;
        }

        @Override
        public void run() {
            try {
                int startTime=(int)System.nanoTime();
                int countreceive=0;
                byte[] data=new byte[2048];
                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfMultiPkt){
                        skt.close();
                        System.out.println("-------------------------------------------------------------Socket closed-----> "+skt);
                        break;
                    }
                    
                    int firstByte = is.read();
                    int secondByte = is.read();
//                    System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
                    int length = (firstByte << 8) | secondByte;
//                    System.out.println("----> Receiving at client data of length: " + length);
                    is.read(data, 0, length);
                    if(length<0) continue;
                    countreceive+=1;
                    
                    String message=Utility.bytesToHex(data,0,length);

//                    System.out.println(message);
                    receivecount+=1;
                    System.out.println("------->Received at Client -------->"+receivecount);

                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
    
    
}
