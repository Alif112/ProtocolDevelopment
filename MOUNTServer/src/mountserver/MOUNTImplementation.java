
package mountserver;
public class MOUNTImplementation {
    public long xID;
    public long messageType;
    public long programVersion;
    public long rpcProcedure;
    public long replyState;
    public long rpcExecutedSuccessCode;
    public long mountStatus;
    
    public MOUNTImplementation() {
//        xID=Functions.random.nextInt();
        messageType=1;      
        replyState=0;
        rpcExecutedSuccessCode=0;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len +28)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 28] = data[i];
        
        int index=offset;
        Functions.putLong(data, index, xID);
        index+=4;
        Functions.putLong(data, index, messageType);
        index+=4;
        Functions.putLong(data, index, replyState);
        index+=4;
        
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        Functions.putLong(data, index, rpcExecutedSuccessCode);
        index+=4;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        xID=Functions.getLong(data, offset);
        
        
        for(int i=offset;i<offset+4;i++) data[i]=data[i+32];
        for(int i=offset;i<offset+20;i++) data[i+4]=data[i+64];
        for(int i=offset;i<offset+len-96;i++) data[i+24]=data[i+96];
        
        
        return len-72;
    }
    
}
