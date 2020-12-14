/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cqlserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class CQLImplementation {
    
    private int createLen;
    private byte[] messageData1,secondHeaderHand;
    private short streamCode;
    
    public CQLImplementation() {
        streamCode=3;
        messageData1=Utility.hexStringToByteArray("0002000b434f4d5052455353494f4e00020006736e6170707900036c7a34000b43514c5f56455253494f4e00010005332e332e31");
   }

    public boolean cqlHandshakeAtServer(Socket socket){
        byte data[]=new byte[1024];
        int index=0,offset=0;
        byte[] username,password;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            is.read(data, offset, 9);
            index=getBaseStructure(data, offset, (byte)132, (short)0, (byte)6, 52);
            System.arraycopy(messageData1, 0, data, index, messageData1.length);
            index+=messageData1.length;
            os.write(data, offset, index);
            is.read(data, offset, 31);
            index=getBaseStructure(data, offset, (byte)132, (short)1, (byte)2, 0);
            os.write(data, offset, index);
            is.read(data, offset, 58);
            index=getBaseStructure(data, offset, (byte)132, (short)2, (byte)2, 0);
            os.write(data, offset, index);
            
            
            System.out.println("CQL HandShake Completed at Server !!!\n");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }
    

    public int createPacketAtServer(byte [] data, int offset, int len){
        if(data.length <= offset + len + 27)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 23] = data[i]; 
        
        int index=offset;
        index=getBaseStructure(data, index, (byte)132, streamCode, (byte)8, len+18);
//        resulting kind
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01;
//        rows result flags
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01;
//        column count 
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x01;
//        Datalen
        Functions.putShort2(data, index, (short)len);;
        index+=2;
        index+=len;
        
//        CQL result rows
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        return index;
    }
    
    public int decodePacketAtServer(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 2);
            streamCode=Utility.buildLen2(is);
            Functions.ignoreByte(is, 5);
            createLen=Utility.buildLen4(is);
            is.read(data, offset, createLen);
            Functions.ignoreByte(is, 3);

            return createLen;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public int getBaseStructure(byte[] data, int offset,byte version,short streamCode,byte opCode,int lenWithExtra)
    {
        int index=offset;
        data[index++]=version;
        data[index++]=0x00;
        Functions.putShort2(data, index, streamCode);
        index+=2;
        data[index++]=opCode;
        Functions.putInt4(data, index, lenWithExtra);
        index+=4;
        
        return index;
    }

}