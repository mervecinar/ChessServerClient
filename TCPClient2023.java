/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.fsm.tcpclient2023;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skaya
 */
public class TCPClient2023 {

    public static void main(String[] args) {
        try {
            //localhost -> 127.0.0.1
            Socket clientSocket = new Socket("localhost", 5000);
            OutputStream clientOutput = clientSocket.getOutputStream();
            InputStream clientInput = clientSocket.getInputStream();

            byte bytes[] = " merhaba".getBytes();
            clientOutput.write(bytes);
            while (!clientSocket.isClosed()) {
                int byteSize = clientInput.read(); //blocking
                bytes = new byte[byteSize];
                clientInput.read(bytes);

                System.out.println(new String(bytes, StandardCharsets.UTF_8));

            }

            /*sleep(5000);
            bytes = " nasilsin".getBytes();
            clientOutput.write(bytes);
            sleep(5000);
            bytes = " iyi misin?".getBytes();
            clientOutput.write(bytes);

            sleep(20000);*/
        } catch (IOException ex) {
            Logger.getLogger(TCPClient2023.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
