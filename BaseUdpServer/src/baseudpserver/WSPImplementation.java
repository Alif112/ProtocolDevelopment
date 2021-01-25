
package baseudpserver;


/**
 *
 * @author User
 */

public class WSPImplementation {
    public byte transactionID;
    public byte pduType;
    public byte uriLen;
    public byte headerLen;
    
    public String uri;
    public byte[] uriData;
    
    public WSPImplementation() {
        transactionID=(byte) Functions.random.nextInt(256);
        pduType=0x60;
        uriLen=0x17;
        uri="613a2f2f3132372e302e302e312f696e6465782e776d6c000000";
        uriData=Utility.hexStringToByteArray(uri);
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 30)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 30] = data[i];
        
        
        int index=offset;
        /** IG Control started**/
        data[index++]=transactionID;
        data[index++]=pduType;
        data[index++]=uriLen;
        headerLen=(byte) len;
        data[index++]=headerLen;

        System.arraycopy(uriData, 0, data, index, uriData.length);
        index+=uriData.length;

        transactionID=(byte) Functions.random.nextInt(256);
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        transactionID=data[offset];
        for(int i=offset;i<offset+len-1;i++){
            data[i]=data[i+32];
        }

        return len-32;
    }
    
}
