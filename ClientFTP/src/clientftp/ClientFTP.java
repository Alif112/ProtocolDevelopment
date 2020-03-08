/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientftp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class ClientFTP {
/**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException {
        System.out.println("Udp Client Started...........");
        DatagramSocket ds=new DatagramSocket();
        
        while(true){
            Scanner sc=new Scanner(System.in);

            String message=sc.nextLine();

            try{
                byte[] b=message.getBytes();
                InetAddress ia=InetAddress.getLocalHost();
                DatagramPacket dp=new DatagramPacket(b, b.length,ia,1111);
                ds.send(dp);
                System.err.println("send from client---> : "+message);
                
                byte[] b1= new byte[2048];
                DatagramPacket dp1=new DatagramPacket(b1, b1.length);
                ds.receive(dp1);

                String received= new String(dp1.getData());
                System.err.println("Received at client:-->  "+ received);


            }catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    
}
