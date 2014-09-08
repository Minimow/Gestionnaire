package boxes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import main.DBConnection;
import dao.EmployeDAO;
import dao.QualificationDAO;
import dao.StatutDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOGetAllException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Qualification;
import dao.daoPOJO.Statut;
import dialogs.AjouterEmployeDialog;
import dialogs.ErrorDialog;
import utilities.IconLoader;
import utilities.Observer;
import utilities.Subject;
import utilitiesBound.ComboDropDown;
import utilitiesBound.ToggleButtonSubject;

public class OptionsListBox extends JPanel implements Subject {

	private static final long serialVersionUID = 1L;
	
	public OptionsListBox(JList<Employe> list) {
		curList = list;
		initComponent();
	}

	public ComboDropDown getBtnFiltresStatut() {
		return btnFiltersStatus;
	}

	public ComboDropDown getBtnFiltresQualif() {
		return btnFiltersQualif;
	}

	public AjouterEmployeDialog getAjoutEmployeDialog() {
		return addEmployeDialog;
	}
	
	public ToggleButtonSubject getTglReverseSort(){
		return tglReverseSort;
	}
	
	public ToggleButtonSubject getTglReverseQualif(){
		return tglReverseQualif;
	}
	
	public ToggleButtonSubject getTglReverseStatut(){
		return tglReverseStatut;
	}

