package SatrancServer;

import SatrancClient.Mesaj;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class Server {

    public static ServerSocket sSoket;//soket 
    public static int portNumber;//port numarası aynı port üzerinden buraya gelecekler 
    public static int number = 0;// oyuncu numarası initial değer 0 atadım zaman ile artacak.
    public static Semaphore eşDurum = new Semaphore(1, true);
    public static ServerT thread;//serveri bu sayede dinleyebilcek 
    public static ArrayList<ServerofClient> oyuncular = new ArrayList<>();// oyuncuların olduğu havuz.

    public static void Start(int portNumber) throws IOException {// verilen port numarasına göre serveri başlatır.

        Server.portNumber = portNumber;
        Server.sSoket = (new ServerSocket(Server.portNumber));
        Server.thread = (new ServerT());
        Server.thread.start();

    }

    public static void Send(ServerofClient client, Mesaj gndt) {//bu sayede hamlelerin sıranın bilgileri  gibi mesajları yollarım.

        try {
            client.out.writeObject(gndt);
        } catch (IOException ex) {

            Logger.getLogger(ServerofClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) throws IOException {//Sereri çağıran ve bailatan psvm function.
        System.out.println("Server has been started. The Clients are waiting...");
        Server.Start(5010);

    }

}

class ServerT extends Thread {

    public void run() {
        while (!Server.sSoket.isClosed()) {
            try {

                Socket clientSocket = Server.sSoket.accept();
                ServerofClient oyuncular = (new ServerofClient(clientSocket, Server.number));//gelen oyuncuyu kabul et

                Server.number++;// oyuncu sayısını arttır.
                Server.oyuncular.add(oyuncular);//Arrayliste oyuncuyu ekle
                oyuncular.Thread.start();// threadin gönderceği mesajjı vbekle ve dinle
                System.out.println(oyuncular.oyuncuNumber + 1 + ".  player is connected!" + "    Info  of client : " + oyuncular.Soket);

            } catch (IOException ex) {
                Logger.getLogger(ServerT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
