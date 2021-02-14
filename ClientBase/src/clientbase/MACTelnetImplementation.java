package clientbase;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MACTelnetImplementation {
    private static final byte protocolVersion;
    private short sessionId;
    private int sessionDataBytes;
    private static int controlDataLengthFixed;
    private static byte[] macAddressBytes;
    private byte[] destAdd;
    private static final int magicNumber;
    private static int pos=0;
    
    static{
        try {
            getMacAddress();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(MACTelnetImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        protocolVersion=0x01;
        controlDataLengthFixed=Utility.random.nextInt(9)+12;
        magicNumber = 1446253311;
        
    }
    private final byte[] packetType={0x00,0x02,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01};
    private final byte[] dataPacketType={0x07,0x07,0x00,0x03,0x02,0x01,0x04,0x05,0x06,0x09};
    
    public static void getMacAddress() throws UnknownHostException, SocketException{
        InetAddress ipAddress = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
        macAddressBytes = networkInterface.getHardwareAddress();
//        System.out.println(Utility.bytesToHex(macAddressBytes));
    }
    
    
    
    public MACTelnetImplementation() {
        destAdd=new byte[6];
        pos=0;
        sessionId=(short) Utility.random.nextInt();
        sessionDataBytes=Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len){
        synchronized(this){
            if(data.length <= offset + len + 25) return len;
            for(int i = offset + len - 1; i >=offset+6; i--) data[i + 25] = data[i];

            for(int i=offset+5,j=5;i>=offset;i--,j--) destAdd[j]=data[i];

            int index=offset;
            data[index++]=protocolVersion;
            data[index++]=packetType[pos];
            System.arraycopy(macAddressBytes, 0, data, index, macAddressBytes.length);
            index+=macAddressBytes.length;
            System.arraycopy(destAdd, 0, data, index, destAdd.length);
            index+=destAdd.length;
            Functions.putShort2(data, index, sessionId);
            index+=2;
            data[index++]=0x00; data[index++]=0x15;
            Functions.putInt4(data, index, sessionDataBytes);
            index+=4;
            /** Control Packet*/
            Functions.putInt4(data, index, magicNumber);
            index+=4;
            data[index++]=dataPacketType[pos++];
            if(pos==10) pos=0;
            Functions.putInt4(data, index, controlDataLengthFixed);
            index+=4;
        }
        controlDataLengthFixed=Utility.random.nextInt(9)+12;
        
        return len+25;
        
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        System.arraycopy(data, offset+8, data, offset, 6);
        System.arraycopy(data, offset+22, data, offset+6, len-16);
        
        return len-16;
    }
    
    
}
