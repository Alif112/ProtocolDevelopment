/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author User
 */
public class IPAImplementation {
    public int createLen;
    private static String[] header_Type_String_Array={"ANNOUNCE", "SETUP", "RECORD", "PAUSE", 
        "FLUSH", "TEARDOWN", "OPTIONS", "GET_PARAMETER", "SET_PARAMETER"};
    public static int packetController;
    private static byte[] headerBuilder,first_Header_link,last_Header_Link;
    private static byte[] firstHeaderHand,secondHeaderHand,thirdHeaderHand;
    
    
    static{
        first_Header_link=Utility.hexStringToByteArray("727473703a2f2f3139322e3136382e");
        last_Header_Link=Utility.hexStringToByteArray("525453502f312e3020");
        firstHeaderHand=Utility.hexStringToByteArray("5245434f524420727473703a2f2f3139322e3136382e332e3130372f3334313539313037333820525453502f312e300d0a435365713a20330d0a53657373696f6e3a2044454144424545460d0a52616e67653a206e70743d302d0d0a5254502d496e666f3a207365713d35353438313b72747074696d653d313132363030303333310d0a557365722d4167656e743a206954756e65732f31302e3620284d6163696e746f73683b20496e74656c204d6163204f5320582031302e372e3329204170706c655765624b69742f3533342e35332e31310d0a436c69656e742d496e7374616e63653a20");
        secondHeaderHand=Utility.hexStringToByteArray("444143502d49443a20");
        thirdHeaderHand=Utility.hexStringToByteArray("414e4e4f554e434520727473703a2f2f3139322e3136382e332e3130372f3334313539313037333820525453502f312e300d0a435365713a20310d0a436f6e74656e742d547970653a206170706c69636174696f6e2f7364700d0a436f6e74656e742d4c656e6774683a203537320d0a557365722d4167656e743a206954756e65732f31302e3620284d6163696e746f73683b20496e74656c204d6163204f5320582031302e372e3329204170706c655765624b69742f3533342e35332e31310d0a436c69656e742d496e7374616e63653a20334235373030313030384638443545380d0a444143502d49443a20334235373030313030384638443545380d0a4163746976652d52656d6f74653a20313436303031373834370d0a0d0a763d300d0a6f3d6954756e65732033343135393130373338203020494e20495034203139322e3136382e332e3130370d0a733d6954756e65730d0a633d494e20495034203139322e3136382e332e3132330d0a743d3020300d0a6d3d617564696f2030205254502f4156502039360d0a613d7274706d61703a3936204170706c654c6f73736c6573730d0a613d666d74703a3936203335322030203136203430203130203134203220323535203020302034343130300d0a613d7273616165736b65793a4272324d562f7555514f41626935774b4235767451484e393454546d7770377267465478434b4468726f625a77784375314562677536414b344f4a7950594f4f764f4d70726c44386b557168543068556e586553625557376a686d773148534f76586e4d4731594f52416e706a5755654f44376852582f6d5a76614d725150786a335344694c5751786a5a7630335245485562514d4f48747a50796f4546344b52376767726331326c455870486149594b7932565134726b342b3249764f4665667365682f7751463779446b7a48766973546c58425a6e767571702b4e4c73524b474c4970723745466b6c44752f30586363634d392b68483238644a73724c6d446c6e6d7864796e34387551706f45435732634d6f57424876434b4c744146623859364d74693457446c453364673931424559425676683967756d454c5a4332416933727a66564373305333454654783974757176670d0a613d61657369763a66344f5a652f357659784970695459556539627346510d0a");
//        thirdHeaderHand=Utility.hexStringToByteArray("4163746976652d52656d6f74653a20313436303031373834370d0a0d0a");
    }
    
    public IPAImplementation() {
        packetController=0;
   }
    
