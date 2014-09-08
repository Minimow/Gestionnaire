package boxes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.DBConnection;
import utilities.Observer;
import utilities.Subject;
import dao.EmployeDAO;
import dao.QualificationDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Qualification;
import dialogs.ErrorDialog;

/**
 * JPanel utilisé pour afficher la liste des employés. Le panel est implémente
 * l'interface Subject. Une classe interne implémente l'interface Observer afin
 * de réagir face au changement de filtres.
 * 
 * @author Julien Bergeron
 * @version 1.0
 */
public class ListEmployesBox extends JPanel implements Subject, Observer{

	private static final long serialVersionUID = 1L;

	public ListEmployesBox() {
		initComponent();
		updateListModel(EmployeDAO.EMPLOYE_SORT.NOM, null, null, false, false, false);
		
		// Register subjects
		optionBox.register(this);
		optionBox.getBtnFiltresStatut().register(this);
		optionBox.getBtnFiltresQualif().register(this);
		optionBox.getAjoutEmployeDialog().register(this);
		optionBox.getTglReverseQualif().register(this);
		optionBox.getTglReverseSort().register(this);
		optionBox.getTglReverseStatut().register(this);
		
	}

	/*
	 * Update all the combo boxes with current status, qualif, employee... It also notifies its observers.
	 */
	public void updateListModel(EmployeDAO.EMPLOYE_SORT sort, DefaultListModel<JCheckBox> modelStatut,
			DefaultListModel<JCheckBox> modelQualif, boolean reverseSort, boolean reverseQualif,
			boolean reverseStatut) {
		EmployeDAO empDAO = new EmployeDAO(DBConnection.getInstance());

		// Transforme le model recu avec le nom des qualifications en une
		// ArrayList contenant l'id des qualifications.
		ArrayList<String> filtresQualifId = new ArrayList<String>();
		if (modelQualif != null) {
			for (int i = 0; i < modelQualif.size(); i++) {
				QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
				Qualification qualif;
				try {
					qualif = qualifDAO.findByNom(modelQualif.get(i).getText());
				} catch (DAOFindException e) {
					ErrorDialog err = new ErrorDialog(null, DAOException.TITRE, DAOException.TYPE_FIND,
							e.getMessage());
					err.setVisible(true);
					return;
				}
				if (qualif != null && modelQualif.get(i).isSelected() == true) {
					filtresQualifId.add(Integer.toString(qualif.getId()));
				}
			}
		}

		// Transforme le model checkbox en arraylist string
		ArrayList<String> arrayStatut = new ArrayList<String>();
		if (modelStatut != null) {
			for (int i = 0; i < modelStatut.size(); i++) {
				if (modelStatut.get(i).isSelected()) {
					arrayStatut.add(modelStatut.get(i).getText());
				}
			}
		}
		
		ArrayList<Employe> al;
		try {
			al = empDAO.getAll(sort, arrayStatut, filtresQualifId, reverseSort,
					reverseQualif, reverseStatut);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE, DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		Employe emp = ((Employe) optionBox.getAjoutEmployeDialog().getUpdate(this));
		if (emp != null) {
			curEmploye = emp;
		}
		
		modelListEmployees.clear();
		int lastSelectedNoEmp = 0;
		for (int i = 0; i < al.size(); i++) {
			modelListEmployees.addElement(al.get(i));
			if (curEmploye != null) {
				if (al.get(i).getNoEmploye() == curEmploye.getNoEmploye()) {
					curEmploye = al.get(i);
					lastSelectedNoEmp = i;
				}
			}
		}
		
		listEmployees.validate();
		listEmployees.setSelectedIndex(lastSelectedNoEmp);
		changed = true;
		notifyObservers();
	}

	public DefaultListModel<Employe> getListModel() {
		return modelListEmployees;
	}

	public JList<Employe> getList() {
		return listEmployees;
	}

	public OptionsListBox getOptionBox(){
		return optionBox;
	}
	
	@Override
	public void register(Observer obj) {
		if (obj == null)
			throw new NullPointerException("Null Observer");
		synchronized (mutex) {
			if (!observers.contains(obj)) {
			}
			observers.add(obj);
		}
	}

	@Override
	public void unregister(Observer obj) {
		synchronized (mutex) {
			observers.remove(obj);
		}
	}

	@Override
	public void notifyObservers() {
		List<Observer> observersLocal = null;
		// synchronization is used to make sure any observer registered after
		// message is received is not notified
		synchronized (mutex) {
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
	public Employe getUpdate(Observer obj) {
		return this.curEmploye;
	}
	
	@Override
	public void update() {
		EmployeDAO.EMPLOYE_SORT type = (EmployeDAO.EMPLOYE_SORT) optionBox.getUpdate(this);
		DefaultListModel<JCheckBox> modelStatut = (DefaultListModel<JCheckBox>) optionBox.getBtnFiltresStatut().getUpdate(this);
		DefaultListModel<JCheckBox> modelQualif = (DefaultListModel<JCheckBox>) optionBox.getBtnFiltresQualif().getUpdate(this);
		boolean reverseSort = (boolean) optionBox.getTglReverseSort().getUpdate(this);
		boolean reverseQualif = (boolean) optionBox.getTglReverseQualif().getUpdate(this);
		boolean reverseStatut = (boolean) optionBox.getTglReverseStatut().getUpdate(this);
		updateListModel(type, modelStatut, modelQualif, reverseSort, reverseQualif, reverseStatut);
	}


	/**
	 * Méthode d'initialisation des components de panel. Cette méthode ne
	 * devrait se faire appeler seulement une fois dans le constructeur. Pour
	 * réinitialiser ou mettre à jour des champs, utilisez les méthodes
	 * correspondantes.
	 */
	private void initComponent() {
		listEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listEmployees.setSelectedIndex(0);
		listEmployees.setVisibleRowCount(5);
		listEmployees.addListSelectionListener(new ListEmployeesSelectionHandler());
		listEmployees.setCellRenderer(new ListEmployeRenderer());
		listEmployees.addMouseListener(new MouseAdapt());
		
		this.setLayout(new BorderLayout());
		this.add(listScrollPane, BorderLayout.CENTER);
		this.add(new JScrollPane(optionBox), BorderLayout.NORTH);
	}

	private class MouseAdapt extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent evt) {
			if (evt.getClickCount() == 2) {
				Rectangle r = listEmployees.getCellBounds(0, listEmployees.getLastVisibleIndex());
				if (r != null && r.contains(evt.getPoint())) {
					Employe selectedEmploye = (Employe) listEmployees
							.getSelectedValue();
					optionBox.getAjoutEmployeDialog().setIsModeAdd(selectedEmploye.getNoEmploye());
					optionBox.getAjoutEmployeDialog().setVisible(true);
					/*AjouterEmployeDialog mod = new AjouterEmployeDialog(null,
							selectedEmploye.getNoEmploye());
					mod.setVisible(true);*/
				}
			}
		}
	}

	private class ListEmployeesSelectionHandler implements
			ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				@SuppressWarnings("unchecked")
				JList<Employe> l = (JList<Employe>) e.getSource();
				curEmploye = l.getSelectedValue();
				changed = true;
				if (curEmploye != null) {
					optionBox.getBtnModifier().setEnabled(true);
					notifyObservers();
				} else {
					optionBox.getBtnModifier().setEnabled(false);
				}
			}
		}
	}
	
	private class ListEmployeRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (renderer instanceof JLabel && value instanceof Employe) {
				((JLabel) renderer).setText(((Employe) value).getLastName() + " " + ((Employe) value).getFirstName());
			}
			return renderer;
		}
	}

	private DefaultListModel<Employe> modelListEmployees = new DefaultListModel<Employe>();
	private JList<Employe> listEmployees = new JList<Employe>(modelListEmployees);;
	private JScrollPane listScrollPane = new JScrollPane(listEmployees);
	private OptionsListBox optionBox = new OptionsListBox(listEmployees);
	
	// Variables for the Observer pattern
	private boolean changed;
	private Employe curEmploye;
	private final Object mutex = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}
