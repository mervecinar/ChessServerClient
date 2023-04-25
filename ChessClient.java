/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChessClient {
    
    private String serverName = "localhost";
    private int port = 8888;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public void start() {
        try {
            socket = new Socket(serverName, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Sunucudan gelen mesaj: " + inputLine);
            }
        } catch (IOException e) {
            System.out.println("İstemci hatası: " + e.getMessage());
        }
    }
 public static void main(String[] args) {
        ChessClient client = new ChessClient();
        client.start();
    }
}
  

