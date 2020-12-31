
package mmseserver;

public class MMSEImplementation {
    public short transactionID;
    public byte pduType;
    public byte uriLength;
    public byte headerLength;
    public byte[] uriData,contentData,mmseData;
    
    public MMSEImplementation() {
        
        transactionID=(short) Utility.random.nextInt();
        pduType=0x60;
        uriLength=0x24;
        headerLength=0x04;
        uriData=Utility.hexStringToByteArray("687474703a2f2f6d6d7363656e7465722e73756e63656c6c756c61722e636f6d2e70682f");
        contentData=Utility.hexStringToByteArray("61707000");
        mmseData=Utility.hexStringToByteArray("8c839841354a446c697547653877516b38505a77008d9095819180");
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 78)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 77] = data[i];

        int index=offset;
        /**WTP started**/
        data[index++]=0x0b;
        Functions.putShort2(data, index, transactionID);
        index+=2;
        data[index++]=0x12;
        /** WTP finished**/
        /**WSP Started**/
        data[index++]=pduType;
        data[index++]=uriLength;
        data[index++]=headerLength;
//        copy of uri
        System.arraycopy(uriData, 0, data, index, uriData.length);
        index+=uriData.length;
        
//        content copy
        System.arraycopy(contentData, 0, data, index, contentData.length);
        index+=contentData.length;
        
//        copy mmse
        System.arraycopy(mmseData, 0, data, index, mmseData.length);
        index+=mmseData.length;
        
        data[index++]=0x00;
        data[index++]=0x00;
        data[index++]=(byte) 0x80;
        
        index+=len;
        data[index++]=0x00;
        transactionID=(short) Utility.random.nextInt();
        return len+78;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        transactionID=Functions.getShort2(data, offset+1);
        
        System.arraycopy(data, offset+77, data, offset, len-78);
        
        return len-78;
    }
    
}