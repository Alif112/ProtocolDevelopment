/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copsserver;

import static copsserver.COPSServer.offset;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author User
 */
public class COPSImplementation {
    private byte protocolVersion;
    private byte clientOpCode;
    private short clientType;
    private byte messageIntegrity;
    private byte macDigest;
    private int keyId;
    private int sequenceNumber,createLen;
    private byte[] timeObjectData;
    
    public COPSImplementation() {
        protocolVersion=0x10;
        clientOpCode=0x07;
        clientType=0; 
        messageIntegrity=0x10;
        macDigest=0x01;
        keyId=1;
        sequenceNumber=0;
        timeObjectData=Utility.hexStringToByteArray("00080a010004000a");
        
   }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 28)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 28] = data[i];
        
        int index=offset;
        data[index++]=protocolVersion;
        data[index++]=clientOpCode;
        Functions.putShort2(data, index, clientType);
        index+=2;
        Functions.putInt4(data, index, len+28);
        index+=4;
        //add time object
        
        System.arraycopy(timeObjectData, 0, data, index, timeObjectData.length);
        index+=timeObjectData.length;
        // message integrity object
        Functions.putShort2(data, index, (short) (len+12));
        index+=2;
        data[index++]=messageIntegrity;
        data[index++]=macDigest;
        Functions.putInt4(data, index, keyId);
        index+=4;
        Functions.putInt4(data, index, sequenceNumber);
        index+=4;
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is,4);
            createLen=Utility.buildLen4(is);
            
            Functions.ignoreByte(is, 44);
            is.read(data,offset,createLen-52);
            return createLen-52;
        }catch(Exception e) {e.printStackTrace();}
        return -1;

        
    }
    
}
