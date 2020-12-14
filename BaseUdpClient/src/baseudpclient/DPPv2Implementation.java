package baseudpclient;

public class DPPv2Implementation {
    public byte dofNetworkHeader1;
    public byte flag1;
    public byte srcAddress1;
    public byte destAddress1;
    
    public byte dppv2NetworkHeader2;
    public byte flag2;
    public byte delay;
    
    public byte securityHeader;
    
    
    public DPPv2Implementation() {
        dofNetworkHeader1=(byte) 0x81;
        flag1=0x0c;
        srcAddress1=0x2;
        destAddress1=0x0c;
        dppv2NetworkHeader2=(byte) 0x82;
        flag2=(byte) 0x80;
        delay=(byte) Utility.random.nextInt(255);
        securityHeader=(byte) 0x80;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 10)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 10] = data[i];

        int index=offset;
        
        //DOF network version 1
        data[index++]=dofNetworkHeader1;
        data[index++]=flag1;
        data[index++]=srcAddress1;
        data[index++]=destAddress1;
        
//        DPPv2  protocol started
        data[index++]=dppv2NetworkHeader2;
        data[index++]=flag2;
        data[index++]=delay;
        data[index++]=securityHeader;
        
        data[index++]=0x00;
        data[index++]=0x00;
        
        
        delay=(byte) Utility.random.nextInt(255);
        
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        srcAddress1=data[offset+3];
        destAddress1=data[offset+2];
        for(int i=offset;i<offset+len-9;i++) data[i]=data[i+10];
        return len-10;
    }
    
}