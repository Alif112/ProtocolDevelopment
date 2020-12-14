/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author User
 */
public class SMTPImplementation {

    private byte[] header,mime,content;
    private String parameterReceiver;
    private int createLen;

    public SMTPImplementation() {
        header=Utility.hexStringToByteArray("5265706c792d546f3a203c7878787878784078787878782e636f2e756b3e0d0a46726f6d3a202257536861726b205573657222203c7878787878784078787878782e636f2e756b3e0d0a546f3a203c7878787878782e787878784078787878782e636f6d3e0d0a5375626a6563743a2054657374206d65737361676520666f7220636170747572650d0a446174653a20");
        mime=Utility.hexStringToByteArray("4d494d452d56657273696f6e3a20312e300d0a");
        content=Utility.hexStringToByteArray("436f6e74656e742d547970653a206d756c7469706172742f6d697865643b0d0a");
    }


    public boolean smtpHandshakeAtClient(Socket socket){
        byte[] data=new byte[1024];
        int index;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();

            os.write(Utility.hexStringToByteArray("45484c4f20506572636976616c0d0a"));
            is.read(data, 0, 94);
//            parameterReceiver=Utility.bytesToHex(data, 0, 15);
//            System.out.println(parameterReceiver);;
            os.write(Utility.hexStringToByteArray("41555448204c4f47494e0d0a"));

            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("paramenter===================> "+parameterReceiver);
            index=0;
            index=Functions.getUserName(data, index);
            data[index++]=0x0d; data[index++]=0x0a;
            os.write(data, 0, index);

            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("3334 2nd user===================> "+parameterReceiver);

            index=0;
            index=Functions.getPassWord(data, index);
            data[index++]=0x0d; data[index++]=0x0a;
            os.write(data, 0, index);

            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("ok lets go===================> "+parameterReceiver);

            os.write(Utility.hexStringToByteArray("4d41494c2046524f4d3a203c7878787878784078787878782e636f2e756b3e0d0a"));
            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("email back go===================> "+parameterReceiver);
            os.write(Utility.hexStringToByteArray("5243505420544f3a203c7878787878782e787878784078787878782e636f6d3e0d0a"));
            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("email 2 back go===================> "+parameterReceiver);
            os.write(Utility.hexStringToByteArray("444154410d0a"));
            parameterReceiver=Functions.readLine(is, data);
//            System.out.println("Data ok go===================> "+parameterReceiver);


            System.out.println("\nSMTP HandShake Successfull at client!!\n");
            return true;
        } catch (IOException ex) {
            System.out.println("SMTP Core HandShake Failed");
            ex.printStackTrace();
        }
        return false;


    }
    public String getDateAndTime(){
        String result = null;
//
//        Calendar cal = Calendar.getInstance();
//        int day = cal.get(Calendar.DAY_OF_WEEK);
//
//        switch (day) {
//            case 1:
//                result="Sun";
//                break;
//            case 2:
//                result="Mon";
//                break;
//            case 3:
//                result="Tue";
//                break;
//            case 4:
//                result="Wed";
//                break;
//            case 5:
//                result="Thu";
//                break;
//            case 6:
//                result="Fri";
//                break;
//            case 7:
//                result="Sat";
//                break;
//        }
//        result=result+", ";

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        result = df.format(Calendar.getInstance().getTime());

        return result;
    }

    public boolean smtpHandshakeAtServer(Socket socket){
        byte data[]=new byte[1024];
        int index=0;
        byte[] username,password;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();

            is.read(data, 0, 15);
//            String sd=Utility.bytesToHex(data, 0, 15);
//            System.out.println(sd);;

            os.write(Utility.hexStringToByteArray("3235302d736d74703030362e6d61696c2e7878782e78787878782e636f6d0d0a3235302d41555448204c4f47494e20504c41494e2058594d434f4f4b49450d0a3235302d504950454c494e494e470d0a32353020384249544d494d450d0a"));
            is.read(data, 0, 12);
//            sd=Utility.bytesToHex(data, 0, 12);
//            System.out.println(sd);

            index=0;
            data[index++]=0x33; data[index++]=0x33; data[index++]=0x34; data[index++]=0x20;
            index=Functions.getUserName(data, index);
            data[index++]=0x0d; data[index++]=0x0a;
            os.write(data, 0, index);

            String sd=Functions.readLine(is, data);
//            System.out.println("----------> usernaem "+sd);

            index=0;
            data[index++]=0x33; data[index++]=0x33; data[index++]=0x34; data[index++]=0x20;
            index=Functions.getUserName(data, index);
            data[index++]=0x0d; data[index++]=0x0a;
            os.write(data, 0, index);

            sd=Functions.readLine(is, data);
//            System.out.println("----------> password Received "+sd);

            os.write(Utility.hexStringToByteArray("323335206f6b2c20676f206168656164202823322e302e30290d0a"));
            sd=Functions.readLine(is, data);
//            System.out.println("----------> email received "+sd);
            os.write(Utility.hexStringToByteArray("323530206f6b0d0a"));
            sd=Functions.readLine(is, data);
//            System.out.println("----------> email 2 received "+sd);
            os.write(Utility.hexStringToByteArray("323530206f6b0d0a"));
            sd=Functions.readLine(is, data);
//            System.out.println("----------> DATA "+sd);
            os.write(Utility.hexStringToByteArray("33353420676f2061686561640d0a"));


            System.out.println("HandShake Completed at Server !!!\n");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }




    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 233)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 231] = data[i];

        int index=offset;
        System.arraycopy(header, 0, data, index, header.length);
        index+=header.length; //144
        byte[] dateData=getDateAndTime().getBytes();
        System.arraycopy(dateData, 0, data, index, dateData.length);
        index+=dateData.length; //30 bytes
        data[index++]=0x0d; data[index++]=0x0a;
        System.arraycopy(mime, 0, data, index, mime.length);
        index+=mime.length; //19
        System.arraycopy(content, 0, data, index, content.length);
        index+=content.length; //32
        Functions.putInt4(data, index, len);
        index+=4;
        index+=len;
        data[index++]=0x0d; data[index++]=0x0a;

        return index;
    }

    public int decodePacket(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 227);
            createLen=Utility.buildLen4(is);
            is.read(data,0,createLen);
            Functions.ignoreByte(is, 2);
            return createLen;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }

}