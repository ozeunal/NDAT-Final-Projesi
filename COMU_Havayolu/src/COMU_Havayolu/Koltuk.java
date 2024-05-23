package COMU_Havayolu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        if (seçilenKoltuk.getKoltukDurumu() == koltukDurumu.DOLU) {
            System.out.println("Bu koltuk zaten dolu.");
            return false;
        }

        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
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
            System.out.println("Koltuk rezervasyonu başarılı.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Koltuk koltuklar = new Koltuk();
        koltuklar.koltukListele();
        
        System.out.print("Hangi koltuğu seçmek istersiniz?: ");
        int koltukNo = input.nextInt();
        
        int koltukId = 0;
        for (Koltuk koltuk : koltuklar.koltuklar) {
            if (koltuk.getKoltukNo() == koltukNo) {
                koltukId = koltuk.getKoltukId();
                break;
            }
        }
        if (koltukId == 0) {
            System.out.println("Belirtilen koltuk bulunamadı.");
            return;
        }

        /*
        System.out.println("UYE ID: "+Main.kullanici.getId());
        System.out.println("UYE TİPİ: "+Main.kullanici.getUyelikTipi());
        System.out.println("UCUS ID: "+Ucus.ucus.getId());
        System.out.println("KOLTUK ID: "+koltukId);
        */
        
        boolean başarı = koltuklar.koltukRezerveEt(koltukId, Main.kullanici.getId(), Main.kullanici.getUyelikTipi());

        if (başarı) {
            System.out.println("Koltuk başarıyla rezerve edildi.");
        } else {
            System.out.println("Koltuk rezervasyonu başarısız.");
        }
    }
}