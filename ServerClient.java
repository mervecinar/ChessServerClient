/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fsm.tcpserver2023;

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
public class ServerClient extends Thread {

    Socket socket;
    Server server;
    OutputStream output;
    InputStream input;
    boolean isListening;

    public ServerClient(Socket socket, Server server) throws IOException {
        this.server= server;
        this.socket = socket;
        this.output = socket.getOutputStream();
        this.input = socket.getInputStream();
        this.isListening = false;
        //System.out.println(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "-> connected");
        //Frm_Server.lst_clients_model.addElement(this.socket.getInetAddress().toString() + ":" + this.socket.getPort());

    }

    public void Listen() {
        this.isListening = true;
        this.start();
    }

    public void Stop() {
        try {
            this.isListening = false;
            this.output.close();
            this.input.close();
            this.socket.close();
            this.server.RemoveClient(this);
            

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
                //Frm_Server.lst_messagesFromClient_model.addElement(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "->" + new String(bytes, StandardCharsets.UTF_8));
            }

        } catch (IOException ex) {
            this.Stop();
            
            System.out.println(this.socket.getInetAddress().toString() + ":" + this.socket.getPort() + "-> closed");
            //Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
