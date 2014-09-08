package dao.daoPOJO;

import java.util.ArrayList;

import utilitiesBound.StringUtils;
import dialogs.ErrorDialog;

public class Coordonnees {

	public Coordonnees() {
		_noEmploye = 0;
		_noCivic = 0;
	}

	public Coordonnees(int noEmploye, String email, int noCivic,
			String streeName, String noApt, String city, String zipCode) {
		_noEmploye = noEmploye;
		_email = email;
		_noCivic = noCivic;
		_streetName = streeName;
		_noApt = noApt;
		_city = city;
		_zipCode = zipCode;
	}

	public int getNoEmploye() {
		return _noEmploye;
	}

	public String getEmail() {
		return _email;
	}

	public String getStreetName() {
		return _streetName;
	}

	public String getNoApt() {
		return _noApt;
	}

	public String getPhone1() {
		return _phone1;
	}

	public String getExt1() {
		return _ext1;
	}

	public String getPhone2() {
		return _phone2;
	}

	public String getExt2() {
		return _ext2;
	}

	public String getCity() {
		return _city;
	}

	public String getZipCode() {
		return _zipCode;
	}

	public int getNoCivic() {
		return _noCivic;
	}

	public void setEmail(String email) {
		_email = email;
	}

	public void setStreetName(String streetName) {
		_streetName = streetName;
	}

	public void setNoApt(String noApt) {
		_noApt = noApt;
	}

	public void setPhone1(String phone1) {
		_phone1 = phone1;
	}

	public void setExt1(String ext1) {
		_ext1 = ext1;
	}

	public void setPhone2(String phone2) {
		_phone2 = phone2;
	}

	public void setExt2(String ext2) {
		_ext2 = ext2;
	}

	public void setCity(String city) {
		_city = city;
	}

	public void setZipCode(String zipCode) {
		_zipCode = zipCode;
	}

	public void setNoCivic(int noCivic) {
		_noCivic = noCivic;
	}

	public void setNoEmploye(int noEmploye) {
		_noEmploye = noEmploye;
	}

	/*
	 * Validate every field of the Coordonnees object. This is where you can set
	 * your restrictions on the coordonnees. It is usually the last verifaction
	 * before sending info to the database.
	 */
	public static boolean isValid(Coordonnees obj) {
		boolean erreurPresente = false;
		String erreur = "Une erreur est survenue, veuillez vérifier les champs de formulaire et saisir des informations valides";
		ArrayList<String> errType = new ArrayList<String>();
		ArrayList<String> errDetails = new ArrayList<String>();

		String regexEmail = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
		String regxNum = "[0-9]+";

		if (!obj.getEmail().matches(regexEmail)
				&& !obj.getEmail().isEmpty()) {
			errType.add("Courriel invalide : ");
			errDetails.add("vérifier qu'il s'agit d'une adresse valide\n");
			erreurPresente = true;
		}

		obj.setZipCode(StringUtils.TrimFull(obj.getZipCode())
				.toUpperCase());
		if (obj.getZipCode().length() != 6 && !obj.getZipCode().isEmpty()) {
			errType.add("Code Postal invalide : ");
			errDetails.add("vérifier qu'il s'agit d'un code postal valide\n");
			erreurPresente = true;
		}

		obj.setPhone1(StringUtils.TrimForNumbers(obj.getPhone1()));
		if (!obj.getPhone1().matches(regxNum) && !obj.getPhone1().isEmpty()) {
			errType.add("Telephone 1 invalide : ");
			errDetails
					.add("vérifier qu'il s'agit d'un numero valide, seul les chiffres sont acceptés\n");
			erreurPresente = true;
		}

		obj.setExt1(StringUtils.TrimForNumbers(obj.getExt1()));
		if (!obj.getExt1().matches(regxNum) && !obj.getExt1().isEmpty()) {
			errType.add("Extension 1 invalide : ");
			errDetails
					.add("vérifier qu'il s'agit d'une extension valide, seul les chiffres sont acceptés\n");
			erreurPresente = true;
		}

		obj.setPhone2(StringUtils.TrimForNumbers(obj.getPhone2()));
		if (!obj.getPhone2().matches(regxNum) && !obj.getPhone2().isEmpty()) {
			errType.add("Telephone 2 invalide : ");
			errDetails
					.add("vérifier qu'il s'agit d'un numero valide, seul les chiffres sont acceptés\n");
			erreurPresente = true;
		}

		obj.setExt2(StringUtils.TrimForNumbers(obj.getExt2()));
		if (!obj.getExt2().matches(regxNum) && !obj.getExt2().isEmpty()) {
			errType.add("Extension 2 invalide : ");
			errDetails
					.add("vérifier qu'il s'agit d'une extension valide, seul les chiffres sont acceptés\n");
			erreurPresente = true;
		}

		if (obj.getPhone1().isEmpty() && !obj.getExt1().isEmpty()) {
			errType.add("Extension1 invalide : ");
			errDetails
					.add("Une extension  doit abosulement être associé à un numéro\n");
			erreurPresente = true;
		}

		if (obj.getPhone2().isEmpty() && !obj.getExt2().isEmpty()) {
			errType.add("Extension2 invalide : ");
			errDetails
					.add("Une extension  doit abosulement être associé à un numéro\n");
			erreurPresente = true;
		}

		if ((obj.getNoCivic() != 0 && obj.getStreetName().isEmpty())
				|| (obj.getNoCivic() == 0 && !obj.getStreetName().isEmpty())
				|| (!obj.getNoApt().isEmpty() && obj.getStreetName().isEmpty())) {
			errType.add("Adresse invalide : ");
			errDetails
					.add("Un numéro civic doit être associé à une adresse, tout comme un numéro d'appartement à une adresse\n");
			erreurPresente = true;
		}

		if (erreurPresente) {
			ErrorDialog err = new ErrorDialog(null, "Erreur", erreur, errType,
					errDetails);
			err.setVisible(true);
			return false;
		} else {
			return true;
		}
	}

	private String _streetName;
	private String _noApt;
	private String _city;
	private String _zipCode;
	private String _phone1;
	private String _ext1;
	private String _phone2;
	private String _ext2;
	private String _email;
	private int _noCivic;
	private int _noEmploye;
}
