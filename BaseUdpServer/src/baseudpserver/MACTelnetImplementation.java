package baseudpserver;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author User
 */
public class MACTelnetImplementation {
    private static byte protocolVersion;
    private static byte pacType;
    private short sessionId;
    private int sessionDataBytes;
    
    static byte[] macAddressBytes;
    byte[] destAdd,packetMagicNumber,md5Password;
    
    
    static{
        try {
            getMacAddress();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(MACTelnetImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        protocolVersion=0x01;
        pacType=0x02;
    }
    public static void getMacAddress() throws UnknownHostException, SocketException{
        InetAddress ipAddress = InetAddress.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
        macAddressBytes = networkInterface.getHardwareAddress();
//        System.out.println(Utility.bytesToHex(macAddressBytes));
    }
    
    
    public MACTelnetImplementation(){
        destAdd=new byte[6];
        sessionId=(short) Utility.random.nextInt();
        sessionDataBytes=Utility.random.nextInt();
    }
    
    public int createPacket(byte [] data, int offset, int len){
        synchronized(this){
            if(data.length <= offset + len + 16)
                return len;
            for(int i = offset + len - 1; i >=offset+6; i--)
                data[i + 16] = data[i];
            
            for(int i=offset+5,j=5;i>=offset;i--,j--) destAdd[j]=data[i];

            int index=offset;
            data[index++]=protocolVersion;
            data[index++]=pacType;
            System.arraycopy(macAddressBytes, 0, data, index, macAddressBytes.length);
            index+=macAddressBytes.length;
            System.arraycopy(destAdd, 0, data, index, destAdd.length);
            index+=destAdd.length;
            Functions.putShort2(data, index, sessionId);
            index+=2;
            data[index++]=0x00; data[index++]=0x15;
            Functions.putInt4(data, index, sessionDataBytes);
            index+=4;
        }
        
        return len+16;
        
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        sessionId=Functions.getShort2(data, offset+14);
        sessionDataBytes=Functions.getInt4(data, offset+18);
        System.arraycopy(data, offset+8, data, offset, 6);
        System.arraycopy(data, offset+31, data, offset+6, len-25);
        return len-25;
    }
}
