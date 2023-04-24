/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fsm.tcpserver2023;

import static com.fsm.tcpserver2023.Frm_Server.lst_clients_model;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author skaya
 */
public class Server extends Thread {

    ServerSocket serverSocket;
    int port;
    boolean isListening;
    ArrayList<ServerClient> clients;
    //Thread listenThread;

    public Server(int port) {
        try {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            this.isListening = false;
            this.clients = new ArrayList<>();
            //this.listenThread= new Thread();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Listen() {
        this.isListening = true;

        this.start();
        System.out.println("Server startted...");

    }

    public void Stop() {
        try {
            this.isListening = false;
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AddClient(ServerClient serverClient) {
        this.clients.add(serverClient);
        System.out.println("Client Added...");
        //Frm_Server.lst_clients_model.addElement(serverClient.socket.getInetAddress().toString() + ":" + serverClient.socket.getPort());

       
    }

    public void RemoveClient(ServerClient serverClient) {
        this.clients.remove(serverClient);
         System.out.println("Client removed...");
        /*lst_clients_model.removeAllElements();
        for (ServerClient client : clients) {
            Frm_Server.lst_clients_model.addElement(client.socket.getInetAddress().toString() + ":" + client.socket.getPort());
            break;
        }*/
    }

    public ServerClient GetClientByIndex(int index) {
        return this.clients.get(index);
    }
    
    public void SendBroadcast(String message) throws IOException
    {
        byte[] bytes = (" "+message).getBytes();
        for (ServerClient client : clients) {
            client.SendMessage(bytes);
        }
         System.out.println("Broadcast message send...");
    }
    
     public void SendToClient(String message, int index) throws IOException
    {
        byte[] bytes = (" "+message).getBytes();
       this.clients.get(index).SendMessage(bytes);
        System.out.println("Client message send...");
    }

    @Override
    public void run() {

        while (this.isListening) {
            try {

                Socket clientSocket = this.serverSocket.accept(); //blocking
                 System.out.println("Client connected...");
                ServerClient nclient = new ServerClient(clientSocket, this);
                this.AddClient(nclient);
                nclient.Listen();
                
                nclient.SendMessage(("Merhaba:"+nclient.socket.getInetAddress().toString() + ":" + nclient.socket.getPort()).getBytes());
                /*System.out.println("ip= " + clientSocket.getInetAddress().toString());
                System.out.println("port= " + clientSocket.getPort());
                OutputStream clientOutput = clientSocket.getOutputStream();
                InputStream clientInput = clientSocket.getInputStream();

                while (!clientSocket.isClosed()) {
                    int byteSize = clientInput.read(); //blocking
                    byte bytes[] = new byte[byteSize];
                    clientInput.read(bytes);
                    System.out.println(new String(bytes, StandardCharsets.UTF_8));
                }*/
                //clientOutput.close();
                //clientInput.close();
                //clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
