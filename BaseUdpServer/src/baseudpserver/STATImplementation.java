
package baseudpserver;

public class STATImplementation {
    public long xID;
    public long messageType;
    public long rpcVersion;
    public long programName;
    public long programVersion;
    public long programProcedure;
    
    
    public STATImplementation() {
        xID=Functions.random.nextInt();
        messageType=0;
        rpcVersion=2;
        programName=100024;
        programVersion=1;
        programProcedure=2;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 64)
            return len;
        for(int i = offset + len - 1; i >=offset+20; i--)
            data[i + 24] = data[i];
        int privLen=20;
        byte[] privData=new byte[privLen];
        for(int i=offset+19,j=19;i>=offset;i--,j--)
            privData[j]=data[i];
        
        int index=offset;
        
        Functions.putLong(data, index, xID);
        index+=4;
        Functions.putLong(data, index, messageType);
        index+=4;
        Functions.putLong(data, index, rpcVersion);
        index+=4;
        Functions.putLong(data, index, programName);
        index+=4;
        Functions.putLong(data, index, programVersion);
        index+=4;
        Functions.putLong(data, index, programProcedure);
        index+=4;
        
//        credentials
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
//        verifier
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        Functions.putLong(data, index, len-20);
        index+=4;
        
        index=index+len-20;
        
//        Copying my id       
        String myID="000000086d795f6e616d653100030d7000000001";
        byte[] myIDBytes=Utility.hexStringToByteArray(myID);
        System.arraycopy(myIDBytes, 0, data, index, myIDBytes.length);
        index+=myIDBytes.length;
        
        System.arraycopy(privData, 0, data, index, privLen);
        index+=privLen;
        
        xID=Functions.random.nextInt();
        return index;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        xID=Functions.getLong(data, offset);
        
        for(int i=offset;i<offset+20;i++) data[i]=data[i+len-20];
        for(int i=offset+20;i<offset+len-64;i++) data[i]=data[i+24];
        
        return len-64;
    }
    
}
