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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import dao.RaisonRemplacementDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.RaisonRemplacement;

public class AdminRaison extends JDialog implements Subject{
	private static final long serialVersionUID = 1L;

	public AdminRaison(){
		this.setLayout(new BorderLayout());
		initComponent();
		
		this.setTitle("Admin : Raisons");
		this.setModal(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void initComponent(){
		// Initialize the list with all the sessions
		listReasons.addListSelectionListener(new ListSessionsSelectionHandler());
		refreshModel();
		
		//Add actionlistener to the component
		chkAdd.addActionListener(actionChkAdd);
		btnApply.addActionListener(actionApply);
		btnDelete.addActionListener(actionDelete);
		btnCancel.addActionListener(actionCancel);
		btnAdd.addActionListener(actionAdd);
		
		// Add components to their respective container
		pnlListRaisons.add(new JScrollPane(listReasons));
		pnlListRaisons.add(btnDelete);
		
		pnlModName.add(lblName);
		pnlModName.add(jtfName);
		
		pnlModButtons.add(chkAdd);
		pnlModButtons.add(btnAdd);
		pnlModButtons.add(btnApply);
		
		pnlModRaison.add(pnlModName);
		pnlModRaison.add(pnlModButtons);
		
		pnlButtons.add(btnCancel);
		pnlButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		
		this.add(pnlListRaisons, BorderLayout.WEST);
		this.add(pnlModRaison, BorderLayout.EAST);
		this.add(pnlButtons, BorderLayout.SOUTH);
		
		//Set the default value of the components
		btnAdd.setEnabled(false);
	}
	
	public void changeRaison(RaisonRemplacement raisonRemp){
		jtfName.setText(raisonRemp.getName());
	}
	
	private void refreshModel(){
		RaisonRemplacementDAO raisonRempDAO = new RaisonRemplacementDAO(DBConnection.getInstance());
		ArrayList<RaisonRemplacement> arrayRaisonsRemp;
		try {
			arrayRaisonsRemp = raisonRempDAO.getAll();
			modelReasons.removeAllElements();
			for(int i = 0; i < arrayRaisonsRemp.size(); i++){
				modelReasons.addElement(arrayRaisonsRemp.get(i));
			}
			listReasons.setSelectedIndex(0);
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
				JList<RaisonRemplacement> l = (JList<RaisonRemplacement>) e.getSource();
				if(l.getSelectedValue() != null){
					changeRaison(l.getSelectedValue());
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
			RaisonRemplacement curRaison = listReasons.getSelectedValue();
			
			curRaison.setName(jtfName.getText());

			RaisonRemplacementDAO raisonRempDAO = new RaisonRemplacementDAO(DBConnection.getInstance());
			try {
				raisonRempDAO.update(curRaison);
				JOptionPane.showMessageDialog(null,
						"Les modifications sur la raison ont bien été appliquées.",
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
			RaisonRemplacement newRaison = new RaisonRemplacement();
			
			newRaison.setName(jtfName.getText());

			RaisonRemplacementDAO raisonRemplacementDAO = new RaisonRemplacementDAO(DBConnection.getInstance());
			try {
				raisonRemplacementDAO.create(newRaison);
				JOptionPane.showMessageDialog(null,
						"La création de la raison s'est effectuée correctement.",
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
			RaisonRemplacement curRaison = listReasons.getSelectedValue();

			RaisonRemplacementDAO raisonRempDAO = new RaisonRemplacementDAO(DBConnection.getInstance());
			try {
				raisonRempDAO.delete(curRaison);
				JOptionPane.showMessageDialog(null,
						"La suppression de la raison s'est effectuée correctement.",
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
	
	private DefaultListModel<RaisonRemplacement> modelReasons = new DefaultListModel<RaisonRemplacement>();
	private JList<RaisonRemplacement> listReasons = new JList<RaisonRemplacement>(modelReasons);
	
	private JPanel pnlModRaison = new JPanel(new VerticalLayout());
	private JPanel pnlListRaisons = new JPanel(new VerticalLayout());
	private JPanel pnlModName = new JPanel(new VerticalLayout());
	private JPanel pnlModButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	private JLabel lblName = new JLabel("Nom de la raison");
	private JTextField jtfName = new JTextField("", 15);
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
