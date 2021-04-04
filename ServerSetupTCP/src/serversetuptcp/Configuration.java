/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serversetuptcp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Inet4Address;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class Configuration {
    public static Logger logger=Logger.getLogger(Configuration.class);
    
    public static HashMap<String,String> config = new HashMap<String,String>();
    public static String fixedConfigIP;
    public static int enableFixedPort;
    public static int serverSocketPort;
    public static int protocolNumber;
    public static int minimumPortRange;
    public static int maximumPortRange;
    public static int enableSocialBypass;
    public static String protocolType;
    public static int fixedConfigPort;
    public static String protocolName=null;
    public static String ip;
    public static int dataLen;
    public static int inputConfigFromServer;
    public static int enableRandomData,randomDataminimumLen,randomDatamaximumLen;
    public static Inet4Address addr;
    public static String rtpHeader,serverIP;
    public static int fixedPortForServer,socialPacketCount;
    
    
    public static boolean readConfigFile(){
        try{    
        BufferedReader br = new BufferedReader(new FileReader(Constants.fileName));
            if(br== null){
                throw new FileNotFoundException();
            }
        
            String line;
            while((line = br.readLine()) != null) {
                if(line.startsWith("#"))continue;
                String [] str = line.split("=");
                if(str.length < 2)continue;
                config.put(str[0].toLowerCase(), str[1]);
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        Configuration.loadConfig(config);
        
        return true;
    }
   

    private static void loadConfig(HashMap<String, String> config) {
        logger.info("----------------------- loading Config file--------------------");
        try {
            inputConfigFromServer=0;
            protocolType="udp";
            protocolNumber=1000;
            fixedConfigIP="0.0.0.0";
            fixedConfigPort=100;
            dataLen=200;
            rtpHeader="121212";
            serverIP="65.99.254.85";
            enableFixedPort=1;
            fixedPortForServer=500;
            enableSocialBypass=0;
            socialPacketCount=10;
            minimumPortRange=6000;
            maximumPortRange=8000;


            String str = config.get((Constants.inputConfigFromServer).toLowerCase());
            if(str != null){
                inputConfigFromServer = Integer.parseInt(str.trim());
                logger.info("inputConfigFromServer "+inputConfigFromServer);

            }
            str = config.get((Constants.protocolType).toLowerCase());
            if(str != null){
                protocolType = str.trim();
                logger.info("protocolType "+protocolType);
            }
            str = config.get((Constants.protocolNumber).toLowerCase());
            if(str != null){
                protocolNumber = Integer.parseInt(str.trim());
                logger.info("protocolNumber "+protocolNumber);
            }
            if(protocolType.equals("tcp"))
                protocolName=Constants.protocolNameListTCP[protocolNumber-2000];
            if(protocolType.equals("udp"))
                protocolName=Constants.protocolNameListUDP[protocolNumber-1000];
            if(protocolType==null) {
                protocolName="";
                logger.warn("protocol name is empty");
            }
            logger.info("protocolName "+protocolName);

            str = config.get((Constants.fixedConfigIP).toLowerCase());
            if(str != null){
                fixedConfigIP = str.trim();
                logger.info("fixedConfigIP "+fixedConfigIP);
            }
            str = config.get((Constants.fixedConfigPort).toLowerCase());
            if(str != null){
                fixedConfigPort = Integer.parseInt(str.trim());
                logger.info("fixedConfigPort "+fixedConfigPort);
            }
            str = config.get((Constants.dataLen).toLowerCase());
            if(str != null){
                dataLen = Integer.parseInt(str.trim());
                logger.info("dataLen "+dataLen);
            }

            str = config.get((Constants.rtpHeader).toLowerCase());
            if(str != null){
                rtpHeader = str.trim();
                logger.info("rtpHeader "+rtpHeader);
            }
            str = config.get((Constants.serverIP).toLowerCase());
            if(str != null){
                serverIP = str.trim();
                addr=(Inet4Address) Inet4Address.getByName(serverIP);
                logger.info("serverIP "+serverIP);
            }

            str = config.get((Constants.enableFixedPort).toLowerCase());
            if(str != null){
                enableFixedPort = Integer.parseInt(str.trim());
                logger.info("enableFixedPort "+enableFixedPort);
            }
            str = config.get((Constants.fixedPortForServer).toLowerCase());
            if(str != null){
                fixedPortForServer = Integer.parseInt(str.trim());
                logger.info("fixedPortForServer "+fixedPortForServer);
            }

            str = config.get((Constants.enableSocialBypass).toLowerCase());
            if(str != null){
                enableSocialBypass = Integer.parseInt(str.trim());
                logger.info("enableSocialBypass "+enableSocialBypass);
            }

            str = config.get((Constants.socialPacketCount).toLowerCase());
            if(str != null){
                socialPacketCount = Integer.parseInt(str.trim());
                logger.info("socialPacketCount "+socialPacketCount);
            }

            str = config.get((Constants.minimumPortRange).toLowerCase());
            if(str != null){
                minimumPortRange = Integer.parseInt(str.trim());
                logger.info("minimumPortRange "+minimumPortRange);
            }
            str = config.get((Constants.maximumPortRange).toLowerCase());
            if(str != null){
                maximumPortRange = Integer.parseInt(str.trim());
                logger.info("maximumPortRange "+maximumPortRange);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        
        
    }
}
