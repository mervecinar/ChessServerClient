/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication7;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer {
    
    private ServerSocket serverSocket;
    private int port = 8888;

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Sunucu başlatıldı. Port numarası: " + port);

            while (true) {
                System.out.println("Bağlantı bekleniyor...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Bir istemci bağlandı: " + clientSocket);

                ChessClient clientHandler = new ChessClient();
                clientHandler.start();
            }
        } catch (IOException e) {
            System.out.println("Sunucu hatası: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ChessServer server = new ChessServer();
        server.start();
    }
}
