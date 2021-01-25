/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsetuptcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class CQLImplementation {
    
    private short streamCode;
    private int createLen;
    private byte[] messageHeader1,messageHeader2;
    
    public CQLImplementation() {
        streamCode=3;
        messageHeader1=Utility.hexStringToByteArray("0001000b43514c5f56455253494f4e0005");
        messageHeader2=Utility.hexStringToByteArray("0003000f544f504f4c4f47595f4348414e4745000d5354415455535f4348414e4745000d534348454d415f4348414e4745");
        
   }
    
    
    public boolean cqlHandshakeAtClient(Socket socket){
        byte[] data=new byte[1024];
        int index=0,offset=0;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            
            index=getBaseStructure(data, offset, (byte)4, (short)0, (byte)5, 0);
            os.write(data, offset, index);
            is.read(data, offset, 61);
            
            index=getBaseStructure(data, offset, (byte)4, (short)1, (byte)1, 22);
            System.arraycopy(messageHeader1, 0, data, index, messageHeader1.length);
            index+=messageHeader1.length;
            //version creation
            data[index++]=0x33; data[index++]=0x2e;
            data[index++]=0x33; data[index++]=0x2e;
            data[index++]=0x31;
            os.write(data, offset, index);
            is.read(data, offset, 9);
            
            index=getBaseStructure(data, offset, (byte)4, (short)2, (byte)11, 49);
            System.arraycopy(messageHeader2, 0, data, index, messageHeader2.length);
            index+=messageHeader2.length;
            os.write(data, offset, index);
            is.read(data, offset, 9);
            
            System.out.println("\n CQL HandShake Successfull at client!!\n");
            return true;
        } catch (IOException ex) {
            System.out.println("CQL Core HandShake Failed");
            ex.printStackTrace();
        }
        return false;
        
        
    }
    

    public int createPacketAtClient(byte [] data, int offset, int len){
        if(data.length <= offset + len + 16)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 13] = data[i]; 
        int index=offset;
        index=getBaseStructure(data, index, (byte)4, streamCode, (byte)7, len+7);
        Functions.putInt4(data, index, len);
        index+=4;
        index+=len;
        data[index++]=0x00; data[index++]=0x01; 
        data[index++]=0x00; 
        streamCode++;
        return index;
    }
    
    public int decodePacketAtClient(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 21);
            createLen=Utility.buildLen2(is);
            is.read(data, offset, createLen);
            Functions.ignoreByte(is, 4);
            
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