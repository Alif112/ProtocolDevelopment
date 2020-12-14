
package baseudpclient;

public class EDonkeyImplementation {
    public byte protocolHeader;
    public byte meesageType;
    public byte requestType;
    
    
    public EDonkeyImplementation() {
        protocolHeader=(byte) 0xe4;
        meesageType=0x19;
        requestType=0x04;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 35)
            return len;
        int fillLen=35-len;
        
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + fillLen] = data[i];

        int index=offset;
        data[index++]=protocolHeader;
        data[index++]=meesageType;
        data[index++]=requestType;
        data[index++]=(byte) len;
        fillLen-=4;
        for(int i=index;i<index+fillLen;i++)
            data[i]=(byte) Utility.random.nextInt();
        index+=fillLen;
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        int dataLen=(int)data[offset+3];
        System.arraycopy(data,offset+35-dataLen,data,offset,dataLen);
        return dataLen;
    }
    
}