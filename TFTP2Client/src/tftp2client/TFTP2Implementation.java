package tftp2client;

public class TFTP2Implementation {
    private byte[] srcFileData;
    public String srcFile;
    
    public TFTP2Implementation() {
        srcFile="000162696e38303638532d686561646572006f63746574007473697a65003000626c6b73697a6500313432380074696d656f7574003100ff00";
        srcFileData=Utility.hexStringToByteArray(srcFile);
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 58)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 57] = data[i];

        int index=offset;
        System.arraycopy(srcFileData, 0, data, index, srcFileData.length);
        index+=srcFileData.length;
        
        for(int i=index;i<index+len;i++) data[i]=(byte) (data[i]+1);
        index=index+len;
        data[index++]=0x00;

        return index;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        for(int i=offset;i<offset+len-58;i++) data[i]=(byte) (data[i+57]-1);
        return len-58;
    }
}