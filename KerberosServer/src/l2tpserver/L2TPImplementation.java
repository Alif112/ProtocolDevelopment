
package l2tpserver;


public class L2TPImplementation {
    public int modifiedLen;
    public int numberOfZeros;
    public int numberOfBlocks;
    
    public L2TPImplementation() {
        modifiedLen=0;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 15)
            return len;
        for(int i = offset + len - 1; i >=offset+8; i--)
            data[i + 7] = data[i];
        
        int smallLen=8;
        byte[] saveData=new byte[smallLen];
        for(int i=offset+7,j=7;i>=offset;i--,j--){
            saveData[j]=data[i];
        }
        
        int index=offset;
        /** IG Control started**/
        data[index++]=(byte) 0xc8;
        data[index++]=0x02;
        modifiedLen=12;
        Functions.putInt(data, index, modifiedLen);
        index+=2;
        System.arraycopy(saveData, 0, data, index, smallLen);
        index+=smallLen;
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        return index+len-8;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        numberOfZeros=(int)data[offset+4];
        int realLen=len-numberOfZeros;
        numberOfBlocks=(len-12)/10;
        
        for(int i=offset;i<offset+7;i++)
            data[i]=data[i+5];
        int index=offset+7;
        int tempIndex=12;
        for(int i=0;i<numberOfBlocks-1;i++){
            tempIndex+=2;
            System.arraycopy(data, tempIndex, data, index, 8);
            index+=8;
            tempIndex+=8;
        }
        tempIndex+=2;
        while(tempIndex<realLen){
            data[index++]=data[tempIndex++];
        }
//        System.out.println("=========> NumberOfBlocks = "+numberOfBlocks+"====> number of Zeros= "+numberOfZeros);
        return numberOfBlocks*8+7-numberOfZeros;
    }
    
}
    