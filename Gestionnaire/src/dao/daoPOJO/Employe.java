package dao.daoPOJO;

import java.util.Calendar;

public class Employe {
	
	public Employe() {
		_noEmploye = 0;
		_hiringRank = 0;
		_hiringDate = Calendar.getInstance();
		_dateOfBirth = Calendar.getInstance();
	}
	
	public Employe(int numEmploye, String nom, String prenom, String sexe,
			Calendar dateEmbauche, Calendar dateNaissance, String numSDeS) {
		_lastName = nom;
		_firstName = prenom;
		_noEmploye = numEmploye;
		_hiringDate = dateEmbauche;
		_dateOfBirth = dateNaissance;
		_noSDeS = numSDeS;
		_sex = sexe;
		_hiringRank = 0;
		_imagePath = _noEmploye + "_profil.jpg";
	}
	
	public void setLastName(String lastName){
		_lastName = lastName;
	}
	
	public void setFirstName(String firstName){
		_firstName = firstName;
	}
	
	public void setNoEmploye(int noEmploye){
		_noEmploye = noEmploye;
		// If we change the noEmployee we have to change its path.
		_imagePath = _noEmploye + "_profil.jpg";
	}
	
	public void setHiringDate(Calendar hiringDate){
		_hiringDate = hiringDate;
	}
	
	public void setDateOfBirth(Calendar dateOfBirth){
		_dateOfBirth = dateOfBirth;
	}
	
	public void setNoSDeS(String noSDeS){
		_noSDeS = noSDeS;
	}
	
	public void setSex(String sex){
		_sex = sex;
	}
	
	public void setStatus(Statut status){
		_status = status;
	}
	
	public void setHiringRank(int rank){
		_hiringRank = rank;
	}
	
	public String getLastName(){
		return _lastName;
	}
	
	public String getFirstName(){
		return _firstName;
	}
	
	public int getNoEmploye() {
		return _noEmploye;
	}
	
	public Calendar getHiringDate(){
		return _hiringDate;
	}
	
	public Calendar getDateOfBirth(){
		return _dateOfBirth;
	}

	public String getNoSDeS(){
		return _noSDeS;
	}
	
	public String getSex(){
		return _sex;
	}
	
	public Statut getStatus(){
		return _status;
	}
	
	public String getImagepath(){
		return _imageLocPath + _imagePath;
	}
	
	public String getFullName(){
		return _firstName + " " + _lastName;
	}
	
	public int getHiringRank(){
		return _hiringRank;
	}
	
	@Override
	public String toString(){
		return _lastName + " " + _firstName;
	}
	
	private String _lastName;
	private String _firstName;
	private String _noSDeS;
	private String _sex;
	private Statut _status;
	private String _imagePath;
	private String _imageLocPath = "/profil/";
	private int _noEmploye;
	private int _hiringRank;
	private Calendar _dateOfBirth;
	private Calendar _hiringDate;
}
