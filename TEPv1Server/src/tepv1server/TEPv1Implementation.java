
package tepv1server;

public class TEPv1Implementation {
    public byte dofNetworkHeader1;
    public byte flag1;
    public byte srcAddress1;
    public byte destAdress;
    
    public byte dofNetworkHeader2;
    public byte flag2;
    public byte operationID;
    public byte operation;
    
    public byte appID;
    
    public byte credentialType;
    public byte stage;
    
    public TEPv1Implementation() {
        dofNetworkHeader1=(byte) 0x81;
        flag1=0x0c;
        srcAddress1=0x01;
        destAdress=0x12;
        
        dofNetworkHeader2=(byte) 0x82;
        flag2=0x50;
        operationID=0x04;
        
        operation=0x01;
        appID=0x00;
        credentialType=0x01;
        stage=0x00;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 138)
            return len;
        for(int i = offset + len - 1; i >=offset+8; i--)
            data[i + 137] = data[i];
        
        
        int nonceLen=8;
        byte[] nonceData=new byte[nonceLen];
        for(int i=offset+7,j=7;i>=offset;i--,j--)
        nonceData[j]=data[i];
        
        int index=offset;
        //DOF network version 1
        data[index++]=dofNetworkHeader1;
        data[index++]=flag1;
        data[index++]=srcAddress1;
        data[index++]=destAdress;
        
//        DOF network version 2
        data[index++]=dofNetworkHeader2;
        data[index++]=flag2;
        data[index++]=operationID;
        
        //DOF TEPv1 protocol
        data[index++]=(byte) 0x80; data[index++]=(byte) 0x80;
        data[index++]=operation;
            //mac
        String mac="d47a85e073484b94239217a9d3957f920b637d659212f49387c64e68858777e8";
        byte macData[]=Utility.hexStringToByteArray(mac);
        System.arraycopy(macData, 0, data, index, macData.length);
        index+=macData.length;
            //key
        String key="b9d5ddc8ebd19697d7b780e5e3e3daae7363dbf23bf228f99a8bc05012748166";
        byte keyData[]=Utility.hexStringToByteArray(key);
        System.arraycopy(keyData, 0, data, index, keyData.length);
        index+=keyData.length;
        
            //ticket
        String ticket="f49c8ba80e1649e69e96e9f0268680d9682ba36093f7117e78fbc27b09255492";
        byte ticketData[]=Utility.hexStringToByteArray(ticket);
        System.arraycopy(ticketData, 0, data, index, ticketData.length);
        index+=ticketData.length;
        
//            responder Initialization
        data[index++]=0x00;
        data[index++]=0x01; data[index++]=(byte) 0x89;
        
            //responder Block
        data[index++]=0x07;
        data[index++]=credentialType;
        data[index++]=stage;
//             object
        data[index++]=(byte) 7f;
        data[index++]=0x03;
//        append 3data
        data[index++]=(byte) 0xbf; data[index++]=(byte) 0xfb; data[index++]=(byte) 0xff;  
//        copy nonce
        System.arraycopy(nonceData, 0, data, index, nonceLen);
        index+=nonceLen;
//        data[index++]=0x20; data[index++]=0x0e; data[index++]=(byte) 0x86; data[index++]=(byte) 0x9b;
//        data[index++]=0x1d; data[index++]=0x69; data[index++]=(byte) 0xc2; data[index++]=0x6c;
        
        
        //permission set
        data[index++]=0x03;
        data[index++]=0x01; data[index++]=0x01; data[index++]=0x01;
        data[index++]=(byte) 0x80; data[index++]=(byte) 0x80;
        data[index++]=0x03; data[index++]=0x01; data[index++]=0x01;
        
//        Authenticator Initialization
        data[index++]=(byte) 0xb4;
        data[index++]=0x01;
        data[index++]=(byte) 0xff; data[index++]=(byte) 0xff; data[index++]=(byte) 0xff;
        data[index++]=0x01; data[index++]=0x00;
        data[index++]=0x01; data[index++]=0x00;
        
        
        data[index++]=0x00; data[index++]=0x00;
        
        
        return index+len-8;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+6;i++) data[i]=data[i+13];
        for(int i=offset+6;i<offset+20;i++) data[i]=data[i+26];
        for(int i=offset+20;i<offset+len-37;i++) data[i]=data[i+37];
        
        return len-37;
    }
    
}
