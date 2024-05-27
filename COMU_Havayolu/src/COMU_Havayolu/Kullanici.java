package COMU_Havayolu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Kullanici {
	
	private int k_id;;
	private String ad;
	private String soyad;
	private String tc;
	private LocalDate dogum_tarihi;
	private String telefon_numarasi;
	private char cinsiyet;
	private char medeni_durum;
	private String e_posta;
	private int parola;
	private LocalDate kayit_tarihi;
	private String uyelik_tipi;
	
	public Kullanici(String email, int parola) {
		e_posta = email;
		this.parola = parola; 
	}
	
	public void setId(int id) {
	    k_id=id;
	}
	
	public int getId() {
        return k_id;
    }
	
	public void setUyelikTipi(String uyelikTipi) {
        uyelik_tipi= uyelikTipi;
        
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "")) {
            String updateSql = "UPDATE kullanici SET uyelik_tipi = ? WHERE k_id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, uyelikTipi);
            updateStatement.setInt(2, this.k_id);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public String getUyelikTipi() {
        return uyelik_tipi;
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

    public static int[] kayitBilgileri(int kId) {
        int[] bilgiler = new int[2]; // 0 index -> geçen yıl, 1 index -> total bilet sayısı
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "")) {
            // Kayıt tarihini ve total bilet sayısını çekmek için SQL sorgusu
            String sql = "SELECT kayitTarihi, COUNT(bilet_id) AS total_bilet FROM kullanici LEFT JOIN bilet ON kullanici.k_id = bilet.k_id WHERE kullanici.k_id = ? GROUP BY kullanici.k_id";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, kId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                LocalDate kayitTarihi = resultSet.getDate("kayitTarihi").toLocalDate();
                // Bugünün tarihini al
                LocalDate bugun = LocalDate.now();

                // Kayıt tarihinden bugüne kadar geçen yılları hesapla
                Period period = Period.between(kayitTarihi, bugun);
                bilgiler[0] = period.getYears();

                // Total bilet sayısını al
                bilgiler[1] = resultSet.getInt("total_bilet");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bilgiler;
    }
}


