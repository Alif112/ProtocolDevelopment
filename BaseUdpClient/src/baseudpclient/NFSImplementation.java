package baseudpclient;

public class NFSImplementation {
    public long programNFS;
    public long credentialLen;
    public long NFSLen;
    
    public NFSImplementation() {
        programNFS=100003;
        credentialLen=52;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 36)
            return len;
        for(int i = offset + len - 1; i >=offset+60; i--)
            data[i + 36] = data[i];
        int opaqueDataLen=52;
        byte[] opaqueData=new byte[opaqueDataLen];
        for(int i=offset+59,j=51;i>=offset+8;i--) opaqueData[j--]=data[i];
        
        int flavorLen=4;
        byte[] flavorData=new byte[flavorLen];
        for(int i=offset+7,j=3;i>=offset+4;i--) flavorData[j--]=data[i];
        
        int XIDLen=4;
        byte[] XIDData=new byte[XIDLen];
        for(int i=offset+3,j=3;i>=offset;i--) XIDData[j--]=data[i];
        
        
        
        int index=offset;
        /** XID Data copy**/
        System.arraycopy(XIDData, 0, data, index, XIDLen);
        index+=XIDLen;
        //message type
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        //RPC Version
        data[index++]=0x00;data[index++]=0x00;data[index++]=0x00;data[index++]=0x02;
        //program NFS
//        data[index++]=0x00; data[index++]=0x01; data[index++]=(byte) 0x86; data[index++]=(byte) 0xa3;
        Functions.putLong(data, index, programNFS);
        index+=4;
        // program Version
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x03;
        //procedure FSINFO
        data[index++]=0x00;data[index++]=0x00; data[index++]=0x00; data[index++]=0x13;
        
        //Credential Flavor data random
        System.arraycopy(flavorData, 0, data, index, flavorLen);
        index+=flavorLen;
        Functions.putLong(data, index, credentialLen);
        index+=4;
        
        //opaque position data copy
        System.arraycopy(opaqueData, 0, data, index, opaqueDataLen);
        index+=opaqueDataLen;
        
        //varifiable padding
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        
        // NFS Length copy
        NFSLen=len-60;
        
        Functions.putLong(data, index, NFSLen);
        index+=4;
        
        return index+len-60;
    }
    

    public int decodePacket(byte [] data, int offset, int len){
        for(int i=offset;i<offset+4;i++){
            data[i+4]=data[i+24];
        }
        for(int i=offset;i<offset+52;i++){
            data[i+8]=data[i+32];
        }
       
        for(int i=offset;i<offset+len+32;i++){
            data[i+60]=data[i+96];
        }

        return len-36;
    }
    
    
}
