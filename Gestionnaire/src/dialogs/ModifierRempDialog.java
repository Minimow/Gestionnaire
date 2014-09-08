package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.DBConnection;
import utilities.AlphaContainer;
import utilities.DateComboBox;
import utilities.Observer;
import utilities.Subject;
import utilities.TimeComboBox;
import utilitiesBound.StringUtils;
import dao.EmployeDAO;
import dao.RaisonRemplacementDAO;
import dao.RemplacementDAO;
import dao.TypeRemplacementDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.RaisonRemplacement;
import dao.daoPOJO.Remplacement;
import dao.daoPOJO.TypeRemplacement;

public class ModifierRempDialog extends JDialog implements Subject {

	private static final long serialVersionUID = 1L;

	public ModifierRempDialog(JFrame parent, String title, boolean modal,
			Remplacement remp, int demandeurId) {
		super(parent, title, modal);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		this.demandeurId = demandeurId;
		rempAModifier = remp;
		initComponent();
		this.pack();
		this.setLocationRelativeTo(null);
		setDefaultValues();

		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				setDefaultValues();
			}
		});
		
		btnDeleteRemp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				deleteRemp();
			}
		});
		btnValider.addActionListener(modifierRemplacement);
		
		chkPreneurInconnu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(chkPreneurInconnu.isSelected()){
					jcbPreneurs.setEnabled(false);
				}
				else{
					jcbPreneurs.setEnabled(true);
				}
			}
		});
	}

	public void setModifMode(Remplacement remp) {
		rempAModifier = remp;
		demandeurId = remp.getRequester();
		setDefaultValues();
	}
	
	public void setCreateMode(int demandeurId){
		this.demandeurId = demandeurId;
		rempAModifier = null;
		setDefaultValues();
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

	AbstractAction modifierRemplacement = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			Remplacement rempNew = getRempFromFields();

			RemplacementDAO rempDAO = new RemplacementDAO(
					DBConnection.getInstance());
			try {
				// Si le remplacement n<existait pas, on le creer
				if (rempAModifier == null || !rempDAO.updateFull(rempAModifier, rempNew)) {
					rempDAO.create(rempNew);
				}

				rempAModifier = rempNew;
				changed = true;
				notifyObservers();
				dispose();
			} catch (DAOUpdateException | DAOCreateException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_UPDATE, e.getMessage());
				err.setVisible(true);
				return;
			}
		}
	};

	private Remplacement getRempFromFields(){
		int preneurId = ((Employe) jcbPreneurs.getSelectedItem())
				.getNoEmploye();
		if(chkPreneurInconnu.isSelected()){
			preneurId = 0;
		}
		Calendar dateDebut = getDateDebut();
		Calendar dateFin = getDateFin();
		String type = ((TypeRemplacement) jcbType.getSelectedItem())
				.getName();
		String raison = ((RaisonRemplacement) jcbRaison.getSelectedItem())
				.getName();
		String details = _jtfRempDetail.getText();
		details = StringUtils.TrimDoubleSpaces(details);
		details = StringUtils.setEmptyToNull(details);
		boolean approuve = checkApprouve.isSelected();

		Remplacement rempNew = new Remplacement();
		rempNew.setApproved(approuve);
		rempNew.setBeginDate(dateDebut);
		rempNew.setEndDate(dateFin);
		rempNew.setRequester(demandeurId);
		rempNew.setTaker(preneurId);
		rempNew.setDetails(details);
		rempNew.setReason(raison);
		rempNew.setType(type);
		
		return rempNew;
	}
	
	private void deleteRemp(){
		RemplacementDAO rempDAO = new RemplacementDAO(
				DBConnection.getInstance());
		try{
			if(rempAModifier != null){
				rempDAO.delete(rempAModifier);
				rempAModifier = null;
				changed = true;
				notifyObservers();
				dispose();
			}
		}
		catch(DAODeleteException e){
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_DELETE, e.getMessage());
			err.setVisible(true);
			return;
		}
	}
	
	public void updateComboBoxes() {
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

	@Override
	public void register(Observer obj) {
		if (obj == null)
			throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if (!observers.contains(obj)) {
			}
			observers.add(obj);
		}
	}

	@Override
	public void unregister(Observer obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}

	@Override
	public void notifyObservers() {
		List<Observer> observersLocal = null;
		// synchronization is used to make sure any observer registered after
		// message is received is not notified
		synchronized (MUTEX) {
			if (!changed)
				return;
			observersLocal = new ArrayList<>(this.observers);
			this.changed = false;
		}
		for (Observer obj : observersLocal) {
			obj.update();
		}

	}

	@Override
	public Remplacement getUpdate(Observer obj) {
		return rempAModifier;
	}

	private void setDefaultValues() {
		
		//Si on est en mode ajouter
		if (rempAModifier == null) {
			//Calendar date = Calendar.getInstance();
			//cmbDate.setDate(date);
			//cmbTempsDebut.setTime(date);
			//cmbTempsFin.setTime(date);
			_jtfRempDetail.setText("");
			return;
		}
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		Employe demandeur;
		try {
			demandeur = employeDAO.find(rempAModifier.getRequester());
		} catch (DAOFindException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_FIND, e.getMessage());
			err.setVisible(true);
			return;
		}
		lblDemandeur.setText(demandeur.getFullName());
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy à HH:mm");
		lblDebut.setText(sdf.format(rempAModifier.getBeginDate().getTime()));
		cmbDate.setDate(rempAModifier.getBeginDate());
		cmbTempsDebut.setTime(rempAModifier.getBeginDate());
		cmbTempsFin.setTime(rempAModifier.getEndDate());
		_jtfRempDetail.setText(rempAModifier.getDetails());

		for (int i = 0; i < typeModel.getSize(); i++) {
			if (typeModel.getElementAt(i).getName()
					.equals(rempAModifier.getType())) {
				jcbType.setSelectedIndex(i);
				break;
			}
		}

		for (int i = 0; i < raisonModel.getSize(); i++) {
			if (raisonModel.getElementAt(i).getName()
					.equals(rempAModifier.getReason())) {
				jcbRaison.setSelectedIndex(i);
				break;
			}
		}

		if(rempAModifier.getTaker() == 0){
			chkPreneurInconnu.setSelected(true);
			jcbPreneurs.setEnabled(false);
		}
		else{
			jcbPreneurs.setEnabled(true);
			chkPreneurInconnu.setSelected(false);
			for (int i = 0; i < empModel.getSize(); i++) {
				if (empModel.getElementAt(i).getNoEmploye() == rempAModifier
						.getTaker()) {
					jcbPreneurs.setSelectedIndex(i);
					break;
				}
			}
		}

		checkApprouve.setSelected(rempAModifier.isApproved());
		_jtfRempDetail.setText(rempAModifier.getDetails());
	}

	private void initComponent() {
		updateComboBoxes();
		_jtfRempDetail.setMaximumSize(new Dimension(150, 75));
		_jtfRempDetail.setPreferredSize(new Dimension(150, 75));
		_jtfRempDetail.setLineWrap(true);
		_jtfRempDetail.setWrapStyleWord(true);
		JScrollPane detailsScroll = new JScrollPane(_jtfRempDetail);

		// cmbDate.setEnabled(false);
		// cmbTempsDebut.setEnabled(false);

		paneButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		paneButtons.add(btnValider);
		paneButtons.add(btnReset);
		paneButtons.add(btnDeleteRemp);
		paneButtons.add(btnAnnuler);
		this.add(paneButtons, BorderLayout.SOUTH);

		JPanel lineDemandeur = new JPanel();
		lineDemandeur.setLayout(new BoxLayout(lineDemandeur, BoxLayout.X_AXIS));
		lineDemandeur.setAlignmentX(LEFT_ALIGNMENT);
		lineDemandeur.add(lblDemandeur);
		lineDemandeur.add(Box.createHorizontalGlue());

		JPanel lineHeureDebut = new JPanel();
		lineHeureDebut
				.setLayout(new BoxLayout(lineHeureDebut, BoxLayout.X_AXIS));
		lineHeureDebut.setAlignmentX(LEFT_ALIGNMENT);
		lineHeureDebut.add(lblDebut);
		lineHeureDebut.add(Box.createHorizontalGlue());

		int alpha = 50;
		cmbDate.setOpaque(false);
		JPanel lineDate = new JPanel();
		Color DateTimeColor = new Color(0, 255, 0, alpha);
		lineDate.setBackground(DateTimeColor);
		lineDate.setLayout(new BoxLayout(lineDate, BoxLayout.X_AXIS));
		lineDate.setAlignmentX(LEFT_ALIGNMENT);
		lineDate.add(new JLabel("Date :"));
		lineDate.add(Box.createHorizontalGlue());
		lineDate.add(cmbDate);

		cmbTempsDebut.setOpaque(false);
		cmbTempsFin.setOpaque(false);
		JPanel lineHeure = new JPanel();
		lineHeure.setBackground(DateTimeColor);
		lineHeure.setLayout(new BoxLayout(lineHeure, BoxLayout.X_AXIS));
		lineHeure.setAlignmentX(LEFT_ALIGNMENT);
		lineHeure.add(new JLabel("Heure :"));
		lineHeure.add(Box.createHorizontalGlue());
		lineHeure.add(cmbTempsDebut);
		lineHeure.add(new JLabel("à"));
		lineHeure.add(cmbTempsFin);

		JPanel lineType = new JPanel();
		lineType.setLayout(new BoxLayout(lineType, BoxLayout.X_AXIS));
		lineType.setAlignmentX(LEFT_ALIGNMENT);
		lineType.add(new JLabel("Type :"));
		lineType.add(Box.createHorizontalGlue());
		lineType.add(jcbType);

		JPanel lineRaison = new JPanel();
		lineRaison.setLayout(new BoxLayout(lineRaison, BoxLayout.X_AXIS));
		lineRaison.setAlignmentX(LEFT_ALIGNMENT);
		lineRaison.add(new JLabel("Raison :"));
		lineRaison.add(Box.createHorizontalGlue());
		lineRaison.add(jcbRaison);

		JPanel lineDetails = new JPanel();
		lineDetails.setLayout(new BoxLayout(lineDetails, BoxLayout.X_AXIS));
		lineDetails.setAlignmentX(LEFT_ALIGNMENT);
		lineDetails.add(new JLabel("Detail :"));
		lineDetails.add(Box.createHorizontalGlue());
		lineDetails.add(detailsScroll);

		JPanel linePreneur = new JPanel();
		linePreneur.setLayout(new BoxLayout(linePreneur, BoxLayout.X_AXIS));
		linePreneur.setAlignmentX(LEFT_ALIGNMENT);
		linePreneur.add(new JLabel("Preneur :"));
		linePreneur.add(Box.createHorizontalGlue());
		linePreneur.add(new JLabel("Non-défini"));
		linePreneur.add(chkPreneurInconnu);
		linePreneur.add(jcbPreneurs);

		JPanel lineApprouve = new JPanel();
		lineApprouve.setLayout(new BoxLayout(lineApprouve, BoxLayout.X_AXIS));
		lineApprouve.setAlignmentX(LEFT_ALIGNMENT);
		lineApprouve.add(new JLabel("Approuvé :"));
		lineApprouve.add(Box.createHorizontalGlue());
		lineApprouve.add(checkApprouve);

		paneMiddle.setLayout(new BoxLayout(paneMiddle, BoxLayout.PAGE_AXIS));
		paneMiddle.add(new AlphaContainer(lineDemandeur));
		paneMiddle.add(new AlphaContainer(lineHeureDebut));
		paneMiddle.add(new AlphaContainer(lineDate));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(lineHeure));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(lineType));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(lineRaison));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(lineDetails));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(linePreneur));
		paneMiddle.add(Box.createVerticalGlue());
		paneMiddle.add(new AlphaContainer(lineApprouve));
		this.add(paneMiddle, BorderLayout.CENTER);
	}

	
	private JButton btnAnnuler = new JButton("Annuler"),
			btnValider = new JButton("Valider"),
			btnReset = new JButton("Reset");
	private JButton btnDeleteRemp = new JButton("Supprimer");
	private JPanel paneButtons = new JPanel(), paneMiddle = new JPanel();
	private DefaultComboBoxModel<RaisonRemplacement> raisonModel = new DefaultComboBoxModel<RaisonRemplacement>();
	private DefaultComboBoxModel<Employe> empModel = new DefaultComboBoxModel<Employe>();
	private DefaultComboBoxModel<TypeRemplacement> typeModel = new DefaultComboBoxModel<TypeRemplacement>();
	private JComboBox<TypeRemplacement> jcbType = new JComboBox<TypeRemplacement>(
			typeModel);
	private JComboBox<RaisonRemplacement> jcbRaison = new JComboBox<RaisonRemplacement>(
			raisonModel);
	private JComboBox<Employe> jcbPreneurs = new JComboBox<Employe>(empModel);
	private DateComboBox cmbDate = new DateComboBox();
	private TimeComboBox cmbTempsDebut = new TimeComboBox(),
			cmbTempsFin = new TimeComboBox();
	private JTextArea _jtfRempDetail = new JTextArea();
	private JCheckBox checkApprouve = new JCheckBox();
	private JCheckBox chkPreneurInconnu = new JCheckBox();
	private JLabel lblDemandeur = new JLabel("Demandeur :"),
			lblDebut = new JLabel("Heure :");
	private Remplacement rempAModifier;
	private int demandeurId;

	// Variables pour le patron observer
	private boolean changed;
	private final Object MUTEX = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}
