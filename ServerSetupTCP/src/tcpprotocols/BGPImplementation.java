/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpprotocols;

import java.io.InputStream;
import utils.Utility;
import utils.Functions;

/**
 *
 * @author User
 */
public class BGPImplementation {
    
    private byte packetTypeController;
    private int createLen;
    private byte[] messageHeader1;
    private short myAs,holdTime;
    
    public BGPImplementation() {
        packetTypeController=1;
        messageHeader1=Utility.hexStringToByteArray("ffffffffffffffffffffffffffffffff");
        myAs=(short) Utility.random.nextInt();
        holdTime=(short) Utility.random.nextInt();
    }

    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 34)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 34] = data[i]; 
        int index=offset;
        System.arraycopy(messageHeader1, 0, data, index, messageHeader1.length);
        index+=messageHeader1.length;
        switch(packetTypeController){
            case 1:
                index=setLengthAndType(data, index, (short)29, packetTypeController);
                packetTypeController=4;
                index=openMessage(data, index);
                break;
                
            case 4:
                index=setLengthAndType(data, index, (short)19, packetTypeController);
                packetTypeController=2;
                index=Functions.makeSameLength(data, index, 11); //make 30
                break;
                
            case 2:
                index=setLengthAndType(data, index, (short)23, packetTypeController);
                packetTypeController=1;
                index=updateMessage(data, index);
                index=Functions.makeSameLength(data, index, 7);
                break;
        }
        
        Functions.putInt4(data, index, len);
        index+=4;

        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 18);
            packetTypeController=(byte) is.read();
            Functions.ignoreByte(is, 11);
            createLen=Utility.buildLen4(is);
            is.read(data, offset, createLen);
            return createLen;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    
   public int openMessage(byte[] data, int offset){
       int index=offset;
       data[index++]=0x04;
       Functions.putShort2(data, index, myAs);
       index+=2;
       Functions.putShort2(data, index, holdTime);
       index+=2;
//       BGP Identifer
       data[index++]=(byte) 0xc0; data[index++]=(byte) 0xa8;
       data[index++]=(byte) (Utility.random.nextInt(250)+1);
       data[index++]=(byte) (Utility.random.nextInt(200)+1);
//       optional padding 
       data[index++]=0x00;
       data[index++]=0x00;
       
       myAs=(short) Utility.random.nextInt();
        holdTime=(short) Utility.random.nextInt();
       return index;
   }
   
   public int setLengthAndType(byte[] data, int offset,short length, byte type){
       int index=offset;
       Functions.putShort2(data, index, length);
       index+=2;
       data[index++]=type;
       
       return index;
   }
   
   public int updateMessage(byte[] data, int offset){
       int index=offset;
//       withdraw route len
       data[index++]=0x00; data[index++]=0x00; 
//       total path attribute len
       data[index++]=0x00; data[index++]=0x00; 
       
       return index;
   }

}

