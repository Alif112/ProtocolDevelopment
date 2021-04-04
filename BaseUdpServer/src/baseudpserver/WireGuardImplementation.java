/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baseudpserver;

/**
 *
 * @author User
 */
public class WireGuardImplementation {
    public static byte dataTypeTransport=0x04;
    public int receiver;
    public long counter;
    
    public WireGuardImplementation(){
        receiver=Utility.random.nextInt();
        counter=1;
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 16)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 16] = data[i];
        
        int index=offset;
        data[index++]=dataTypeTransport;
//        zero padding
        data[index++]=0x00; data[index++]=0x00; data[index++]=0x00;
        Functions.putInt4(data, index, receiver);
        index+=4;
        Functions.putLong8(data, index, counter);
        index+=8;
        for(int i=1;i<=4;i++){
            byte temp=data[index-i];
            data[index-i]=data[index-8+i-1];
            data[index-8+i-1]=temp;
        }
        counter++;
        return 16+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        System.arraycopy(data, offset+16, data, offset, len-16);
        return len-16;
    }
}
