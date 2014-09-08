package boxes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import main.DBConnection;
import main.TableRemplacements;
import utilities.IconLoader;
import utilities.DateComboBox;
import utilities.Observer;
import utilities.Subject;
import utilities.TimeComboBox;
import utilitiesBound.ButtonDropDown;
import dao.EmployeDAO;
import dao.RaisonRemplacementDAO;
import dao.RemplacementDAO;
import dao.SessionDAO;
import dao.TypeRemplacementDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOGetAllException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.RaisonRemplacement;
import dao.daoPOJO.Remplacement;
import dao.daoPOJO.TypeRemplacement;
import dialogs.ErrorDialog;
import dialogs.ModifierRempDialog;

public class RemplacementsBox extends JPanel implements Observer {

	private static final long serialVersionUID = 1L;
	
	public RemplacementsBox() {
		initComponent();
		refresh();
		refreshFiltres();
		refreshTable(null, null);

		btnAddRemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				modRempDialog.setCreateMode(curEmploye.getNoEmploye());
				modRempDialog.setVisible(true);
			}
		});
		btnAddRemp.setFocusPainted(false);
		
		// register observers to the subject
		subjectAnnee.register(obj);
		subjectSession.register(obj);
		subjectModRemp.register(obj);
	}

	public ModifierRempDialog getAjoutModRempDialog(){
		return modRempDialog;
	}
	
	public void refresh() {
		if (curEmploye == null) {
			return;
		}
		// Refresh type
		TypeRemplacementDAO typeRempDAO = new TypeRemplacementDAO(
				DBConnection.getInstance());
		ArrayList<TypeRemplacement> arrayType;
		try {
			arrayType = typeRempDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		typeModel.removeAllElements();
		for (int i = 0; i < arrayType.size(); i++) {
			typeModel.addElement(arrayType.get(i));
		}

		// Refresh raison
		RaisonRemplacementDAO raisonRempDAO = new RaisonRemplacementDAO(
				DBConnection.getInstance());
		ArrayList<RaisonRemplacement> arrayRaison;
		try {
			arrayRaison = raisonRempDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		raisonModel.removeAllElements();
		for (int i = 0; i < arrayRaison.size(); i++) {
			raisonModel.addElement(arrayRaison.get(i));
		}

		// Refresh employe
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		ArrayList<Employe> arrayEmp;
		try {
			arrayEmp = employeDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		empModel.removeAllElements();
		for (int i = 0; i < arrayEmp.size(); i++) {
			empModel.addElement(arrayEmp.get(i));
		}
	}

	public void refreshFiltres() {
		SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
		ArrayList<Integer> arraySession;
		try {
			arraySession = sessionDAO.getYears();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		_modelFiltresAnnee.removeAllElements();
		for (int i = 0; i < arraySession.size(); i++) {

			_modelFiltresAnnee.addElement(new JCheckBox(Integer
					.toString(arraySession.get(i))));
		}

		ArrayList<String> arraySessionsNoms;
		try {
			arraySessionsNoms = sessionDAO.getNoms();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
		_modelFiltresSession.removeAllElements();
		for (int i = 0; i < arraySessionsNoms.size(); i++) {
			_modelFiltresSession.addElement(new JCheckBox(arraySessionsNoms
					.get(i)));
		}
		_btnFiltreSession.setText(arraySessionsNoms.get(0));
	}

	public void refreshTable(ArrayList<String> modelAnnee,
			ArrayList<String> modelSession) {
		if (curEmploye == null) {
			return;
		}
		RemplacementDAO rempDAO = new RemplacementDAO(DBConnection.getInstance());
		ArrayList<Remplacement> arrayRemp;
		try {
			arrayRemp = rempDAO.getAll(curEmploye.getNoEmploye(), modelAnnee,
					modelSession);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
		_tableRemp.getModel().removeAllRows();
		for (int i = 0; i < arrayRemp.size(); i++) {
			_tableRemp.getModel().addRow(arrayRemp.get(i));
		}
		lblCount.setText("Total affiché : " + arrayRemp.size());
		_tableRemp.repaint();
	}

	public Calendar getDateDebut() {
		Calendar date = cmbDate.getDate();
		Calendar dateDebut = cmbTempsDebut.getTime();
		dateDebut.set(Calendar.YEAR, date.get(Calendar.YEAR));
		dateDebut.set(Calendar.MONTH, date.get(Calendar.MONTH));
		dateDebut.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		dateDebut.set(Calendar.SECOND, 0);
		return dateDebut;
	}

	public Calendar getDateFin() {
		Calendar date = cmbDate.getDate();
		Calendar dateFin = cmbTempsFin.getTime();
		dateFin.set(Calendar.YEAR, date.get(Calendar.YEAR));
		dateFin.set(Calendar.MONTH, date.get(Calendar.MONTH));
		dateFin.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		return dateFin;
	}

	public TableRemplacements getTable(){
		return _tableRemp;
	}
	
	public void setEmploye(Employe employe) {
		if (employe == null) {
			curEmploye = null;
			_btnAjouterRemp.setEnabled(false);
		} else {
			curEmploye = employe;
			_btnAjouterRemp.setEnabled(true);
		}
		refresh();
		update();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update() {
		DefaultListModel<JCheckBox> modelAnnee = (DefaultListModel<JCheckBox>) subjectAnnee
				.getUpdate(obj);
		DefaultListModel<JCheckBox> modelSession = (DefaultListModel<JCheckBox>) subjectSession
				.getUpdate(obj);
		ArrayList<String> arrayAnnee = new ArrayList<String>(), arraySession = new ArrayList<String>();
		int cpt = 0;
		// text annee
		for (int i = 0; i < modelAnnee.size(); i++) {
			if (modelAnnee.get(i).isSelected()) {
				cpt++;
				_btnFiltreAnnee.setText("Années : "
						+ modelAnnee.get(i).getText());
				arrayAnnee.add(modelAnnee.get(i).getText());
			}
		}
		if (cpt == 0 || cpt == modelAnnee.size()) {
			_btnFiltreAnnee.setText("Années : Toutes");
			_btnFiltreAnnee.setForeground(Color.blue);
		} else if (cpt != 1) {
			_btnFiltreAnnee.setText("Années : Custom");
			_btnFiltreAnnee.setForeground(Color.red);
		}
		cpt = 0;
		// text session
		for (int i = 0; i < modelSession.size(); i++) {
			if (modelSession.get(i).isSelected()) {
				cpt++;
				_btnFiltreSession.setText("Session : "
						+ modelSession.get(i).getText());
				arraySession.add(modelSession.get(i).getText());
			}
		}
		if (cpt == 0 || cpt == modelSession.size()) {
			_btnFiltreSession.setText("Session : Toutes");
			_btnFiltreSession.setForeground(Color.blue);
		} else if (cpt != 1) {
			_btnFiltreSession.setText("Session : Custom");
			_btnFiltreSession.setForeground(Color.red);
		}
		refreshTable(arrayAnnee, arraySession);

	}

	private void initComponent() {
		/*_tableRemp.setPreferredScrollableViewportSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width - 300,
				225));*/
		_tableRemp.setBackground(getBackground());
		_tableRemp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					modRempDialog.setModifMode(_tableRemp.getModel()
							.getBruteData().get(row));
					modRempDialog.setVisible(true);
				}
			}
		});
		
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Remplacements"));
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(_tableRemp), BorderLayout.CENTER);
		this.add(pnlTableOptions, BorderLayout.NORTH);
		
		pnlTableOptions.setLayout(new BoxLayout(pnlTableOptions, BoxLayout.X_AXIS));
		pnlTableOptions.add(lblFiltres);
		pnlTableOptions.add(_btnFiltreAnnee);
		pnlTableOptions.add(_btnFiltreSession);
		pnlTableOptions.add(lblCount);
		pnlTableOptions.add(Box.createHorizontalGlue());
		pnlTableOptions.add(btnAddRemp);
	}

	private JPanel pnlTableOptions = new JPanel();
	private DateComboBox cmbDate = new DateComboBox();
	private TimeComboBox cmbTempsDebut = new TimeComboBox(),
			cmbTempsFin = new TimeComboBox();
	private TableRemplacements _tableRemp = new TableRemplacements();
	private DefaultComboBoxModel<RaisonRemplacement> raisonModel = new DefaultComboBoxModel<RaisonRemplacement>();
	private DefaultComboBoxModel<Employe> empModel = new DefaultComboBoxModel<Employe>();
	private DefaultComboBoxModel<TypeRemplacement> typeModel = new DefaultComboBoxModel<TypeRemplacement>();
	private JButton _btnAjouterRemp = new JButton("Ajouter");
	private DefaultListModel<JCheckBox> _modelFiltresAnnee = new DefaultListModel<JCheckBox>(),
			_modelFiltresSession = new DefaultListModel<JCheckBox>();
	private ButtonDropDown _btnFiltreAnnee = new ButtonDropDown("",
			_modelFiltresAnnee, pnlTableOptions),
			_btnFiltreSession = new ButtonDropDown("", _modelFiltresSession,
					pnlTableOptions);
	private Employe curEmploye;
	private JLabel lblFiltres = new JLabel("Filtres : "),
			lblCount = new JLabel("Total affiché :");
	private ModifierRempDialog modRempDialog = new ModifierRempDialog(null,
			"Modif", true, null, 0);
	private JButton btnAddRemp = new JButton(IconLoader.createImageIcon("/add.png", new Dimension(20,20)));

	// Pour le patron observer
	private Observer obj = this;
	private Subject subjectAnnee = (Subject) _btnFiltreAnnee;
	private Subject subjectSession = (Subject) _btnFiltreSession;
	private Subject subjectModRemp = (Subject) modRempDialog;
}
