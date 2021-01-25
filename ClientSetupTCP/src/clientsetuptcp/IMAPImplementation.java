/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsetuptcp;

import com.sun.org.apache.xalan.internal.xsltc.trax.OutputSettings;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import javax.print.attribute.standard.OutputDeviceAssigned;

/**
 *
 * @author User
 */
public class IMAPImplementation {
    
    private int reqTrackNumber;
    private byte[] headerArrayAtClient;
    private int id,mod;
    private int receiveLenAtClient;
    private int readLenatClientHandshake;
    
    private byte[] tempArray=new byte[2048];
    private byte[] partOneLogin,userName,passWord;
    static String partOneOfLogin;
    
    static{partOneOfLogin="6130303031204c4f47494e2022";}
    
    public IMAPImplementation() {
        reqTrackNumber=Utility.random.nextInt(9999);

   }

    public boolean imapHandshakeAtClient(Socket socket){
        try {
            
            int index,offset;
            index=offset=0;
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            os.write(Functions.hexStringToByteArray("6130303030204341504142494c4954590d0a"));
            readLenatClientHandshake=Functions.readByte(is, tempArray, 130);
//            System.out.println("------------------------>readLen client 1:>= "+readLenatClientHandshake);
            //capability check completed
            
            partOneLogin=Functions.hexStringToByteArray(partOneOfLogin);
            System.arraycopy(partOneLogin, 0, tempArray, index, partOneLogin.length);
            index+=partOneLogin.length;
            
            index=Functions.getUserName(tempArray,index);
            
            tempArray[index++]=0x22; tempArray[index++]=0x20; tempArray[index++]=0x22;
            index=Functions.getPassWord(tempArray,index);
            tempArray[index++]=0x22;
            tempArray[index++]=0x0d;
            tempArray[index++]=0x0a;
            
            os.write(tempArray,offset,index);
            is.read(tempArray,offset,27);
            System.out.println("HandShaking Completed Successfully at Client!!!");
            
            return true;
        } catch (Exception e) {
            System.out.println("Ugh, HandShaking Error!");
            e.printStackTrace();
        }
        
        return false;
    }
    

    
    public int createPacketAtClient(byte [] data, int offset, int len){
        if(data.length <= offset + len + 55)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 55] = data[i];

        int index=offset;
        data[index++]=0x61;
        for(int i=index+3;i>=index;i--){
            mod=reqTrackNumber%10;
            data[i]=(byte) (0x30+mod);
            reqTrackNumber=reqTrackNumber/10;
        }
        index+=4;
        
        id=Utility.random.nextInt(8);
        headerArrayAtClient=Functions.getImapClientHeader(id);
        System.arraycopy(headerArrayAtClient, 0, data, index, 45);
        index+=45;
        data[index++]=(byte) id;
        Functions.putInt4(data, index, len);
        index+=4;
        
        reqTrackNumber=Utility.random.nextInt(9999);
        return index+len;
    }
    
    public int decodePakcetAtClient(byte [] data, int offset, InputStream is){
        try{
            Functions.ignoreByte(is, 41);
            receiveLenAtClient=Utility.buildLen4(is);
            is.read(data, offset, receiveLenAtClient);
            return receiveLenAtClient;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    
    
    
}
