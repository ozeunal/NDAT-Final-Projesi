package COMU_Havayolu;
import java.time.LocalDate;

public class Kullanici {
	
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
	//private uyeliktipi;
	
	public Kullanici(String name, String surname, String id, LocalDate birthday, String telephone_number, char sex, 
			char marital_status, String email, int parola, LocalDate date_of_record) {
		ad = name;
		soyad = surname;
		tc = id;
		dogum_tarihi = birthday;
		telefon_numarasi = telephone_number;
		cinsiyet = sex;
		medeni_durum = marital_status;
		e_posta = email;
		this.parola = parola; 
		kayit_tarihi = date_of_record;
	
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

    public void setTc(String id) {
        tc = id;
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


}
