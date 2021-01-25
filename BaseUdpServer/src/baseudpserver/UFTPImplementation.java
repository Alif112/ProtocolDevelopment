/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baseudpserver;


import java.net.InetAddress;

/**
 *
 * @author Reve Systems
 */

public class UFTPImplementation {
    public byte protocolVersion;
    public byte type;
    public int blockSize;
    public long groupID;
//    public byte [] sourceAddress;
//    public byte [] destAddress;
    public int fieldID;
    public byte pass;
    public long sequenceNumber;
    
    public UFTPImplementation()
    {
        protocolVersion = 0x31;
        type = 0x08;
        groupID = Functions.random.nextLong();
//        try{
//            sourceAddress = InetAddress.getByName(sAddress).getAddress();
//        }catch(Exception e){}
        fieldID = 1;
        pass = (byte) Functions.random.nextInt(255);
        sequenceNumber = 0;
    }
    
    public int createPacket(byte [] data, int offset, int len, InetAddress destAddress, InetAddress sAddress)
    {
        if(data.length <= offset + len + 28)
            return len;
        for(int i = offset + len - 1; i >= offset; i--)
            data[i + 28] = data[i];
        int index = offset;
        data[index++] = protocolVersion;
        data[index++] = type;
        Functions.putInt(data, index, len + 12);
        index += 2;
        Functions.putLong(data, index, groupID);
        index +=4;
        System.arraycopy(sAddress.getAddress(), 0, data, index, 4);
        index += 4;
        System.arraycopy(destAddress.getAddress(), 0, data, index, 4);
        index += 4;
        data[index++] = type;
        data[index++] = 0x00;
        Functions.putInt(data, index, fieldID);
        index += 2;
        data[index++] = pass;
        data[index++] = 0x00;
        data[index++] = 0x00;
        data[index++] = 0x01;
        Functions.putLong(data, index, sequenceNumber);
        index += 4;
        
//        Functions.debug(Constants.logFile, "Index started from: " + offset + " index end: " + index + " header: " + (index - offset));
        
        sequenceNumber++;
        fieldID++;
        pass = (byte) Functions.random.nextInt(255);
        
        return index + len;
    }
    
    public int decodePacket(byte [] data, int offset, int len)
    {
        groupID = Functions.getLong(data, offset + 4);
        for(int i=offset;i < offset + len;i++)
        {
            data[i] = data[i + 28];
        }
        return len - 28;
    }
}
