import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*ElitUye classı içerisinde tanımlanmış olan değişkenlere kullanıcının girdiği bilgiler atanır.
Main fonksiyonu içerisinde döngüde olduğundan üç tane üye bilgisi dosyaya kaydedilebilir.*/
class ElitUye {
    String elitIsim;
    String elitSoyisim;
    String elitMail;
    public void elitUyeBilgiGiris () {
        Scanner scan = new Scanner(System.in);
        System.out.println("Elit üye bilgilerini giriniz:");
        System.out.println("İsim:");
        elitIsim=scan.nextLine();
        System.out.println("Soyisim:");
        elitSoyisim=scan.nextLine();
        System.out.println("Mail:");
        elitMail=scan.nextLine();
    }
}

/*ElitUye classının alt sınıf olan ElitUyeBilgiKayıt classında ise üst classda değer atanan değişkenlerin değerleri
birer dizi elemanına dönüştürülür.Main fonksiyonu içerisinde döngüde olduğundan üç üyeninde bilgileri ayrı elemanlar
şeklinde tutulur.(Elit üye bilgilerini dosyadan okutamadım.)*/
class ElitUyeBilgiKayit extends ElitUye{
    String[] elitIsimDizi=new String[3];
    String[] elitMailDizi=new String[3];
    String[] elitSoyisimDizi=new String[3];
    public void diziIsimKayit(int e) {
        elitIsimDizi[e]= elitIsim;
        elitSoyisimDizi[e]=elitSoyisim;
        elitMailDizi[e]=elitMail;
    }
}

/*GenelUye classı içerisinde tanımlanmış olan değişkenlere kullanıcının girdiği bilgiler atanır.*/
class GenelUye {
    String genelIsim;
    String genelSoyisim;
    String genelMail;
    public void genelUyeBilgiGiris() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Genel üye bilgilerini giriniz:");
        System.out.println("İsim:");
        genelIsim=scan.nextLine();
        System.out.println("Soyisim:");
        genelSoyisim=scan.nextLine();
        System.out.println("Mail:");
        genelMail=scan.nextLine();
    }
}

/*Mail classı içerisinde, kullanıcı mail gönderme işlemini seçtikten sonra karşısına çıkacak menü ve seçenekler yer alır.
Kullanıcının girecek olduğu mail başlığı ve içeriği tanımlanan değişkenlere atanır.*/
class Mail {
    static int islemNo;
    static String mailElitBaslik,mailElitIcerik;
    static String mailGenelBaslik,mailGenelIcerik;
    static String mailTumBaslik,mailTumIcerik;

    public void mailSec() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Mail Gönderme Seçenekleri:");
        System.out.println("1.Elit Üyelere Gönder\n2.Genel Üyelere Gönder\n3.Tüm Üyelere Gönder");
        System.out.println("Yapmak istediğiniz işlemin sıra numarasını tuşlayınız:");
        islemNo = scan.nextInt();

        switch (islemNo) {
            case 1:
                System.out.println("Mail Başlık:");
                scan.nextLine();
                mailElitBaslik=scan.nextLine();
                System.out.println("Mail İçerik:");
                mailElitIcerik = scan.nextLine();
                break;
            case 2:
                System.out.println("Mail Başlık:");
                scan.nextLine();
                mailGenelBaslik=scan.nextLine();
                System.out.println("Mail İçerik:");
                mailGenelIcerik = scan.nextLine();
                break;
            case 3:
                System.out.println("Mail Başlık:");
                scan.nextLine();
                mailTumBaslik=scan.nextLine();
                System.out.println("Mail İçerik:");
                mailTumIcerik = scan.nextLine();
                break;
        }
    }
}

