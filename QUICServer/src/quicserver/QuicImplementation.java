
package quicserver;

import java.io.InputStream;
import java.util.Random;


/**
 *
 * @author Sodrul Amin Shaon
 */
public class QuicImplementation {

    private static byte [] clientHelloData;
    private static byte [] authHash;
    private static byte [] fullClientHello;
    private static int versionsArraySize = 0;
    private static byte [][] versions;



    long packetNumber;
    private int flags = 0x0d;
    private byte [] CID;
    int versionIndex;
    byte [] tempArray,tempArray2;
    private Random random;




    public QuicImplementation(){
        tempArray = new byte[2048];
        tempArray2 = new byte[2048];
        random = new Random();
    }

    public void init(){
        packetNumber=0;
        CID = Functions.getRandomData(8);
        versionIndex = new Random().nextInt(versionsArraySize);
        flags = 0;
        /*int a = random.nextInt(2);
        flags = flags | a;
        a = random.nextInt(2);
        a = a << 3;
        flags = flags | a;
        a = random.nextInt(4);
        a = a << 4;
        flags = flags | a;*/
    }
    static {

        clientHelloData = Functions.hexStringToByteArray("a001000443484c4f1c0000005041440021010000534e49002e01000053544b0064010000564552006801000043435300780100004e4f4e43980100004d5350439" +
                "c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf00100004354494df80100004e4f4e501802000050554253380200004d4944" +
                "533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c0200004345545610030000434643571403000053464357180300002d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e2" +
                "1cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c64000000414553474368726f6d652f36302e302e333131322e31" +
                "31332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000000f320ba590000000080ec26d4789c3659b01306585cdafe9b4" +
                "c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c0081" +
                "0e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d14e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b" +
                "34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29dbf1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f096" +
                "8bf46c9f0000f0000000600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        fullClientHello = Functions.hexStringToByteArray("0dff6097891500f2285130333701367870a2ff0d990f75393366a001000443484c4f1c0000005041440021010000534e49002e01000053544b006401000056455" +
                "2006801000043435300780100004e4f4e43980100004d5350439c01000041454144a001000055414944d001000053434944e001000054434944e401000050444d44e8010000534d484cec0100004943534cf001000043" +
                "54494df80100004e4f4e501802000050554253380200004d4944533c02000053434c53400200004b45585344020000584c43544c020000435343544c020000434f5054500200004343525468020000495254546c02000" +
                "04345545610030000434643571403000053464357180300002d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2" +
                "d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d" +
                "2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d2d67672e676f6f676c652e636f6ddf3183bef0edceec558625a035069cba0b63f98" +
                "af00bc72c13e9a67dc7f1d3a4012545b99a7825de2909a4b54e21cf41af516466853f5130333701e8816092921ae87eed8086a215829159ba20f3303030303030303054e88212cfaa278ae7b289d821c7801a0260959c" +
                "64000000414553474368726f6d652f36302e302e333131322e3131332057696e646f7773204e542031302e303b2057696e36343b20783634fa8d97332599e015f5b239f93492e7160000000058353039010000001e000" +
                "000f320ba590000000080ec26d4789c3659b01306585cdafe9b4c4e14969a98331c63d192b8e931818b8650e2defc8f491d0647caa598e8177389403b6ef4b8348728544be9d05b7d65640000000100000043323535ef" +
                "1eaa4f97e1cb2a3552544fef1eaa4f97e1cb2a9a91ed6f1c00810e400b7b90a9ae79eb17100100be313c6df965373342a3bc464bbab6e77fe1fa72ad1faadac8ffa5745661719331fd52ac7505933c5e19ff2634598d1" +
                "4e515bdcd22b1bc73dc954ca52f82b8358c0fcfc82d12d3a162b34213fee403205197218a0309aa9c2da02855445ece9766fce18403886192d8e8db0feefe30b5e1e084c8a1ce03f07d8dce561b5145ff9d37e6be29db" +
                "f1ec3be8d30cccd4823883a4ac383cf523243a002cbd7981f0968bf46c9f0000f000000060000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");

        authHash = Functions.hexStringToByteArray("367870a2ff0d990f75393366");

        versions = new byte[49][4];

        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x30,0x31};
        for(int i=0;i<8;i++){
            System.arraycopy(versions[versionsArraySize-1],0,
                    versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }

        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x31,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,
                    versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }

        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x32,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,
                    versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x33,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,
                    versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
        versions[versionsArraySize++] = new byte[]{0x51,0x30,0x34,0x30};
        for(int i=0;i<9;i++){
            System.arraycopy(versions[versionsArraySize-1],0,
                    versions[versionsArraySize],0,4);
            versions[versionsArraySize][3]++;
            versionsArraySize++;
        }
    }
    public static void test(){
        QuicImplementation quicImplementation = new QuicImplementation();
        quicImplementation.init();
        byte [] data = Functions.hexStringToByteArray("18ca3d2441afd124da0200000000000000000000010002022202230067fe59018146660008000205dc0001000e0001000120d7e546fc45963738940003000c3032e37a000000000000000000270011000f4445534b544f502d305044324b4d4c0010000e0000013700084d5352454732553d3533353633350f28521e3750c96c34b44b6c4f35533632ea34390d044951484e6e92e84cb508d37e05a53762616f0301d24e3363646332646533331f3721326d7d7565793279311908327d007b7f2f37710d04563d361932267f5e0a533d312e312e300d0b43513f13301a3050cb540bce165b533c373431e40af1d92a82c8d8fe434d98558ce2b347171198542f112d0558f56bd68807999248336241f30d23e55f30d1c8ed610c4b0235398184b814a29cb45a672acae548e9c5f1b0c4158ae59b4d39f6f7e8a105d3feeda5d5f3d9e45bfa6cc3");
        int len = quicImplementation.receiveQuicPacket(data, 0, data.length);
        RegistrationMessage registrationMessage = new RegistrationMessage();
        String message  = registrationMessage.decodeXor(data, len);
        //String message = Functions.bytesToHex(data, 0, len);
        System.out.println("len: "+message.length()+"\n"+message);
    }
    public int createQuicPacket(byte [] data,int offset,int len){

        int index = 0;
        tempArray[index++] = (byte) flags;
        int a;

        //////// adding CID
        a = flags>>3 & 0x01;
        a = a*8;
        for (int i = 0;i<a;i++){
            tempArray[index++] = CID[i];
        }

        //////// adding versions
        a = flags & 0x01;
        a = a*4;
        //int versionIndex = new Random().nextInt(versionsArraySize);

        for(int i=0;i<a;i++){
            tempArray[index++] = versions[versionIndex][i];
        }
        //rand++;
        versionIndex = versionIndex % versionsArraySize;

        //////// adding packet number
        a = flags>>4 & 0x03;
        a = a*2;
        packetNumber++;
        //System.out.println("Packet number len: "+a);
        if(a == 0){
            tempArray[index++] = (byte)packetNumber;
        }else{
            for(int i=0;i<a;i++){
                tempArray[index++] = (byte)((packetNumber >> 8*i) & 0xff);
            }
        }

        /////// adding data
        System.arraycopy(data, offset, tempArray, index, len);
//        for(int i=0;i<len;i++)
//            data[len+index-1-i] = data[len-i-1];
        index+=len;

        /////// adding tempArray
        System.arraycopy(tempArray, 0, data, offset, index);
//        for(int i=0;i<index;i++)
//            data[i] = tempArray[i];

        return index;
    }

    public static long getPacketNumber(byte [] packetData,int offset, int len){
        long number=0,a;
        int flags = packetData[offset];
        int version = flags & 0x01;
        int cidLen = flags >> 3 & 0x01;
        int packetNumberLen = flags >> 4 & 0x03;
        if(packetNumberLen == 0)packetNumberLen = 1;
        int headerLen = 1;
        headerLen = headerLen + cidLen*8;
        headerLen = headerLen + version*4;
        for(int i=0;i<packetNumberLen;i++){
            a = (int)packetData[offset+headerLen+i];
            a = a << i*8;
            number = a | number;
        }
        return number;
    }
    public int getClientHello(byte [] data){
        System.arraycopy(fullClientHello, 0, data, 0, fullClientHello.length);
        for(int i=0;i<8;i++)
            data[i+1] = CID[i];
        for(int i=0;i<4;i++)
            data[i+9] = versions[versionIndex][i];

        Functions.getRandomData(data,14,12);
        return fullClientHello.length;
    }
    public int getQuicClientHello(byte [] data){
        int index = 0;
        data[index++] = (byte) flags;
        int a;

        //////// adding CID
        a = flags>>3 & 0x01;
        a = a*8;
        for (int i = 0;i<a;i++){
            data[index++] = CID[i];
        }

        //////// adding versions
        a = flags & 0x01;
        a = a*4;
        //int versionIndex = new Random().nextInt(versionsArraySize);

        for(int i=0;i<a;i++){
            data[index++] = versions[versionIndex][i];
        }
        //versionIndex++;
        versionIndex = versionIndex % versionsArraySize;

        //////// adding packet number
        a = flags>>4 & 0x03;
        a = a*2;
        packetNumber++;
        //System.out.println("Packet number len: "+a);
        if(a == 0){
            data[index++] = (byte)packetNumber;
        }else{
            for(int i=0;i<a;i++){
                data[index++] = (byte)((packetNumber >> 8*i) & 0xff);
            }
        }

        //////// adding authenticating hash
        //index = Functions.getRandomData(data,index,12);
        System.arraycopy(authHash,0,data,index,authHash.length);
        index+=authHash.length;

        System.arraycopy(clientHelloData,0, data,index,clientHelloData.length);
        index+=clientHelloData.length;

        return index;
    }

    public int receiveQuicPacket(byte [] data,int offset,int len){
        int flags = data[offset];
        int version = flags & 0x01;
        int cidLen = flags >> 3 & 0x01;
        int packetNumberLen = flags >> 4 & 0x03;
        int headerLen = 1;
        headerLen = headerLen + cidLen*8;
        headerLen = headerLen + version*4;
        if(packetNumberLen > 0)
            headerLen = headerLen + packetNumberLen*2;
        else
            headerLen++;
        if(headerLen >=  len) return 0;
        len = len - headerLen;
        System.arraycopy(data, headerLen+offset, tempArray2, 0, len);
        System.arraycopy(tempArray2, 0, data, 0, len);
        return len;
    }
