/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package baseudpclient;


import java.net.InetAddress;

/**
 *
 * @author User
 */

public class NTPImplementation {
    public byte packetStartFlag;
    public byte peerClock;
    public byte peerPollingInterval;
    public byte peerClockPrecision;
    
    public NTPImplementation() {
        packetStartFlag=(byte) 0xd9;
        peerClock=0x02;
        peerPollingInterval=0x0a;
        peerClockPrecision=(byte) Functions.random.nextInt(128);
    }
    
    
    public int createPacket(byte [] data, int offset, int len){
        if(data.length <= offset + len + 6)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 6] = data[i];
        int index=offset;
        data[index++]=packetStartFlag;
        data[index++]=peerClock;
        data[index++]=peerPollingInterval;
        data[index++]=peerClockPrecision;
        data[index++]=0x00; data[index++]=0x00;
        
        peerClockPrecision=(byte) Functions.random.nextInt(128);
        return index+len;
    }
    
    public int decodePacket(byte [] data, int offset, int len){
        
        for(int i=offset;i<offset+len;i++){
            data[i]=data[i+6];
        }

        return len-6;
    }
    
}
