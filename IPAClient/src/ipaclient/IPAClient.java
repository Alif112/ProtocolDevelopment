/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipaclient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


/**
 *
 * @author User
 */
public class IPAClient {
    public static int delayTime=700;
    public static int clientport=5000;
    public static int numberOfPackets=5000;
    public static int numberOfMultiPkt=25;
    public static long receiverTime=(long) 10e9;
    static String ip="65.99.254.85";
    public static int offset,len;
    public static boolean check=true;
    
    public static int sendcount=0;
    public static int receivecount=0;
    static int chk;
    static IPAImplementation smtp=new IPAImplementation();
    
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
                boolean isHandShake=smtp.ipaHandshakeAtClient(socket);
                if(!isHandShake) throw new Exception("HandShaking Failed!!!");
                
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
                    
//                    String m="10060000"
//                            + "0000016c"
//                            + "001f0b01412050455020666f72206578616d706c6520707572706f7365730000"
//                            + "0144"
//                            + "1001000000010000000067fa197f98283b0aefa31042"+hexdata2;
                    
//                    System.out.println("-------------------->");
//                    System.out.println(hexdata2);
                    
                    
                    offset=0;
                    len=500;
                    
                    byte[] newdata=new byte[offset+len+300];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=smtp.createPacketAtClient(newdata, offset, len);
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
                int index=offset;
                while(true) {
//                    Functions.ignoreByte(is,4);
//                    int createLen=Utility.buildLen4(is);
//                    Functions.ignoreByte(is, 32);
//                    System.out.println("=============================> "+(createLen-40));
//                    is.read(data,offset,createLen-40);
//                    String msg=Utility.bytesToHex(data, offset, createLen-40);
//                    System.out.println(msg);
                    
                    len=smtp.decodePacketAtClient(data, offset, is);
                    String msg=Utility.bytesToHex(data, offset, len);
                    System.out.println("====received at client============> "+len);
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
                boolean isHandShake=smtp.ipaHandshakeAtClient(skt);
                if(!isHandShake) throw new Exception("HandShaking Failed!!!");
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
                    
//                    String m="10060000"
//                            + "0000016c"
//                            + "001f0b01412050455020666f72206578616d706c6520707572706f7365730000"
//                            + "0144"
//                            + "10010000000100000000"
//                            + "67fa197f98283b0aefa31042"+hexdata2;
                    
//                    System.out.println("-------------------->");
//                    System.out.println(hexdata2);
                    
                    offset=0;
                    len=500;
                    
                    byte[] newdata=new byte[offset+len+500];
                    int len2=Utility.getRandomData(newdata, offset, len);
                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
                    
                    len2=smtp.createPacketAtClient(newdata, offset, len);
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
                int index=offset=0;
                
                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfMultiPkt){
                        skt.close();
                        System.out.println("------------------------------------------Socket closed-----> "+skt);
                        break;
                    }
                    
//                    Functions.ignoreByte(is,344);
//                    int createLen=Utility.buildLen4(is);
//                    Functions.ignoreByte(is, 32);
//                    System.out.println("=============================> "+(createLen-40));
//                    is.read(data,offset,createLen-40);
//                    String msg=Utility.bytesToHex(data, offset, createLen-40);
//                    System.out.println(msg);

                    countreceive++;
                    len=smtp.decodePacketAtClient(data, offset, is);
//                    String msg=Utility.bytesToHex(data, offset, len);
//                    System.out.println("=============================> "+len);
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
