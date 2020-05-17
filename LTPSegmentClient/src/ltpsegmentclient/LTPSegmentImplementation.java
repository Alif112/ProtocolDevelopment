
package ltpsegmentclient;

public class LTPSegmentImplementation {

    
    public LTPSegmentImplementation() {
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 5)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 5] = data[i];
        
        int index=offset;
        String header="e318020100";
        int headerLen=5;
        byte[] headerData=new byte[headerLen];
        headerData=Utility.hexStringToByteArray(header);
        System.arraycopy(headerData, 0, data, index, headerLen);
        index+=headerLen;
        
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        System.arraycopy(data, offset+5, data, offset, len-5);
        
        return len-5;
    }
    
}
