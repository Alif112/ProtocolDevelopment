
package baseudpclient;


import java.net.InetAddress;

/**
 *
 * @author User
 */

public class WSPImplementation {
    public byte transactionID;
    public byte pduType;
    public byte uriLen;
    public byte headerLen;

    public WSPImplementation() {
        transactionID=(byte) Functions.random.nextInt(256);
        pduType=0x40;
        uriLen=0x1b;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 32)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 32] = data[i];
        
        int index=offset;
        /** IG Control started**/
        data[index++]=transactionID;
        data[index++]=pduType;
        data[index++]=uriLen;

        String m="687474703a3a2f2f3132372e302e302e312f696e6465782e776d6c0000";
        byte[] b=Utility.hexStringToByteArray(m);
        int bLen=b.length;
        System.arraycopy(b, 0, data, index, bLen);
        index+=bLen;

        transactionID=(byte) Functions.random.nextInt(256);
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+len-1;i++){
            data[i]=data[i+30];
        }
        

        return len-30;
    }
    
}
