/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vxi11coreserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class Vxi11CoreImplementation {
    
    private short xID;
    private int packetType;
    private int clientIDOrLinkID;
    private int packetLen;
    
    
    public boolean vxi11CoreHandshakeAtServer(Socket socket){
        byte[] sendData=new byte[44];
        int index=0;
        
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            
            Functions.ignoreByte(is, 4);
            xID=(short) Utility.buildLen4(is);
            Functions.ignoreByte(is, 72);;
            System.out.println("Create link XID---> "+xID);
            
            index=rpcProtocolServer(sendData, 0,40);
            System.out.println("-------------------------> "+index);
            index=createLinkReply(sendData, index);
            os.write(sendData);
            
            Functions.ignoreByte(is, 4);
            xID=(short) Utility.buildLen4(is);
            Functions.ignoreByte(is, 72);;
            System.out.println("Device Do CMD XID---> "+xID);
            
            index=rpcProtocolServer(sendData, 0,36);
            System.out.println("-------------------------> "+index);
            index=deviceDocmdReply(sendData, index);
            os.write(sendData);
            
            Functions.ignoreByte(is, 4);
            xID=(short) Utility.buildLen4(is);
            Functions.ignoreByte(is, 72);;
            System.out.println("Device write XID---> "+xID);
            
            index=rpcProtocolServer(sendData, 0,32);
            
            System.out.println("-------------------------> "+index);
            index=deviceWriteReply(sendData, index);
            os.write(sendData);
            
            Functions.ignoreByte(is, 4);
            xID=(short) Utility.buildLen4(is);
            Functions.ignoreByte(is, 72);;
            System.out.println("Device read XID---> "+xID);
            
            index=rpcProtocolServer(sendData, 0,36);
            
            System.out.println("-------------------------> "+index);
            index=deviceReadReply(sendData, index);
            os.write(sendData);
            
            Functions.ignoreByte(is, 4);
            xID=(short) Utility.buildLen4(is);
            Functions.ignoreByte(is, 72);;
            System.out.println("Device destry XID---> "+xID);
            
            index=rpcProtocolServer(sendData, 0,28);
            System.out.println("-------------------------> "+index);
            index=deviceDestroyReply(sendData, index);
            os.write(sendData);
            
            
            
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
        
        
    }
    
    
    public Vxi11CoreImplementation() {
        xID=(short) Utility.random.nextInt();
   }
    
    public int createPacketAtServer(byte [] data, int offset, int len){
        if(data.length <= offset + len + 48)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 48] = data[i];
        
        int index=offset;
        
        switch(packetType){
            case 10:
                index=rpcProtocolServer(data, index,40);
                index=createLinkReply(data, index);
                break;
            case 11:
                index=rpcProtocolServer(data, index,32);
                index=deviceWriteReply(data, index);
                break;
            case 12:
                index=rpcProtocolServer(data, index,36);
                index=deviceReadReply(data, index);
                break;
            case 23:
                index=rpcProtocolServer(data, index,28);
                index=deviceDestroyReply(data, index);
                break;
            
        }
        
        Functions.putInt4(data, index, len);
        index+=4;
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 4);
            xID=Utility.buildLen2(is);
            Functions.ignoreByte(is, 18);
            packetType=Utility.buildLen4(is);
            Functions.ignoreByte(is, 52);
//            clientIDOrLinkID=Utility.buildLen4(is);
//            Functions.ignoreByte(is, 32);
            packetLen=Utility.buildLen4(is);
            is.read(data, offset, packetLen);
            return packetLen;
            
        }catch(Exception e) {e.printStackTrace();}
        return -1;

        
    }

    public int rpcProtocolServer(byte[] data, int offset, int fragmentLen) {
        int index=offset;
//        fragment details
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=(byte) fragmentLen;
        Functions.putShort2(data,index,xID);
        index+=2;
        data[index++]=0x27; data[index++]=0x36;
//        message type
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01;
//        reply state accepted
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        varifier
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        Accepted successfully
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 

        return index;
    }
    
    
    public int createLinkReply(byte[] data, int offset){
        int index=offset;
//        Error Code
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        Functions.putInt4(data, index, clientIDOrLinkID);
        index+=4;
//        Abbort Port
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x03; data[index++]=0x2d; 
//        maximum received size
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x40; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 16-(index-offset));

        return index;
    }
    public int deviceDocmdReply(byte[] data, int offset){
        int index=offset;
//        error code 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        data 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 16-(index-offset));
        
        return index;
    }
    public int deviceWriteReply(byte[] data, int offset){
        int index=offset;
//        error Code
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x0f;
//        size len
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 16-(index-offset));
        return index;
    }
    public int deviceReadReply(byte[] data, int offset){
        int index=offset;
        //        error Code
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x0f;
//        size len
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
//        data empty
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 16-(index-offset));
        
        return index;
    }
    
    public int deviceDestroyReply(byte[] data, int offset){
        int index=offset;
//        error code
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        index=Functions.makeSameLength(data, index, 16-(index-offset));
        
        return index;
    }
    
    
}
