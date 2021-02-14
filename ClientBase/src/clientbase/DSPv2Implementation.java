package clientbase;

public class DSPv2Implementation {
    public byte dofNetworkHeader1;
    public byte flag1;
    public byte srcAddress1;
    
    public byte dofNetworkHeader2;
    public byte flag2;
    public byte class2;
    
    public byte operationCount;
    public byte delay;
    public byte appID;
    public byte opCode;
    
    
    public DSPv2Implementation() {
        dofNetworkHeader1=(byte) 0x81;
        flag1=0x04;
        srcAddress1=0x12;
        dofNetworkHeader2=(byte) 0x82;
        flag2=0x60;
        class2=0x03;
        operationCount=0x03;
        delay=(byte) Utility.random.nextInt(255);
        appID=0x00;
        opCode=0x06;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 14)
            return len;
        for(int i = offset + len - 1; i >=offset+16; i--)
            data[i + 13] = data[i];
        
        int objectLen=16;
        byte[] objectData=new byte[objectLen];
        for(int i=offset+15,j=15;i>=offset;i--,j--)
            objectData[j]=data[i];
        
        
        int index=offset;
        
        //DOF network version 1
        data[index++]=dofNetworkHeader1;
        data[index++]=flag1;
        data[index++]=srcAddress1;
        
//        DOF network version 2
        data[index++]=dofNetworkHeader2;
        data[index++]=flag2;
        data[index++]=class2;
        
        data[index++]=(byte) objectLen;
        System.arraycopy(objectData, 0, data, index, objectLen);
        index+=objectLen;
        data[index++]=operationCount;
        data[index++]=delay;
        
        //DOF session protocol
        data[index++]=appID; data[index++]=opCode;
        data[index++]=0x00; data[index++]=0x00;
        
        
        delay=(byte) Utility.random.nextInt(255);
        
        
        return index+len-16;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        
        for(int i=offset;i<offset+16;i++) data[i]=data[i+8];
        for(int i=offset+16;i<offset+len-21;i++)data[i]=data[i+19];
        
        return len-21;
    }
    
}