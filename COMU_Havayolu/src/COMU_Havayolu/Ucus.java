package COMU_Havayolu;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ucus {
    private String nereden;
    private String nereye;
    private String kalkis_saat;
    private String varis_saat;
    private String gun;
    private double fiyat;
    private String ucus_sikligi;
    private Date son_uctugu_gun;
    Koltuk koltuk;
    //private List<Koltuk> koltuklar;
    /*
    private int koltuk_kapasite;
    private int VIP_koltuk_kapasite;
    */

    
    public Ucus(String nereden,String nereye, String kalkis_saat, String varis_saat, String gun, double fiyat,String ucus_sikligi, Date son_uctugu_gun) {
        this.nereden = nereden;
        this.nereye = nereye;
        this.kalkis_saat = kalkis_saat;
        this.varis_saat = varis_saat;
        this.gun = gun;
        this.fiyat = fiyat;
        this.ucus_sikligi = ucus_sikligi;
        this.son_uctugu_gun = son_uctugu_gun;
        //this.koltuklar=new ArrayList<>();
        /*
        this.koltuk_kapasite = koltuk_kapasite;
        this.VIP_koltuk_kapasite = VIP_koltuk_kapasite;
        */
    }



	public String getNereden() {
        return nereden;
    }
    public String getNereye() {
        return nereye;
    }
    public void setNereden(String nereden) {
        this.nereden = nereden;
    }
    public void setNereye(String nereye) {
        this.nereye = nereye;
    }
    public String getKalkisSaat() {
        return kalkis_saat;
    }
    public String getVarisSaat() {
        return varis_saat;
    }
    public void setKalkisSaat(String kalkis_saat) {
        this.kalkis_saat = kalkis_saat;
    }
    public void setVarisSaat(String varis_saat) {
        this.varis_saat = varis_saat;
    }
    public String getGun() {
        return gun;
    }
    public void setGun(String gun) {
        this.gun = gun;
    }
    public double getFiyat() {
        return fiyat;
    }
    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }
    public String getUcusSikligi() {
    	return ucus_sikligi;
    }
    public void setUcusSikligi(String ucus_sikligi) {
    	this.ucus_sikligi = ucus_sikligi;
    }
    public Date getSonUctuguGun() {
    	return son_uctugu_gun;
    }
    public void setSonUctuguGun(Date son_uctugu_gun) {
    	this.son_uctugu_gun = son_uctugu_gun;
    }
    /*
    public int getKoltukKapasite() {
    	return koltuk_kapasite;
    }
    public void setKoltukKapasite(int koltuk_kapasite) {
    	this.koltuk_kapasite = koltuk_kapasite;
    }
    public int getVIPKoltukKapasite() {
    	return VIP_koltuk_kapasite;
    }
    public void setVIPKoltukKapasite(int VIP_koltuk_kapasite) {
    	this.VIP_koltuk_kapasite = VIP_koltuk_kapasite;
    }
    */
    
 // toString() metodu: Uçuş bilgilerini metin olarak döndüren bir metod
    @Override
    public String toString() {
        return String.format(
            "Uçuş: %s'dan %s'ye | Kalkış: %s | Varış: %s | Gün: %s | Fiyat: %.2f TL | Uçuş Sıklığı: %s | Son Uçtuğu Gün: %s | ",
            nereden, nereye, kalkis_saat, varis_saat, gun, fiyat, ucus_sikligi, son_uctugu_gun
        );
    }
    public static void main(String[] args) {
    	
    
    }
    

    public static void ucus() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("1-Tek Yön\n2-Gidiş - Dönüş\n3-Çıkış Yap");
        System.out.println();
        System.out.println("Yapmak istediğiniz işlemi seçiniz:");
        int secim = input.nextInt();
        switch (secim) {
            case 1:
                ucusTekYon();
                break;
            case 2:
                ucusGidisDonus();
                break;
            case 3:
                cikis();
                // Ana menüye dönüş
                Main.main(null);
                break;
            default:
                System.out.print("Hatalı Secim");
                ucus();
                break;
        }
    }
    
    public static void ucusTekYon() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("TEK YÖN UÇUŞ");
        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("NEREDEN:");
        String nereden = input.nextLine();

        System.out.print("NEREYE:");
        String nereye = input.nextLine();

        System.out.print("Hangi gün uçmak istersiniz:");
        String gun = input.nextLine();

        // Veritabanından tek yönlü uçuşları sorgula ve listele
        sorgulaVeListele(nereden, nereye, gun);
    }

    public static void ucusGidisDonus() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("GİDİŞ-DÖNÜŞ UÇUŞ");
        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("GİDİŞ İÇİN");
        System.out.println("NEREDEN:");
        String neredenGidis = input.nextLine();

        System.out.print("NEREYE:");
        String nereyeGidis = input.nextLine();

        System.out.print("Hangi gün uçmak istersiniz:");
        String gunGidis = input.nextLine();
        sorgulaVeListele(neredenGidis, nereyeGidis, gunGidis);
        System.out.println();
        System.out.println("DÖNÜŞ İÇİN");
        System.out.println("NEREDEN:");
        String neredenDonus = input.nextLine();

        System.out.print("NEREYE:");
        String nereyeDonus = input.nextLine();

        System.out.print("Hangi gün dönmek istersiniz:");
        String gunDonus = input.nextLine();

        // Veritabanından gidiş-dönüş uçuşları sorgula ve listele
        
        sorgulaVeListele(neredenDonus, nereyeDonus, gunDonus);
    }

    public static void sorgulaVeListele(String nereden, String nereye, String gun) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "")) {
            String sql = "SELECT * FROM Ucus WHERE nereden = ? AND nereye = ? AND gun LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nereden);
                statement.setString(2, nereye);
                statement.setString(3, "%" + gun + "%"); 

                ResultSet resultSet = statement.executeQuery();

                int index = 1;
                while (resultSet.next()) {
                    Ucus ucus = new Ucus(
                        resultSet.getString("nereden"),
                        resultSet.getString("nereye"), 
                        resultSet.getString("kalkis_saati"),
                        resultSet.getString("varis_saati"),
                        resultSet.getString("gun"),
                        resultSet.getDouble("fiyat"),
                        resultSet.getString("ucus_sıklığı"),
                        resultSet.getDate("son_uctugu_gun")
                    );
                    System.out.println(index + ". " + ucus.toString());
                    index++;
                }
                Scanner input = new Scanner(System.in);
                
                if (index > 1) {
                	 
	                 System.out.print("Hangi uçuşu seçmek istersiniz? (1-" + (index - 1) + "): ");
	                 int secilen_ucus = input.nextInt();

	                 if (secilen_ucus > 0 && secilen_ucus < index) {
	                     System.out.println("Seçilen uçuş: " + secilen_ucus);
	                     Koltuk.main(null);
	                     
	                 } 
	                 else {
	                        System.out.println("Geçersiz seçim.");
	                    }
	                } 
	                else {
	                    System.out.println("Belirtilen kriterlere uygun hiçbir uçuş bulunamadı. :(");
	                    System.out.println("Yeni rota beliryebilir veya çıkış yapabilirsiniz");
	                    int secilen_ucus = input.nextInt();
	            		
	            		switch(secilen_ucus) {
	            		case 1: 
	            			ucus();
	            			break;
	            		case 2: 
	            			cikis();
	            			
	            			return;
	            	
	            		}
	                }
	            }
	        } catch (SQLException e) {
	            System.err.println("Hata: " + e.getMessage());
	        }
    }

    private static void cikis() {
        System.out.print("Güle güle!");
        System.out.println();
    }

    
    
}
