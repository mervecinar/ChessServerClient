/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SatrancClient;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public class Mesaj implements Serializable {
    //Bu sınıfta sayesinde karşı tarafa gönderilen hamleler tutulur ve aracılık eder.

    public enum gndr_t {
        Basla, Bağlan, Kapat, Hamle, Taraf
    }//case için ihtimaller 

    public gndr_t type;// gönderilen mesajın türü
    public Object content;

    public Mesaj(gndr_t m) {//consturactor
        this.type = m;
    }
}
