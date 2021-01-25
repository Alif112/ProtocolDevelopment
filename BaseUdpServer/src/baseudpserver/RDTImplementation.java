
package baseudpserver;

public class RDTImplementation {
    private short sequenceNumber;
    private int timeStamp;
    
    public RDTImplementation(){
       sequenceNumber=1;
       timeStamp=Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 8)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 8] = data[i];
        int index=offset;
        data[index++]=0x00;
        Functions.putShort2(data, index, sequenceNumber);
        index+=2;
        data[index++]=0x1d;
        Functions.putInt4(data, index, timeStamp);
        index+=4;
        
        sequenceNumber++;
        timeStamp=Utility.random.nextInt();
        return len+8;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        sequenceNumber=Functions.getShort2(data, offset+1);
        timeStamp=Functions.getInt4(data, offset+4);
        
        System.arraycopy(data, offset+8, data, offset, len-8);
        return len-8;
    }
    
}
