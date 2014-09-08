package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.DBConnection;
import utilities.IconLoader;
import utilities.Observer;
import utilities.Subject;
import utilities.VerticalLayout;
import dao.QualificationDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Qualification;

public class AdminQualification extends JDialog implements Subject{
	
	private static final long serialVersionUID = 1L;

	public AdminQualification(){
		this.setLayout(new BorderLayout());
		initComponent();
		
		this.setTitle("Admin : Qualifications");
		this.setModal(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void initComponent(){
		// Initialize the list with all the sessions
		listQualifs.addListSelectionListener(new ListSessionsSelectionHandler());
		refreshModel();
		
		//Initialize the combobox with the years possible
		for(int i = 1; i < 7; i++){
			modelYearValid.addElement(i);
		}
		// For the ones that do not expire
		modelYearValid.addElement(100);
		
		//Initialize the combobox with the priorities possible
		for(int i = 1; i < 6; i++){
			modelPriority.addElement(i);
		}
		
		//Add actionlistener to the component
		chkAdd.addActionListener(actionChkAdd);
		btnApply.addActionListener(actionApply);
		btnDelete.addActionListener(actionDelete);
		btnCancel.addActionListener(actionCancel);
		btnAdd.addActionListener(actionAdd);
		
		// Add components to their respective container
		pnlListQualifs.add(new JScrollPane(listQualifs));
		pnlListQualifs.add(btnDelete);
		
		pnlModYearValid.add(cmbYearValid);
		pnlModYearValid.add(lblYear);
		
		pnlModDate.add(lblName);
		pnlModDate.add(jtfName);
		pnlModDate.add(lblAcronyme);
		pnlModDate.add(jtfAcronyme);
		pnlModDate.add(lblYearValid);
		pnlModDate.add(pnlModYearValid);
		pnlModDate.add(lblPriority);
		pnlModDate.add(cmbPriority);
		
		pnlModButtons.add(chkAdd);
		pnlModButtons.add(btnAdd);
		pnlModButtons.add(btnApply);
		
		pnlModQualifs.add(pnlModDate);
		pnlModQualifs.add(pnlModButtons);
		
		pnlButtons.add(btnCancel);
		pnlButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		
		this.add(pnlListQualifs, BorderLayout.WEST);
		this.add(pnlModQualifs, BorderLayout.EAST);
		this.add(pnlButtons, BorderLayout.SOUTH);
		
		//Set the default value of the components
		btnAdd.setEnabled(false);
		changeQualif(listQualifs.getSelectedValue());
	}
	
	public void changeQualif(Qualification qualif){
		jtfName.setText(qualif.getName());
		jtfName.setCaretPosition(0);
		jtfAcronyme.setText(qualif.getAcronyme());
		cmbYearValid.setSelectedItem(qualif.getYearsValid());
		cmbPriority.setSelectedItem(qualif.getPriority());
	}
	
	private void refreshModel(){
		QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
		ArrayList<Qualification> arraySessions;
		try {
			arraySessions = qualifDAO.getAll();
			modelQualifs.removeAllElements();
			for(int i = 0; i < arraySessions.size(); i++){
				modelQualifs.addElement(arraySessions.get(i));
			}
			listQualifs.setSelectedIndex(0);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
	}

	private class ListSessionsSelectionHandler implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				@SuppressWarnings("unchecked")
				JList<Qualification> l = (JList<Qualification>) e.getSource();
				if(l.getSelectedValue() != null){
					changeQualif(l.getSelectedValue());
				}
			}
		}
	}
	
