package COMU_Havayolu;

import java.sql.Connection;
import java.sql.DriverManager;
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
    

    public Koltuk(int koltuk_id, int k_id, koltukDurumu koltukDurumu) {
        this.koltuk_id = koltuk_id;
        this.k_id = k_id;
        this.koltukDurumu = koltukDurumu;
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
        koltukCek();
    }

    private void koltukCek() {
        String url = "jdbc:mysql://localhost:3306/havayolu";
        String username = "root";
        String password = "";
        String query = "SELECT koltuk_id, k_id, koltukDurumu FROM koltuk";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int koltuk_id = rs.getInt("koltuk_id");
                int k_id = rs.getInt("k_id");
                String koltukDurumuString = rs.getString("koltukDurumu");
                koltukDurumu koltukDurumu = COMU_Havayolu.koltukDurumu.valueOf(koltukDurumuString);

                koltuklar.add(new Koltuk(koltuk_id, k_id, koltukDurumu));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void koltukListele() {
        System.out.println("Koltuklar:");
        for (Koltuk koltuk : koltuklar) {
            System.out.println("Koltuk " + koltuk.getKoltukId() + ": " + koltuk.getKoltukDurumu());
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

        if (seçilenKoltuk.getKoltukId() <= 4 && !kullanıcıVIP.equals("VIP")) {
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
        Kullanici kullanici=new Kullanici();
        Main.setKullanici(kullanici);
        
        koltuklar.koltukListele();
        
        System.out.print("Hangi koltuğu seçmek istersiniz?: ");
        int koltukId = input.nextInt();

        boolean başarı = koltuklar.koltukRezerveEt(koltukId, Main.getKullanici().getId(), Main.getKullanici().getUyelikTipi());

        if (başarı) {
            System.out.println("Koltuk başarıyla rezerve edildi.");
        } else {
            System.out.println("Koltuk rezervasyonu başarısız.");
        }
    }
}
