package COMU_Havayolu_1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

	Ucus ucus;

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
			kayit();
			break;
		case 2: 
			giris();
			
			return;
		case 3: 
			System.out.print("Güle güle!");
			System.out.println();
			break;
		default:
			System.out.print("Tekrar girin!");
			System.out.println();
			return;
		}
	}

}
	
	public static void kayit() {
		   try {
		Scanner input = new Scanner(System.in);
		System.out.print("İsim: ");
		String name=input.nextLine();
		System.out.print("Soyisim: ");
		String surname=input.nextLine();
		System.out.print("TC: ");
		String id=input.nextLine();
		System.out.print("Doğum Tarihi (GG.AA.YYYY formatında): ");
	    String birthdayStr = input.nextLine();
	    LocalDate birthday = LocalDate.parse(birthdayStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
		System.out.print("Telefon Numarası: ");
		String telephone_number=input.nextLine();
		System.out.print("Cinsiyet: Kadın(K)/Erkek(E)");
		String sexStr=input.nextLine();
		char sex = sexStr.charAt(0);
		System.out.print("Medeni Durum: Bekar(B)/Evli(E)");
        String maritalStatusStr = input.nextLine();
        char maritalStatus = maritalStatusStr.charAt(0);
		System.out.print("E-posta: ");
		String email=input.nextLine();
		System.out.print("Parola: ");
		String parola=input.nextLine();
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/deneme", "root", "");
        // SQL sorgusu oluşturma
        String sql = "INSERT INTO kullanici (ad, soyad, tc, dogumTarihi, telefon, cinsiyet, medeniDurum, eposta, parola) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, surname);
        statement.setString(3, id);
        statement.setDate(4, java.sql.Date.valueOf(birthday));
        statement.setString(5, telephone_number);
        statement.setString(6, String.valueOf(sex));
        statement.setString(7, String.valueOf(maritalStatus));
        statement.setString(8, email);
        statement.setString(9, parola);

        // Sorguyu çalıştırma
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Kullanıcı başarıyla eklendi.");
        }

        // Bağlantıyı kapatma
        connection.close();
    } catch (Exception e) {
        System.err.println("Hata: " + e.getMessage());
      }
		
	}
	
	public static void giris() {
		try {
		Scanner input = new Scanner(System.in);
		System.out.print("E-posta: ");
		String email=input.nextLine();
		System.out.print("Parola: ");
		String parola=input.nextLine();
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/deneme", "root", "");
		
		 String sql = "SELECT * FROM kullanici WHERE eposta = ? AND parola = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setString(1, email);
	        statement.setString(2, parola);

	        // Sorguyu çalıştırma
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            System.out.println("Giriş başarılı!");
	            ucus();
	        } else {
	            System.out.println("E-posta veya parola hatalı! Tekrar dene");
	            giris();
	       }

	        // Bağlantı kapatma
	        connection.close();
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	 public static void ucus() {
		 Scanner input = new Scanner(System.in);
		 System.out.println("---------------------------------------------------------------------------------------");
		 System.out.println("1-Tek Yön\n2-Gidis - Dönüş\n3-Çıkış Yap");
		 System.out.println();
		 System.out.println("Yapmak istediğiniz işlemi seçiniz:");
		 int secim = input.nextInt();
		 switch(secim) {
		 case 1: 
			 System.out.println("Tek Yön");
			 break;
		 case 2:
			 System.out.println("Gidiş - Dönüş");
			 break;
		 case 3:
			 cikis();
			 //Ana menüye dönüş
			 main(null);
			 return;
		default:
			System.out.print("Hatalı Secim");
			ucus();
			return;
		 }
		 Scanner input1 = new Scanner(System.in);
	     System.out.println("NEREDEN:");
	     String nereden = input1.nextLine();

	     System.out.print("NEREYE:");
	     String nereye = input1.nextLine();

	     System.out.print("Hangi gün uçmak istersiniz:");
	     String gun = input1.nextLine();

	     try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/deneme", "root", "")) {
	            // SQL sorgusunu oluşturun. Nereden, nereye ve istenilen gün kriterine göre sorgu 
	         String sql = "SELECT * FROM Ucus WHERE nereden = ? AND nereye = ? AND gun LIKE ?";
	         try (PreparedStatement statement = connection.prepareStatement(sql)) {
	                // Kullanıcının belirttiği kriterlere göre sorgu hazırlama.
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
	                     resultSet.getDate("son_uctugu_gun"),
	                     resultSet.getInt("koltuk_sayisi"),
	                     resultSet.getInt("VIP_koltuk_sayisi")
	                 );
	                 System.out.println(index + ". " + ucus.toString());
	                 index++;
	             }

	             if (index > 1) {
	                 System.out.print("Hangi uçuşu seçmek istersiniz? (1-" + (index - 1) + "): ");
	                 int secilen_ucus = input1.nextInt();

	                 if (secilen_ucus > 0 && secilen_ucus < index) {
	                     System.out.println("Seçilen uçuş: " + secilen_ucus);
	                 } 
	                 else {
	                        System.out.println("Geçersiz seçim.");
	                    }
	                } 
	                else {
	                    System.out.println("Belirtilen kriterlere uygun hiçbir uçuş bulunamadı. :(");
	                    System.out.println("Yeni rota beliryebilir veya çıkış yapabilirsiniz");
	                    int secilen_ucus = input1.nextInt();
	            		
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
