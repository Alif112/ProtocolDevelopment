/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tftpserver;

import java.net.InetAddress;
import java.util.Random;

/**
 *
 * @author User
 */
public class SNMPImplementationServer {
    public byte protocolHeader;
    public byte protocolVersion;
    public byte responseID;
    public byte errorStatus;
    public byte errorIndex;
    public byte dataGetResponse;
    public byte dataGetResponseLen;
    public byte bindingLen;
    public byte objectLen;
    public byte objectDataLen;
    public byte octateLen;

    
    
    public SNMPImplementationServer() {
        protocolHeader=0x30;
        protocolVersion=0x00;
        dataGetResponse=(byte) 0xa2;
        dataGetResponseLen=0x21;
        errorStatus=0x00;
        errorIndex=0x00;
        bindingLen=0x16;
        objectLen=(byte) 0x0a;
        objectDataLen=0x06;
        octateLen=0x06;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 51)
            return len;
        
        for(int i = offset + len - 1; i >= offset; i--)
            data[i + 51] = data[i];
        int index = offset;
        data[index++]=protocolHeader;
        byte sizeOfPacket=(byte)(49+len);
        data[index++]=sizeOfPacket;
        
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=protocolVersion;
        data[index++]=0x04; data[index++]=0x06;
        /**Community Public started**/
        data[index++]=0x70;data[index++]=0x75;data[index++]=0x62;
        data[index++]=0x6c;data[index++]=0x69;data[index++]=0x63;
        /** Community Public finished**/
        
        data[index++]=dataGetResponse;
        data[index++]=dataGetResponseLen;
        /*** Separator***/
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=responseID;
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
            data[index++]=(byte) (byte) Functions.random.nextInt(10);
        }
        data[index++]=0x06;
        data[index++]=0x01;
        
        data[index++]=0x04;
        data[index++]=octateLen;
        data[index++]=0x08; data[index++]=0x00;
        for(int i=0;i<octateLen-2;i++){
            data[index++]=(byte) Functions.random.nextInt(10);
        }
        
        data[index++]=0x00;
        data[index++]=0x00;
        data[index++]=0x00;

        return index+len;
                
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        responseID=data[offset+17];
        
        for(int i=offset;i < offset + len;i++)
        {
            data[i] = data[i + 42];
        }
        return len - 42;
        
    }
    
    
}