    public boolean ipaHandshakeAtClient(Socket socket){
        byte[] data=new byte[1024];
        int index=0,offset=0;
        try {
            InputStream is=socket.getInputStream();
            OutputStream os=socket.getOutputStream();
            
            System.arraycopy(firstHeaderHand, 0, data, index, firstHeaderHand.length);
            index+=firstHeaderHand.length; //231
            index=getNumberAndUpperCase(data, index, 16);
            data[index++]=0x0d; data[index++]=0x0a; 
            System.arraycopy(secondHeaderHand, 0, data, index, secondHeaderHand.length);
            index+=secondHeaderHand.length; //9
            System.arraycopy(data, index-27, data, index, 16);
            index+=16;
            data[index++]=0x0d; data[index++]=0x0a; 
//            System.out.println("------------------------> "+index);
            
            os.write(data, offset, index);
            os.flush();
            is.read(data, offset, 527);
            os.write(thirdHeaderHand);
            os.flush();
            is.read(data, offset, 71);
            
            System.out.println("\nIPA HandShake Successfull at client!!\n");
            return true;
        } catch (IOException ex) {
//            System.out.println("IPA HandShake Failed");
            ex.printStackTrace();
        }
        return false;
        
        
    }
    

    public int createPacketAtClient(byte [] data, int offset, int len){
        if(data.length <= offset + len + 72)
            return len;
        for(int i = offset + len - 1; i >=offset; i--)
            data[i + 68] = data[i]; 
        
        int index=offset;
        headerBuilder=header_Type_String_Array[packetController++].getBytes();
        System.arraycopy(headerBuilder, 0, data, index, headerBuilder.length);
        index+=headerBuilder.length;
        data[index++]=0x20;
        System.arraycopy(first_Header_link, 0, data, index, first_Header_link.length);
        index+=first_Header_link.length; //15 bytes
        data[index++]=(byte) (Utility.random.nextInt(9)+49);
        data[index++]=0x2e;
        data[index++]=(byte) (Utility.random.nextInt(9)+49);
        data[index++]=(byte) (Utility.random.nextInt(5)+48);
        data[index++]=(byte) (Utility.random.nextInt(10)+48);
        
        data[index++]=0x2f;
        data[index++]=(byte) (Utility.random.nextInt(9)+49);
        index=getRandom30Plus(data,index,9);
        data[index++]=0x20;//46
        System.arraycopy(last_Header_Link, 0, data, index, last_Header_Link.length);
        index+=last_Header_Link.length; //9 b
        index=Functions.getRandomData(data,index,13-headerBuilder.length);
        data[index++]=0x0d; data[index++]=0x0a;
        data[index++]=0x43; data[index++]=0x53; data[index++]=0x65; data[index++]=0x71;
        data[index++]=0x3a; data[index++]=0x20;
        data[index++]=(byte) (Utility.random.nextInt(10)+48);
        data[index++]=0x0d; data[index++]=0x0a;
        Functions.putShort2(data, index, (short) len);
        index+=2;
        index+=len;
        data[index++]=0x0d; data[index++]=0x0a;
        data[index++]=0x0d; data[index++]=0x0a;
        
        if(packetController==header_Type_String_Array.length) packetController=0;
        
        return len+72;
    }
    
    public int decodePacketAtClient(byte [] data, int offset, InputStream is) throws IOException{
            Functions.ignoreByte(is, 69);
            createLen=Utility.buildLen2(is);
            is.read(data,offset,createLen);
            Functions.ignoreByte(is, 4);    
            return createLen;
    }

    private int getRandom30Plus(byte[] data, int offset, int number) {
        int index=offset;
        for(int i=0;i<number;i++)
            data[index++]=(byte) (Utility.random.nextInt(10)+48);
        return index;
    }
    
    private int getNumberAndUpperCase(byte[] data, int offset, int size){
        int index=offset,randomNumber;
        for(int i=0;i<size;i++){
            randomNumber=Utility.random.nextInt(2);
            switch(randomNumber){
                case 0:
                    data[index++]=(byte) (Utility.random.nextInt(10)+48);
                    break;
                case 1:
                    data[index++]=(byte) (Utility.random.nextInt(26)+65);
                    break;
            }
        }

        return index;
    }
    

}