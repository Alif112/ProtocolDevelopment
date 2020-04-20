package kerberosclient;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class KerberosClient {
    static int clientToServerPort=88;
    static int numberOfPackets=5;
    static int totalSend=0;
    static int totalReceive=0;
    static long receiverTime=(long) 1e9;
    
    static KerberosImplementation l2tp=new KerberosImplementation();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException, InterruptedException {
        
        int numberOfSockets=10;
        int i=0;
        System.out.println("Udp L2TP Client Started...........");
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
                    int len=150;
                    byte[] data = new byte[len];
                    data=Utility.getRandomData(data, len);
                    String hexdata=Utility.bytesToHex(data);
                    
                    len=112;
                    byte[] data2 = new byte[len];
                    data2=Utility.getRandomData(data2, len);
                    String hexdata2=Utility.bytesToHex(data2);
                    
                    
//                    Random rand=new Random();
//                    int idint=rand.nextInt();
//                    byte bid=(byte) idint;
//                    String id=Utility.byteToHex(bid);
//                    System.out.println("id ----> "+id );
//                    String m="6c82"+"0201"+
//                            "3082"+"01fd"+
//                            "a103020105a20302010ca3820190"
//                            + "3082018c"
//                            + "30820188"
//                            + "a103020101a282017f"
//                            + "0482017b"
//                            + "6e820177"
//                            + "30820173"
//                            + "a003020105a10302010ea20703050000000000a381e2"
//                            + "6181df"
//                            + "3081dc"
//                            + "a003020105a1091b07554d522e454455a21c301aa003020100a113"
//                            + "30111b066b72627467741b07554d522e454455a381a9"
//                            + "3081a6"
//                            + "a003020101a103020172a28199"
//                            + "048196"
//                            + hexdata
//                            + "a47b"
//                            + "3079"
//                            + "a003020101a272"
//                            + "0470"
//                            + hexdata2
//                            + "a45d305ba00703050040000000a2091b07554d522e454455a310300ea003020100a10730051b03616673a511180f31393939313231313231343830335aa7060204382b3993a8053003020101a911300f300da003020102a106040483972015";
//                    String m="6c820203308201ffa103020105a20302010ca38201923082018e3082018aa103020101a28201810482017d6e82017930820175a003020105a10302010ea20703050000000000a381e26181df3081dca003020105a1091b07554d522e454455a21c301aa003020100a11330111b066b72627467741b07554d522e454455a381ab3081a8a003020101a103020172a2819b0481986cb05fd9ad09ae9c62a8f8e73b57d6ec2fe816e9b42f0f6e301b5b6dcd1434d229039f43210a08ea1e7fcffb5d3fb0d57a6da17b80f49e9938b8e16d0195fe894cd16b202fa9b6e403f21f64192d68eca1a623f3de3355be9160f8685d4139391fbfc484cca1d23f660d634a477ca7ed58ab91437f050e68f9eb7822f91df670929bda3ab4874042f6fb7bc7e87ca8af45bfbfdeeddb8873a47b3079a003020101a272047090f8782d33c4399ff546a6942b5f7e1cb5fb0e72efe6e04a702a6481550aafe4feb08e79921e15dbefb59a7116f43c595445b80ef5ea6f5632a8e6615d5e54670c90691bc3d3ed77c7a3c746ecdbeb10c7e42cf454c33208de9f68924fe1c13f478625be6f80f6465d6a55b62daf9227a45d";
//                    kerberos only with ticket is not working
//                    String m="6181ce"
//                            + "3081cb"
//                            + "a003020105a1091b07554d522e454455a210300ea003020100a10730051b03616673a381a6"
//                            + "3081a3"
//                            + "a003020101a2819b"
//                            + "048198"
//                            + "48217864b46571a0942bfd36be763afe68480aeeb770e898117eec5261355a67792207a93d655c053961c664989ba61a98d0bd8de977cf2f373a4c986255a125a69147821d6517e341fad3fe6e7b0098e5c8894240d0905fe4c4385841a539caf40ad9d63169b1c5ba904704786249be33c54e6cd77915d525b076ec4fb547416d1d0e274e27c163178693232f9f2654a045df488a095cab";
//                    
//                    String m=   "6c820203" +
//                                "308201ffa1030201" +
//                                "05" +
//                                "a2030201" +
//                                "0c" +
//                                "a38201923082018e" +
//                                "3082018aa1030201" +
//                                "01" +
//                                "a28201810482017d" +
//                                "6e820179" +
//                                "30820175a0030201" +
//                                "05" +
//                                "a1030201" +
//                                "0e" +
//                                "a2070305" +
//                                "00" +
//                                "00000000" +
//                                "a381e26181df" +
//                                "3081dca0030201" +
//                                "05" +
//                                "a1091b07" +
//                                "554d522e454455" +
//                                "a21c" +
//                                "" +
//                                "301aa0030201" +
//                                "00" +
//                                "a1133011" +
//                                "1b06" +
//                                "6b7262746774" +
//                                "1b07" +
//                                "554d522e454455" +
//                                "" +
//                                "a381ab" +
//                                "" +
//                                "enc-part" +
//                                "3081a8a0030201" +
//                                "01" +
//                                "a1030201" +
//                                "72" +
//                                "a2819b048198" +
//                                "6cb05fd9ad09ae9c62a8f8e73b57d6ec2fe816e9b42f0f6e301b5b6dcd1434d229039f43210a08ea1e7fcffb5d3fb0d57a6da17b80f49e9938b8e16d0195fe894cd16b202fa9b6e403f21f64192d68eca1a623f3de3355be9160f8685d4139391fbfc484cca1d23f660d634a477ca7ed58ab91437f050e68f9eb7822f91df670929bda3ab4874042f6fb7bc7e87ca8af45bfbfdeeddb8873" +
//                                "" +
//                                "a47b" +
//                                //"authenticator\n" +
//                                "3079a0030201" +
//                                "01" +
//                                "a2720470" +
//                                "90f8782d33c4399ff546a6942b5f7e1cb5fb0e72efe6e04a702a6481550aafe4feb08e79921e15dbefb59a7116f43c595445b80ef5ea6f5632a8e6615d5e54670c90691bc3d3ed77c7a3c746ecdbeb10c7e42cf454c33208de9f68924fe1c13f478625be6f80f6465d6a55b62daf9227" +
//                                "" +
//                                "a45d" +
//                                "reqbody" +
//                                "305ba0070305" +
//                                "00" +
//                                "40000000" +
//                                "a2091b07" +
//                                "554d522e454455" +
//                                "" +
//                                "a310" +
//                                "300ea0030201" +
//                                "00" +
//                                "a1073005" +
//                                "1b03616673" +
//                                "a511180f" +
//                                "31393939313231313231343830335a" +
//                                "a7060204" +
//                                "382b3993" +
//                                "a8053003" +
//                                "020101" +
//                                "a911300f" +
//                                "300da0030201" +
//                                "02" +
//                                "a1060404" +
//                                "83972015";
                    
                    String m="6c820203308201ffa103020105a20302010ca38201923082018e3082018aa103020101a28201810482017d6e82017930820175a003020105a10302010ea20703050000000000a381e26181df3081dca003020105a1091b07554d522e454455a21c301aa003020100a11330111b066b72627467741b07554d522e454455a381ab3081a8a003020101a103020172a2819b0481986cb05fd9ad09ae9c62a8f8e73b57d6ec2fe816e9b42f0f6e301b5b6dcd1434d229039f43210a08ea1e7fcffb5d3fb0d57a6da17b80f49e9938b8e16d0195fe894cd16b202fa9b6e403f21f64192d68eca1a623f3de3355be9160f8685d4139391fbfc484cca1d23f660d634a477ca7ed58ab91437f050e68f9eb7822f91df670929bda3ab4874042f6fb7bc7e87ca8af45bfbfdeeddb8873a47b3079a003020101a272047090f8782d33c4399ff546a6942b5f7e1cb5fb0e72efe6e04a702a6481550aafe4feb08e79921e15dbefb59a7116f43c595445b80ef5ea6f5632a8e6615d5e54670c90691bc3d3ed77c7a3c746ecdbeb10c7e42cf454c33208de9f68924fe1c13f478625be6f80f6465d6a55b62daf9227a45d305ba00703050040000000a2091b07554d522e454455a310300ea003020100a10730051b03616673a511180f31393939313231313231343830335aa7060204382b3993a8053003020101a911300f300da003020102a106040483972015";
                    
                    
                    
                    
                    
//                    int offset=0;
//                    len=100;
////                    
//                    byte[] newdata=new byte[offset+len+100];
//                    int len2=Utility.getRandomData(newdata, offset, len);
//                    String m1=Utility.bytesToHex(newdata,offset,len);
////                    System.out.println("--------------> ");
////                    System.out.println(m1);
//                    
//                    len2=l2tp.createPacket(newdata, offset, len);
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
                    Thread.sleep(500);
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
//                    String received= new String(dp1.getData(),0,b1.length);
                    int ll=l2tp.decodePacket(b1, 0, dp1.getLength());
//                    System.out.println("==============================================> "+ll);
                    String ack=Utility.bytesToHex(b1, 0, ll);                   
//                    System.out.println(ack);
                    
                    
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
