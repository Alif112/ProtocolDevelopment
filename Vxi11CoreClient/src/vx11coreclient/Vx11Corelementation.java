/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vx11coreclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class Vx11Corelementation {
    private short xID;
    private int ck=0;
    private int clientID;
    private int deviceNameLength;
    private int linkID;
    private int  lengthAtDeviceWrite; 
    int createLen;
    

    
    public boolean vxi11CoreHandshakeAtClient(Socket socket){
        int index;
        byte[] sendData=new byte[80];
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            index=rpcProtocolClient(sendData, 0, 10,64);
            System.out.println("--------------------------> "+index);
            index=createLinkCall(sendData, index);
            System.out.println("--------------------------> "+index);
            os.write(sendData);
            
            is.read(sendData,0,44);
            String m=Utility.bytesToHex(sendData, 0, 44);
            System.out.println(m);
            
            System.out.println("-------------------- Device DoCMD");
            index=rpcProtocolClient(sendData, 0, 22,72);
            System.out.println("--------------------------> "+index);
            index=deviceDocmdCall(sendData, index);
            System.out.println("--------------------------> "+index);
            os.write(sendData);
            
            is.read(sendData,0,44);
            m=Utility.bytesToHex(sendData, 0, 44);
            System.out.println(m);
            
            System.out.println("-------------------- Device write");
            index=rpcProtocolClient(sendData, 0, 11,68);
            System.out.println("--------------------------> "+index);
            index=deviceWriteCall(sendData, index);
            System.out.println("--------------------------> "+index);
            os.write(sendData);
            
            System.out.println("========> write received at client");
            is.read(sendData,0,44);
            m=Utility.bytesToHex(sendData, 0, 44);
            System.out.println(m);
            
            
            System.out.println("-------------------- Device Read");
            index=rpcProtocolClient(sendData, 0, 12,64);
            System.out.println("--------------------------> "+index);
            index=deviceReadCall(sendData, index);
            System.out.println("--------------------------> "+index);
            os.write(sendData);
            
            System.out.println("========> read received at client");
            is.read(sendData,0,44);
            m=Utility.bytesToHex(sendData, 0, 44);
            System.out.println(m);
            
            System.out.println("-------------------- Device Read");
            index=rpcProtocolClient(sendData, 0, 23,44);
            System.out.println("--------------------------> "+index);
            index=deviceDestroyCall(sendData, index);
            System.out.println("--------------------------> "+index);
            os.write(sendData);
            
            System.out.println("========> destroy received at client");
            is.read(sendData,0,44);
            m=Utility.bytesToHex(sendData, 0, 44);
            System.out.println(m);

            System.out.println("VXI-11 Core HandShake Successfull at client!!");
            return true;
        } catch (IOException ex) {
            System.out.println("VXI-11 Core HandShake Failed");
            ex.printStackTrace();
        }
        return false;
        
        
    }
    
    public Vx11Corelementation() {
        ck=0;
        
        deviceNameLength=5;
        clientID=Utility.random.nextInt();
        linkID=Utility.random.nextInt();
   }
    
    public int rpcProtocolClient(byte[] data, int offset, int procedure, int fragmentLen){
        int index=offset;
//        fragment header
        data[index++]=(byte) 0x80; data[index++]=0x00; data[index++]=0x00; data[index++]=(byte) fragmentLen; 
        Functions.putShort2(data,index,xID);
        index+=2;
        data[index++]=0x27; data[index++]=0x36;
//        message Type call
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        RPC version 2
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x02; 
//        VXI-11 Core
        data[index++]=0x00; data[index++]=0x06; data[index++]=0x07; data[index++]=(byte) 0xaf; 
//        Program Versoin
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; 
//        insert Procedure
        Functions.putInt4(data, index, procedure);
        index+=4;
//        insert credentials and verifier
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 

        xID=(short) Utility.random.nextInt();
        return index;
    }
     
    
    public int createLinkCall(byte[] data, int offset){
        int index=offset;
        Functions.putInt4(data, index, clientID);
        index+=4;
//        Lock Device nad Lock Timeout
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        Device Details
        Functions.putInt4(data, index, deviceNameLength);
        index+=4;
        for(int i=0;i<deviceNameLength;i++)
            data[index++]=(byte) (Utility.random.nextInt(26)+97);
//        opaque data
        data[index++]=0x00;
        data[index++]=0x00;
        data[index++]=0x00;
        
        index=Functions.makeSameLength(data, index, 36-(index-offset));

        return index;
    }
    public int deviceDocmdCall(byte[] data, int offset){
        int index=offset;
        Functions.putInt4(data, index, linkID);
        index+=4;
//        Flags
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01;
//        i/o lock and timeout
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        IFC Control
        data[index++]=0x00; data[index++]=0x02; data[index++]=0x00; data[index++]=0x10; 
//        network bytes order
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; 
//        size
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        data detail
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 36-(index-offset));
        
        return index;
    }
    public int deviceWriteCall(byte[] data, int offset){
        int index=offset;
        Functions.putInt4(data, index, linkID);
        index+=4;
//        i/o timeout and lock Timeout
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; data[index++]=(byte) 0xf4;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; data[index++]=(byte) 0xf4;
//        flag
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x08;
//        Data
        lengthAtDeviceWrite=Utility.random.nextInt(3)+2;
        Functions.putInt4(data, index, lengthAtDeviceWrite);
        index+=4;
        
        for(int i=0;i<lengthAtDeviceWrite-2;i++)
            data[index++]=(byte) Utility.random.nextInt();
        data[index++]=0x0d;
        data[index++]=0x0a;
        data[index++]=0x00; //padding
        
        index=Functions.makeSameLength(data, index, 36-(index-offset));
        return index;
    }
    public int deviceReadCall(byte[] data, int offset){
        int index=offset;
        Functions.putInt4(data, index, linkID);
        index+=4;
//        size
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x04; data[index++]=0x00;
//        i/o timeout and lock Timeout
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; data[index++]=(byte) 0xf4;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x01; data[index++]=(byte) 0xf4;
        
//        flag
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
//        tarmination character
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        
        index=Functions.makeSameLength(data, index, 36-(index-offset));
        
        return index;
    }
    
    public int deviceDestroyCall(byte[] data, int offset){
        int index=offset;
        Functions.putInt4(data, index, linkID);
        index+=4;
//        padding 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        index=Functions.makeSameLength(data, index, 36-(index-offset));
        
        return index;
    }

    public int createPacketAtClient(byte [] data, int offset, int len){
        if(data.length <= offset + len + 84)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 84] = data[i];
        
        int index=offset;
        
        switch(ck){
            case 0:
                
                index=rpcProtocolClient(data, index,10,64);
                index=createLinkCall(data, index);
                
                clientID=Utility.random.nextInt();
                linkID=Utility.random.nextInt();
                ck=1;
                break;
            case 1:
                index=rpcProtocolClient(data, index,11,68);
                index=deviceWriteCall(data, index);
                ck=2;
                break;
            case 2:
                index=rpcProtocolClient(data, index,12,44);
                index=deviceReadCall(data, index);
                ck=3;
                break;
            case 3:
                index=rpcProtocolClient(data, index,23,44);
                index=deviceDestroyCall(data, index);
                ck=0;
                break;
            
        }
        Functions.putInt4(data, index, len);
        index+=4;
        
        return index+len;
    }
    
    public int decodePacketAtClient(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 44);
            createLen=Utility.buildLen4(is);
            is.read(data, offset, createLen);
            return createLen;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    
    
    
}
