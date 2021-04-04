/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpprotocols;

import utils.Functions;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author User
 */
public class NineP2000Implementation {
    public byte messageVersion;
    
    public NineP2000Implementation(boolean isClient) {
        if(isClient) messageVersion=0x64;
        else messageVersion=0x65;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 19)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 19] = data[i];

        int index=offset;
        int newLen=len+19;
        Functions.putInt4(data, index, newLen);
        byte temp=data[index];
        data[index]=data[index+3];
        data[index+3]=temp;                    
        temp=data[index+1];
        data[index+1]=data[index+2];
        data[index+2]=temp;
        index+=4;
        
        data[index++]=messageVersion;
        data[index++]=(byte) 0xff; data[index++]=(byte) 0xff;
        //max message size
        data[index++]=0x18; data[index++]=0x20;
        data[index++]=0x00; data[index++]=0x00;
//        create version 
        data[index++]=0x06; data[index++]=0x00; //length 06
        data[index++]=0x39; data[index++]=0x50; data[index++]=0x32;
        data[index++]=0x30; data[index++]=0x30;data[index++]=0x30;

        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, InputStream is) throws IOException{
        
        try{
            int firstByte = is.read();
            int secondByte = is.read();
            int thirdByte=is.read();
            int fourthByte=is.read();

    //        System.out.println("firstbyte "+firstByte+" ---Second byte-- "+secondByte);
            int length=firstByte;
            length=length|secondByte<<8;
            length=length|thirdByte<<16;
            length=length|fourthByte<<24;
//            System.out.println("Receiving data of length: " + length);
           if(length<0) throw new Exception("receive completed");

            Functions.ignoreByte(is, 15);
            is.read(data, offset, length-19);

            return length-19;
        }catch(Exception e){e.printStackTrace();}
        return -1;

    }
//    public static final <T> void swap (T[] a, int i, int j) {
//        T t = a[i];
//        a[i] = a[j];
//        a[j] = t;
//    }
    
}