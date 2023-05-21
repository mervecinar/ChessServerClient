/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SatrancClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import satranctcp.SatrancTCP;

/**
 *
 * @author LENOVO
 */
public class Client extends Thread {

    static Socket socket;
    static SatrancTCP SatrancM;
    static ObjectInputStream sInp;
    static ObjectOutputStream sOut;
    static int k = 1;
    static boolean heyo = true;

    public Client(SatrancTCP x, int l) {
        this.k = l;
        this.SatrancM = x;
    }

    public static void Stop() throws IOException {

        if (Client.socket != null) {
            Client.socket.close();
            Client.sOut.close();
            Client.sInp.close();
            Client.sOut.flush();
            ;
            System.out.println("Kapatıldı");
        }
    }

    public static void Start(String ipAdress, int portNumber) throws IOException {
        Mesaj gndr = new Mesaj(Mesaj.gndr_t.Basla);
        socket = new Socket(ipAdress, portNumber);
        sInp = new ObjectInputStream(socket.getInputStream());
        sOut = new ObjectOutputStream(socket.getOutputStream());
        new Client(SatrancM, k).start();

        Send(gndr);

    }

    public static void Send(Mesaj gndr) throws IOException {

        sOut.writeObject(gndr);

    }

    @Override
    public void run() {
        while (Client.socket.isConnected()) {

            Mesaj received = null;
            try {
                received = (Mesaj) (Client.sInp.readObject());
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }

            switch (received.type) {
                case Kapat:
                    break;
                case Hamle:
                    Client.SatrancM.SunucuMesaj2((ArrayList<Object>) received.content, true);
                    heyo = false;
                    break;
                case Taraf:
                    Client.SatrancM.oyuncu2 = (boolean) received.content;
                    int k = 2;
                    break;

            }
        }
    }
}
