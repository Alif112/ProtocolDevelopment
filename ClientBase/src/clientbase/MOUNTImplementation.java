package clientbase;


public class MOUNTImplementation {
    public long xID;
    public long messageType;
    public long rpcVersion;
    public long mountHeader;
    public long programVersion;
    public long rpcProcedure;
    public long flavor;
    public long credentialLen;
    public long uID;
    public long gID;
    public long numberOfAuxiliaryGID;
    public long mountServiceLen;
    
    public MOUNTImplementation() {
        xID=Functions.random.nextInt();
        messageType=0;
        rpcVersion=2;
        mountHeader=100005;
        programVersion=1;
        rpcProcedure=1;
        flavor=1;
        credentialLen=52;
        uID=0;
        gID=1;
        numberOfAuxiliaryGID=5;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 96)
            return len;
        for(int i = offset + len - 1; i >=offset+24; i--)
            data[i + 72] = data[i];
        
        int auxiliaryGIDLen=20;
        byte[] auxiliaryGIDData=new byte[auxiliaryGIDLen];
        for(int i=offset+23,j=19;i>=offset+4;i--,j--)
            auxiliaryGIDData[j]=data[i];
        
        int stampLen=4;
        byte[] stampData=new byte[stampLen];
        for(int i=offset+3,j=3;i>=offset;i--,j--) 
            stampData[j]=data[i];
        
        
        int index=offset;
        Functions.putLong(data, index, xID);
        index+=4;
//        data[index++]=0x38;
//        data[index++]=0x41;
//        data[index++]=0x16;
//        data[index++]=(byte) 0x9f;
        
        Functions.putLong(data, index, messageType);
        index+=4;
        Functions.putLong(data, index, rpcVersion);
        index+=4;
        Functions.putLong(data, index, mountHeader);
        index+=4;
        Functions.putLong(data, index, programVersion);
        index+=4;
        Functions.putLong(data, index, rpcProcedure);
        index+=4;
        Functions.putLong(data, index, flavor);
        index+=4;
        Functions.putLong(data, index, credentialLen);
        index+=4;
        
        System.arraycopy(stampData, 0, data, index, stampLen);
        index+=stampLen;
        String machine="00000009776572726d73636865000000";
        byte[] mbyte=Utility.hexStringToByteArray(machine);
        
        System.arraycopy(mbyte, 0, data, index, mbyte.length);
        index+=mbyte.length;
        
        Functions.putLong(data, index, uID);
        index+=4;
        Functions.putLong(data, index, gID);
        index+=4;
        
        Functions.putLong(data, index, numberOfAuxiliaryGID);
        index+=4;
        
        System.arraycopy(auxiliaryGIDData, 0, data, index, auxiliaryGIDLen);
        index+=auxiliaryGIDLen;
        
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        mountServiceLen=len-24;
        Functions.putLong(data, index, mountServiceLen);
        index+=4;
        
        xID=Functions.random.nextInt();
        return index+len-24;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        for(int i=offset;i<offset+len-28;i++) data[i]=data[i+28];
        
        return len-28;
    }
    
}
