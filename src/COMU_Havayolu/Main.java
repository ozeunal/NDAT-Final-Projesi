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
		
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "");
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
		            int gecenYil = kayitTarihindenGecenYil(kId);
		            if (gecenYil >= 3) {
		                System.out.println("Hoşgeldiniz VIP " + resultSet.getString("ad").substring(0, 1).toUpperCase()+resultSet.getString("ad").substring(1) + " " + resultSet.getString("soyad").toUpperCase());
		            } else {
		                System.out.println("Hoşgeldiniz! "+ resultSet.getString("ad").substring(0, 1).toUpperCase()+resultSet.getString("ad").substring(1) + " " + resultSet.getString("soyad").toUpperCase());
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
	
	
	public static int kayitTarihindenGecenYil(int kId) {
		
	     try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "")) {
	            // Kayıt tarihini çekmek için SQL sorgusu
	    	   String sql = "SELECT kayitTarihi FROM kullanici WHERE k_id = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setInt(1, kId);

	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                LocalDate kayitTarihi = resultSet.getDate("kayitTarihi").toLocalDate();
	                // Bugünün tarihini al
	                LocalDate bugun = LocalDate.now();

	                // Kayıt tarihinden bugüne kadar geçen yılları hesapla
	                Period period = Period.between(kayitTarihi, bugun);
	                return period.getYears();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return 0;
    }
	
}
