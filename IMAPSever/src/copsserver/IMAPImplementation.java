/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package copsserver;

import com.sun.org.apache.xalan.internal.xsltc.trax.OutputSettings;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class IMAPImplementation {
    public int reqTrackNumber;
    public byte idOfHeaderServer;
    public int lenAtServer;
    public byte[] headerArrayAtServer;
    private int sum,mod,multiplier,reader;
    private int readLenatServerHandshake;
    byte [] tempArray = new byte[2048];
    
    public boolean imapHandshakeAtServer(Socket socket){
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            
            readLenatServerHandshake=is.read(tempArray, 0, 18);
//            System.out.println("received at server----------1: -> "+readLenatServerHandshake);
            os.write(Functions.hexStringToByteArray("2a204341504142494c49545920494d41503420494d415034726576312049444c45204c49544552414c2b204c4f47494e2d524546455252414c53204d41494c424f582d524546455252414c53204e414d45535041434520415554483d4e544c4d0d0a6130303030204f4b204341504142494c49545920636f6d706c657465642e0d0a"));
            String serverLoginReceiver=Functions.readLine(is, tempArray);
//            System.out.println("-------Login receiver---> "+serverLoginReceiver);
            os.write(Functions.hexStringToByteArray("6130303031204f4b204c4f47494e20636f6d706c657465642e0d0a"));
            System.out.println("HandShaking Successful at Server!!!");
            return true;
            
        } catch (Exception e) {
            System.out.println("Ugh! HandShaking Error");
            e.printStackTrace();
        }
        
        
        return false;
    }
    
    public int createPacketAtServer(byte [] data, int offset, int len){
        if(data.length <= offset + len + 45)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 45] = data[i];
        
        int index=offset;
        data[index++]=0x61;
        for(int i=index+3;i>=index;i--){
            mod=reqTrackNumber%10;
            data[i]=(byte) (0x30+mod);
            reqTrackNumber=reqTrackNumber/10;
        }
        index+=4;
        
        headerArrayAtServer=Functions.getImapServerHeader(idOfHeaderServer);
        System.arraycopy(headerArrayAtServer, 0, data, index, headerArrayAtServer.length);
        index+=headerArrayAtServer.length;
        Functions.putInt4(data, index, len);
        index+=4;

        return index+len;
    }
    
    public int decodePacketAtServer(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 1);
            reqTrackNumber=getReqTrackNumber(is);
            Functions.ignoreByte(is, 45);
            idOfHeaderServer=(byte) is.read();
            lenAtServer=Utility.buildLen4(is);
            is.read(data, offset, lenAtServer);
            
            return lenAtServer;
            
        }catch(Exception e) {e.printStackTrace();}
        return -1;

        
    }

    private int getReqTrackNumber(InputStream is) {
        
        sum=0;
        multiplier=1000;
        try {
            for(int i=0;i<4;i++){
                reader=is.read();
                reader=reader-0x30;
                sum=sum+reader*multiplier;
                multiplier=multiplier/10;
        }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sum;
    }

    
}
