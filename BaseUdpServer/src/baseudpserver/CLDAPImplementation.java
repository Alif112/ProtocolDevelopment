
package baseudpserver;


public class CLDAPImplementation {
    public byte protocolHeader;
    public byte headerLen;
    public byte messageID;
    public byte bindReqHeader;
    public byte bindReqLen;
    public byte resultCode;
    
    
    public CLDAPImplementation() {
        protocolHeader=0x30;
        headerLen=0x0c;
        bindReqHeader=0x61;
        bindReqLen=0x07;
        resultCode=0x00;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 16)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 16] = data[i];
        
        int index=offset;
        /** IG Control started**/
        data[index++]=protocolHeader;
        data[index++]=headerLen;
        data[index++]=0x02;data[index++]=0x01;
        
        data[index++]=messageID;
        data[index++]=bindReqHeader;
        data[index++]=bindReqLen;
        
        data[index++]=0x0a;data[index++]=0x01;
        data[index++]=resultCode;
        
        data[index++]=0x04;data[index++]=0x00;
        data[index++]=0x04;data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00;
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        messageID=data[offset+4];
        for(int i=offset;i<offset+len;i++){
            data[i]=data[i+16];
        }

        return len-16;
    }
    
}
    