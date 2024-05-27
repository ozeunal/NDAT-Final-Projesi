package COMU_Havayolu;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ucus {
    private int ucus_id;
    private String nereden;
    private String nereye;
    private String kalkis_saat;
    private String varis_saat;
    private String gun;
    private double fiyat;
    private String ucus_sikligi;
    private Date son_uctugu_gun;
    Koltuk koltuk;
    static Ucus ucus;

    public Ucus(int ucusId, String nereden, String nereye, String kalkis_saat, String varis_saat, String gun,
            double fiyat, String ucus_sikligi, Date son_uctugu_gun) {
        this.ucus_id = ucusId;
        this.nereden = nereden;
        this.nereye = nereye;
        this.kalkis_saat = kalkis_saat;
        this.varis_saat = varis_saat;
        this.gun = gun;
        this.fiyat = fiyat;
        this.ucus_sikligi = ucus_sikligi;
        this.son_uctugu_gun = son_uctugu_gun;
    }

    public int getId() {
        return ucus_id;
    }

    public void setId(int ucusId) {
        ucus_id = ucusId;
    }

    public static void ucusIslemleriMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("1-Uçuş işlemleri\n2-Biletlerim\n3-Çıkış Yap");
        System.out.println();
        System.out.print("Yapmak istediğiniz işlemi seçiniz:");
        int secim = input.nextInt();
        switch (secim) {
            case 1:
                Ucus.main(null);
                break;

            case 2:
               // Koltuk koltuk = new Koltuk();
                Bilet bilet=new Bilet();
                bilet.biletleriListele(Main.kullanici.getId());
                break;

            case 3:
                // Çıkış işlemi
                System.out.print("Güle güle!");
                System.out.println();
                break;
            default:
                System.out.println("Hatalı seçim!");
                break;

        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("1-Tek Yön\n2-Gidiş - Dönüş\n3-Geri Çık");
        System.out.println();
        System.out.print("Yapmak istediğiniz işlemi seçiniz:");
        int secim = input.nextInt();
        switch (secim) {
            case 1:
                // Tek yön uçuş işlemleri
                tekYonUcus();
                break;
            case 2:
                // Gidiş-dönüş uçuş işlemleri
                gidisDonusUcus();
                break;

            case 3:
                ucusIslemleriMenu();
                break;
            default:
                System.out.println("Hatalı seçim!");
                break;
        }

    }

    public static void tekYonUcus() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("TEK YÖN UÇUŞ");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("NEREDEN:");
        String nereden = input.nextLine();
        System.out.print("NEREYE:");
        String nereye = input.nextLine();
        System.out.print("Hangi gün uçmak istersiniz:");
        String gun = input.nextLine();
        List<Integer> ucusIdList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root",
                "")) {
            String sql = "SELECT * FROM Ucus WHERE nereden = ? AND nereye = ? AND gun LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nereden);
                statement.setString(2, nereye);
                statement.setString(3, "%" + gun + "%");

                ResultSet resultSet = statement.executeQuery();

                int index = 1;

                while (resultSet.next()) {
                    int ucusId = resultSet.getInt("id");
                    String neredenUcus = resultSet.getString("nereden");
                    String nereyeUcus = resultSet.getString("nereye");
                    String kalkisSaat = resultSet.getString("kalkis_saati");
                    String varisSaat = resultSet.getString("varis_saati");
                    String ucusGun = resultSet.getString("gun");
                    double fiyat = resultSet.getDouble("fiyat");
                    String ucusSikligi = resultSet.getString("ucus_sıklığı");
                    java.sql.Date sonUctuguGun = resultSet.getDate("son_uctugu_gun");

                    ucus = new Ucus(ucusId, resultSet.getString("nereden"), resultSet.getString("nereye"),
                            resultSet.getString("kalkis_saati"), resultSet.getString("varis_saati"),
                            resultSet.getString("gun"), resultSet.getDouble("fiyat"),
                            resultSet.getString("ucus_sıklığı"), resultSet.getDate("son_uctugu_gun"));

                    System.out.printf(
                            "%d. Uçuş ID: %d | Nereden: %s | Nereye: %s | Kalkış Saati: %s | Varış Saati: %s | Gün: %s | Fiyat: %.2f TL | Uçuş Sıklığı: %s | Son Uçtuğu Gün: %s%n",
                            index, ucusId, neredenUcus, nereyeUcus, kalkisSaat, varisSaat, ucusGun, fiyat,
                            ucusSikligi, sonUctuguGun);
                    ucusIdList.add(resultSet.getInt("id"));
                    index++;
                }

                if (index > 1) {

                    System.out.print("Hangi uçuşu seçmek istersiniz? (1-" + (index - 1) + "): ");
                    int secilen_ucus = input.nextInt();

                    if (secilen_ucus > 0 && secilen_ucus < index) {
                        System.out.println("Seçilen uçuş: " + secilen_ucus);
                        int secilenUcusId = ucusIdList.get(secilen_ucus - 1);
                        ucus.setId(secilenUcusId);
                        Koltuk.main(null);

                    } else {
                        System.out.println("Geçersiz seçim.");
                    }
                } else {
                    System.out.println("Belirtilen kriterlere uygun hiçbir uçuş bulunamadı. :(");
                    System.out.println("Yeni rota belirleyebilir veya çıkış yapabilirsiniz");
                    Ucus.main(null);
                    int secilen_ucus = input.nextInt();

                    switch (secilen_ucus) {
                        case 1:
                            Ucus.main(null);
                            break;
                        case 2:
                            // cikis();
                            return;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }

    public static void gidisDonusUcus() {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("GİDİŞ-DÖNÜŞ UÇUŞ");
        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("GİDİŞ İÇİN");
        System.out.println("NEREDEN:");
        String neredenGidis = input.nextLine();
        System.out.print("NEREYE:");
        String nereyeGidis = input.nextLine();
        System.out.print("Hangi gün uçmak istersiniz:");
        String gunGidis = input.nextLine();

        List<Integer> ucusIdList2 = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/havayolu", "root",
                "")) {
            String sql = "SELECT * FROM Ucus WHERE nereden = ? AND nereye = ? AND gun LIKE ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, neredenGidis);
                statement.setString(2, nereyeGidis);
                statement.setString(3, "%" + gunGidis + "%");

                ResultSet resultSet = statement.executeQuery();

                int index = 1;
                while (resultSet.next()) {
                    int ucusId = resultSet.getInt("id");
                    String neredenUcus = resultSet.getString("nereden");
                    String nereyeUcus = resultSet.getString("nereye");
                    String kalkisSaat = resultSet.getString("kalkis_saati");
                    String varisSaat = resultSet.getString("varis_saati");
                    String ucusGun = resultSet.getString("gun");
                    double fiyat = resultSet.getDouble("fiyat");
                    String ucusSikligi = resultSet.getString("ucus_sıklığı");
                    java.sql.Date sonUctuguGun = resultSet.getDate("son_uctugu_gun");

                    ucus = new Ucus(ucusId, resultSet.getString("nereden"), resultSet.getString("nereye"),
                            resultSet.getString("kalkis_saati"), resultSet.getString("varis_saati"),
                            resultSet.getString("gun"), resultSet.getDouble("fiyat"),
                            resultSet.getString("ucus_sıklığı"), resultSet.getDate("son_uctugu_gun"));

                    System.out.printf(
                            "%d. Uçuş ID: %d | Nereden: %s | Nereye: %s | Kalkış Saati: %s | Varış Saati: %s | Gün: %s | Fiyat: %.2f TL | Uçuş Sıklığı: %s | Son Uçtuğu Gün: %s%n",
                            index, ucusId, neredenUcus, nereyeUcus, kalkisSaat, varisSaat, ucusGun, fiyat,
                            ucusSikligi, sonUctuguGun);
                    ucusIdList2.add(resultSet.getInt("id"));
                    index++;
                }

                if (index > 1) {

                    System.out.print("Hangi uçuşu seçmek istersiniz? (1-" + (index - 1) + "): ");
                    int secilen_ucus = input.nextInt();

                    if (secilen_ucus > 0 && secilen_ucus < index) {
                        int secilenUcusId = ucusIdList2.get(secilen_ucus - 1);
                        System.out.println("Seçilen uçuş: " + secilen_ucus);
                        ucus.setId(secilenUcusId);
                        Koltuk.main(null);

                    } else {
                        System.out.println("Geçersiz seçim.");
                    }
                } else {
                    System.out.println("Belirtilen kriterlere uygun hiçbir uçuş bulunamadı. :(");
                    System.out.println("Yeni rota belirleyebilir veya çıkış yapabilirsiniz");
                    Ucus.main(null);
                    int secilen_ucus = input.nextInt();

                    switch (secilen_ucus) {
                        case 1:
                            Ucus.main(null);
                            break;
                        case 2:
                            // cikis();
                            return;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Hata: " + e.getMessage());
        }
    }
}
