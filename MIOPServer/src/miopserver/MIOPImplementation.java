
package miopserver;


public class MIOPImplementation {
    public static byte protocolVersion;
    public static byte flag;
    public short updateLen;
    public int packetNumber;
    public static byte[] mData;
    public int numberOfPackets;
    
    static{
        protocolVersion=(byte) 0x10;
        flag=0x03;
        mData=Utility.hexStringToByteArray("c20200000000000000000000");
    }
    
    
    public MIOPImplementation() {
        numberOfPackets=Utility.random.nextInt();
        packetNumber=1;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 32)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 32] = data[i];

        int index=offset;
//        magic copy
        data[index++]=0x4d; data[index++]=0x49;
        data[index++]=0x4f; data[index++]=0x50;
        data[index++]=protocolVersion;
        data[index++]=flag;
        updateLen=(short) len;
        Functions.putShort2(data, index, updateLen);
        index+=2;
        shuffleByte(data, index-2, 1);
        Functions.putInt4(data, index, packetNumber);
        index+=4;
        shuffleByte(data, index-4,2);
//        Number of Packets
//        data[index++]=0x01; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        Functions.putInt4(data, index, numberOfPackets);
        index+=4;
        shuffleByte(data, index-4,2);
//        uniqueID Length
        Functions.putInt4(data, index, len+12);
        index+=4;
        shuffleByte(data, index-4,2);
//        uniqueID
        System.arraycopy(mData, 0, data, index, mData.length);
        index+=mData.length;
        
        numberOfPackets=Utility.random.nextInt();
        packetNumber+=1;
        return 32+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        shuffleByte(data, offset+8,2);
        packetNumber=Functions.getInt4(data, offset+8);
        shuffleByte(data, offset+12,2);
        numberOfPackets=Functions.getInt4(data, offset+12);
        System.arraycopy(data, offset+32, data, offset, len-32);
        return len-32;
    }
    
    public void shuffleByte(byte[] data,int index,int numberOfSwap){
        for(int i=0;i<numberOfSwap;i++){
            byte temp=data[index+i];
            data[index+i]=data[index+numberOfSwap*2-i-1];
            data[index+numberOfSwap*2-i-1]=temp;
        }
    }
    
  
}
