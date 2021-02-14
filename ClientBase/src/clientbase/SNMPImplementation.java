
package clientbase;

public class SNMPImplementation {
    public byte protcolHeader;
    public byte lengthStarter;
    public short lengthUpdate;
    public byte protocolVersion;
    public byte setType;
    public short reqID;
    public byte errorStatus;
    public byte errorIndex;

    public SNMPImplementation(boolean isClient) {
        protcolHeader=0x30;
        lengthStarter=(byte) 0x82;
        protocolVersion=0x00;
        if(isClient)setType=(byte) 0xa3;
        else setType=(byte) 0xa2;
        reqID=(short) Utility.random.nextInt();
        errorStatus=0x00;
        errorIndex=0x00;

    }

    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 52)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 52] = data[i];

        int index=offset;
        data[index++]=protcolHeader;

        data[index++]=lengthStarter;
        lengthUpdate=(short) (len+48);
        Functions.putShort2(data, index, lengthUpdate);
        index+=2;

        data[index++]=0x02; data[index++]=0x01;
        data[index++]=protocolVersion;

        data[index++]=0x04; data[index++]=0x06;
//        community public
        data[index++]=0x70; data[index++]=0x75; data[index++]=0x62;
        data[index++]=0x6c; data[index++]=0x69; data[index++]=0x63;
//        data set request/response
        data[index++]=setType;

        data[index++]=lengthStarter;
        lengthUpdate=(short) (len+33);
        Functions.putShort2(data, index, lengthUpdate);
        index+=2;
        //req id copy
        data[index++]=0x02; data[index++]=0x02;
        Functions.putShort2(data, index, reqID);
        index+=2;

        data[index++]=0x02; data[index++]=0x01;
        data[index++]=errorStatus;
        data[index++]=0x02; data[index++]=0x01;
        data[index++]=errorIndex;
//        variable bindings
        data[index++]=protcolHeader;
        data[index++]=lengthStarter;
        lengthUpdate=(short) (len+19);
        Functions.putShort2(data, index, lengthUpdate);
        index+=2;

        data[index++]=protcolHeader;
        data[index++]=lengthStarter;
        lengthUpdate=(short) (len+15);
        Functions.putShort2(data, index, lengthUpdate);
        index+=2;
//        object create
        data[index++]=0x06; data[index++]=0x08; data[index++]=0x2b; data[index++]=0x06;
        for(int i=index;i<index+6;i++)
            data[i]=(byte) Utility.random.nextInt(10);
        index+=6;

        //data append
        data[index++]=0x44;
        data[index++]=lengthStarter;
        lengthUpdate=(short) len;
        Functions.putShort2(data, index, lengthUpdate);
        index+=2;
        data[index++]=0x00;

        reqID=(short) Utility.random.nextInt();
        return index+len;
    }

    public int decodePacket(byte [] data, int offset, int len){
        reqID=Functions.getShort2(data, offset+21);

        for(int i=offset;i<offset+len-52;i++) data[i]=data[i+52];

        return len-52;
    }

}
