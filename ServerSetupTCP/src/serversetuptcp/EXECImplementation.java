/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import static serversetuptcp.ServerSetupTCP.offset;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author User
 */
public class EXECImplementation {
    
    public EXECImplementation() {
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 7)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 7] = data[i];
        int index=offset;
        data[index++]=0x33; data[index++]=0x38;
        data[index++]=0x34; data[index++]=0x30; data[index++]=0x00;
        Functions.putShort2(data, index, (short) (len+7));
        index+=2;
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, InputStream is) throws IOException{
        Functions.ignoreByte(is,5);
        int createLen=Utility.buildLen2(is);
        if(createLen<0) return createLen;
        is.read(data,offset,createLen-7);
        return createLen-7;
    }
}

