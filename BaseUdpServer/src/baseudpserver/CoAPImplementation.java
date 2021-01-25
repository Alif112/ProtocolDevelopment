
package baseudpserver;

public class CoAPImplementation {
    public byte version;
    public byte code;
    public short messageID;
    public int tokenID;
    
    public CoAPImplementation() {
        version=0x64;
        code=(byte) 0x85;
        messageID= (short) Utility.random.nextInt();
        tokenID=Utility.random.nextInt();
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 11)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 10] = data[i];

        
        int index=offset;
        data[index++]=version;
        data[index++]=code;
        Functions.putShort2(data, index, messageID);
        index+=2;
        Functions.putInt4(data, index, tokenID);
        index+=4;
        data[index++]=0x00; data[index++]=0x00;
        
        return len+10;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        messageID=Functions.getShort2(data, offset+2);
        tokenID=Functions.getInt4(data, offset+4);
        
        for(int i=offset;i<offset+len-12;i++) data[i]=data[i+12];
        return len-12;
    }
    
}