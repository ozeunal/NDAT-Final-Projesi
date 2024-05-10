package COMU_Havayolu_1;

import java.sql.Date;

public class Ucus {
	private String nereden;
    private String nereye;
    private String kalkis_saat;
    private String varis_saat;
    private String gun;
    private double fiyat;
    private String ucus_sikligi;
    private Date son_uctugu_gun;
    private int koltuk_kapasite;
    private int VIP_koltuk_kapasite;
    

    public Ucus(String nereden,String nereye, String kalkis_saat, String varis_saat, String gun, double fiyat,String ucus_sikligi, Date son_uctugu_gun, int koltuk_kapasite, int VIP_koltuk_kapasite) {
        this.nereden = nereden;
        this.nereye = nereye;
        this.kalkis_saat = kalkis_saat;
        this.varis_saat = varis_saat;
        this.gun = gun;
        this.fiyat = fiyat;
        this.ucus_sikligi = ucus_sikligi;
        this.son_uctugu_gun = son_uctugu_gun;
        this.koltuk_kapasite = koltuk_kapasite;
        this.VIP_koltuk_kapasite = VIP_koltuk_kapasite;
    }



	public String getNereden() {
        return nereden;
    }
    public String getNereye() {
        return nereye;
    }
    public void setNereden(String nereden) {
        this.nereden = nereden;
    }
    public void setNereye(String nereye) {
        this.nereye = nereye;
    }
    public String getKalkisSaat() {
        return kalkis_saat;
    }
    public String getVarisSaat() {
        return varis_saat;
    }
    public void setKalkisSaat(String kalkis_saat) {
        this.kalkis_saat = kalkis_saat;
    }
    public void setVarisSaat(String varis_saat) {
        this.varis_saat = varis_saat;
    }
    public String getGun() {
        return gun;
    }
    public void setGun(String gun) {
        this.gun = gun;
    }
    public double getFiyat() {
        return fiyat;
    }
    public void setFiyat(double fiyat) {
        this.fiyat = fiyat;
    }
    public String getUcusSikligi() {
    	return ucus_sikligi;
    }
    public void setUcusSikligi(String ucus_sikligi) {
    	this.ucus_sikligi = ucus_sikligi;
    }
    public Date getSonUctuguGun() {
    	return son_uctugu_gun;
    }
    public void setSonUctuguGun(Date son_uctugu_gun) {
    	this.son_uctugu_gun = son_uctugu_gun;
    }
    public int getKoltukKapasite() {
    	return koltuk_kapasite;
    }
    public void setKoltukKapasite(int koltuk_kapasite) {
    	this.koltuk_kapasite = koltuk_kapasite;
    }
    public int getVIPKoltukKapasite() {
    	return VIP_koltuk_kapasite;
    }
    public void setVIPKoltukKapasite(int VIP_koltuk_kapasite) {
    	this.VIP_koltuk_kapasite = VIP_koltuk_kapasite;
    }
    
 // toString() metodu: Uçuş bilgilerini metin olarak döndüren bir metod
    @Override
    public String toString() {
        return String.format(
            "Uçuş: %s'dan %s'ye | Kalkış: %s | Varış: %s | Gün: %s | Fiyat: %.2f TL | Uçuş Sıklığı: %s | Son Uçtuğu Gün: %s | Boş Koltuk Sayısı: %s | Boş VIP Koltuk Sayısı: %s",
            nereden, nereye, kalkis_saat, varis_saat, gun, fiyat, ucus_sikligi, son_uctugu_gun, koltuk_kapasite, VIP_koltuk_kapasite 
        );
    }

}
