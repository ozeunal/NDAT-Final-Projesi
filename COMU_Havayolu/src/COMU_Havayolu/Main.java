package COMU_Havayolu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

	Ucus ucus;
	private static Kullanici kullanici;

	public static void main(String[] args) {
		
		while(true) {
		Scanner input = new Scanner(System.in);
		System.out.println();
		System.out.println("COMU Havayolu Sistemine Hoş Geldiniz!");
		System.out.println();
		System.out.println("-----Menu-----\n1-Kayıt Ol\n2-Giriş Yap\n3-Kapat");
		System.out.println();
		System.out.print("Yapmak istediğiniz işlemi seçiniz:");
		
		int secim = input.nextInt();
		
		switch(secim) {
		case 1: 
			Kullanici.kayit();
			break;
		case 2: 
			giris();
		case 3: 
			System.out.print("Güle güle!");
			System.out.println();
			break;
		default:
			System.out.print("Tekrar girin!");
			System.out.println();
		}
		}

	}
	
	
		public static void giris() {
		    try {
		        Scanner input = new Scanner(System.in);
		        System.out.print("E-posta: ");
		        String email = input.nextLine();
		        System.out.print("Parola: ");
		        String parola = input.nextLine();

		        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "");

		        String sql = "SELECT * FROM kullanici WHERE eposta = ? AND parola = ?";
		        PreparedStatement statement = connection.prepareStatement(sql);
		        statement.setString(1, email);
		        statement.setString(2, parola);
		        
		        // Sorguyu çalıştırma
		        ResultSet resultSet = statement.executeQuery();

		        if (resultSet.next()) {
		            int kId = resultSet.getInt("k_id");
		            String ad = resultSet.getString("ad");
		            String soyad = resultSet.getString("soyad");
		            String tc = resultSet.getString("tc");
		            LocalDate dogumTarihi = resultSet.getDate("dogumTarihi").toLocalDate();
		            String telefonNumarasi = resultSet.getString("telefon");
		            char cinsiyet = resultSet.getString("cinsiyet").charAt(0);
		            char medeniDurum = resultSet.getString("medeniDurum").charAt(0);
		            String eposta = resultSet.getString("eposta");
		            int parola1 = resultSet.getInt("parola");
		            LocalDate kayitTarihi = resultSet.getDate("kayitTarihi").toLocalDate();
		            String uyelikTipi = resultSet.getString("uyelik_tipi");
		            Kullanici kullanici = new Kullanici();
		            kullanici.setId(kId);
		            kullanici.setAd(ad);
		            kullanici.setSoyad(soyad);
		            kullanici.setCinsiyet(cinsiyet);
		            kullanici.setDogumTarihi(dogumTarihi);
		            kullanici.setEposta(eposta);
		            kullanici.setMedeniDurum(medeniDurum);
		            kullanici.setParola(parola1);
		            kullanici.setTc(tc);
		            kullanici.setTelefonNumarasi(telefonNumarasi);
		            kullanici.setKayitTarihi(kayitTarihi);
		            kullanici.setUyelikTipi(uyelikTipi);
		            setKullanici(kullanici);
		            
		            int gecenYil = Kullanici.kayitTarihindenGecenYil(kId);
		            if (gecenYil >= 10) {
		            	Kullanici.uyelikTipi(kId, "VIP");
		                System.out.println("Hoşgeldiniz VIP " + resultSet.getString("ad").substring(0, 1).toUpperCase()+resultSet.getString("ad").substring(1) + " " + resultSet.getString("soyad").toUpperCase());
		            } else if(gecenYil>=5) {
		            	Kullanici.uyelikTipi(kId, "Daimi Üye");
		                System.out.println("Hoşgeldiniz Daimi Üyemiz "+ resultSet.getString("ad").substring(0, 1).toUpperCase()+resultSet.getString("ad").substring(1) + " " + resultSet.getString("soyad").toUpperCase());
		            }else {
		            	System.out.println("Hoşgeldiniz "+ resultSet.getString("ad").substring(0, 1).toUpperCase()+resultSet.getString("ad").substring(1) + " " + resultSet.getString("soyad").toUpperCase());
		            }
		            
		            

		            Ucus.ucus();
		        } else {
		            System.out.println("E-posta veya parola hatalı!");
		        }

		        // Bağlantıyı kapatma
		        connection.close();

		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		
	}
		static void setKullanici(Kullanici kullanici) {
	        Main.kullanici = kullanici;
	    }
		
		public static Kullanici getKullanici() {
		    return kullanici;
		}
		
}
