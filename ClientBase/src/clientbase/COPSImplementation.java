/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase;

import java.io.InputStream;

/**
 *
 * @author User
 */
public class COPSImplementation {
    public byte protocolVersion;
    public byte clientOpCode;
    public short clientType;
    public byte messageIntegrity;
    public byte macDigest;
    public int keyId;
    public int sequenceNumber;
    int createLen;
    
    public COPSImplementation() {
        protocolVersion=0x10;
        clientOpCode=0x06;
        clientType=0; 
        messageIntegrity=0x10;
        macDigest=0x01;
        keyId=1;
        sequenceNumber=0;
        
   }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 52)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 52] = data[i];
        
        int index=offset;
        data[index++]=protocolVersion;
        data[index++]=clientOpCode;
        Functions.putShort2(data, index, clientType);
        index+=2;
        Functions.putInt4(data, index, len+52);
        index+=4;
        //add PEPID
        String pepId="001f0b01412050455020666f72206578616d706c6520707572706f73657300";
        byte[] pepIdData=Utility.hexStringToByteArray(pepId);
        System.arraycopy(pepIdData, 0, data, index, pepIdData.length);
        index+=pepIdData.length;
        //padding 00
        data[index++]=0x00;
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
        
        Functions.ignoreByte(is, 20);
        is.read(data,offset,createLen-28);
        return createLen-28;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
}
