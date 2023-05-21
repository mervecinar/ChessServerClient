/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SatrancServer;

import SatrancClient.Mesaj;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class ServerofClient {

    int oyuncuNumber;// oyuncunun id numarası tutulur.
    public String name = "-";// baslaması için defaault string atıyorum
    Socket Soket;
    ObjectOutputStream out;
    ObjectInputStream inp;
    Listen Thread;//eşinden gelenleri dinler.
    Esleştirme ikili;// bu sayede iki kişi oynayabilir.
    ServerofClient rival;//karşı tarafı temsil eder.
    public boolean es = false;//eşi var mı kontrol sağlar
    public boolean taraf;
    public static int k = 1;

    public ServerofClient(Socket soket, int number) throws IOException {//soket içerisinde ip ve port var
        this.Soket = soket;
        this.oyuncuNumber = number;

        this.out = new ObjectOutputStream(this.Soket.getOutputStream());
        this.inp = new ObjectInputStream(this.Soket.getInputStream());

        this.Thread = new Listen(this);
        this.ikili = new Esleştirme(this);

    }

    public void Send(Mesaj gndr) throws IOException {

        this.out.writeObject(gndr);
    }

}

class Esleştirme extends Thread {//Oyun ikili olduğu için o iki server arasında eşleme yapılmalı ki mesajı kime göndereceğini server anlasın.

    ServerofClient oyuncu;

    Esleştirme(ServerofClient TheClient) {
        this.oyuncu = TheClient;
    }

    public void run() {
        while (oyuncu.Soket.isConnected() && oyuncu.es == false) {

            try {
                Server.eşDurum.acquire(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Esleştirme.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (!oyuncu.es) {
                ServerofClient rakip = null;
                while (rakip == null && oyuncu.Soket.isConnected()) {
                    for (ServerofClient client : Server.oyuncular) {
                        if (oyuncu != client && client.rival == null) {
                            rakip = client;
                            rakip.es = true;
                            rakip.rival = oyuncu;
                            oyuncu.rival = rakip;
                            oyuncu.es = true;// bu şekilde yapınca sadeece tek taraf oynuyor bu yüzden tekrar servera istenilen mesajları yolluyorum.
                            oyuncu.k = 1;
                            //servera burda eşleme ile ilhili tekrar mesaj yolluyorum
                            Mesaj gndr = new Mesaj(Mesaj.gndr_t.Taraf);
                            gndr.content = !(oyuncu.taraf);
                            Server.Send(rakip, gndr);
                            oyuncu.k = 2;
                            break;
                        }
                    }
                    try {
                        sleep(1500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Esleştirme.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Mesaj istek1 = new Mesaj(Mesaj.gndr_t.Bağlan);//Burada istenilenleri servea gönderiyorum öreneğim Hamle mi istedi onu 
                istek1.content = oyuncu.name;
                Server.Send(oyuncu.rival, istek1);

                Mesaj istek2 = new Mesaj(Mesaj.gndr_t.Bağlan);//mesela taraf bilgisi istediği onu yolluyorum gerekli askiyon bu şekilde alınıyor.
                istek2.content = oyuncu.rival.name;
                Server.Send(oyuncu, istek2);

                Server.eşDurum.release(1);
            }
        }
    }
}

class Listen extends Thread {// Bu sayede oyuncu dinlenir her defasında yeni bir nesne oluşturur. 

    ServerofClient oyuncu;

    Listen(ServerofClient TheClient) {
        //gelen oyuncuya atama yapılır.
        this.oyuncu = TheClient;
    }

    public void run() {// bu çalıştığında dinleme de başlar.

        while (oyuncu.Soket.isConnected()) {// bağlantı kopmadı müddetçe dinlemeye devaö et.
            try {

                Mesaj gelenMesaj = (Mesaj) (oyuncu.inp.readObject());

                switch (gelenMesaj.type) {//Gelen mesajdan ne istendi öreneğin Hamle yapıldığında 
                    //karşı tarafta da yapsın diye Hamle seçtim bu yüzden case hamle ye gider ve rakipde hamlemi gösterir.
                    case Basla:
                        oyuncu.ikili.start();//baslatır.
                        break;

                    case Hamle:
                        Server.Send(oyuncu.rival, gelenMesaj);//gelen mesajı suunucuya yollar
                        break;
                    case Taraf:
                        oyuncu.taraf = ((boolean) gelenMesaj.content);
                        break;
                    case Kapat:

                        break;//kapatır.

                }

            } catch (IOException ex) {//hata olursa exceptiona gider.
                Logger.getLogger(ServerofClient.class.getName()).log(Level.SEVERE, null, ex);
                Server.oyuncular.remove(oyuncu);

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServerofClient.class.getName()).log(Level.SEVERE, null, ex);
                Server.oyuncular.remove(oyuncu);
            }
        }

    }
}
