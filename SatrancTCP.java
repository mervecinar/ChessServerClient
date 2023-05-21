/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satranctcp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import SatrancClient.Client;
import SatrancClient.Mesaj;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class SatrancTCP extends JFrame implements ActionListener {

    JFrame frm = new JFrame();//görüntü olarak değişiklik yapmak için jfream nesnesi oluşturuyorum
    public static SatrancTCP satrancM;// her client bağlangığında yeni bir oyun nesnesi oluşturuyor bu yüzden birbirlerinden ayrılıyorlar

    public static JPanel panel1;// satranç için panel
    public static JButton[][] buton1;// satrançın kareleri için buton 
    // Burada hiçbir şey seçili olmadığını belirtmek için default value ataması yapıyorum.
    public static int secimX = -1;
    public static int SecimY = -1;
    static int sahBX = 0;
    static int sahBY = 4;
    static int sahSX = 7;
    static int sahSY = 4;

    public boolean oyuncu1 = false;// sırayı kontrol etmek için iki client için bool tanımlama
    public boolean oyuncu2 = true;

    //Beyaz taşların tanımlanması 
    public static ImageIcon piyonBeyaz;
    public static ImageIcon kaleBeyaz;
    public static ImageIcon atBeyaz;
    public static ImageIcon filBeyaz;
    public static ImageIcon vezirBeyaz;
    public static ImageIcon sahBeyaz;

    //Siyah taş sanımlanması;
    public static ImageIcon piyonSiyah;
    public static ImageIcon kaleSiyah;
    public static ImageIcon atSiyah;
    public static ImageIcon filSiyah;
    public static ImageIcon vezirSiyah;
    public static ImageIcon sahSiyah;

    public LinkedList<Icon> taslarListe = new LinkedList<>();

    public SatrancTCP() {//constructor
        frm = this;//  atama
        taslaslar(taslarListe);// taslar içerisine atılsın diye çağırıyorum

        setTitle("SATRANÇ SEVER");// jfream başlık
        setSize(750, 750);// frame boyut
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        panel1 = new JPanel(new GridLayout(8, 8));// 8x8 lik kareleri hazırlıyor.
        buton1 = new JButton[8][8];// seçimler rahat olsun diye her bir kare buton olacak 

        //Burada satrançın tabaka kısmı oluşturuluyor.
        for (int x = 0; x <= 7; x++) {
            for (int y = 0; y <= 7; y++) {

                buton1[x][y] = new JButton();

                buton1[x][y].addActionListener(this);
                panel1.add(buton1[x][y]);
                int l = x + y;
                // arkaplan için bir beyaz bir kahve olsun diye %2 ile mod alınıyor ve bu şekilde bulunuyor
                if (l % 2 == 0) {
                    buton1[x][y].setBackground(Color.WHITE);

                } else {
                    buton1[x][y].setBackground(new java.awt.Color(180, 150, 140));
                }
            }
        }
        // tasların yerine koyulması için bu şekil de bir methodu çiziyoruz
        taslarıYerlestir();
        // çizilen ve yapılan islem kullanıcı tarafından gözüksün diye panele aktarıp görünebilir yapıyorum
        add(panel1, BorderLayout.CENTER);

        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {    // Burası önemli bir fonksiyon bu adımda kimin sırası olduğu hareket ve server a gönderme gibi fonksiyonun çağırırması gibi koşulların takibi yapılıyor.
        JButton kk = (JButton) e.getSource();   // Buton oluşturuyorum.

        for (int a = 0; a <= 7; a++) {
            for (int b = 0; b <= 7; b++) {

                if (buton1[a][b] == kk) { //  seçilen buton defaulta eşit ise yani seçim yapılmadıysa 

                    if (secimX == -1 && SecimY == -1) {
                        secimX = a;//
                        SecimY = b;
                        buton1[a][b].setBackground(new java.awt.Color(255, 255, 204));// seçim yapılan yerin butonun arkaplanı rengini krem renk yapıyor.
                        // x ve y  bizim A(x,y) olarak kordinasyonlarımız oluyor satır ve sutun satranç tahtasındaki konumu belli eder.
                    } else {

                        Icon tas = buton1[secimX][SecimY].getIcon();// Seçilen taşı burada belirliyoruz 

                        if (tas == null) {
                            System.out.println("tas secmedi");
                            JOptionPane.showMessageDialog(frm, "Please Select from your piece", "SELECT PIECE", JOptionPane.WARNING_MESSAGE);//UYARI
                            //Seçimi temizliyorum.
                            SecimiSıfırla();
                        }

                        if (oyuncu1 = !oyuncu2) {
                            if (buton1[sahSX][sahSY] == buton1[a][b]) {// sah kontrolü eğer yer ise karşı taraf oyun biter.

                                JOptionPane.showMessageDialog(frm, "GAME IS OVER WHITE HAS BEEN WON");//UYARI

                                frm.dispose();//kapat programı
                                try {
                                    Client.Stop();//Serveri kapatsın
                                } catch (IOException ex) {
                                    Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (tas == filSiyah || tas == vezirSiyah || tas == piyonSiyah || tas == kaleSiyah || tas == atSiyah || tas == sahSiyah) {// Eğer olurda bir yanlışlık olup Beyaz sırası iken siyah seçilirse uyarı mesajı koyuyorum.

                                JOptionPane.showMessageDialog(frm, "White Player Turn Please Wait", "INVALID TURN", JOptionPane.WARNING_MESSAGE);//UYARI
                                //Seçimi temizliyorum.
                                SecimiSıfırla();
                                oyuncu1 = true;// bu conditiona girsin diye boolean değerini düzlüyorum.
                            } else {//eğer doğru taraf taşından;
                                if (tas == piyonBeyaz) {
                                    try {
                                        //piyona basıldıysa
                                        SunucuMesaj(secimX, SecimY, a, b);// Sunucuya mesajı gönder rakibe mesajı iletsin
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    piyonOyna(secimX, SecimY, a, b, tas, oyuncu1);// tası butona yere harket ettin
                                    SecimiSıfırla(); // seçimi temizle
                                    oyuncu1 = !oyuncu1;// senin sıran bitti diğer tarafta sıra
                                    break;

                                } else if (tas == filBeyaz) {
                                    try {
                                        // secilen tas fil ise
                                        SunucuMesaj(secimX, SecimY, a, b);// sunucuya mesajı yolla 
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    filOyna(secimX, SecimY, a, b, tas, oyuncu1);// hareket ettir.
                                    SecimiSıfırla();// secimi temizle
                                    oyuncu1 = !oyuncu1;// senin sıran değil.
                                    break;
                                } else if (tas == vezirBeyaz) {
                                    try {
                                        // secilen tas vezir mi
                                        SunucuMesaj(secimX, SecimY, a, b);//sunucuya mesaj ilet.
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    vezirOyna(secimX, SecimY, a, b, tas, oyuncu1);//veziri hareket ettir.
                                    SecimiSıfırla();// secimi sıfırla
                                    oyuncu1 = !oyuncu1;// senin sıran değil.
                                    break;
                                } else if (tas == kaleBeyaz) {
                                    try {
                                        // secilen tas kaleise
                                        SunucuMesaj(secimX, SecimY, a, b);//Sunucuya bilgileri yolla rakibe iletsin
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    kaleOyna(secimX, SecimY, a, b, tas, oyuncu1);//Kaleyi hareket ettir.
                                    SecimiSıfırla();// secilen butonu  temizle 
                                    oyuncu1 = !oyuncu1;// sırayı diğerine ver.
                                    break;
                                } else if (tas == atBeyaz) {
                                    try {
                                        // secilen tas at ise
                                        SunucuMesaj(secimX, SecimY, a, b);// sunucuya bilgileir yolla rakibe iletsin 
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    atOyna(secimX, SecimY, a, b, tas, oyuncu1);// atı hamle yaptır
                                    SecimiSıfırla();// seçimi temizle 
                                    oyuncu1 = !oyuncu1;// senin sıran değil 
                                    break;
                                } else if (tas == sahBeyaz) {
                                    try {
                                        // secilen sah mı
                                        SunucuMesaj(secimX, SecimY, a, b);// sunucuya mesajı yolla
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    sahOyna(secimX, SecimY, a, b, tas, oyuncu1);// sah oynat
                                    SecimiSıfırla();// sıfırla
                                    oyuncu1 = !oyuncu1;// sırayı diğerine ver.
                                    break;
                                }
                            }

                        } else if (oyuncu1 = oyuncu2) {
                            if (buton1[sahBX][sahBY] == buton1[a][b]) {// sah kontrolü eğer yer ise karşı taraf oyun biter.

                                JOptionPane.showMessageDialog(frm, "GAME IS OVER BLACK HAS BEEN WON");//UYARI
                                frm.dispose();//kapat programı
                                try {
                                    Client.Stop();//Serveri kapatsın
                                } catch (IOException ex) {
                                    Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (tas == piyonBeyaz || tas == vezirBeyaz || tas == sahBeyaz || tas == kaleBeyaz || tas == atBeyaz || tas == filBeyaz) {//Eger beyaz secildiyse siyah secilsin diyoruz.
                                JOptionPane.showMessageDialog(frm, "Black Player Turn Please Wait", "INVALID TURN", JOptionPane.WARNING_MESSAGE);//UYARI
                                SecimiSıfırla();//hatalı secim sıfırla
                                oyuncu1 = false;//siyaha yolla
                            } else {
                                if (tas == piyonSiyah) {
                                    try {
                                        //secilen tas siyah Piypn mu
                                        SunucuMesaj(secimX, SecimY, a, b);// sucuya mesajı ilet 
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    piyonOyna(secimX, SecimY, a, b, tas, oyuncu2);// siyah piyonu oynat 

                                    SecimiSıfırla();// yapılan secimi sıfırla
                                    oyuncu1 = true;// karsı rakipe yolla
                                    break;// aralıksız bilgi akımını bu sayedee durduruyor..

                                } else if (tas == atSiyah) {
                                    try {
                                        //icon  at mi
                                        SunucuMesaj(secimX, SecimY, a, b);
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    atOyna(secimX, SecimY, a, b, tas, oyuncu2);

                                    SecimiSıfırla();
                                    oyuncu1 = true;
                                    break;
                                } else if (tas == sahSiyah) {
                                    try {
                                        SunucuMesaj(secimX, SecimY, a, b);
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    sahOyna(secimX, SecimY, a, b, tas, oyuncu2);
                                    SecimiSıfırla();
                                    oyuncu1 = true;
                                    break;
                                } else if (tas == kaleSiyah) {
                                    try {
                                        //tas kale mı
                                        SunucuMesaj(secimX, SecimY, a, b);//mesajı yolla
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    kaleOyna(secimX, SecimY, a, b, tas, oyuncu2);//Kale hareket etsin

                                    SecimiSıfırla();//secimi sıfırla 
                                    oyuncu1 = true;//karşı rakibe yolla 
                                    break;//burda bitir.

                                } else if (tas == filSiyah) {
                                    try {
                                        SunucuMesaj(secimX, SecimY, a, b);// sunucuya mesajı yolla
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    filOyna(secimX, SecimY, a, b, tas, oyuncu2);// fili oynat

                                    SecimiSıfırla();//secimi sıfırla
                                    oyuncu1 = true;//karsı rakibe yolla
                                    break;
                                } else if (tas == vezirSiyah) {
                                    try {
                                        SunucuMesaj(secimX, SecimY, a, b);
                                    } catch (IOException ex) {
                                        Logger.getLogger(SatrancTCP.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    vezirOyna(secimX, SecimY, a, b, tas, oyuncu2);
                                    SecimiSıfırla();
                                    oyuncu1 = true;
                                    break;
                                }
                            }

                            SecimiSıfırla();//Seçili kalan bir şey var ise sıfırlanııyor.
                            oyuncu1 = true;//sıra siyaha gitsin

                        } else {
                            System.out.println("siyahmi" + oyuncu1 + "beyazdami" + oyuncu2);
                            System.out.println(" hamleyi anlayamadık kontrol et ");
                            SecimiSıfırla();//Seçili kalan bir şey var ise sıfırlanııyor.
                        }
                    }

                    return;
                }
            }
        }
    }

    public void SunucuMesaj(int BasX, int BasY, int BitX, int BitY) throws IOException {//Burası rakip takıma kendi yaptığım hamleleri gönderen fonksiyon 
        //bu fonksiyon olmaz ise eğer rakip takımda benim yaptığım hamle gözükmez.
        Icon icon = satrancM.buton1[BasX][BasY].getIcon();// ilk olarak hahngi tas seçildiği bulunur.
        ArrayList<Point> noktolar = new ArrayList<>();// hamlenin kordinasyonu tutulsun diye arraylist oluşturuluyor.
        ArrayList<Object> x = new ArrayList<>();//buda servera gönderilsin diye oluşturulan arrayList 
        Mesaj gndr = new Mesaj(Mesaj.gndr_t.Hamle);//gönderilecek mesaj oluşturulur.
        Point a = new Point(BasY, BasX);// kordinat olduğu için point olması gerekir.
        Point b = new Point(BitY, BitX);// kordinat olduğu için point olması gerekir.

        noktolar.add(a);// listeye eklenir
        noktolar.add(b);
        x.add(noktolar);
        System.out.println("noktalar" + noktolar);
        x.add(taslarListe);
        System.out.println("taslar" + noktolar);
        gndr.content = x;
        Client.Send(gndr);// ve son olarak sunucuya yollanır
    }

    public void tasıOynat(int BasX, int BasY, int BitX, int BitY, Icon tas, boolean oyuncu) {//satranç ttahtası üzerinde secilen  konumu  sağlar eski konumu siler
        buton1[BasX][BasY].setIcon(null);// Mevcut konumdan siliyorum
        buton1[BitX][BitY].setIcon(tas);// Seçilen konuma iconu koyuyorum.
        //  System.out.println("hangiTas"+tas);
        // System.out.println("oyunvcu kim"+oyuncu );
        System.out.println("moved from " + "(" + BasX + "," + BasY + ")" + " to (" + BitX + "," + BitY + ")");
    }

    // Aşşağıdaki  kodlarda kurallarına uygun mu taşlar haraket ediyor kontroller sağlanıyor.
    public void piyonOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        System.out.println("oyuncu" + oyuncu);
        if (piyonGecerliMi(basX, basY, bitX, bitY, tas)) {
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);
        } else {
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "!Invalid move for pawn. Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            SecimiSıfırla();

        }
    }

    public void kaleOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        System.out.println("oyuncu" + oyuncu);

        if (kaleGecerliMi(basX, basY, bitX, bitY, tas)) {
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);
        } else {
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "!Invalid move for rook. Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            SecimiSıfırla();

        }
    }

    public void atOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        System.out.println("oyuncu" + oyuncu);
        if (atGecerliMi(basX, basY, bitX, bitY, tas)) {
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);
        } else {
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "!Invalid move for Knight . Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            SecimiSıfırla();

        }
    }

    public void filOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        System.out.println("oyuncu" + oyuncu);
        if (filGecerliMi(basX, basY, bitX, bitY, tas)) {
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);
        } else {
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "!Invalid move for Bishop . Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            SecimiSıfırla();

        }
    }

    public void vezirOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        System.out.println("oyuncu" + oyuncu);
        if (vezirGecerliMi(basX, basY, bitX, bitY, tas)) {
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);
        } else {
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "!Invalid move for Queen . Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            SecimiSıfırla();

        }
    }

    public void sahOyna(int basX, int basY, int bitX, int bitY, Icon tas, boolean oyuncu) {
        // sahın hareketleri herzaman global değişkende tutulur null ollduğunda oyun biter 
        System.out.println("oyuncu" + oyuncu);
        if (sahGecerliMi(basX, basY, bitX, bitY, tas)) {
            if (tas == sahSiyah) {
                sahSX = bitX;
                sahSY = bitY;
            } else if (tas == sahBeyaz) {
                sahBX = bitX;
                sahBY = bitY;
            }
            tasıOynat(basX, basY, bitX, bitY, tas, oyuncu);

        } else {
            System.out.println("Gecersiz hamle yaptı");
            JOptionPane.showMessageDialog(frm, "Invalid move for King . Please select again!", "INVALID MOVE", JOptionPane.WARNING_MESSAGE);
            buton1[bitX][bitY].setBackground(new java.awt.Color(255, 0, 0));
            SecimiSıfırla();

        }
    }

    private boolean atGecerliMi(int BasX, int BasY, int BitX, int BitY, Icon tas) {
        // AT L L gittiği için 2 sağ 1 karşı veya bir karşı 2 sağ olacak şekilde düzenliyorum.
        int m = BasX - BitX;
        if (m < 0) {
            m = m * (-1);
        }
        int k = BasY - BitY;
        if (k < 0) {
            k = k * (-1);
        }
        // eğer negatif değer gelirse diye -1 ilr yspıp mutlak değeri sağlıyorum.

        return ((m == 1 && k == 2) || (m == 2 && k == 1));// kontrol sağlanıyor.
    }

    private boolean sahGecerliMi(int BasX, int BasY, int BitX, int BitY, Icon tas) {
        //SAH  1 bir istediği yöne gider 
        int m = BasX - BitX;
        if (m < 0) {
            m = m * (-1);
        }
        int k = BasY - BitY;
        if (k < 0) {
            k = k * (-1);
        }
        // değerler mutlak değer kontrolü yapılıyor ve pozitif olmasını sağlıyorum.
        return ((m == 1 && k <= 1) || (k == 1 && m <= 1) || (m == 1 && k == 1));// dönüş şekli döndürülüyor.
    }

    private boolean kaleGecerliMi(int a, int b, int d, int e, Icon tas) {
        // kale sağ veya sol farketmez çapraz hariç çektiğin yere gider
        return (a == d || b == e);// aynı güzergahta mı kontrolü sağlanıyor.
    }

    private boolean filGecerliMi(int BasX, int BasY, int BitX, int BitY, Icon tas) {
        // fil çapraz istediği kadar gider.
        int m = BasX - BitX;
        if (m < 0) {
            m = m * (-1);
        }
        int k = BasY - BitY;
        if (k < 0) {
            k = k * (-1);
        }
        // burada mutlak değer kontrollerini sağlıyorum. 
        return (m == k);//eşit ise true döndürüuyorum.
    }

    private boolean vezirGecerliMi(int BasX, int BasY, int BitX, int BitY, Icon tas) {
        //Vezir hem kale gibi sağ sol yukarı aşşağı hemde fil gibi çapraz hareket edebilir.iki koşulu sağlanmalı.
        int m = BasX - BitX;
        if (m < 0) {
            m = m * (-1);
        }
        int k = BasY - BitY;
        if (k < 0) {
            k = k * (-1);
        }
        // burada mutlak değer kontrollerini sağlıyorum. 

        return (m == k || BasX == BitX || BasY == BitY);
    }

    private boolean piyonGecerliMi(int BasX, int BasY, int BitX, int BitY, Icon tas) {
        int temp = 0;
        int d = 0;
        int m = BasX - BitX;
        if (m < 0) {
            m = m * (-1);
        }
        int k = BasY - BitY;
        if (k < 0) {
            k = k * (-1);
        }
        // burada k ile m yi poztif değer olarak alıyorum aksi takdirde hata veriyor.
// bunu yapma sebebi eğer orda eleman var ise çapraz yiyebiliyor piyon bu yüzden.çaprazda  da değer olup olmadığı kontrolü sağlanıyor.
        if (BasX > BitX) {
            temp = -1;// temp değerini değiştiriyorum

        } else if (BasX < BitX) {
            temp = 1;// eğer baslangıç değeri  küçükse temp değeerine 1 atıyorum.
        }

        if (m == 1 && BasY == BitY) {
            return (buton1[BitX][BitY].getIcon() == null);
        } else if (BasY == BitY && m == 2) {

            d = BasX + temp;
            return (buton1[BitX][BitY].getIcon() == null && buton1[d][BitY].getIcon() == null);
        } else if (m == 1 && k == 1) {
            return (buton1[BitX][BitY].getIcon() != null);
        }

        return false;
    }

    public static void taslarıYerlestir() {// Tasları satranç panelimdeki butonların üstüne tek tek el ile yerleştiriyorum.

        // Tasların konumundan iconları alıyorum tek tek konumlarına el ile yerleştiriyorum.
        // koydum adresten tek tek iconları çekiyorum.
        vezirSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/vezirsiyah.png"));//.class.getResource("/img/vezirsiyah.png") bunu kullanmak zorundayım aksi takdirde .jar dosyamda taşlar gözükmüyor.Bu sayede geliyor
        sahSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/sahsiyah.png"));
        kaleSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/kalesiyah.png"));
        atSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/atsiyah.png"));
        filSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/filsiyah.png"));
        atBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/atbeyaz.png"));
        filBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/filbeyaz.png"));
        vezirBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/vezirbeyaz.png"));
        sahBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/sahbeyaz.png"));
        piyonSiyah = new ImageIcon(SatrancTCP.class.getResource("/resim/piyonsiyah.png"));
        kaleBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/kalebeyaz.png"));
        piyonBeyaz = new ImageIcon(SatrancTCP.class.getResource("/resim/piyonbeyaz.png"));
        // butonlara iconlar yerleştiriyor.

        buton1[0][5].setIcon(filBeyaz);
        buton1[0][6].setIcon(atBeyaz);
        buton1[0][7].setIcon(kaleBeyaz);
        buton1[0][0].setIcon(kaleBeyaz);
        buton1[0][1].setIcon(atBeyaz);
        buton1[0][2].setIcon(filBeyaz);
        buton1[0][3].setIcon(vezirBeyaz);
        buton1[0][4].setIcon(sahBeyaz);

        buton1[1][0].setIcon(piyonBeyaz);
        buton1[1][1].setIcon(piyonBeyaz);
        buton1[1][2].setIcon(piyonBeyaz);
        buton1[1][3].setIcon(piyonBeyaz);
        buton1[1][4].setIcon(piyonBeyaz);
        buton1[1][5].setIcon(piyonBeyaz);
        buton1[1][6].setIcon(piyonBeyaz);
        buton1[1][7].setIcon(piyonBeyaz);

        buton1[7][5].setIcon(filSiyah);
        buton1[7][6].setIcon(atSiyah);
        buton1[7][0].setIcon(kaleSiyah);
        buton1[7][1].setIcon(atSiyah);
        buton1[7][2].setIcon(filSiyah);
        buton1[7][3].setIcon(vezirSiyah);
        buton1[7][4].setIcon(sahSiyah);
        buton1[7][7].setIcon(kaleSiyah);

        buton1[6][0].setIcon(piyonSiyah);
        buton1[6][1].setIcon(piyonSiyah);
        buton1[6][2].setIcon(piyonSiyah);
        buton1[6][3].setIcon(piyonSiyah);
        buton1[6][4].setIcon(piyonSiyah);
        buton1[6][5].setIcon(piyonSiyah);
        buton1[6][6].setIcon(piyonSiyah);
        buton1[6][7].setIcon(piyonSiyah);

    }

    public void SunucuMesaj2(ArrayList<Object> hamleler, boolean oyuncu) {

        ArrayList<Point> noktalar = (ArrayList<Point>) hamleler.get(0);
        //Buradan noktalar alınıyor ve cliente mesaj olarak döndürülüyorç 
        int BitY = noktalar.get(1).x;
        int BasX = noktalar.get(0).y;
        int BitX = noktalar.get(1).y;
        int BasY = noktalar.get(0).x;

        Icon icon = satrancM.buton1[BasX][BasY].getIcon();
        // icona uygun hamle yapılıyor.

        if (icon == piyonSiyah || icon == piyonBeyaz) {
            piyonOyna(BasX, BasY, BitX, BitY, icon, oyuncu);
        } else if (icon == kaleBeyaz || icon == kaleSiyah) {
            kaleOyna(BasX, BasY, BitX, BitY, icon, oyuncu);

        } else if (icon == atBeyaz || icon == atSiyah) {
            atOyna(BasX, BasY, BitX, BitY, icon, oyuncu);

        } else if (icon == filBeyaz || icon == filSiyah) {
            filOyna(BasX, BasY, BitX, BitY, icon, oyuncu);

        } else if (icon == vezirBeyaz || icon == vezirSiyah) {
            vezirOyna(BasX, BasY, BitX, BitY, icon, oyuncu);

        } else if (icon == sahBeyaz || icon == sahSiyah) {
            sahOyna(BasX, BasY, BitX, BitY, icon, oyuncu);
        }

        oyuncu1 = !oyuncu1;

    }

    private void SecimiSıfırla() {// Yapılan seçimi sıfırlar ki bir sonrakinde takılı kalmasın ve bir daha seçim yapabilsin oyuncu.
        // hiçbir şey seçilmemiş gibi olması için default boş değerleri atıyorum.
        secimX = -1;
        SecimY = -1;
        for (int a = 0; a <= 7; a++) {// butonları satır ve sutun olarak geziyor.

            for (int b = 0; b <= 7; b++) {
                int k = a + b;
                if (k % 2 == 0) {///% 2   sebebi bir pembe bir beyaz olsun diye 
                    buton1[a][b].setBackground(Color.WHITE);// tekrardan beyaz rengi 2 de bir butona atıyor.

                } else {

                    buton1[a][b].setBackground(new java.awt.Color(180, 150, 140));// tekrardan pembe rengi 2 de 1 butonaatıyor.
                }
            }
        }
    }

    public static void taslaslar(LinkedList taslarListe) {
        taslarListe.add(kaleBeyaz);
        taslarListe.add(kaleSiyah);
        taslarListe.add(atBeyaz);
        taslarListe.add(filBeyaz);
        taslarListe.add(vezirBeyaz);
        taslarListe.add(sahBeyaz);

        taslarListe.add(piyonBeyaz);
        taslarListe.add(atSiyah);
        taslarListe.add(atBeyaz);
        taslarListe.add(filSiyah);
        taslarListe.add(vezirSiyah);
        taslarListe.add(sahSiyah);
        taslarListe.add(piyonSiyah);
        // tasların hepsini linkList içerine atıyoruz.Çünkü Bunlar servera gönderilecek.
    }

    public static void main(String[] args) throws IOException {
        satrancM = new SatrancTCP();//oyun nesneesi oluşturuyorum ki ekrana jframe gelsin
        Client oyuncu = new Client(satrancM, 2);// oyuncu oluşturuyorum.
        oyuncu.Start("13.50.235.201", 5010);// benim aws ip ve 5010 porta bağlansın

        Mesaj gndr = new Mesaj(Mesaj.gndr_t.Taraf);// hangi taraf 
        gndr.content = satrancM.oyuncu2;

        System.out.println("kimde sıra" + satrancM.oyuncu1);
        Client.Send(gndr);

    }

}
