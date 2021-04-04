/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpprotocols;

import utils.Functions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import utils.Utility;

/**
 *
 * @author User
 */
public class CQLImplementation {
    
    private int createLen;
    private int streamCode;
    private static byte[] messageData1=Utility.hexStringToByteArray("0002000b434f4d5052455353494f4e00020006736e6170707900036c7a34000b43514c5f56455253494f4e00010005332e332e31");
    private static byte[] resultRowAndCol={0x00,0x00,0x00,0x01,0x00,0x00,0x00,0x01,0x00,0x00,0x00,0x01};
    public int querySize;
    
    
    public CQLImplementation() {
        streamCode=3;
   }

    public boolean cqlHandshakeAtServer(Socket socket){
        byte data[]=new byte[1024];
        int index=0,offset=0;
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
        index=getBaseStructure(data, index, (byte)132, (short) streamCode, (byte)8, len+18);
        System.arraycopy(resultRowAndCol, 0, data, index, resultRowAndCol.length);
        index+=resultRowAndCol.length;
        Functions.putShort2(data, index, (short)len);
        index+=2;
        index+=len;
        
//        CQL result rows
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        return len+27;
    }
    
    public int decodePacketAtServer(byte [] data, int offset, InputStream is) throws IOException{
            Functions.ignoreByte(is, 2);
            streamCode=Utility.readLen2(is);
            is.read();
            createLen=Utility.readLen4(is);
            querySize=Utility.readLen4(is);
            createLen=createLen-querySize-9;
            
            Functions.ignoreByte(is, querySize+3);
            
            is.read(data, offset, createLen);
            Functions.ignoreByte(is, 3);
//            System.out.print("---------------------create len at server----->  "+createLen);
            return createLen;
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