	private AbstractAction actionChkAdd = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if(chkAdd.isSelected()){
				btnApply.setEnabled(false);
				btnAdd.setEnabled(true);
				jtfName.setBackground(new Color(100,100,175));
				jtfName.setForeground(Color.white);
			}
			else{
				btnApply.setEnabled(true);
				btnAdd.setEnabled(false);
				jtfName.setBackground(Color.white);
				jtfName.setForeground(Color.black);
			}
        }
	};
	
	private AbstractAction actionApply = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Qualification curQualif = listQualifs.getSelectedValue();
			
			curQualif.setName(jtfName.getText());
			curQualif.setAcronyme(jtfAcronyme.getText());
			curQualif.setYearsValid((Integer)cmbYearValid.getSelectedItem());
			curQualif.setPriority((Integer)cmbPriority.getSelectedItem());

			QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
			try {
				qualifDAO.update(curQualif);
				JOptionPane.showMessageDialog(null,
						"Les modifications sur la qualification ont bien été appliquées.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModel();
				changed = true;
				notifyObservers();
			} catch (DAOUpdateException e1) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_UPDATE, e1.getMessage());
				err.setVisible(true);
				return;
			} 
		}
	};

	private AbstractAction actionAdd = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Qualification newQualif = new Qualification();
			
			newQualif.setName(jtfName.getText());
			newQualif.setAcronyme(jtfAcronyme.getText());
			newQualif.setYearsValid((Integer)cmbYearValid.getSelectedItem());
			newQualif.setPriority((Integer)cmbPriority.getSelectedItem());

			QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
			try {
				qualifDAO.create(newQualif);
				JOptionPane.showMessageDialog(null,
						"La création de la qualification s'est effectuée correctement.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModel();
				changed = true;
				notifyObservers();
			} catch (DAOCreateException e1) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_CREATE, e1.getMessage());
				err.setVisible(true);
				return;
			} 
		}
	};
	
	private AbstractAction actionDelete = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			Qualification curQualif = listQualifs.getSelectedValue();

			QualificationDAO qualifDAO = new QualificationDAO(DBConnection.getInstance());
			try {
				qualifDAO.delete(curQualif);
				JOptionPane.showMessageDialog(null,
						"La suppression de la qualification s'est effectuée correctement.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModel();
				changed = true;
				notifyObservers();
			} catch (DAODeleteException e1) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_DELETE, e1.getMessage());
				err.setVisible(true);
				return;
			} 
		}
	};
	
	private AbstractAction actionCancel = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	};
	
    @Override
    public void register(Observer obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
        if(!observers.contains(obj)) {}
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
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX) {
            if (!changed)
                return;
            observersLocal = new ArrayList<>(this.observers);
            this.changed=false;
        }
        for (Observer obj : observersLocal) {
            obj.update();
        }
    }
 
    @Override
    public Object getUpdate(Observer obj) {
    		return null;
    }
	
	private DefaultListModel<Qualification> modelQualifs = new DefaultListModel<Qualification>();
	private JList<Qualification> listQualifs = new JList<Qualification>(modelQualifs);
	
	private DefaultComboBoxModel<Integer> modelYearValid = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> cmbYearValid = new JComboBox<Integer>(modelYearValid);
	
	private DefaultComboBoxModel<Integer> modelPriority = new DefaultComboBoxModel<Integer>();
	private JComboBox<Integer> cmbPriority = new JComboBox<Integer>(modelPriority);
	
	private JPanel pnlModQualifs = new JPanel(new VerticalLayout());
	private JPanel pnlListQualifs = new JPanel(new VerticalLayout());
	private JPanel pnlModDate = new JPanel(new VerticalLayout());
	private JPanel pnlModButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private JPanel pnlModYearValid = new JPanel();
	
	private JLabel lblName = new JLabel("Nom de la qualification");
	private JLabel lblAcronyme = new JLabel("Acronyme");
	private JLabel lblYearValid = new JLabel("Valide pendant");
	private JLabel lblYear = new JLabel("années");
	private JLabel lblPriority = new JLabel("Priorité d'affichage");
	private JTextField jtfName = new JTextField("", 15);
	private JTextField jtfAcronyme = new JTextField("", 15);
	private JCheckBox chkAdd = new JCheckBox("Ajouter");
	private JButton btnAdd = new JButton("Ajouter");
	private JButton btnApply = new JButton("Appliquer");
	private JButton btnDelete = new JButton("Supprimer");
	private JButton btnCancel = new JButton("Annuler");
	
	// Variables for the Observer pattern.
    private boolean changed;
    private final Object MUTEX= new Object();
    private List<Observer> observers = new ArrayList<Observer>();
}
