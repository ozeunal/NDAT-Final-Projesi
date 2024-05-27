package COMU_Havayolu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Koltuk {

    private int koltuk_id;
    private int k_id;
    public koltukDurumu koltukDurumu;
    private List<Koltuk> koltuklar;
    private int koltuk_no;

    public Koltuk(int koltuk_id, int k_id, koltukDurumu koltukDurumu, int koltukNo) {
        this.koltuk_id = koltuk_id;
        this.k_id = k_id;
        this.koltukDurumu = koltukDurumu;
        koltuk_no=koltukNo;
    }
    

    public int getKoltukNo() {
        return koltuk_no;
    }
    
    public int getKoltukId() {
        return koltuk_id;
    }
    
    public void setKoltukId(int koltukId) {
        koltuk_id=koltukId;
    }

    public int getKId() {
        return k_id;
    }

    public koltukDurumu getKoltukDurumu() {
        return koltukDurumu;
    }

    public void setKoltukDurumu(koltukDurumu durum) {
        this.koltukDurumu = durum;
    }

    public void setKId(int k_id) {
        this.k_id = k_id;
    }

    public Koltuk() {
        koltuklar = new ArrayList<>();
        //int ucus_Id=ucus.getId();
        koltukCek(Ucus.ucus.getId());
       
    }

    private void koltukCek(int ucusId) {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        String query = "SELECT * FROM koltuk where ucus_id=?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
            // Statement stmt = conn.createStatement();
        		PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, ucusId);
            try (ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int koltuk_id = rs.getInt("koltuk_id");
                int koltuk_no = rs.getInt("koltuk_no");
                int k_id = rs.getInt("k_id");
                String koltukDurumuString = rs.getString("koltukDurumu");
                koltukDurumu koltukDurumu = COMU_Havayolu.koltukDurumu.valueOf(koltukDurumuString);

                koltuklar.add(new Koltuk(koltuk_id, k_id, koltukDurumu, koltuk_no));
            }
             }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void koltukListele() {
        System.out.println("Koltuklar:");
        for (Koltuk koltuk : koltuklar) {
            System.out.println("Koltuk " + koltuk.getKoltukNo() + ": " + koltuk.getKoltukDurumu());
        }
    }

    public boolean koltukRezerveEt(int koltukId, int kullanıcıId, String kullanıcıVIP) {
        Koltuk seçilenKoltuk = null;
        for (Koltuk koltuk : koltuklar) {
            if (koltuk.getKoltukId() == koltukId) {
                seçilenKoltuk = koltuk;
                break;
            }
        }

       
        if (seçilenKoltuk == null) {
            System.out.println("Belirtilen koltuk bulunamadı.");
            return false;
        }
        

        if ((seçilenKoltuk.getKoltukNo() <= 4) && (!kullanıcıVIP.equals("VIP"))) {
            System.out.println("Bu koltuk VIP müşterilere ayrılmıştır.");
            return false;
        }

        if (seçilenKoltuk.getKoltukDurumu() == koltukDurumu.DOLU || seçilenKoltuk.getKoltukDurumu() == koltukDurumu.REZERVE) {
            System.out.println("Bu koltuk zaten dolu.");
            return false;
        }

        
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        
        /*
        int biletId = 0;
        String insertBiletQuery = "INSERT INTO bilet (ucus_id, k_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = conn.prepareStatement(insertBiletQuery, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, Ucus.ucus.getId());
            pstmt.setInt(2, kullanıcıId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Bilet eklenirken bir hata oluştu, hiçbir satır etkilenmedi.");
                return false;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    biletId = generatedKeys.getInt(1);
                  Bilet bilet=new Bilet();
                  bilet.setBiletId(biletId);
                } else {
                    throw new SQLException("Bilet eklenirken hata oluştu, otomatik olarak oluşturulan anahtar alınamadı.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        */
        String query = "UPDATE koltuk SET koltukDurumu = 'REZERVE', k_id = " + kullanıcıId + " WHERE koltuk_id = " + koltukId;

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {

            int affectedRows = stmt.executeUpdate(query);
            
            if (affectedRows == 0) {
                System.out.println("Koltuk rezervasyonu sırasında bir hata oluştu.");
                return false;
            }

            seçilenKoltuk.setKoltukDurumu(koltukDurumu.REZERVE);
            seçilenKoltuk.setKId(kullanıcıId);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


        
    
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        Koltuk koltuklar = new Koltuk();
        koltuklar.koltukListele();
        
        System.out.print("Hangi koltuğu seçmek istersiniz?: ");
        int koltukNo = input.nextInt();
        
        int koltukId = 0;
        for (Koltuk koltuk : koltuklar.koltuklar) {
            if (koltuk.getKoltukNo() == koltukNo) {
                koltukId = koltuk.getKoltukId();
               // koltuk.set
                break;
            }
        }
        if (koltukId == 0) {
            System.out.println("Belirtilen koltuk bulunamadı.");
            return;
        }
        
        boolean başarı = koltuklar.koltukRezerveEt(koltukId, Main.kullanici.getId(), Main.kullanici.getUyelikTipi());

        if (başarı) {
            System.out.println("Koltuk başarıyla rezerve edildi.");
            System.out.println("Bir dakika boyunca rezerve kalmanız durumunda seçiminiz iptal olucaktır.");
            System.out.println("---------------------------------------------------------------------------------------");
            System.out.println("Bilet Bilgileri");
            Bilet bilet1=new Bilet();
            bilet1.r_biletListele(Main.kullanici.getId(),koltukId);
	         System.out.println("\n1-Ödeme Yap 2-Rezerve Kal");
	         System.out.println();
	         System.out.print("Yapmak istediğiniz işlemi seçiniz:");
	         int secim = input.nextInt();
	         switch (secim) {
	         case 1:
	        	 Bilet bilet=new Bilet();
	             bilet.biletAl(Ucus.ucus.getId(), Main.kullanici.getId(), koltukId);
	        	 break;
	         case 2:
	             break;
	         }
        
        } else {
            System.out.println("Koltuk rezervasyonu başarısız.");
        }
    }
}