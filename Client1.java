/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fsm.tcpclient2023;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skaya
 */
public class Client1 extends Thread {

    Socket socket;
    //Server server;
    OutputStream output;
    InputStream input;
    boolean isListening;
    String serverAddress;
    int serverPort;

    public Client1(String serverAddress, int serverPort) throws IOException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.isListening = false;

    }

    public void Listen() {
        try {
            this.socket = new Socket(this.serverAddress, this.serverPort);
            this.output = socket.getOutputStream();
            this.input = socket.getInputStream();
            this.isListening = true;
            this.start();
        } catch (IOException ex) {
            Logger.getLogger(Client1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Stop() {
        try {
            this.isListening = false;
            this.output.close();
            this.input.close();
            this.socket.close();
            

        } catch (IOException ex) {
            System.out.println(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "-> stoped");

        }
    }

    public void SendMessage(byte[] messageBytes) throws IOException {

        this.output.write(messageBytes);
    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                int byteSize = this.input.read(); //blocking
                byte bytes[] = new byte[byteSize];
                this.input.read(bytes);
                System.out.println(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "-> message reacted");
                System.out.println(new String(bytes, StandardCharsets.UTF_8));
                Frm_Client.lst_messagesFromServer_model.addElement(new String(bytes, StandardCharsets.UTF_8));
            }

        } catch (IOException ex) {
            this.Stop();

            System.out.println(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "-> closed");
            //Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
