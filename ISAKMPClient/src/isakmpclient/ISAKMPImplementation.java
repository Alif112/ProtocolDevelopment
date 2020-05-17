
package isakmpclient;

public class ISAKMPImplementation {
    public long initiatorSPI;
    public long responderSPI;
    public byte nextPayload;
    public byte version;
    public byte exchangeType;
    public byte flags;
    public int messageID;
    public int fullLength;
    public short payLoadLength;
    public int initiatorVector;
    
    
    public ISAKMPImplementation(boolean b) {
        initiatorSPI=Utility.random.nextLong();
        responderSPI=Utility.random.nextLong();
        nextPayload=0x2e;
        version=0x20;
        exchangeType=0x23;
        if(b)flags=0x08;
        else flags=0x23;
        messageID=Utility.random.nextInt();
        initiatorVector=Utility.random.nextInt();
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 36)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 36] = data[i];

        int index=offset;
        
        Functions.putLong8(data, index, initiatorSPI);
        index+=8;
        Functions.putLong8(data, index, responderSPI);
        index+=8;
        
        data[index++]=nextPayload;
        data[index++]=version;
        data[index++]=exchangeType;
        data[index++]=flags;
        
        Functions.putInt4(data, index, messageID);
        index+=4;
        fullLength=len+36;
        
        Functions.putInt4(data, index, fullLength);
        index+=4;
        data[index++]=nextPayload;
        data[index++]=0x00; //critical bit
        
        payLoadLength=(short) (len+8);
        Functions.putShort2(data, index, payLoadLength);
        index+=2;
        
        Functions.putInt4(data, index, initiatorVector);
        index+=4;
        
        
        initiatorSPI=Utility.random.nextLong();
        responderSPI=Utility.random.nextLong();
        messageID=Utility.random.nextInt();
        initiatorVector=Utility.random.nextInt();
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        initiatorSPI=Functions.getLong8(data, offset);
        responderSPI=Functions.getLong8(data, offset+8);
        messageID=Functions.getInt4(data, offset+20);
        initiatorVector=Functions.getInt4(data, offset+32);
        
        System.arraycopy(data, offset+36, data, offset, len-36);
        
        return len-36;
    }
    
}