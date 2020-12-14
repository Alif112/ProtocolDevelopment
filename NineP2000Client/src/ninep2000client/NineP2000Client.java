/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ninep2000client;

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
public class NineP2000Client {
    public static int delayTime=500;
    public static int clientport=564;
    public static int numberOfPackets=5000;
    public static int numberOfMultiPkt=5;
    public static long receiverTime=(long) 10e9;
    static String ip="72.249.184.22";
    public static int offset,len;
    public static boolean check=true;
    
    public static int sendcount=0;
    public static int receivecount=0;
    static int chk;
    static NineP2000Implementation nineP2000=new NineP2000Implementation(true);
    
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
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
//                    String m="64ffff182000000600395032303030"+hexdata2;
//                    
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
////                    System.out.println("length hex data-----------> "+m1);
//                    m=m1+m;
//                    message=Utility.hexStringToByteArray(m);
//                    System.out.println(hexdata2);
//                    System.out.println(m);
                    
//                    int size=Utility.getRandomData(message,offset,len);
                    
                    offset=0;
                    len=150;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=nineP2000.createPacket(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] data=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
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
//                    countreceive++;
//                    String msg=Utility.bytesToHex(data, offset, length-19);
//                    System.out.println(msg);
                    
                    len=nineP2000.decodePacket(data, offset, is);
                    String msg=Utility.bytesToHex(data, offset, len);
                    System.out.println("=============================> "+len);
//                    System.out.println(msg);
                    
                    
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
                    len=300;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    len=4;
                    byte[] data3 = new byte[len];
                    data3=Utility.getRandomData(data3, len);
                    String hexdata3=Utility.bytesToHex(data3);
                    
//                    String m="64ffff182000000600395032303030"+hexdata2;
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
//                    System.out.println("length hex data-----------> "+m1);
//                    m=m1+m;
//                    message=Utility.hexStringToByteArray(m);
                    
//                    int size=Utility.getRandomData(message,offset,len);
                    
                    offset=0;
                    len=200;
                    
                    byte[] newdata=new byte[offset+len+100];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=nineP2000.createPacket(newdata, offset, len);
                    String m=Utility.bytesToHex(newdata,offset,len2);
//                    System.out.println("================================>          "+ len2);
//                    System.out.println(m);
                    byte[] data=Utility.hexStringToByteArray(m); 
                    
//                    os.write(message);
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
//                    countreceive++;
//                    String msg=Utility.bytesToHex(data, offset, length-19);
//                    System.out.println(msg);
                    
                    countreceive++;
                    len=nineP2000.decodePacket(data, offset, is);
                    String msg=Utility.bytesToHex(data, offset, len);
                    System.out.println("=============================> "+len);
//                    System.out.println(msg);
                    
                    
                    receivecount+=1;
                    System.out.println("-----> Received at Client -------->  "+receivecount);
                    //is.close();
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
    
    
}
