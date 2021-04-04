/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import org.apache.log4j.Logger;
import serversetuptcp.Configuration;
import serversetuptcp.Constants;

/**
 *
 * @author User
 */
public class TCPServer extends Thread{
    Logger logger=Logger.getLogger(TCPServer.class);
    InetAddress address;
    int port;
    
    public TCPServer(Inet4Address addr,int port){
        this.address=addr;
        this.port=port;
    }

    @Override
    public void run() {
        try{
            ServerSocket serverSocket;
            
            switch(Configuration.protocolNumber){
                case Constants.NineP2000:
                    serverSocket = new ServerSocket(port,0,address);
                    break;
                default:
                    serverSocket = new ServerSocket(port,0,address);
                    new TCPSocketListener(serverSocket,Constants.NORMAL_SERVER).start();
                    break;
            }
            logger.debug("Server Socket created");
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
}
