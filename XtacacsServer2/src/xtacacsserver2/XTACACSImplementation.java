
package xtacacsserver2;

import java.net.Inet4Address;

public class XTACACSImplementation {
    public byte type;
    public int result1;
    public int result2;
    public short line;
    public short result3;
    
    
    public XTACACSImplementation(boolean  b) {
        if(b) type=0x03;
        else type=0x02;
        result1=Utility.random.nextInt();
        result2=Utility.random.nextInt();
        line=(short) Utility.random.nextInt();
        result3=(short) Utility.random.nextInt();
        
    }
    
    public int createPacket(byte [] data, int offset, int len, Inet4Address destAddress, int destPort){
        if(data.length <= offset + len + 41)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 41] = data[i];

        int index=offset;
        data[index++]=0x30;
        data[index++]=type;
        data[index++]=0x02; data[index++]=0x01;
//        username and pass len
        data[index++]=0x06; data[index++]=0x06;
//        response and reason
        data[index++]=0x01; data[index++]=0x01;
        Functions.putInt4(data, index, result1);
        index+=4;
        System.arraycopy(destAddress.getAddress(), 0, data, index, 4);
        index += 4;
        //port is 2 byte
        Functions.putInt(data, index, destPort); 
        index+=2;
        //copy line
        Functions.putShort2(data, index, line);
        index+=2;
//        result2 and result3
        Functions.putInt4(data, index, result2);
        index+=4;
        Functions.putShort2(data, index, result3);
        index+=2;
        String uPass="676f6f676c65736563726574";
        byte[] uPassData=Utility.hexStringToByteArray(uPass);
        System.arraycopy(uPassData, 0, data, index, uPassData.length);
        index+=uPassData.length;
//        zero padding
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        result1=Utility.random.nextInt();
        result2=Utility.random.nextInt();
        line=(short) Utility.random.nextInt();
        result3=(short) Utility.random.nextInt();
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        result1=Functions.getInt4(data, offset+8);
        line=Functions.getShort2(data, offset+18);
        result2=Functions.getInt4(data, offset+20);
        result3=Functions.getShort2(data, offset+24);
        System.arraycopy(data, offset+41, data, offset, len-41);
        return len-41;
        
//        Slimp3 decoder
        
//        System.arraycopy(data, offset+21, data, offset, len-21);
//        return len-21;
    }
    
}