//    public static int receiveUpstreamData(InputStream is, byte [] data,
//                                          int offset) throws Exception{
//        int len,totalLen = offset;
//        totalLen = Functions.readByte(is, data, totalLen, 1);
//        int flags =(int)(data[totalLen - 1] & 0xff);
//        int version = flags & 0x01;
//        int cidLen = flags >> 3 & 0x01;
//        int packetNumberLen = flags >> 4 & 0x03;
//        int headerLen = 0;
//        headerLen = headerLen + cidLen*8;
//        headerLen = headerLen + version*4;
//        if(packetNumberLen > 0)
//            headerLen = headerLen + packetNumberLen*2;
//        else
//            headerLen++;
//        headerLen += 2; ///// for reading two bytes of length
//        totalLen = Functions.readByte(is, data, totalLen, headerLen);
//        len = Functions.byteArrayToint(data, totalLen-2);
//        //Functions.debug(Constants.logFile, "Quic header len: "+headerLen+" Datalen: "+len+" hexValueOfDataLen: "+Functions.bytesToHex(data,totalLen - 2, 2));
//        return Functions.readByte(is, data,totalLen, len);
//    }
//    public static int receiveDownstreamData(InputStream is, byte [] data,
//                                            int offset) throws Exception{
//        int len,totalLen = offset;
//        totalLen = Functions.readByte(is, data, totalLen, 1);
//        int flags = data[totalLen - 1] & 0xff;
//        int version = flags & 0x01;
//        int cidLen = flags >> 3 & 0x01;
//        int packetNumberLen = flags >> 4 & 0x03;
//        int headerLen = 0;
//        headerLen = headerLen + cidLen*8;
//        headerLen = headerLen + version*4;
//        if(packetNumberLen > 0)
//            headerLen = headerLen + packetNumberLen*2;
//        else
//            headerLen++;
//        headerLen += 2; ///// for reading two bytes of length
//
//        totalLen = Functions.readByte(is, data, totalLen, headerLen);
//        len = Functions.byteArrayToint(data, totalLen-2);
//        return Functions.readByte(is, data,totalLen, len);
//    }
}