/*Mail classının alt sınıfı olan MailGonder classı içerisinde simülasyon olmayan, gerçekten mailin gitmesini sağlayan
işlemler yer alır.Ayrıca üst sınıf olan Mail classı içerisinde seçilen işleme göre atanan mailler uygun üyelere gönderilir.*/
class MailGonder extends Mail {
    public void mailGonder(String alici) throws MessagingException {

        System.out.println("Mailiniz gönderilmeye hazırlanılıyor...");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        String gonderenMail="";
        String gonderenSifre="";
        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gonderenMail, gonderenSifre);
            }
        });

        Message message = prepareMessage(session, gonderenMail, alici) ;
        Transport.send(message);
        System.out.println("Mailiniz gönderildi.");
    }
    private static Message prepareMessage(Session session, String myAccountEmail, String recepient){
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            if(islemNo==1) {
                message.setSubject(mailElitBaslik);
                message.setText(mailElitIcerik);
            }
            else if(islemNo==2) {
                message.setSubject(mailGenelBaslik);
                message.setText(mailGenelIcerik);
            }
            else if(islemNo==3) {
                message.setSubject(mailTumBaslik);
                message.setText(mailTumIcerik);
            }
            return message;
        } catch (Exception ex) {
            Logger.getLogger(MailGonder.class.getName()).log(Level.SEVERE, null,ex);
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) throws IOException, MessagingException {

        Scanner scan = new Scanner(System.in);

        File dosya = new File("ElitUyeBilgileri.txt");
        if(!dosya.exists()) {
            dosya.createNewFile();
        }
        File dosya2 = new File("GenelUyeBilgileri.txt");
        if(!dosya2.exists()) {
            dosya2.createNewFile();
        }

        FileWriter fwriter = new FileWriter(dosya,false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);

        FileWriter f2writer = new FileWriter(dosya2,false);
        BufferedWriter b2writer = new BufferedWriter(f2writer);

        ElitUyeBilgiKayit elitUye = new ElitUyeBilgiKayit();
        GenelUye genelUye = new GenelUye();
        MailGonder mail = new MailGonder();

/*do-while döngüsü içerisinde kullanıcıdan menüde yer alan işlemlerden birini seçmesi istenen kısım yer alır.
Döngü sayesinde kullanıcı bir işlemi sonlandırdıktan sonra yeni bir işlem için sorulur.*/
        int devam;

        do {
            System.out.println("---------Menü---------");
            System.out.println("1.Elit Üye Ekleme\n2.Genel Üye Ekleme\n3.Mail Gönderme");
            System.out.println("Yapmak istediğiniz işlemin sıra numarasını tuşlayınız:");
            int siraNo = scan.nextInt();

            if(siraNo==1) {
                bwriter.write("---Elit Üye Bilgileri---\n");
                for(int i=0;i<3;i++) {
                    elitUye.elitUyeBilgiGiris();
                    elitUye.diziIsimKayit(i);
                    bwriter.write(elitUye.elitIsim+"\t");
                    bwriter.write(elitUye.elitSoyisim+"\t");
                    bwriter.write(elitUye.elitMail+"\n");
                }
                bwriter.close();
            }
            else if(siraNo==2) {
                b2writer.write("---Genel Üye Bilgileri---\n");
                genelUye.genelUyeBilgiGiris();
                b2writer.write(genelUye.genelIsim+"\t");
                b2writer.write(genelUye.genelSoyisim+"\t");
                b2writer.write(genelUye.genelMail+"\n");
                b2writer.close();
            }
            else if(siraNo==3) {
                mail.mailSec();
                if(mail.islemNo==1) {
                    /*ElitUye classının alt sınıf olan ElitUyeBilgiKayıt classında yapılan işlemde dizinin elemanları
                    olarak atanan elit üye mailleri birer String değişkene atanır.*/
                    String elitMailOku1=elitUye.elitMailDizi[0];
                    String elitMailOku2=elitUye.elitMailDizi[1];
                    String elitMailOku3=elitUye.elitMailDizi[2];

                    /*Bu atamanın sebebi, değer atanan String değişkenlerini Mail classının alt sınıf olan MailGonder
                    classı içerisinde yer alan mailgonder fonksiyonunda parametre olarak kullanabilmektir.*/
                    mail.mailGonder(elitMailOku1);
                    mail.mailGonder(elitMailOku2);
                    mail.mailGonder(elitMailOku3);
                }
                else if(mail.islemNo==2) {
                    //Bu işlem genel üye olarak kaydedilen kişinin bilgilerini dosyadan, tek bir satırdan okur.
                    int genelSatirNo=2;
                    BufferedReader breader = new BufferedReader(new FileReader("GenelUyeBilgileri.txt"));
                    String genelSatir;
                    int genelIndex=1;
                    boolean kontrol=false;
                    while((genelSatir=breader.readLine())!=null) {
                        if(genelIndex==genelSatirNo) {
                            kontrol=true;
                            break;
                        }
                        genelIndex++;
                    }
                    if(!kontrol) {
                        System.out.println("Bir hata oluştu.");
                    }

                    /*Aşağıdaki iki satır kod da, bütün halinde okunan bu satırdaki bilgileri aralarında bulunan
                    tab karakterinden bölmek için kullanılır.Bölünen bilgiler bir dizinin elemanları şeklinde
                    kaydedilir ve üçüncü eleman (yani 2. indexte bulunan eleman) maildir.*/
                    String genelSatirOku=genelSatir;
                    String[] genelSatirEleman = genelSatirOku.split("\t");

                    /*Bu iki satırda da maialin saklandığı dizi elemanındaki mail bilgisi başka bir String
                    değişkenine atanır ve bu değişken mailGonder fonksiyonunda parametre olarak kullanılır.*/
                    String genelMailOku = genelSatirEleman[2];
                    mail.mailGonder(genelMailOku);
                }
                else if(mail.islemNo==3) {
                    /*Tüm üyeler seçilip mail atılmak istendiğinde de mail atma işlemlerinden 1. de, 2. de bu kontrol
                    yapısı içerisinde gerçekleştirilir.*/
                    String elitMailOku1=elitUye.elitMailDizi[0];
                    String elitMailOku2=elitUye.elitMailDizi[1];
                    String elitMailOku3=elitUye.elitMailDizi[2];

                    mail.mailGonder(elitMailOku1);
                    mail.mailGonder(elitMailOku2);
                    mail.mailGonder(elitMailOku2);

                    int genelSatirNo=2;
                    BufferedReader breader = new BufferedReader(new FileReader("GenelUyeBilgileri.txt"));
                    String genelSatir;
                    int genelIndex=1;
                    boolean kontrol=false;
                    while((genelSatir=breader.readLine())!=null) {
                        if(genelIndex==genelSatirNo) {
                            kontrol=true;
                            break;
                        }
                        genelIndex++;
                    }
                    if(!kontrol) {
                        System.out.println("Bir hata oluştu.");
                    }

                    String genelSatirOku=genelSatir;
                    String[] genelSatirEleman = genelSatirOku.split("\t");

                    String genelMailOku = genelSatirEleman[2];
                    mail.mailGonder(genelMailOku);
                }
            }
            System.out.println("İşlem yapmaya devam etmek istiyor musunuz? \nEvet ise 1, Hayır ise 0 tuşlayınız:");
            devam=scan.nextInt();
        } while (devam==1);
    }
}

