package baseudpclient;
public class LoWPANImplementation {
    public int protocolID;
    public byte protocolVersion;
    public byte type;
    public byte channelID;
    public int deviceID;
    public byte mode;
    public long zigBeeSequenceNumber;
    public byte length;
    //IEEE part
    public int frameControlField;
    public byte ieeeSequenceNumber;
    
    public int datagramTag;
    public byte datagramOffset;
    
    public LoWPANImplementation() {
        protocolID=17752;
        protocolVersion=0x02;
        type=0x01;
        channelID=0x00;
        deviceID=0;
        mode=0x01;
        zigBeeSequenceNumber=Utility.random.nextInt();
        length=0x7f;
        ieeeSequenceNumber=(byte) Utility.random.nextInt(255);
        
        datagramTag=2;
        datagramOffset=(byte) 0xfc;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len +58)
            return len;
        for(int i = offset + len - 1; i >=offset+24; i--)
            data[i + 34] = data[i];
        
        int extendedSourceLen=8;
        byte[] extendedSourceData=new byte[extendedSourceLen];
        for(int i=offset+23,j=7;i>=offset+16;i--,j--)
            extendedSourceData[j]=data[i];
        
        int extendedDestinationLen=8;
        byte[] extendedDestinationData=new byte[extendedDestinationLen];
        for(int i=offset+15,j=7;i>=offset+8;i--,j--)
            extendedDestinationData[j]=data[i];
        
        int timeStampLen=8;
        byte[] timeStampData=new byte[timeStampLen];
        for(int i=offset+7,j=7;i>=offset;i--,j--)
            timeStampData[j]=data[i];
        
        int index=offset;
        /**Zigbee protocol Started**/
        Functions.putInt(data, index, protocolID);
        index+=2;
        data[index++]=protocolVersion;
        data[index++]=type;
        data[index++]=channelID;
        Functions.putInt(data, index, deviceID);
        index+=2;
        data[index++]=mode;
        
        data[index++]=(byte) 0xff;
        System.arraycopy(timeStampData, 0, data, index, timeStampLen);
        index+=timeStampLen;
        Functions.putLong(data, index, zigBeeSequenceNumber);
        index+=4;
        //zero padding
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00;
        
        
        data[index++]=length;
        
        /**Zigbee protocol finished**/
        
        /**IEEE protocol Started**/
        //header
        data[index++]=0x41;
        data[index++]=(byte) 0xcc;
        data[index++]=ieeeSequenceNumber;
        data[index++]=(byte) 0xff;
        data[index++]=(byte) 0xff;
        
        System.arraycopy(extendedDestinationData, 0, data, index, extendedDestinationLen);
        index+=extendedDestinationLen;
        System.arraycopy(extendedSourceData, 0, data,index, extendedSourceLen);
        index+=extendedSourceLen;
        /**IEEE protocol Finished**/
        
        //6LoWPAN started
        data[index++]=(byte) 0xe1;
        data[index++]=0x09;
        Functions.putInt(data, index, datagramTag);
        index+=2;
        data[index++]=datagramOffset;
        
        
        
        ieeeSequenceNumber=(byte) Utility.random.nextInt(255);
        zigBeeSequenceNumber=Utility.random.nextInt();
        
        return index+len-24;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        zigBeeSequenceNumber=Functions.getLong(data, offset+17);
        ieeeSequenceNumber=data[offset+34];
        
        
        for(int i=offset;i<offset+8;i++) data[i]=data[i+9];
        for(int i=offset;i<offset+8;i++) data[i+8]=data[i+37];
        for(int i=offset;i<offset+8;i++) data[i+16]=data[i+45];
        for(int i=offset;i<offset+len-58;i++) data[i+24]=data[i+58];
        
        
        return len-34;
    }
    
}