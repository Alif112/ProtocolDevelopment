
package clientbase;

public class LTPSegmentImplementation {
    public byte headerData[];
    
    public LTPSegmentImplementation() {
        headerData=Utility.hexStringToByteArray("e318020100");
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 5)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 5] = data[i];
        
        int index=offset;
        System.arraycopy(headerData, 0, data, index, headerData.length);
        index+=headerData.length;
        
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        System.arraycopy(data, offset+5, data, offset, len-5);
        
        return len-5;
    }
    
}
