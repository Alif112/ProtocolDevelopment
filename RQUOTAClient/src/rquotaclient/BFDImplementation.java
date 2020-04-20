
package rquotaclient;

public class BFDImplementation {
//    length range is 26<=len<=250
    public byte protocolHeader;
    public byte messageFlag;
    public int packetLen;
    public byte passwordLen;
    
    
    public BFDImplementation() {
        protocolHeader=0x20;
        messageFlag=0x44;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 27)
            return len;
        for(int i = offset + len - 1; i >=offset+20; i--)
            data[i + 7] = data[i];
        int smallLen=20;
        byte[] smallData=new byte[smallLen];
        for(int i=offset+19,j=19;i>=0;i--,j--)
            smallData[j]=data[i];
        
        int index=offset;
        
        data[index++]=protocolHeader;
        data[index++]=messageFlag;
        packetLen=len+7;
        Functions.putInt(data, index, packetLen);
        index+=2;
        
        System.arraycopy(smallData, 0, data, index, 20);
        index+=20;
        data[index++]=0x01;
        passwordLen=(byte) (len-17);
        data[index++]=passwordLen;
        data[index++]=02;

        return index+len-20;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        for(int i=offset;i<offset+20;i++)
            data[i]=data[i+4];
        for(int i=offset+20;i<offset+len-1;i++)
            data[i]=data[i+7];
        
        return len-7;
    }
    
}
