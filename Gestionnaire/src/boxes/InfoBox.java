package boxes;

import java.awt.Color;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dao.daoPOJO.Employe;
import utilities.JLabelSelectable;

/**
 * JPanel utilisé pour afficher les informations générales d'un employé.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class InfoBox extends JPanel {

	/**
	 * Le numéro de sérialisation de la classe.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur par défaut
	 * 
	 */
	public InfoBox() {
		super();

		Dimension size = new Dimension(300, 100);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black),
				"Informations Générales"));
		this.setPreferredSize(size);
		this.setMaximumSize(size);

		initComponent();
	}

	/**
	 * Méthode qui affiche les coordonnées de l'employé selon l'objet
	 * {@link Employe} passé en paramètre.
	 * 
	 * @param employe
	 *            {@link Employe} à afficher.
	 * 
	 */
	public void setInfoEmploye(Employe employe) {
		_numEmploye.setText(Integer.toString(employe.getNoEmploye()));
		_numSDeS.setText(employe.getNoSDeS());
		_statut.setText(employe.getStatus().getName());
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		_dateNaissance
				.setText(sdf.format(employe.getDateOfBirth().getTime()));
		_dateEmbauche.setText(sdf.format(employe.getHiringDate().getTime())
				+ " (" + Integer.toString(employe.getHiringRank()) + ")");
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
		JPanel rowNumEmploye = new JPanel();
		rowNumEmploye.setAlignmentX(LEFT_ALIGNMENT);
		rowNumEmploye.setLayout(new BoxLayout(rowNumEmploye,
				BoxLayout.LINE_AXIS));
		rowNumEmploye.add(new JLabel("Numéro d'employé : "));
		rowNumEmploye.add(Box.createHorizontalGlue());
		rowNumEmploye.add(_numEmploye);

		JPanel rowStatut = new JPanel();
		rowStatut.setAlignmentX(LEFT_ALIGNMENT);
		rowStatut.setLayout(new BoxLayout(rowStatut, BoxLayout.LINE_AXIS));
		rowStatut.add(new JLabel("Statut : "));
		rowStatut.add(Box.createHorizontalGlue());
		rowStatut.add(_statut);

		JPanel rowDateEmbauche = new JPanel();
		rowDateEmbauche.setAlignmentX(LEFT_ALIGNMENT);
		rowDateEmbauche.setLayout(new BoxLayout(rowDateEmbauche,
				BoxLayout.LINE_AXIS));
		rowDateEmbauche.add(new JLabel("Date d'embauche : "));
		rowDateEmbauche.add(Box.createHorizontalGlue());
		rowDateEmbauche.add(_dateEmbauche);

		JPanel rowNumSDeS = new JPanel();
		rowNumSDeS.setAlignmentX(LEFT_ALIGNMENT);
		rowNumSDeS.setLayout(new BoxLayout(rowNumSDeS, BoxLayout.LINE_AXIS));
		rowNumSDeS.add(new JLabel("Numero de la SdeS : "));
		rowNumSDeS.add(Box.createHorizontalGlue());
		rowNumSDeS.add(_numSDeS);

		JPanel rowDateNaissance = new JPanel();
		rowDateNaissance.setAlignmentX(LEFT_ALIGNMENT);
		rowDateNaissance.setLayout(new BoxLayout(rowDateNaissance,
				BoxLayout.LINE_AXIS));
		rowDateNaissance.add(new JLabel("Date de naissance : "));
		rowDateNaissance.add(Box.createHorizontalGlue());
		rowDateNaissance.add(_dateNaissance);

		this.add(rowNumEmploye);
		this.add(rowStatut);
		this.add(rowDateEmbauche);
		this.add(rowNumSDeS);
		this.add(rowDateNaissance);
	}

	/**
	 * SelectJLabel pour afficher les informations.
	 * 
	 */
	private JLabelSelectable _numEmploye = new JLabelSelectable(),
			_dateEmbauche = new JLabelSelectable(), _numSDeS = new JLabelSelectable(),
			_dateNaissance = new JLabelSelectable(), _statut = new JLabelSelectable();
}