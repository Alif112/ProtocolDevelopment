package baseudpclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class UDP100Implementation {

    public int createPacket(byte [] data, int offset, int len,byte[] header,int headerLen){
        if(data.length <= offset + len + headerLen)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + headerLen] = data[i];
        
        int index=offset;
        System.arraycopy(header, 0, data, index, headerLen);
        index+=headerLen;
        return headerLen+len;
    }

    public int decodePacket(byte [] data, int offset, int len,int headerLen){
        System.arraycopy(data, offset+headerLen-1, data, offset, len-headerLen);

        return len-headerLen;
    }

}
