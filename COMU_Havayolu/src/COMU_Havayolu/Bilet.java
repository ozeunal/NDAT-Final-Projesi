package COMU_Havayolu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Bilet {
	
	private int bilet_id;
	
	public int getBiletId() {
        return bilet_id;
    }
    
    public void setBiletId(int biletId) {
        bilet_id=biletId;
    }
    
    public void biletleriListele(int kullaniciId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        String query = "SELECT bilet.*, ucus.*, koltuk.*, kullanici.* " +
                "FROM bilet " +
                "INNER JOIN ucus ON bilet.ucus_id = ucus.id " +
                "INNER JOIN koltuk ON bilet.bilet_id = koltuk.bilet_id " +
                "INNER JOIN kullanici ON bilet.k_id = kullanici.k_id " +
                "WHERE bilet.k_id = ? ";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, kullaniciId);
            try (ResultSet rs = pstmt.executeQuery()) {
            	
            	 boolean biletVarMi = false;
            	 
            	while (rs.next()) {
            		biletVarMi=true;
            		int bilet_id = rs.getInt("bilet_id");
                    int koltuk_no = rs.getInt("koltuk_no");
                    double normalFiyat = rs.getDouble("fiyat");
                    String uyeTipi=rs.getString("uyelik_tipi");
	                String koltukDurumuString = rs.getString("koltukDurumu");
	                koltukDurumu koltukDurumu = COMU_Havayolu.koltukDurumu.valueOf(koltukDurumuString);
	                String kullanici_ad = rs.getString("ad");
	                String kullanici_soyad = rs.getString("soyad");
	                
	                double indirimOrani=0;
	                
	                if(uyeTipi.equals("VIP")) {
	                	 indirimOrani = VIP_Kullanici.indirim_Orani;
	                }
	                else if(uyeTipi.equals("Daimi Üye")) {
	                	 indirimOrani = Daimi_Kullanici.indirim_Orani;
	                }
      
	                double indirimliFiyat = normalFiyat - (normalFiyat * indirimOrani);

	                System.out.printf("Bilet ID: %d | Ad-Soyad: %s %s |  Nereden: %s | Nereye: %s | Kalkış: %s | Varış: %s | Koltuk: %d | Koltuk Durumu: %s | Bilet Fiyatı: %.2fTL | Ödenen İndirimli Fiyat: %.2fTL%n",
	                		bilet_id,kullanici_ad.substring(0, 1).toUpperCase()+kullanici_ad.substring(1),kullanici_soyad.substring(0, 1).toUpperCase()+kullanici_soyad.substring(1),rs.getString("nereden"), rs.getString("nereye"),  rs.getString("kalkis_saati"),  rs.getString("varis_saati"), koltuk_no, koltukDurumu, normalFiyat, indirimliFiyat );
	            } 
            	
            	 if (!biletVarMi) {
                     System.out.println("Henüz biletiniz yok.");
                 }
            	
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Scanner input = new Scanner(System.in);
        System.out.println();
		System.out.print("Geri çık (1)\nBilet iptal işlemleri (2)");
		System.out.println();
        System.out.print("Yapmak istediğiniz işlemi seçiniz:");
		
		int secim = input.nextInt();
		switch(secim) {
		case 1: 
			Ucus.ucusIslemleriMenu();
			break;
		case 2:
			biletIade(Main.kullanici.getId());
			break;
		}
    }
    
    public void biletAl(int ucusId, int kullaniciId, int koltukId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        
        String fiyatSorgusu = "SELECT ucus.fiyat, kullanici.uyelik_tipi " +
                "FROM ucus " +
                "INNER JOIN kullanici ON kullanici.k_id = ? " +
                "WHERE ucus.id = ?";
        
        double normalFiyat = 0;
        String uyeTipi = "";
        
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement fiyatStmt = conn.prepareStatement(fiyatSorgusu)) {
            
            fiyatStmt.setInt(1, kullaniciId);
            fiyatStmt.setInt(2, ucusId);
            
            try (ResultSet fiyatResultSet = fiyatStmt.executeQuery()) {
                if (fiyatResultSet.next()) {
                    normalFiyat = fiyatResultSet.getDouble("fiyat");
                    uyeTipi = fiyatResultSet.getString("uyelik_tipi");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        double indirimliFiyat = normalFiyat;
        
        if (uyeTipi.equals("VIP")) {
            indirimliFiyat -= (normalFiyat * VIP_Kullanici.indirim_Orani);
        } else if (uyeTipi.equals("Daimi Üye")) {
            indirimliFiyat -= (normalFiyat * Daimi_Kullanici.indirim_Orani);
        }
        if (Ucus.ucusTipi.equals("GİDİŞ") || Ucus.ucusTipi.equals("DÖNÜŞ")) {
            indirimliFiyat -= (indirimliFiyat * 0.02);
        }
        
        Scanner input = new Scanner(System.in);
        System.out.printf("İndirimli bilet fiyatı: %.2fTL\n", indirimliFiyat);
        System.out.print("Onaylıyor musunuz? (E/H): ");
        String onay = input.next();
        
        if (onay.equalsIgnoreCase("E")) {
            String insertQuery = "INSERT INTO bilet (ucus_id, k_id) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                // Bilet tablosuna yeni bilet ekleniyor
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, ucusId);
                    pstmt.setInt(2, kullaniciId);
                    pstmt.executeUpdate();
                    
                    // Eklenen biletin ID'sini al
                    ResultSet generatedKeys = pstmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int biletId = generatedKeys.getInt(1);
                        Bilet bilet = new Bilet();
                        bilet.setBiletId(biletId);
                        
                        // Koltuk tablosunda koltuk durumunu güncelle
                        String updateQuery = "UPDATE koltuk SET koltukDurumu = 'DOLU', bilet_id = ? WHERE ucus_id = ? AND koltuk_id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            updateStmt.setInt(1, biletId);
                            updateStmt.setInt(2, ucusId);
                            updateStmt.setInt(3, koltukId);
                            updateStmt.executeUpdate();
                            
                            System.out.println("Ödemeniz başarıyla alındı.");
                            if(Ucus.ucusTipi.equals("GİDİŞ")) {
                            	Ucus.donusUcus();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
        	Rezervasyon.r_biletIptal(koltukId);
            System.out.println("Bilet alımı iptal edildi.");
        }
        
        System.out.println();
        System.out.print("Ana Menuye dönmek için (1)e basın: ");
        
        int secim = input.nextInt();
        switch (secim) {
            case 1: 
                Ucus.ucusIslemleriMenu();
        }
    }
    
    
    public void biletIade(int kullaniciId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        
        double iadeOrani = 0.25; 
        
        Scanner input = new Scanner(System.in);
        System.out.print("İade etmek istediğiniz biletin ID'sini girin: ");
        int iadeEdilecekBiletId = input.nextInt();
        // Kullanıcının sahip olduğu biletin fiyatını alalım
        String fiyatSorgusu = "SELECT ucus.fiyat, kullanici.uyelik_tipi " +
                "FROM bilet " +
                "INNER JOIN kullanici ON bilet.k_id = kullanici.k_id " +
                "INNER JOIN ucus ON bilet.ucus_id = ucus.id " +
                "WHERE bilet.bilet_id = ?";
        double biletFiyati = 0;
        String uyeTipi = "";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement fiyatStmt = conn.prepareStatement(fiyatSorgusu)) {
        	fiyatStmt.setInt(1, iadeEdilecekBiletId);
            try (ResultSet fiyatResultSet = fiyatStmt.executeQuery()) {
                if (fiyatResultSet.next()) {
                    biletFiyati = fiyatResultSet.getDouble("fiyat");
                    uyeTipi=fiyatResultSet.getString("uyelik_tipi");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        double odenenFiyat=biletFiyati;
        
        if(uyeTipi.equals("VIP")) {
           odenenFiyat=biletFiyati - (biletFiyati * VIP_Kullanici.indirim_Orani);
       }
       else if(uyeTipi.equals("Daimi Üye")) {
    	   odenenFiyat=biletFiyati - (biletFiyati * Daimi_Kullanici.indirim_Orani);
       }

        double iadeFiyati = odenenFiyat - (odenenFiyat * iadeOrani);

        // Kullanıcıya iade edilecek miktarı gösterelim
        System.out.printf("İade edilecek miktar: %.2fTL\n" , iadeFiyati);
        
        System.out.print("Bilet iade edilsin mi? (E/H): ");
        String iadeOnay = input.next();

        if (iadeOnay.equalsIgnoreCase("E")) {
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                // Koltuk tablosunda koltuk durumunu boşa çıkar ve bilet id'sini null yap
                String updateQuery = "UPDATE koltuk SET koltukDurumu = 'BOŞ', bilet_id = null WHERE bilet_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, iadeEdilecekBiletId);
                    updateStmt.executeUpdate();

                    // Bilet tablosundan biletin silinmesi
                    String deleteQuery = "DELETE FROM bilet WHERE bilet_id = ?";
                    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                        deleteStmt.setInt(1, iadeEdilecekBiletId);
                        int affectedRows = deleteStmt.executeUpdate();
                        if (affectedRows > 0) {
                            System.out.println("Bilet iade edildi, ödemeniz gün içinde iade edilecektir.");
                        } else {
                            System.out.println("Bilet iadesi yapılamadı.");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Bilet iade işlemi iptal edildi.");
        }

        System.out.println();
        System.out.print("Ana Menüye dönmek için (1)'e basın: ");
        int secim = input.nextInt();
        switch (secim) {
            case 1:
                Ucus.ucusIslemleriMenu();
        }
    }
}
