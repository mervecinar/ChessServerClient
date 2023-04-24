/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.fsm.tcpserver2023;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skaya
 */
public class TCPServer2023 {

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(5000);
        server.Listen();
        while (server.isListening) {
            
             sleep(1000);
        }
       
       

        /*int port = 5000;
         ServerSocket serverSocket; 
        try {
           serverSocket= new ServerSocket(port);
            while (!serverSocket.isClosed()) {

                Socket clientSocket = serverSocket.accept(); // client bağlantısı dinleme //blocking 
                
                
                System.out.println("ip= " + clientSocket.getInetAddress().toString());
                System.out.println("port= " + clientSocket.getPort());
                OutputStream clientOutput = clientSocket.getOutputStream();
                InputStream clientInput = clientSocket.getInputStream();

                int byteSize = clientInput.read(); //blocking
                byte bytes[] = new byte[byteSize];
                clientInput.read(bytes);
                System.out.println(new String(bytes, StandardCharsets.UTF_8));

                clientOutput.close();
                clientInput.close();
                clientSocket.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(TCPServer2023.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
