package baseudpclient;
public class LoWPANImplementation {
    public short deviceID;
    public static byte protocolVersion;
    public static byte type;
    public static byte channelID;
    public static byte mode;
    public int zigBeeSequenceNumber;
    public static byte length;
    public static byte[] zeroPadding;
    //IEEE part
    public int frameControlField;
    public byte ieeeSequenceNumber;
    public static short datagramTag;
    public static byte datagramOffset;
    byte[] extendedSourceData;
    byte[] extendedDestinationData;
    byte[] timeStampData;

    static {
        type=0x01;
        channelID=0x00;
        protocolVersion=0x02;
        zeroPadding=Utility.hexStringToByteArray("00000000000000000000");
        mode=0x01;
        length=0x7f;
        datagramTag=2;
        datagramOffset=(byte) 0xfc;
    }
    
    
    public LoWPANImplementation() {
//        protocolID=17752;
        
//        deviceID=0;
        deviceID=(short) Utility.random.nextInt();
        zigBeeSequenceNumber=Utility.random.nextInt();
        ieeeSequenceNumber=(byte) Utility.random.nextInt(255);
        extendedSourceData=new byte[8];
        extendedDestinationData=new byte[8];
        timeStampData=new byte[8];
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len +58)
            return len;
        for(int i = offset + len - 1; i >=offset+24; i--)
            data[i + 34] = data[i];
        int index=offset;
        
        synchronized(this){        
            for(int i=offset+23,j=7;i>=offset+16;i--,j--)
                extendedSourceData[j]=data[i];
            
            for(int i=offset+15,j=7;i>=offset+8;i--,j--)
                extendedDestinationData[j]=data[i];
            
            for(int i=offset+7,j=7;i>=offset;i--,j--)
                timeStampData[j]=data[i];
            
            
            /**Zigbee protocol Started**/
    //        Functions.putShort2(data, index, protocolID);
    //        index+=2;
            data[index++]=0x45; data[index++]=0x58; //ProtocolID
            data[index++]=protocolVersion;
            data[index++]=type;
            data[index++]=channelID;
            Functions.putShort2(data, index, deviceID);
            index+=2;
            data[index++]=mode;
            
            data[index++]=(byte) 0xff;
            System.arraycopy(timeStampData, 0, data, index, 8);
            index+=8;
            Functions.putInt4(data, index, zigBeeSequenceNumber);
            index+=4;
            //zero padding
            System.arraycopy(zeroPadding, 0, data, index, zeroPadding.length);
            index+=zeroPadding.length;
            
            data[index++]=length;
            
            /**Zigbee protocol finished**/
            
            /**IEEE protocol Started**/
            //header
            data[index++]=0x41;
            data[index++]=(byte) 0xcc;
            data[index++]=ieeeSequenceNumber;
            data[index++]=(byte) 0xff;
            data[index++]=(byte) 0xff;
            
            System.arraycopy(extendedDestinationData, 0, data, index, 8);
            index+=8;
            System.arraycopy(extendedSourceData, 0, data,index, 8);
            index+=8;
            
        /**IEEE protocol Finished**/
        //6LoWPAN started
            data[index++]=(byte) 0xe1;
            data[index++]=0x09;
            Functions.putShort2(data, index, datagramTag);
            index+=2;
            data[index++]=datagramOffset;
        }
        ieeeSequenceNumber=(byte) Utility.random.nextInt(255);
        zigBeeSequenceNumber=Utility.random.nextInt();
        
        return len+34;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
//       zigBeeSequenceNumber=Functions.getInt4(data, offset+17);
//       ieeeSequenceNumber=data[offset+34];
        
        
        for(int i=offset;i<offset+8;i++) data[i]=data[i+9];
        for(int i=offset;i<offset+16;i++) data[i+8]=data[i+37];
//        for(int i=offset;i<offset+8;i++) data[i+16]=data[i+45];
        for(int i=offset;i<offset+len-58;i++) data[i+24]=data[i+58];
        
        
        return len-34;
    }
    
}