	public JButton getBtnModifier() {
		return btnModifier;
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
	public EmployeDAO.EMPLOYE_SORT getUpdate(Observer obj) {
		return this.curSort;
	}

	/*
	 * Update the comboBox with the current status from the database.
	 */
	public void refreshStatus(){
		StatutDAO statutDAO = new StatutDAO(DBConnection.getInstance());
		ArrayList<Statut> arrayStatut;
		try {
			arrayStatut = statutDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		modelFiltreStatut.removeAllElements();
		for (int i = 0; i < arrayStatut.size(); i++) {
			JCheckBox checkBox = new JCheckBox(arrayStatut.get(i).getName());
			modelFiltreStatut.addElement(checkBox);
		}
		btnFiltersStatus = new ComboDropDown("Statuts", modelFiltreStatut, this);
	}
	
	/*
	 * Update the comboBox with the current qualifications from the database.
	 */
	public void refreshQualif(){
		QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
		ArrayList<Qualification> arrayQualif;
		try {
			arrayQualif = qualifDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		modelFiltreQualif.removeAllElements();
		for (int i = 0; i < arrayQualif.size(); i++) {
			JCheckBox checkBox = new JCheckBox(arrayQualif.get(i).getName());
			modelFiltreQualif.addElement(checkBox);
		}
		btnFiltersQualif.setListModel(modelFiltreQualif);
		btnFiltersQualif = new ComboDropDown("Qualifications", modelFiltreQualif, this);
	}
	
	private void initComponent() {
		btnAjouter.addActionListener(addEmployee);
		btnModifier.addActionListener(modifyEmployee);
		cmbTrier.addActionListener(sortByType);
		
		if (curList.getSelectedValue() == null) {
			btnModifier.setEnabled(false);
		}
		
		tglReverseQualif.setPreferredSize(new Dimension(25,25));
		tglReverseStatut.setPreferredSize(new Dimension(25,25));
		tglReverseSort.setPreferredSize(new Dimension(25,25));
		
		tglReverseQualif.setFocusPainted(false);
		tglReverseStatut.setFocusPainted(false);
		tglReverseSort.setFocusPainted(false);
		
		refreshStatus();
		refreshQualif();

		GridBagConstraints gbc = new GridBagConstraints();
		pnlMiddle.setLayout(new GridBagLayout());
		
		gbc.insets = new Insets(0,0,5,0);
		// Premiere ligne
		gbc.gridx = 0;
		gbc.gridy = 0;
		pnlMiddle.add(btnAjouter, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pnlMiddle.add(btnModifier, gbc);
		
		gbc.anchor = GridBagConstraints.WEST;
		// Deuxieme ligne
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		pnlMiddle.add(lblSort, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		pnlMiddle.add(cmbTrier, gbc);
		
		gbc.gridx = 5;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		pnlMiddle.add(new JLabel(" "), gbc);
		
		gbc.gridx = 6;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		pnlMiddle.add(tglReverseSort, gbc);
		
		// 3e ligne
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		pnlMiddle.add(lblFilters, gbc);
		
		gbc.gridx = 2;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.gridheight = 1;
		pnlMiddle.add(btnFiltersStatus, gbc);
		
		gbc.gridx = 6;
		gbc.gridy = 2;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		pnlMiddle.add(tglReverseStatut, gbc);
		
		// 4e ligne
		gbc.gridx = 2;
		gbc.gridy = 3;
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		pnlMiddle.add(btnFiltersQualif, gbc);
		
		gbc.gridx = 6;
		gbc.gridy = 3;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridheight = 1;
		pnlMiddle.add(tglReverseQualif, gbc);

		pnlContainer.setLayout(new BorderLayout());
		pnlContainer.add(pnlMiddle);
		this.add(pnlMiddle, BorderLayout.CENTER);
	}

	/*
	 * Action to add an employee. Shows the addEmployeDialog.
	 */
	private AbstractAction addEmployee = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			addEmployeDialog.setIsModeAdd(0);
			addEmployeDialog.setVisible(true);
		}
	};

	/*
	 * Action to modify an employee. Shows the addEmployeDialog with addMode to false.
	 */
	private AbstractAction modifyEmployee = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			if (curList.getSelectedValue() == null) {
				return;
			}
			int numEmp = curList.getSelectedValue().getNoEmploye();
			addEmployeDialog.setIsModeAdd(numEmp);
			addEmployeDialog.setVisible(true);
		}
	};

	/*
	 * Update the currently selected sort. Notifies the observers of the change.
	 */
	private AbstractAction sortByType = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			@SuppressWarnings("unchecked")
			JComboBox<EmployeDAO.EMPLOYE_SORT> cb = (JComboBox<EmployeDAO.EMPLOYE_SORT>) ae.getSource();
			EmployeDAO.EMPLOYE_SORT newSortType = (EmployeDAO.EMPLOYE_SORT) cb.getSelectedItem();
			curSort = newSortType;
			changed = true;
			notifyObservers();
		}
	};
	
	private JPanel pnlContainer = new JPanel();
	private JPanel pnlMiddle = new JPanel();
	
	private JButton btnAjouter = new JButton("Ajouter");
	private JButton btnModifier = new JButton("Modifier");
	
	private Dimension dimReverseIcon = new Dimension(15,15);
	private ImageIcon iconReverseSort = IconLoader.createImageIcon("/up_down_arrow.png", dimReverseIcon);
	private ToggleButtonSubject tglReverseSort = new ToggleButtonSubject(iconReverseSort);
	private ToggleButtonSubject tglReverseStatut = new ToggleButtonSubject(iconReverseSort);
	private ToggleButtonSubject tglReverseQualif = new ToggleButtonSubject(iconReverseSort);
	
	private JComboBox<EmployeDAO.EMPLOYE_SORT> cmbTrier = new JComboBox<EmployeDAO.EMPLOYE_SORT>(
			EmployeDAO.EMPLOYE_SORT.values());
	private DefaultListModel<JCheckBox> modelFiltreStatut = new DefaultListModel<JCheckBox>();
	private DefaultListModel<JCheckBox> modelFiltreQualif = new DefaultListModel<JCheckBox>();
	
	private JLabel lblSort = new JLabel("Trier par :");
	private JLabel lblFilters = new JLabel("Filtres :");
	private ComboDropDown btnFiltersStatus = new ComboDropDown("Statuts", modelFiltreStatut, this);
	private ComboDropDown btnFiltersQualif = new ComboDropDown("Qualifications", modelFiltreQualif, this);
	private AjouterEmployeDialog addEmployeDialog = new AjouterEmployeDialog(null, 0);
	private EmployeDAO.EMPLOYE_SORT curSort = EmployeDAO.EMPLOYE_SORT.NOM;
	private JList<Employe> curList;

	// Variables for the Observer pattern
	private boolean changed;
	private final Object MUTEX = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}
