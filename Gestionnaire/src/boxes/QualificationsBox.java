package boxes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.DBConnection;
import utilities.AlphaIcon;
import utilities.IconLoader;
import dao.EmployeQualificationsDAO;
import dao.QualificationDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.EmployeQualifications;
import dao.daoPOJO.Qualification;
import dialogs.ErrorDialog;
import dialogs.ModifierQualifDialog;

@SuppressWarnings("serial")
public class QualificationsBox extends JPanel {

	public QualificationsBox() {
		super();
		this.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Qualifications"));
		Dimension size = new Dimension(325, 230);
		this.setPreferredSize(size);
		this.setMaximumSize(size);

		initComponent();
	}

	private void initComponent() {
		// Initialise les JLabel en fonction de la base de donnees
		QualificationDAO qualifDAO = new QualificationDAO(
				DBConnection.getInstance());
		arrayQualif = new ArrayList<Qualification>();
		try {
			arrayQualif = qualifDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
		qualifIcone = new JLabel[arrayQualif.size()];
		qualifIconeThumb = new ImageIcon[arrayQualif.size()];

		for (int i = 0; i < arrayQualif.size(); i++) {
			ImageIcon icon = IconLoader.createImageIcon("/qualifs/"
					+ arrayQualif.get(i).getAcronyme() + "_icone.jpg", new Dimension (64,64));
			qualifIconeThumb[i] = new ImageIcon(icon.getImage()
					.getScaledInstance(40, 40, Image.SCALE_SMOOTH));
			AlphaIcon alphaTb = new AlphaIcon(qualifIconeThumb[i], alphaValue);
			qualifIcone[i] = new JLabel(arrayQualif.get(i).getAcronyme());
			qualifIcone[i].setIcon(alphaTb);
			qualifIcone[i].setForeground(textPasQualif);
			qualifIcone[i].setHorizontalTextPosition(JLabel.CENTER);
			qualifIcone[i].setVerticalTextPosition(JLabel.BOTTOM);
			qualifIcone[i].addMouseListener(new MouseAdapt(i));
			this.add(qualifIcone[i]);
		}
	}

	public class MouseAdapt extends MouseAdapter {

		public MouseAdapt(int i) {
			index = i;
		}

		@Override
		public void mouseClicked(MouseEvent evt) {
			if (evt.getClickCount() == 2) {
				modQualifDialog.setNewQualif(curEmploye.getNoEmploye(), qualifIcone[index].getText());
				modQualifDialog.setVisible(true);
			}
		}

		private final int index;
	}

	public void setQualifEmploye(Employe employe) {
		curEmploye = employe;
		EmployeQualificationsDAO empQualifDAO = new EmployeQualificationsDAO(
				DBConnection.getInstance());
		for (int i = 0; i < qualifIcone.length; i++) {
			EmployeQualifications empQualif;
			try {
				empQualif = empQualifDAO.find(employe.getNoEmploye(),
						arrayQualif.get(i).getId());
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_FIND, e.getMessage());
				err.setVisible(true);
				return;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
			String tooltip = "";
			if (empQualif != null) {
				tooltip = "<HTML>"
						+ empQualif.getName()
						+ "<br>Date de qualification :"
						+ sdf.format(empQualif.getDateQualification().getTime())
						+ "<br>Date d'expiration :"
						+ sdf.format(empQualif.getDateExpiration().getTime());
				qualifIcone[i].setIcon(qualifIconeThumb[i]);
				qualifIcone[i].setForeground(textQualif);
			} else {
				tooltip = arrayQualif.get(i).getName();
				qualifIcone[i].setIcon(new AlphaIcon(qualifIconeThumb[i],
						alphaValue));
				qualifIcone[i].setForeground(textPasQualif);
			}
			qualifIcone[i].setToolTipText(tooltip);
		}
	}
	
	public ModifierQualifDialog getModQualifDialog(){
		return modQualifDialog;
	}

	private JLabel[] qualifIcone;
	private ImageIcon[] qualifIconeThumb;

	private float alphaValue = 0.1f;
	private ArrayList<Qualification> arrayQualif;
	private Color textQualif = new Color(0, 0, 0), textPasQualif = new Color(0,
			0, 0, 50);
	private Employe curEmploye;
	private ModifierQualifDialog modQualifDialog = new ModifierQualifDialog(null, "Modification qualification", true, 0, null);
}
