package icmpv6client;

public class ICMPv6Implementation {
    public int teredoHeader;
    public byte clientIdentifierLen;
    public byte  authenticationValueLen;
    public long nonceValue;
    public byte confirmationByte;
    
    public byte protocolVersionIPv6;
    public int payloadLen;
    public byte nextHeaderICMPv6;
    public byte hopLimit;
    
    public byte routerSolicitation;
    public byte icmpv6Code;
    
    public long reserved;
    public byte sourceLinkLayerAddress;
    public byte sourceLinkLayerAddressLen;
    
    public ICMPv6Implementation() {
        teredoHeader=1;
        clientIdentifierLen=0x00;
        authenticationValueLen=0x00;
        nonceValue=Functions.random.nextLong();
        confirmationByte=0x00;
        protocolVersionIPv6=0x60;
        payloadLen=24;
        nextHeaderICMPv6=0x3a;
        hopLimit=(byte) 0xff;
        routerSolicitation=(byte) 0x85;
        icmpv6Code=0x00;
        reserved=0;
        sourceLinkLayerAddress=0x01;
        sourceLinkLayerAddressLen=0x02;
        
    }
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 79)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 79] = data[i];
        
        int index=offset;
        
        /*** Teredo protocol started**/
        Functions.putInt(data, index, teredoHeader);
        index+=2;
        data[index++]=clientIdentifierLen;
        data[index++]=authenticationValueLen;
        Functions.putLong8(data, index, nonceValue);
        index+=8;
        data[index++]=confirmationByte;
        /*** Teredo protocol finished**/
        
        /*** IPv6 protocol started**/
        data[index++]=protocolVersionIPv6;
        data[index++]=0x000; data[index++]=0x000; data[index++]=0x000;
        Functions.putInt(data, index, payloadLen);
        index+=2;
        data[index++]=nextHeaderICMPv6;
        data[index++]=hopLimit;
        
//        source destination address copy
        String src="fe800000000000008000fffffffffffd";
        String des="ff020000000000000000000000000002";
        
        byte[] srcByte=Utility.hexStringToByteArray(src);
        byte[] desByte=Utility.hexStringToByteArray(des);
        System.arraycopy(srcByte, 0, data, index, srcByte.length);
        index+=srcByte.length;
        System.arraycopy(desByte, 0, data, index, desByte.length);
        index+=desByte.length;
        
        /*** IPv6 protocol finished**/
        
        /*** ICMPv6 protocol started**/
        data[index++]=routerSolicitation;
        data[index++]=icmpv6Code;
        //        checksum
        data[index++]=(byte) 0xa9;
        data[index++]=0x1d;
        Functions.putLong(data, index, reserved);
        index+=4;
        
        data[index++]=sourceLinkLayerAddress;
        data[index++]=sourceLinkLayerAddressLen;
//        LinkLayerAddress
        String linkLayerAddress="0000000000008000f12ab9c82815";
        byte[] linkLayerAddressByte=Utility.hexStringToByteArray(linkLayerAddress);
        System.arraycopy(linkLayerAddressByte, 0, data, index, linkLayerAddressByte.length);
        index+=linkLayerAddressByte.length;
        
        /*** ICMPv6 protocol finished**/
        
//        zero padding
        data[index++]=0x00; data[index++]=0x00;
        nonceValue=Functions.random.nextLong();
        
        
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        nonceValue=Functions.getLong8(data, offset+4);
        for(int i=offset;i<offset+len-79;i++) data[i]=data[i+79];
        
        return len-79;
    }
    
}