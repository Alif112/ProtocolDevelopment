
package clientbase;

/**
 *
 * @author User
 */
public class UAUDPImplementation {
    private static byte keepAlive;
    private static byte[] paddingZero;
    static {
        paddingZero=Utility.hexStringToByteArray("0000000000000000000000000000000000");
    }
    
    public UAUDPImplementation(boolean isClient){
        if(isClient) keepAlive=0x04;
        else keepAlive=0x05;
        

    }
    
    public int createPacket(byte[] data, int offset, int len){
        if(data.length<=offset+len+18) return len;
        for(int i=len+offset-1;i>=offset;i--) data[i+18]=data[i];
        int index=offset;
        data[index++]=keepAlive;
        System.arraycopy(paddingZero, 0, data, index, paddingZero.length);
        index+=paddingZero.length;
        return 18+len;
    }
    
    public int decodePacket(byte[] data, int offset, int len){
        System.arraycopy(data, offset+18, data, offset, len-18);
        return len-18;
    }
}
