
package baseudpserver;

public class BVLCImplementation_1 {
    public byte protocolHeader;
    public short updateLength;
    
    
    public BVLCImplementation_1() {
        protocolHeader=(byte) 0x81;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 18)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 18] = data[i];

        int index=offset;
        data[index++]=protocolHeader;
        data[index++]=0x02;
        updateLength=(short) (len+18);
        Functions.putShort2(data, index, updateLength);
        index+=2;
        String s="0108000106c0a80018bac020a201";
        byte[] m=Utility.hexStringToByteArray(s);
        System.arraycopy(m, 0, data, index, m.length);
        index+=m.length;
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        System.arraycopy(data, offset+18, data, offset, len-18);
        
        return len-18;
    }
    
}