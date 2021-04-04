/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;
/**
 *
 * @author User
 */
public class TCPSocketListener extends Thread{
    Logger logger =Logger.getLogger(TCPSocketListener.class);
    
    private ServerSocket serverSocket;
    private int serverType;
    Socket socket;
    
    public TCPSocketListener(ServerSocket socket, int NORMAL_SERVER) {
        serverSocket=socket;
        this.serverType=NORMAL_SERVER;
    }

    @Override
    public void run() {
        try {
            socket=serverSocket.accept();
            logger.debug("Accepted Server Socket");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}
