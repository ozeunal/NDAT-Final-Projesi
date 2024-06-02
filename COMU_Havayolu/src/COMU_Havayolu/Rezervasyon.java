package COMU_Havayolu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Rezervasyon {

	public static void r_biletListele(int kullaniciId, int koltukId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        String query = "SELECT u.*,k.* " +
                "FROM koltuk k " +
                "INNER JOIN ucus u ON k.ucus_id = u.id " +
                "WHERE k.koltuk_id=? ";

	    try (Connection conn = DriverManager.getConnection(url, username, password);
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	    	pstmt.setInt(1, koltukId);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                int koltuk_no = rs.getInt("koltuk_no");
	                String koltukDurumuString = rs.getString("koltukDurumu");
	                koltukDurumu koltukDurumu = COMU_Havayolu.koltukDurumu.valueOf(koltukDurumuString);

	                System.out.printf("Nereden: %s | Nereye: %s | Kalkış Saati: %s | Varış Saati: %s | Koltuk: %d | Koltuk Durumu: %s | Bilet Fiyatı: %dTL",
	                		rs.getString("nereden"), rs.getString("nereye"),  rs.getString("kalkis_saati"),  rs.getString("varis_saati"), koltuk_no, koltukDurumu, rs.getInt("fiyat") );
	            } else {
	                System.out.println("Henüz biletiniz yok.");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
    public static boolean r_biletIptal(int koltukId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        String query = "UPDATE koltuk SET koltukDurumu = 'BOŞ', k_id = NULL WHERE koltuk_id = ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, koltukId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Rezervasyon iptali sırasında bir hata oluştu.");
                return false;
            }
            
            System.out.println("Rezervasyonunuz iptal edildi.");
            Ucus.ucusIslemleriMenu();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
