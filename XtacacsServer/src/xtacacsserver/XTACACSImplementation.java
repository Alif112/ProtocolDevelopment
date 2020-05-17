
package xtacacsserver;

public class XTACACSImplementation {

    
    public XTACACSImplementation() {
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 29)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 29] = data[i];

        int index=offset;
        data[index++]=0x30;
        int updateLength=len+27;
        data[index++]=(byte) updateLength;
        String m="0201000406707562386963a4";
        byte[] mData=Utility.hexStringToByteArray(m);
        System.arraycopy(mData, 0, data, index, mData.length);
        index+=mData.length;
        updateLength=len+14;
        data[index++]=(byte) updateLength;
        
        String m2="06092b0601040104010215";
        byte[] m2Data=Utility.hexStringToByteArray(m2);
        System.arraycopy(m2Data, 0, data, index, m2Data.length);
        index+=m2Data.length;
        
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00; 
        
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        System.arraycopy(data, offset+29, data, offset, len-29);
        
        return len-29;
    }
    
}
