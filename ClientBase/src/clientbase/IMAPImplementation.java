/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase;


import static clientbase.Functions.concatenateByteArrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class IMAPImplementation {
    
    private int reqTrackNumber;
    private byte[] headerArrayAtClient;
    private int headerId,mod;
    private int receiveLenAtClient;
    private int readLenatClientHandshake;
    
    private byte[] tempArray=new byte[2048];
    static byte[] partOneLogin;
    static String partOneOfLogin;
    static byte[] capability;
    
    static{
        partOneLogin=Functions.hexStringToByteArray("6130303031204c4f47494e2022");
        capability=Functions.hexStringToByteArray("6130303030204341504142494c4954590d0a");
    }
    
    public IMAPImplementation() {
        reqTrackNumber=Utility.random.nextInt(9999);
   }

    public boolean imapHandshakeAtClient(Socket socket){
        try {
            
            int index,offset;
            index=offset=0;
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            os.write(capability);
            readLenatClientHandshake=Functions.readByte(is, tempArray, 130);
//            System.out.println("------------------------>readLen client 1:>= "+readLenatClientHandshake);
            //capability check completed
            
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
//            System.out.println("HandShaking Completed Successfully at Client!!!");
            
            return true;
        } catch (Exception e) {
//            System.out.println("Ugh, HandShaking Error!");
            e.printStackTrace();
        }
        
        return false;
    }
    
    public static byte[] getImapClientHeader(int index){
        String str="";
        String[] headesrArray={" LIST \"\" \"\"\r\n",
            " SELECT \"INBOX\"\r\n",
            " NOOP\r\n",
            " FETCH 12 RFC822\r\n",
            " STORE 4 +FLAGS.SILENT (\\Seen \\Deleted)\r\n",
            " STORE 4 -FLAGS.SILENT (\\Flagged \\Answered)\r\n",
            " EXPUNGE\r\n",
            " CLOSE\r\n"};
        str=headesrArray[index];
        byte[] data = str.getBytes();
        if(data.length<45){
            data=Functions.concatenateByteArrays(data,Functions.getRandomData(45-data.length));
        }

        return data;
    }
    

    
    public int createPacketAtClient(byte [] data, int offset, int len){
        if(data.length <= offset + len + 53)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 53] = data[i];

        int index=offset;
        data[index++]=0x61;
        for(int i=index+3;i>=index;i--){
            mod=reqTrackNumber%10;
            data[i]=(byte) (0x30+mod);
            reqTrackNumber=reqTrackNumber/10;
        }
        index+=4;
        
        headerId=Utility.random.nextInt(8);
        headerArrayAtClient=getImapClientHeader(headerId);
        System.arraycopy(headerArrayAtClient, 0, data, index, 45);
        index+=45;
        data[index++]=(byte) headerId;
        Functions.putShort2(data, index, (short) len);
        index+=2;
        
        reqTrackNumber=Utility.random.nextInt(9999);
        return 53+len;
    }
    
    public int decodePakcetAtClient(byte [] data, int offset, InputStream is) throws IOException{
        Functions.ignoreByte(is, 41);
        receiveLenAtClient=Utility.buildLen2(is);
        is.read(data, offset, receiveLenAtClient);
        return receiveLenAtClient;

    }
    
}
