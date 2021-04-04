/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpprotocols;

import utils.Functions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import utils.Utility;


public class IPAImplementation {
    static byte[] header;
    public int createLen;
    public byte dataSeq;
    static byte[] firstHeaderHand,secondHeaderHand;
    
    static {
        header=Utility.hexStringToByteArray("525453502f312e3020323030204f4b0d0a417564696f2d4a61636b2d5374617475733a20636f6e6e65637465643b20747970653d616e616c6f670d0a435365713a20");
        firstHeaderHand=Utility.hexStringToByteArray("525453502f312e3020323030204f4b0d0a4170706c652d526573706f6e73653a204d51554f48306d49326f584e4c4a527965376957646d6c683756514248594b4f563466712b75446f66434a7a63304c6f794a4a4241384a7a68562f54776e416e4865477739754565757633456439613578305176616b474b4d31552f7576587a6b6d41342f4e3141696b6a78726b5a616a383343657477514a55665636464534776149394d4175684679625976526b5962335a514f434b753156494e6e6d792b746f326d70337461796434336c316959305334517054734c734d4d4358516f786a79334734423732553349486e34486734565a5457505556426a62496554617a457376734a527863552f6e4d6a5950394f4e364c4c723132706770433169755374756e2f4c515143425a51793450544642316833747067526351555a7a39367644423077644d464b4a50685837794a4253314c344b4a795052737744706b5035316766716d77716a75733678736361796d38776c2f670d0a417564696f2d4a61636b2d5374617475733a20636f6e6e65637465643b20747970653d616e616c6f670d0a435365713a20310d0a5075626c69633a20414e4e4f554e43452c2053455455502c205245434f52442c2050415553452c20464c5553482c2054454152444f574e2c204f5054494f4e532c204745545f504152414d455445522c205345545f504152414d455445520d0a0d0a");
        secondHeaderHand=Utility.hexStringToByteArray("525453502f312e3020323030204f4b0d0a417564696f2d4a61636b2d5374617475733a20636f6e6e65637465643b20747970653d616e616c6f670d0a435365713a20310d0a0d0a");
   }
    
    public boolean ipaHandshakeAtServer(Socket socket){
        byte data[]=new byte[1024];
        int index=0,offset=0;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            is.read(data, offset, 276);
            System.arraycopy(firstHeaderHand, offset, data, index, firstHeaderHand.length);
            index+=firstHeaderHand.length;
            os.write(data, offset, index);
            is.read(data, offset, 857);
            os.write(secondHeaderHand);
//            System.out.println("HandShake Completed at Server !!!\n");
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;

    }
    
    public int createPacketAtServer(byte [] data, int offset, int len){
        if(data.length <= offset + len + 75)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 71] = data[i]; 
        
        int index=offset;
        System.arraycopy(header, 0, data, index, header.length);
        index+=header.length;
        data[index++]=dataSeq;
        data[index++]=0x0d; data[index++]=0x0a; 
        Functions.putShort2(data, index, (short) len);
        index+=2;
        index+=len;
        data[index++]=0x0d; data[index++]=0x0a;
        data[index++]=0x0d; data[index++]=0x0a;

        return len+75;
    }
    
    public int decodePacketAtServer(byte [] data, int offset, InputStream is) throws IOException{
            Functions.ignoreByte(is, 63);
            dataSeq=(byte) is.read();
            Functions.ignoreByte(is, 2);
            createLen=Utility.buildLen2(is);
//            System.out.println("------------------len size at server"+createLen);
            is.read(data, offset, createLen);
            Functions.ignoreByte(is, 4);    
            return createLen;
        
    }

}
