package boxes;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import dao.daoPOJO.Coordonnees;
import utilities.AlphaContainer;
import utilities.JLabelSelectable;

/**
 * JPanel utilisé pour afficher les coordonnées d'un employé.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class CoordonneesBox extends JPanel {
	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur par défaut
	 * 
	 */
	public CoordonneesBox() {
		super();

		Dimension size = new Dimension(300, 100);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Coordonnées"));
		this.setPreferredSize(size);
		this.setMaximumSize(size);

		initComponent();
	}

	/**
	 * Méthode qui affiche les coordonnées de l'employé selon l'objet
	 * {@link Coordonnees} passé en paramètre.
	 * 
	 * @param coord {@link Coordonnees} à afficher.
	 * 
	 */
	public void setCoordEmploye(Coordonnees coord) {
		// Si coord est null, on vide les champs pour ne pas mélanger les
		// données avec celles d'un autre employé.
		if (coord == null) {
			courriel.setText("");
			tel1.setText("");
			tel2.setText("");
			adresse.setText("");
			ville.setText("");
			codePostal.setText("");
			return;
		}

		String strApt = " #";
		if (coord.getNoApt() == null || coord.getNoApt().isEmpty()) {
			strApt = "";
		} else {
			strApt += coord.getNoApt();
		}

		String strExt1 = " #";
		if (coord.getExt1() == null || coord.getExt1().isEmpty()) {
			strExt1 = "";
		} else {
			strExt1 += coord.getExt1();
		}

		String strExt2 = " #";
		if (coord.getExt2() == null || coord.getExt1().isEmpty()) {
			strExt2 = "";
		} else {
			strExt2 += coord.getExt2();
		}

		String strAdresse = "";
		if (coord.getNoCivic() == 0 || coord.getStreetName() == null || coord.getStreetName().isEmpty()) {
			strAdresse.isEmpty();
		} else {
			strAdresse += coord.getNoCivic() + " " + coord.getStreetName() + strApt;
		}
		courriel.setText(coord.getEmail());
		tel1.setText(FormatTelephone(coord.getPhone1()) + strExt1);
		tel2.setText(FormatTelephone(coord.getPhone2()) + strExt2);
		adresse.setText(strAdresse);
		ville.setText(coord.getCity());
		codePostal.setText(coord.getZipCode());
	}

	/**
	 * Méthode qui enlève tous les caractères qui ne sont pas des chiffres. On
	 * peut ainsi stocker le string dans une base de donnée comme un numéro de
	 * téléphone.
	 * 
	 * @param tel
	 *            String auquel on enlève tout sauf les chiffres.
	 * @return String numéto de téléphone avec seulement des chiffres.
	 */
	private String FormatTelephone(String tel) {
		if (tel == null){
			return "";
		}
		return tel.replaceAll("[^0-9]", "");
	}

	/**
	 * Méthode d'initialisation des components de panel. Cette méthode ne
	 * devrait se faire appeler seulement une fois dans le constructeur. Pour
	 * réinitialiser ou mettre à jour des champs, utilisez les méthodes
	 * correspondantes.
	 * <p>
	 * Le panel utilise des boxLayouts horizontals pour l'affichage des données
	 * sur 2 lignes. Pour l'affichage en colonne, un autre boxlayout vertical
	 * est utilisé.
	 * 
	 */
	private void initComponent() {
		int alpha = 15;
		Color rowCourrielColor = new Color(255, 0, 0, alpha);
		Color rowTelColor = new Color(0, 255, 0, alpha);
		Color rowAdresseColor = new Color(0, 0, 255, alpha);

		JPanel rowCourriel = new JPanel();
		rowCourriel.setBackground(rowCourrielColor);
		rowCourriel.setAlignmentX(LEFT_ALIGNMENT);
		rowCourriel.setLayout(new BoxLayout(rowCourriel, BoxLayout.LINE_AXIS));
		rowCourriel.add(lblCourriel);
		rowCourriel.add(Box.createHorizontalGlue());
		rowCourriel.add(courriel);

		JPanel rowTel1 = new JPanel();
		rowTel1.setBackground(rowTelColor);
		rowTel1.setAlignmentX(LEFT_ALIGNMENT);
		rowTel1.setLayout(new BoxLayout(rowTel1, BoxLayout.LINE_AXIS));
		rowTel1.add(lblTel1);
		rowTel1.add(Box.createHorizontalGlue());
		rowTel1.add(tel1);

		JPanel rowTel2 = new JPanel();
		rowTel2.setBackground(rowTelColor);
		rowTel2.setAlignmentX(LEFT_ALIGNMENT);
		rowTel2.setLayout(new BoxLayout(rowTel2, BoxLayout.LINE_AXIS));
		rowTel2.add(lblTel2);
		rowTel2.add(Box.createHorizontalGlue());
		rowTel2.add(tel2);

		JPanel rowAdresse = new JPanel();
		rowAdresse.setBackground(rowAdresseColor);
		rowAdresse.setAlignmentX(LEFT_ALIGNMENT);
		rowAdresse.setLayout(new BoxLayout(rowAdresse, BoxLayout.LINE_AXIS));
		rowAdresse.add(lblAdresse);
		rowAdresse.add(Box.createHorizontalGlue());
		rowAdresse.add(adresse);

		JPanel rowVille = new JPanel();
		rowVille.setBackground(rowAdresseColor);
		rowVille.setAlignmentX(LEFT_ALIGNMENT);
		rowVille.setLayout(new BoxLayout(rowVille, BoxLayout.LINE_AXIS));
		rowVille.add(lblVille);
		rowVille.add(Box.createHorizontalGlue());
		rowVille.add(ville);

		JPanel rowCodePostal = new JPanel();
		rowCodePostal.setBackground(rowAdresseColor);
		rowCodePostal.setAlignmentX(LEFT_ALIGNMENT);
		rowCodePostal.setLayout(new BoxLayout(rowCodePostal,
				BoxLayout.LINE_AXIS));
		rowCodePostal.add(lblCodePostal);
		rowCodePostal.add(Box.createHorizontalGlue());
		rowCodePostal.add(codePostal);

		// On utilise un AlphaContainer pour la transparence. Voir la classe
		// AlphaContainer pour plus d'informations.
		this.add(new AlphaContainer(rowCourriel));
		this.add(new AlphaContainer(rowTel1));
		this.add(new AlphaContainer(rowTel2));
		this.add(new AlphaContainer(rowAdresse));
		this.add(new AlphaContainer(rowVille));
		this.add(new AlphaContainer(rowCodePostal));
	}

	/**
	 * SelectJLabel pour identifier les informations affichées.
	 * 
	 */
	private JLabelSelectable lblCourriel = new JLabelSelectable("Courriel :"),
			lblTel1 = new JLabelSelectable("Téléphone 1 :"),
			lblTel2 = new JLabelSelectable("Téléphone 2 :"),
			lblAdresse = new JLabelSelectable("Adresse :"),
			lblVille = new JLabelSelectable("Ville :"),
			lblCodePostal = new JLabelSelectable("Code postal : ");

	/**
	 * SelectJLabel pour afficher les informations.
	 * 
	 */
	private JLabelSelectable courriel = new JLabelSelectable(),
			tel1 = new JLabelSelectable(), tel2 = new JLabelSelectable(),
			adresse = new JLabelSelectable(), ville = new JLabelSelectable(),
			codePostal = new JLabelSelectable();
}
