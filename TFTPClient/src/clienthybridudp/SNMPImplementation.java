/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthybridudp;

import java.net.InetAddress;
import java.util.Random;

/**
 *
 * @author User
 */

public class SNMPImplementation {
    public byte protocolHeader;
    public byte protocolVersion;
    public byte requestID;
    public byte errorStatus;
    public byte errorIndex;
    public byte dataGetReq;
    public byte dataGetReqLen;
    public byte bindingLen;
    public byte objectLen;
    public byte objectDataLen;
    
    
    public SNMPImplementation() {
        protocolHeader=0x30;
        protocolVersion=0x00;
        requestID=(byte) Functions.random.nextInt(127);
        errorIndex=0x00;
        errorStatus=0x00;
        dataGetReq=(byte) 0xa0;
        dataGetReqLen=0x19;
        bindingLen=0x0e;
        objectLen=0x08;
        objectDataLen=0x06;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 42)
            return len;
        
        for(int i = offset + len - 1; i >= offset; i--)
            data[i + 42] = data[i];
        int index = offset;
        data[index++]=protocolHeader;
        byte sizeOfPacket=(byte)(40+len);
        data[index++]=sizeOfPacket;
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=protocolVersion;
        
        data[index++]=0x04; data[index++]=0x06;
        /**Community Public started**/
        data[index++]=0x70;data[index++]=0x75;data[index++]=0x62;
        data[index++]=0x6c;data[index++]=0x69;data[index++]=0x63;
        /** Community Public finished**/
        data[index++]=dataGetReq;
        data[index++]=dataGetReqLen;
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        
        data[index++]=requestID;
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        
        data[index++]=errorStatus;
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=errorIndex;
        /** Variable Binding started**/
        data[index++]=protocolHeader;
        data[index++]=bindingLen;
        data[index++]=protocolHeader;
        data[index++]=(byte) (bindingLen-(byte)2);
        data[index++]=0x06;
        data[index++]=objectLen;
        data[index++]=0x2b;
        data[index++]=objectDataLen;
        
        for(int i=0;i<objectDataLen;i++){
            data[index++]=(byte) Functions.random.nextInt(10);
        }
        data[index++]=0x05;
        data[index++]=0x00; 
        data[index++]=0x00;data[index++]=0x00;
        
        requestID=(byte) Functions.random.nextInt(127);
        return index+len;
                
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i < offset + len;i++)
        {
            data[i] = data[i + 51];
        }
        return len - 51;
        
    }
    
    
}
