/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author User
 */
public class BasicTcpImplementation {
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 2)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 2] = data[i];

        int index=offset;
        Functions.putShort2(data,index, (short) len);
        index+=2;
        return index+len;
    }

    public int decodePacket(byte [] data, int offset, InputStream is) throws IOException {
        int createLen=Utility.buildLen2(is);
        if(createLen<0) return createLen;
        is.read(data,offset,createLen-2);
        return createLen-2;
    }
}

