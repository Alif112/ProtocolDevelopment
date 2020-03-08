/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ntpclient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 *
 * @author User
 */
public class Config {
    
    
    public static DatagramSocket setConfiguration(DatagramSocket ds){

        return ds;
    
    }
    
    public static void configReader(DatagramSocket ds){
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(	System.getProperty("user.dir")+"/config.txt"));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
	}
        
    }
    public static int nextFreePort(int from, int to) {
    int port = randPort(from, to);

      while (true) {
          if (isLocalPortFree(port)) {
              return port;
          } else {
              port = ThreadLocalRandom.current().nextInt(from, to);
          }
      }
  }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static int randPort(int from, int to) {
        int randomPort;
        Random r = new Random();
        return r.nextInt((to - from) + 1) + from;
            
    }
    
    
}
