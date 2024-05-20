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
	
	/*
	public Kullanici(int id,String name, String surname, String tc, LocalDate birthday, String telephone_number, char sex, 
			char marital_status, String email, int parola, LocalDate date_of_record, String uyelik_tipi) {
		k_id=id;
		ad = name;
		soyad = surname;
		tc = tc;
		dogum_tarihi = birthday;
		telefon_numarasi = telephone_number;
		cinsiyet = sex;
		medeni_durum = marital_status;
		e_posta = email;
		this.parola = parola; 
		kayit_tarihi = date_of_record;
		uyelik_tipi=uyelik_tipi;
	
	}
	*/
	
	public void setId(int id) {
	    k_id=id;
	}
	
	public int getId() {
        return k_id;
    }
	
	public void setUyelikTipi(String uyelik_tipi) {
        uyelik_tipi= uyelik_tipi;
    }
	
	public String getUyelikTipi() {
        return uyelik_tipi;
    }
	
	public String getAd() {
        return ad;
    }

    public void setAd(String name) {
        ad = name;
    }

    // Soyad (Surname)
    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String surname) {
        soyad = surname;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        tc = tc;
    }

    public LocalDate getDogumTarihi() {
        return dogum_tarihi;
    }

    public void setDogumTarihi(LocalDate birthday) {
        dogum_tarihi = birthday;
    }

    public String getTelefonNumarasi() {
        return telefon_numarasi;
    }

    public void setTelefonNumarasi(String telephone_number) {
        telefon_numarasi = telephone_number;
    }

    public char getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(char sex) {
        cinsiyet = sex;
    }

    public char getMedeniDurum() {
        return medeni_durum;
    }

    public void setMedeniDurum(char marital_status) {
        medeni_durum = marital_status;
    }

    public String getEposta() {
        return e_posta;
    }

    public void setEposta(String email) {
    	e_posta = email;
    }

    public int getParola() {
        return parola;
    }

    public void setParola(int parola) {
        this.parola = parola;
    }
  
    public LocalDate getKayitTarihi() {
        return kayit_tarihi;
    }

    public void setKayitTarihi(LocalDate date_of_record) {
        kayit_tarihi = date_of_record;
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
    
    public static void uyelikTipi(int kId, String uyelikTipi) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root", "")) {
            String sql = "UPDATE kullanici SET uyelik_tipi = ? WHERE k_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uyelikTipi);
            statement.setInt(2, kId);

            statement.executeUpdate();
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


