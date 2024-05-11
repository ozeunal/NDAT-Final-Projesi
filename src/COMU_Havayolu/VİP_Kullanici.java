package COMU_Havayolu;

import java.time.LocalDate;

public class VİP_Kullanici extends Kullanici{
	
	private double indirim_Orani=0.25;

	public VİP_Kullanici(String name, String surname, String id, LocalDate birthday, String telephone_number, char sex,
			char marital_status, String email, int parola, LocalDate date_of_record, double indirimOrani) {
		super(name, surname, id, birthday, telephone_number, sex, marital_status, email, parola, date_of_record);
		this.indirim_Orani=indirimOrani;
	}

}
