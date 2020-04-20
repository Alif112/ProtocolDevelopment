
package tepv1server;

public class TEPv1Implementation {
    public byte dofNetworkHeader1;
    public byte flag1;
    public byte srcAddress1;
    public byte destAdress;
    
    public byte dofNetworkHeader2;
    public byte flag2;
    public byte class2;
    public byte operationCount;
    
    public byte appID;
    public byte opCode;
    
    
    public TEPv1Implementation() {
        dofNetworkHeader1=(byte) 0x81;
        flag1=0x0c;
        srcAddress1=0x01;
        
        dofNetworkHeader2=(byte) 0x82;
        flag2=0x70;
        class2=0x03;
        
        operationCount=0x03;
        appID=0x00;
        opCode=0x07;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 20)
            return len;
        for(int i = offset + len - 1; i >=offset+16; i--)
            data[i + 19] = data[i];
        
        int objectLen=16;
        byte[] objectData=new byte[objectLen];
        for(int i=offset+15,j=15;i>=offset;i--,j--)
            objectData[j]=data[i];
        
        
        int index=offset;
        //DOF network version 1
        data[index++]=dofNetworkHeader1;
        data[index++]=flag1;
        data[index++]=srcAddress1;
        data[index++]=destAdress;
        
//        DOF network version 2
        data[index++]=dofNetworkHeader2;
        data[index++]=flag2;
        data[index++]=class2;
        
        data[index++]=(byte) objectLen;
        System.arraycopy(objectData, 0, data, index, objectLen);
        index+=objectLen;
        data[index++]=operationCount;
        
        //DOF session protocol
        data[index++]=appID; data[index++]=opCode;
        String m="808080818082e001";
        byte[] mData=Utility.hexStringToByteArray(m);
        System.arraycopy(mData, 0, data, index, mData.length);
        index+=mData.length;
        
        index=index+len-16;
        
        data[index++]=0x00; data[index++]=0x00;
        
        
        return index;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+6;i++) data[i]=data[i+13];
        for(int i=offset+6;i<offset+20;i++) data[i]=data[i+26];
        for(int i=offset+20;i<offset+len-37;i++) data[i]=data[i+37];
        
        return len-37;
    }
    
}
