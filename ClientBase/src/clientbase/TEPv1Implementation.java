package clientbase;

public class TEPv1Implementation {
    public byte dofNetworkHeader1;
    public byte flag1;
    public byte srcAddress1;
    public byte destAddress1;
    
    public byte dofNetworkHeader2;
    public byte flag2;
    public byte operationID;
    public byte delay;

    public byte operation;
    
    public byte credentialType;
    public byte stage;
    
    public TEPv1Implementation() {
        dofNetworkHeader1=(byte) 0x81;
        flag1=0x0c;
        srcAddress1=0x12;
        destAddress1=0x01;
        
        dofNetworkHeader2=(byte) 0x82;
        flag2=0x20;
        operationID=0x04;
        delay=(byte) Utility.random.nextInt(255);
        
        operation=0x11;
        credentialType=0x01;
        stage=0x00;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 57)
            return len;
        for(int i = offset + len - 1; i >=offset+20; i--)
            data[i + 37] = data[i];
        
        int nonceLen=8;
        byte[] nonceData=new byte[nonceLen];
        for(int i=offset+19,j=7;i>=offset+12;i--,j--)
            nonceData[j]=data[i];
        
        int object2Len=6;
        byte[] object2Data=new byte[object2Len];
        for(int i=offset+11,j=5;i>=offset+6;i--,j--)
            object2Data[j]=data[i];

        int object1Len=6;
        byte[] object1Data=new byte[object1Len];
        for(int i=offset+5,j=5;i>=offset;i--,j--)
            object1Data[j]=data[i];
        
        
        int index=offset;
        
        //DOF network version 1
        data[index++]=dofNetworkHeader1;
        data[index++]=flag1;
        data[index++]=srcAddress1;
        data[index++]=destAddress1;
        
//        DOF network version 2
        data[index++]=dofNetworkHeader2;
        data[index++]=flag2;
        data[index++]=operationID;
        data[index++]=delay;
        
        //DOF TEPv1 started
        data[index++]=(byte) 0x80; data[index++]=(byte) 0x80;
        data[index++]=operation;
        data[index++]=0x06; data[index++]=0x06;
        
//        copy object 1
        System.arraycopy(object1Data, 0, data, index, object1Len);
        index+=object1Len;
        
        data[index++]=(byte) 0xb4;
        data[index++]=0x02; 
        data[index++]=0x60; data[index++]=0x01;
        data[index++]=0x03; 
        data[index++]=0x01; data[index++]=0x01; data[index++]=(byte) 0xf9;   
        
        //initiator request
        data[index++]=(byte) 0xc7;
        data[index++]=credentialType;
        data[index++]=stage;
            //copy object2
        data[index++]=0x03;
        data[index++]=0x06;
        System.arraycopy(object2Data, 0, data, index, object2Len);
        index+=object2Len;
            //copy nonce data
        System.arraycopy(nonceData, 0, data, index, nonceLen);
        index+=nonceLen;
//            permissions
        data[index++]=0x03; 
        data[index++]=0x01; data[index++]=0x01; data[index++]=0x1f;
        data[index++]=(byte) 0x80; data[index++]=(byte) 0x82;
        data[index++]=0x03; data[index++]=0x01; data[index++]=0x01;
        data[index++]=0x00; data[index++]=0x00;
        
        
        delay=(byte) Utility.random.nextInt(255);
        
        
        return index+len-20;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+8;i++) data[i]=data[i+117];
        for(int i=offset+8;i<offset+len-137;i++)data[i]=data[i+137];
        
        return len-137;
    }
    
}