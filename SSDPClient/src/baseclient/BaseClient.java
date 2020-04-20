package baseclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class BaseClient {
    static int lowerRangeOfPort=1100;
    static int highestRangeOfPort=1120;
/**
 * messenger port 1026
 * mpeg ts port 5500
 * pana port 3001
 * pipv2 port 319
 * S-bus port 5050
 * ts2 port 8767
 * dhcpv6 port 547
 * wsp port 19200
 * wow port 8000
 * MTP port 1701
 * mount port 1048
 * stat port 1011
 * rquota port 702
 * klm port 624
 * mndp port 5678
 * ipmi port 623
 * adwin config port 64515
 * adp port 8200
 * raknet port 27196
 * tcp/ndmp port 10000
 * dtls port 4433
 * powerlink port 3819
 * CUPS port 631
 * teredo port 3544
 * 6LowPAN port 17754
 * lon port 1628
 * opensafe port 3819
 * dis port 3000
 * hiqnet port 3804
 * hcrt port 47000
 * llmnr port 5355
 * dspv2 port 3567
 * tepv1 port 3567
 * 
 * 
 ***/
    
    static int clientToServerPort=3567;
    static int numberOfPackets=2;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
//    static CIGIImplementation cigi=new CIGIImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Base UDP Client Started...........");
        while(true){
            
            DatagramSocket ds=new DatagramSocket();
            MySender mySender=new MySender(ds);
            mySender.init();
            Thread myReceiver=new MyReceiver(ds);
            myReceiver.start();
            i++;
        }
       
    }

    private static class MySender {
        DatagramSocket ds;
        public MySender(DatagramSocket ds) {
            this.ds=ds;
        }

        public void init() {
            try {
                int i=0,j=0;
                int countsend=0;
                while(i<numberOfPackets){
                    int len=100;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
//                    System.out.println(hexdata);
                    
                    int len2=150;
                    byte[] data2 = new byte[len2];
                    data2=Utility.getRandomData(data2, len2);
                    String hexdata2=Utility.bytesToHex(data2,0,len2);
//                    System.out.println(hexdata2);
                    
                    len2=14;
                    byte[] data3 = new byte[len2];
                    data=Utility.getRandomData(data3, len2);
                    String hexdata3=Utility.bytesToHex(data,0,len2);
                    
                    
                    int len4=4;
                    byte[] data4 = new byte[len4];
                    data=Utility.getRandomData(data4, len4);
                    String hexdata4=Utility.bytesToHex(data,0,len4);
//                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
                    
                    byte bid=(byte) i;
                    String id=Utility.byteToHex(bid);
                    System.out.println("id ----> "+id );
                    
                    
                    
//                    tepv1
                    String m="810c12018220041e808011060f7468656561737468616d732e6f7267b4026001030101f9c701000316627279616e74407468656561737468616d732e6f726771454b3f645b57240301041f00000080820313031131407468656561737468616d732e6f7267";
                    
//                    DSPv2
//                    String m="8104128260031131407468656561737468616d732e6f7267031e0006"
//                            + "0000"+hexdata;
                    
                    
//                    LLMNR
//                    String m="50490000000100000000000004777061640000010001";
                    
//                    HCrt
//                    String m="90ff010004000000fecaefbe90ff01800a000000fecbefbb";
                    
//                    HiQNet
//                    String m="021900000048ed7200000000ffff0000000000000004040005ed720100108d1ed665fe64bf44afae3e5075b5548000100000271001080027980c9301c0a8010cffffff00c0a80101";
                    
//                    OpenSafe
//                    String m="060000050e120000000000312000000000000000f0bf0101"
//                            + "02a806000060650006a11c03a80001000060650006a13100";
                    
//                    dis
//                    String m="06011a0400000000005800000030000100010001400300640000000001c00000000600ffff00000000000000ffffffffffffffff0000009009080000d80bf0ba0000ffe42c6b4b2acd74b734350500059200720700000001";
                    
                    
                    
                    /** PPP MuxCP 8059, first hex20, second hex94 **/
//                    String m=hexdata+"8059010100640060"+hexdata2;
                    /** PPP OSINLCP 8059, first hex14, second hex94 **/
//                    String m=hexdata+"621b2f7e03f1"+"8023010100640060"+hexdata2;
//                    String m="5e1d0bdd0000000000000002000186a3000000030000001300000001000000343847760b00000009776572726d736368650000000000000000000001000000050000000100000000000000020000000300000011000000000000000000000020"+hexdata;
//                    String m=hexdata2+"00000000"+"00000002"+"000186a3"+"0000000300000013"+hexdata4+"00000034"+hexdata3+"0000000000000000"+"00000064"+hexdata;
                    
//                    String m="3025020101632004000a01020a0100020100020100010100870b6f626a656374436c6173733000";
//                    String m="3081a90201016381a304818264633d"+hexdata+"0a01020a0100020100020100010100870b6f626a656374436c6173733000";
                    /**working cldap**/
//                    String m="3026020101632104000a01020a0100020100020100010100a50c0403756964040541646d696e30000000"+hexdata;
                    
//                    String m="300c0201016007020103040080000000"+hexdata;
//                    String m="c80200764a3200000000000180080000000000028008000000020100800a00000003000000000008000000061130800900000007367065001900000008436973636f2053797374656d732c20496e632e80080000000905f780080000000a03e880160000000b508154fa7878436c331b3a2b11431373";
//                    /SMB not working
//                    String m="ff534d42a2000000000801c80000000000000000000000000100441c6500060018ff000000000e0000000000000000009f01020000000000000000000000000003000000010000000000000002000000001100005c007300720076007300760063000000";
//                    /rx not working
                    
//                    String m="a13ba33e5b27adcc000000000000000000000001060000020000003400000002845fa3390000000000000000";
//                    kerberos still at pause
//                    String m="6c820203308201ffa103020105a20302010ca38201923082018e3082018aa103020101a28201810482017d6e82017930820175a003020105a10302010ea20703050000000000a381e26181df3081dca003020105a1091b07554d522e454455a21c301aa003020100a11330111b066b72627467741b07554d522e454455a381ab3081a8a003020101a103020172a2819b0481986cb05fd9ad09ae9c62a8f8e73b57d6ec2fe816e9b42f0f6e301b5b6dcd1434d229039f43210a08ea1e7fcffb5d3fb0d57a6da17b80f49e9938b8e16d0195fe894cd16b202fa9b6e403f21f64192d68eca1a623f3de3355be9160f8685d4139391fbfc484cca1d23f660d634a477ca7ed58ab91437f050e68f9eb7822f91df670929bda3ab4874042f6fb7bc7e87ca8af45bfbfdeeddb8873a47b3079a003020101a272047090f8782d33c4399ff546a6942b5f7e1cb5fb0e72efe6e04a702a6481550aafe4feb08e79921e15dbefb59a7116f43c595445b80ef5ea6f5632a8e6615d5e54670c90691bc3d3ed77c7a3c746ecdbeb10c7e42cf454c33208de9f68924fe1c13f478625be6f80f6465d6a55b62daf9227a45d305ba00703050040000000a2091b07554d522e454455a310300ea003020100a10730051b03616673a511180f31393939313231313231343830335aa7060204382b3993a8053003020101a911300f300da003020102a106040483972015";
//                    BFD control message works fine
//                    String m="204405210000000100000000000f4240000f424000000000010902736563726574";
                    
//                    Chargen not working
//                    String m="68656c6c6f206368617267656e0a";
//                    Portmap not woking 
//                    String m="3657547a0000000000000002000186a0000000020000000500000001000000203650f3ce0000000b68616e73656e2d6c616233000000000000000000000000000000000000000000"
//                            + "000186a40000000200000002000000100000000c62696f6368656d6973747279";
//                    QUIC protocol full blocked
//                    String m="40280c51425aa103caebcf9d39723843d94b54dc5f616c1c95e1fdb60e6a076b3d659c3a849cb332717b20e1831fc5ea6647088dce0fd6a5e07f852ddf7457cf8641593c0061a3a9872fde71b85d6d71525b71ea774a19cc3c8abeacc4ff65e0791359c8db567817e9103a2997cde8cc1f8cd9236612ae61a4fe48685a45fcf5420949c3ede9753b22696b5ca124239dbad061f7760b5e40f1edc4cb26f7c0ff8f700cd75a8bdea2a5fac07460b5575c78bb9981ceed727fb9b6e2b3ce9e1405c7471e831c92dd8c1caf75a3b469017a6a15c12753e15e3720e8982e12bd1c57140c36a479bd7f64c3690a3509bf54dc7148dd9fd8b651cec4f7506ba5fd513fbecad36b8d52bafdef7444c5310c1d3ab4c9f0d79eec2c3a6a6dedcf339322bbc09ad0f95de2f11a898063760a9663bb4e77639db4c03bca283e0aa2f69cd6067ba9d88bfb8d6e618c154ec8c30a18c36dcb4c47d42ee96db8cb098d84de9caad8e3dfd5b2b8632a3ab95c9be7d7e6980b1008c68d68927ce8a994cb10a9a05498a4149bdffd97b5ade072c0a4ee38ca8ca61b98e6982d1074952a753363edf95c8ce08c054b9a7494dca8fe1203a40b26ab90d8ace1a502c004";
//                    NBDS Failed
//                    String m="010080fc0a0041434144454d593034000500000000000501031001000f0155aa41434144454d59303400";
//                    String m=hexdata+"0000"+hexdata2;
//                    rip v1 failed
//                    String m="0101000000020000c0a8650000000000000000000000000400020000c0a8660000000000000000000000000200020000c0a86b0000000000000000000000000300020000c0a86e0000000000000000000000000200020000c0a86f0000000000000000000000000300020000c0a8700000000000000000000000000300020000c0a8710000000000000000000000000400020000c0a8750000000000000000000000000200020000c0a8770000000000000000000000000400020000c0a8c80000000000000000000000000200020000c0a8c90000000000000000000000000200020000c0a8ca0000000000000000000000000200020000c0a8cb0000000000000000000000000200020000c0a8cc0000000000000000000000000200020000c0a8cd0000000000000000000000000200020000c0a8ce0000000000000000000000000200020000c0ee150000000000000000000000000400020000c642400000000000000000000000000200020000c642410000000000000000000000000200020000c642420000000000000000000000000200020000c642430000000000000000000000000200020000c642440000000000000000000000000200020000c642450000000000000000000000000200020000c642460000000000000000000000000200020000c6424700000000000000000000000002";
//                    String m="0101"+hexdata;       
//                    bootp Not Working
//                    String m="010106000000000000000000816f03780000000000000000816f010d0800870a3c450000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000783061336334352e70726d00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                    
//                    Hart IP not showing hart IP
//                    String m="010003000003001182264e0000d2000038";
//                    DRSUAPI not showing drsuapi
//                    String m="05000003100000006d002d0003000000140000000000010010c174f0d7d25eb52760e82b08547a6852d51f74316292c8c0aab54c43cd412009060c00d841e600602b06092a864886f712010202020111001000ffff70c88f20c38627a36bb5490324e28fb736cdcae63ae0db49";
                    
//                    messenger per socket 1
//                    String m="040028001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fc4808b1e3fe06a6b6aae112fee8ba64a10000000001000000000000000000ffffffff66010000000010000000000000001000000053595354454d00000000000000000000100000000000000010000000414c45525400000000000000000000002201000000000000220100004d6963726f736f66742057696e646f77732068617320646574656374656420796f757220696e7465726e65742062726f777365720a697320696e666563746564207769746820537079776172652c2041642d776172652c20616e64205468696566776172652e0a0a596f7572207072697661637920697320696e2064616e6765722e0a5765207265636f6d6d656e6420616e20696d6d6564696174652073797374656d207363616e2e20506c656173652076697369740a0a687474703a2f2f53776970655370792e636f6d0a0a4661696c75726520746f20646973696e6665637420636f756c6420616c6c6f772074686972642070617274696573206163636573730a746f20796f757220706572736f6e616c20696e666f726d6174696f6e2e0a00";
//                    String m="040028001000000000000000000000000000000000000000f8917b5a00ffd011a9b200c04fb6e6fc4808b1e3fe06a6b6aae112fee8ba64a10000000001000000000000000000ffffffff66010000000010000000000000001000000053595354454d00000000000000000000100000000000000010000000414c4552540000000000000000000000220100000000000022010000"+hexdata;
                    
//                    MPEG blocked
//                    String m="4702001e"+hexdata2;
                    
//                    PANA blocked
//                    String m="0000"+"0010"+"c0000002235c04d111b86f1a00060000000c00000000000200030000000c000000000007";
//                    String m="0000"+"0010"+"c0000002235c04d111b86f1a";
                    
//                    PIPV2 failed 20%
//                    String m="1202003600000000000000000000000000000000008063ffff0009ba00019e48050f000045b111510472f9c100000000000000000000";
                    
//                    S-bus fully bloked
//                    String m="0000000d01000001000a205318";
                    
//                    TeamSpeak protocol TS2
//                    String m="f4be0300000000000000000001000000214583fd095465616d537065616b00000000000000000000000000000000000000001d4c696e757820322e362e32322d31342d67656e6572696320233120534d0200000020003c0000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000662726f6f73730000000000000000000000000000000000000000000000";
                    
//                    DHCPv6 1 packet per socket
//                    String m="03"
//                            + hexdata4
//                            + "0001000e"
//                            + "000100011c38262d080027fe8f95"
//                            + "0002000e000100011c3825e8080027d410bb00060004001700180008000200000019002927fe8f9500000e1000001518001a001900001c2000001d4c40200100000000fe000000000000000000";
//                           
//                    wsp working well
//                    String m="70407f687474703a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a3a2f2f3132372e302e302e312f696e6465782e776d6c8080";
                    
//                    WOW not working
//                    String m="474554202f20485454502f312e300d0a557365722d4167656e743a20576765742f312e352e330d0a486f73743a203139322e3136382e302e323a383030300d0a4163636570743a202a2f2a0d0a0d0a";
//                    MTP not working
//                    String m="0100010100000044020000080000000a00060008000000b10210002a00001f4100001f470300000c060161da0001130014400f00000200104003028302003b4001000000";
//                    Mount working 
//                    String m="3841169f0000000000000002000186a500000001000000010000000100000034384775d000000009776572726d7363686500000000000000000000010000000500000001000000000000000200000003000000110000000000000000"
//                            + "000000142f686f6d652f6769726c6963682f6578706f7274";
//////                    STAT 
//                    String m=hexdata4+"0000000000000002000186b8000000010000000200000000000000000000000000000000"
//                            + "000000096d6f6e5f6e616d6531000000000000086d795f6e616d653100030d70000000010000000131323334353637383941424344454600";
//                    
//                   RQUOTA
//                    String m=hexdata4+"0000000000000002000186ab000000010000000200000000000000000000000000000000"
//                            + "00000003633a5c0000000000";
//                    
//                    KLM
//                    String m="1f5fd6940000000000000002000186b4000000010000000100000000000000000000000000000000"
//                            + "000000000000000b536572766572204e616d650000000005313233343500000000000001000000030000001b";
//                    nbds blocked
//                    String m="2c00ffef0800000000000000574f524b47524f55502020202020201d5746575f484f53545f32202020202000"
//                            + "ff534d42250000000000000000000000000000000000000000000000000000001100002d000000000000000000000000000000000000002d00560003000100010002003e005c4d41494c534c4f545c42524f575345000107000000005746575f484f53545f320000000000000133000000000b0355aa4269742053686f76656c657200"
//                            + "0100010002003e005c4d41494c534c4f545c42524f57534500"
//                            + "0107000000005746575f484f53545f320000000000000133000000000b0355aa4269742053686f76656c657200"
//                            + "";
//                    BROWSER
//                    String m="110280d0c0a87b01008a00a5000020455045434644454a4545454a4542454f4341434143414341434143414341414100204644464a454f45464643454a4645464a4341434143414341434143414341424e00"
//                            + "ff534d42250000000000000000000000000000000000000000000000000000001100000b000000000000000000e80300000000000000000b0056000300"
//                            + "0100010002001c005c4d41494c534c4f545c42524f57534500"
//                            + "02004f4253494449414e00";
                            
//                    SNMP new version
//                    String m="3081c90201033011020430f6f3d7020300ffe304010702010304373035040d80001f888059dc486145a2632202010802020ab90405706970706f040c87b6e330d3123d6f23bdb5f704080000000103d5321c0478e7ecebfbc75eecd42d25815a8d3b7d293b8a42d9002a86fa10b80bf1f1ca53aada57d520644c254f02331f1936831692559dea670efec3055c92721879e6bb3032dd0d040b4c29d17b08813974f4ddedb08e160719bb00e8f7a763a2ffcdd2d4b88169b50e012681dded3ae85ae6d7002c23274189aaa347"
//                            + "";
                    
//                    MNDP
//                    String m="0000318b00010006000c4220710200050009726f7574657246474e00070007352e3072633130000800084d696b726f54696b000a0004fb8c0e00000b00094e5759392d49373957000c0006524231303030000e000101000f0010200106380208cd000000000000000001";
                    
//                    IPMI
//                    String m="0600ff07"
//                            + "0019000000eaeb9c0b0b"
//                            + "2028b8814c11fe6e0016a0";
//                     RMCP
//                    String m="0600ff07";
//                       Adwin config
//                    String m="00020000e091f597516c0003000a00000c07d2f200000000000000020000000000000000000000000009000c70617373776f7264";
                    
//                    ADP
//                    String m="01005e28323c0007ed0fb07f08004500055cf204400080112c75c0a8010ae328323c20002008054800008060c4e1000000000000000061cd00008000000000000354400106000000442d0c8c3b33ea7c23735b8c6bcb51b289c6fab16f09ef62f79a592baf213d2759d22e9b219515bf2ebf4949c37a28d475236db58dae0023210acb1f8dd8de2a90efcea655b060b86bcf8521ab9096b1e237882ee2ced6fd4ae4c510018f7e16ec33caeb3f7ca78f33309c9fa1f9e78a29006f02017a87f6f1a973c7cb5103f12748c4d9c0058b869aeee2abf6fd5d173ab4be20e93f97c15c532893ec121c978531850455aaf04efc6c9a3ddb6753321bec13f57762bbd392a800400a2ec6f7e2e8983e4deab0d1392135e0b5078420cc389222c9516708545902dd579cff75a4403298b2ee80513d24c00d39b0cc766d711a1e801c5a34666accb073be7895943b60e367bb508401bf46771b830015510caac94b3ab7ef875f1743af49520940585243271402492c1c69d501362ef0a5d132d10ba49fb5863f24bf066e8d4881e6090682430272dce8690e66698a3ff72f0a4ebdb53be330dacd51221690dd558c8cd733c6581f618ce5860f634dfde34de017b0070040000eee8e4aed15a6179b96c1997b4a71ee40cb3afd19238d6a25426a0654584ffcced45d1ed3b5a199429c40b7361ced68b864c56a9eda4452e8c566f3f686a2dd54cd209c5e47699c9af7d2c649e64bb8318ef548c0375d0041e2e6f68637e8aeb2d4e3dc2946996e7fd40acf58ded0ccee0647321c75868093a69702eb10fd7ffa4323bd4edc19ccbff2e844bdbb35e733500bc20623db0f934fb11570a491a8a8ab20898b2cbcbb16c4c0590a9d483f75eef0958be0761e5400000002f3e934a86adfdf014ea6ddeff0b9defa88d292b97ec0411172981c443f77a6368f1c17954eaa13097fa05378e8c8eb9b4c63850fbefa6ac37c77e49c26aa68dbb06e63006ee32520d2359253f173aa61be74ba5884506caaf9381cfa8e6c1adff8c2ebd191eae53c1adafd48dbc06c7f59196b46802272edf430a84fa0075b0594b6f3860e46325e93efb85dd3588141cc276bb1e4cd55a7b09960c392325011d92fd617fdab0a44e2b3f3dcef4c87ead434adb6af01cefa00000002e25a96c62f8c3c8fc5e06d58d8f517b747209aadb387b44d43846842d776082e27f5ec0a206b1f390d892eab4fb458bb39417773fb5d4cfbeff3457be721b0af8609661b69c78cee9827ae08f2b08644009bc648e6712892ba5370acf80bbcfb69e40346515d875294119b26698b89b06476b6870da2771e03c46111d5c77a7cace514ceabd367ce96158053586896b9d5ca0240f84968ffab44a2726fb7a481ef23b6588656be4b82decb89b5cf17bed65e654ac7d5f6990000002e3f4c8697cc0c31a92ab4c516de599299c2ca36d9d3c3843fb2c5b82932f34225269a2d02fe8e984afa986e6800afcb826828f287d6e36efa5c6e2cf184cba4f62513974d981c6019977c4ce93ae7865d1f36e3588b38692d3ef1113d21c1fd2e9a73f03f989180385cfbb3ca8a6146ce4212a8a2960085a75a01f9ed48f60cd24beb460b9cc76204180e49cb816cb68880e3e8de5c2b93366d7240c0da4bfbe40a046ef0ba7ca7257ee9f7b164ba8e08aeb46b4a1d0def47005f9b2678388421856ba96cc961cd2168a40a79d292eff036589994de4064d675ddb6ca3bdc9e22baebffaa0a2ce44b7a3dfdde9f842b651c03a47a7df02f26541621f9162e5e70e2cd49d4b0de712536e404f506d95fe5fc22c4303c83e7249c858721dd5dbaf3a9e714f1ea4a7f17e22c66a370f83b3acf20d274d2bd2454ae0712de3b5ec43994b72799a0a46be287a7e8f321057fb8aa6b15dc8ccddf358b7f1d992a3c5e32c52d1aa19679813c5a82aa3f251efca38742b82e";
                    
//                    RakNet
//                    String m="841b00004000481000000000000000000102ff";
                    
//                    NDMP
//                    String m="800000340000000244a4de07000000000000090100000000000000000000000200000004726f6f747d28e56ecd1b6baed6e81c7075cc9da4";

//                    DTLS
                    
//                    String m="1601000000000000000001006a0100005e000100000000005e0100448d7528970b922d9bb345ca87a675fb53cbce274fafe17eb9c2ec8b4b5d645a0000003600390038003500160013000a00330032002f0007006600050004006300620061001500120009006500640060001400110008000600030100";
//                    Powerlink/udp need some work
//                    String m="0600000500010000"
//                            + "000000"+hexdata;
                    
//                    Kerberos
//                    String m="6a81ed3081eaa103020105a20302010aa36430623048a103020102a241043f303da003020117a2360434994f978a2bdf09fe8fcdbbcdc54f9b377b34bd4a50a1145ee6eb6c64f3b703e65db696c52514128cf6dc1f54afa83d90d0db535b300aa10402020096a2020400300aa10402020095a2020400a4783076a00703050040800000a1133011a003020101a10a30081b06617061636865a2061b044e442e43a3193017a003020102a110300e1b066b72627467741b044e442e43a511180f32303138313230343233333233325aa611180f32303138313231303233333233325aa7060204660c8e73a8053003020117";
                    
//                    cups
//                    String m="663034652033206970703a2f2f31302e302e302e3139323a3633312f7072696e746572732f41646f626550444638202222202241646f62652050444620382e3022202241646f62652050444620333031362e31303222206a6f622d7368656574733d6e6f6e652c6e6f6e65206c656173652d6475726174696f6e3d3330300a"
//                            + "";
                    
//                    ICMPv6 working
//                    String m="00010000cd5669400b22df88006000000000183afffe800000000000008000fffffffffffdff0200000000000000000000000000028500a91d0000000001020000000000008000f12ab9c82815"
//                            + "";
//                    6LowPAN working
//                    String m="4558020100000101ff000cd1368bbd273d0005c639000000000000000000007b"
//                            + "41cca6ffff8a1800ffffda1c00881800ffffda1c00c109000242fa400401f0b101066faf48656c6c6f20303036203078464633430a0012131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f50515253546879";
                           
//                    LON
//                    String m="00200101000000006b8b45670000000000000000"
//                            + "010901aa01a90103810d00ca";
                    
                    
//                    int offset=0,len=61;
//                    
//                    byte[] newdata=new byte[offset+len+61];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
//                    System.out.println("--------------> ");
//                    System.out.println(m1);
//                    
//                    len2=cigi.createPacket(newdata, offset, len);
////                    System.out.println("================================>          "+ len2);
//                   String m=Utility.bytesToHex(newdata,offset,len2);
//                   System.out.println(m);
//                   
                    byte[] b1=Utility.hexStringToByteArray(m);
                    
                    InetAddress ia=InetAddress.getByName("191.96.12.12");
//                    InetAddress ia=InetAddress.getByName("191.101.189.89");
//                    InetAddress ia=InetAddress.getByName("localhost");
                    DatagramPacket dp=new DatagramPacket(b1, b1.length,ia,clientToServerPort);
                    ds.send(dp);
                    countsend+=1;
                    String message=new String(dp.getData(),0,dp.getLength());
                    
//                    System.out.println(message.length()+" Send from client---> : "+message);
                    System.out.println("---Send Packet---------------------------------> "+ countsend);
                    totalSend+=1;
                    System.out.println("---Total Packet Send---------------------------------> "+ totalSend);
                    Thread.sleep(200);
                    i++;
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static class MyReceiver extends Thread {
        DatagramSocket ds;
        public MyReceiver(DatagramSocket ds) {
            this.ds=ds;
        }

        @Override
        public void run() {
            try {
                int startTime=(int)System.nanoTime();
                int countreceive=0;

                while(true){
                    int currentTime=(int)System.nanoTime();
                    int checkTime=currentTime-startTime;
                    if(checkTime>=receiverTime || countreceive==numberOfPackets){
                        ds.close();
                        System.out.println("-------------------------------------------------------------Socket closed-----> "+ds);
                        break;
                    }
                    
                    byte[] b1= new byte[2048];
                    
                    DatagramPacket dp1=new DatagramPacket(b1, b1.length);
//                    dp1.setPort(clientReceivedPort);
                    ds.receive(dp1);
                    countreceive+=1;
                    String received= new String(dp1.getData(),0,b1.length);
//                    System.out.println("--------received-----");
//                    System.out.println(received);
//                    System.out.println("Received at client:-->----------------> "+ countreceive);
                    totalReceive+=1;
                    System.out.println("Total Received at client:-->----------------> "+ totalReceive);
                    
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }

    }
}
