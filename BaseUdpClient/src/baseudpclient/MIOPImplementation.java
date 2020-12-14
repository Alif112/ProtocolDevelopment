
package baseudpclient;

public class MIOPImplementation {
    public byte protocolVersion;
    public byte flag;
    public short updateLen;
    public int packetNumber;
    
    public MIOPImplementation() {
        protocolVersion=(byte) 0x10;
        flag=0x03;
        packetNumber=Utility.random.nextInt();
        
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
        byte temp=data[index-2]; data[index-2]=data[index-1]; data[index-1]=temp;
        Functions.putInt4(data, index, packetNumber);
        index+=4;
        temp=data[index-1]; data[index-1]=data[index-4]; data[index-4]=temp;
        temp=data[index-2]; data[index-2]=data[index-3]; data[index-3]=temp;
//        Number of Packets
        data[index++]=0x01; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
//        uniqueID Length
        data[index++]=0x0c; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
//        uniqueID
        String m="c20200000000000000000000";
        byte[] mData=Utility.hexStringToByteArray(m);
        System.arraycopy(mData, 0, data, index, mData.length);
        index+=mData.length;
        
        packetNumber=Utility.random.nextInt();
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        byte mCopy[]=new byte[4];
        for(int i=0,j=offset+11;i<4;i++,j--) mCopy[i]=data[j];
        packetNumber=Functions.getInt4(mCopy,0);
        System.arraycopy(data, offset+32, data, offset, len-32);
        return len-32;
    }
  
}