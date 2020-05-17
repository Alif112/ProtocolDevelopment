package messengerclient;

public class CoAPImplementation {
    public byte version;
    public byte code;
    public short messageID;
    public int tokenID;
    
    public CoAPImplementation() {
        version=0x44;
        code=0x02;
        messageID= (short) Utility.random.nextInt();
        tokenID=Utility.random.nextInt();
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 13)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 12] = data[i];

        
        int index=offset;
        data[index++]=version;
        data[index++]=code;
        Functions.putShort2(data, index, messageID);
        index+=2;
        Functions.putInt4(data, index, tokenID);
        index+=4;
        
        data[index++]=(byte) 0xc1; data[index++]=0x3c;
        data[index++]=(byte) 0xff; data[index++]=0x00;
        
        
        messageID= (short) Utility.random.nextInt();
        tokenID=Utility.random.nextInt();
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+len-10;i++) data[i]=data[i+10];
        
        return len-10;
    }
    
}