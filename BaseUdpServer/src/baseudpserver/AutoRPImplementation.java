
package baseudpserver;

public class AutoRPImplementation {
    public static byte protocolVersion;
    public byte rpCount;
    public short holdTime;
    public static byte mData[];
    
    static{
        mData=Utility.hexStringToByteArray("bdb37708386c25540b1005804b000000000000280000000c000000010100000e");
        protocolVersion=(byte) 0xff;
    }
    
    public AutoRPImplementation() {
        rpCount=(byte) Utility.random.nextInt();
        holdTime=(short) Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 43)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 43] = data[i];

        int index=offset;
        data[index++]=protocolVersion;
        data[index++]=rpCount;
        Functions.putShort2(data, index, holdTime);
        index+=2;
//        Copy reserved data
        data[index++]=0x4f; data[index++]=(byte) 0xef;
        data[index++]=0x78; data[index++]=(byte) 0xc0;
        
        
        System.arraycopy(mData, 0, data, index, mData.length);
        index+=mData.length;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        rpCount=(byte) Utility.random.nextInt();
        holdTime=(short) Utility.random.nextInt();
        return 43+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
    	rpCount=data[offset+1];
    	holdTime=Functions.getShort2(data, offset+2);
        
        System.arraycopy(data, offset+43, data, offset, len-43);
        
        return len-43;
    }
    
}