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
import utilities.DateComboBox;
import utilities.Observer;
import utilities.Subject;
import utilities.VerticalLayout;
import dao.SessionDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Session;

public class AdminSession extends JDialog implements Subject{
	
	private static final long serialVersionUID = 1L;

	public AdminSession(){
		this.setLayout(new BorderLayout());
		initComponent();
		
		this.setTitle("Admin : Sessions");
		this.setModal(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	private void initComponent(){
		// Initialize the list with all the sessions
		listSessions.addListSelectionListener(new ListSessionsSelectionHandler());
		refreshModelSession();
		
		//Add actionlistener to the component
		chkAdd.addActionListener(actionChkAdd);
		btnApply.addActionListener(actionApply);
		btnDelete.addActionListener(actionDelete);
		btnCancel.addActionListener(actionCancel);
		btnAdd.addActionListener(actionAdd);
		
		// Add components to their respective container
		pnlListSessions.add(new JScrollPane(listSessions));
		pnlListSessions.add(btnDelete);
		
		pnlModDate.add(lblName);
		pnlModDate.add(jtfName);
		pnlModDate.add(lblDateDebut);
		pnlModDate.add(dcbDateDebut);
		pnlModDate.add(lblDateFin);
		pnlModDate.add(dcbDateFin);
		
		pnlModButtons.add(chkAdd);
		pnlModButtons.add(btnAdd);
		pnlModButtons.add(btnApply);
		
		pnlModSession.add(pnlModDate);
		pnlModSession.add(pnlModButtons);
		
		pnlButtons.add(btnCancel);
		pnlButtons.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
		
		this.add(pnlListSessions, BorderLayout.WEST);
		this.add(pnlModSession, BorderLayout.EAST);
		this.add(pnlButtons, BorderLayout.SOUTH);
		
		//Set the default value of the components
		btnAdd.setEnabled(false);
	}
	
	public void changeSession(Session session){
		dcbDateDebut.setDate(session.getBeginDate());
		dcbDateDebut.resetModifiedColor();
		dcbDateFin.setDate(session.getEndDate());
		dcbDateFin.resetModifiedColor();
		jtfName.setText(session.getName());
	}
	
	private void refreshModelSession(){
		SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
		ArrayList<Session> arraySessions;
		try {
			arraySessions = sessionDAO.getAll(true);
			modelSessions.removeAllElements();
			for(int i = 0; i < arraySessions.size(); i++){
				modelSessions.addElement(arraySessions.get(i));
			}
			listSessions.setSelectedIndex(0);
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
				JList<Session> l = (JList<Session>) e.getSource();
				if(l.getSelectedValue() != null){
					changeSession(l.getSelectedValue());
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
			Session curSession = listSessions.getSelectedValue();
			
			curSession.setName(jtfName.getText());
			curSession.setBeginDate(dcbDateDebut.getDate());
			curSession.setEndDate(dcbDateFin.getDate());

			SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
			try {
				sessionDAO.update(curSession);
				JOptionPane.showMessageDialog(null,
						"Les modifications sur la session ont bien été appliquées.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModelSession();
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
			Session newSession = new Session();
			
			newSession.setName(jtfName.getText());
			newSession.setBeginDate(dcbDateDebut.getDate());
			newSession.setEndDate(dcbDateFin.getDate());

			SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
			try {
				sessionDAO.create(newSession);
				JOptionPane.showMessageDialog(null,
						"La création de la session s'est effectuée correctement.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModelSession();
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
			Session curSession = listSessions.getSelectedValue();

			SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
			try {
				sessionDAO.delete(curSession);
				JOptionPane.showMessageDialog(null,
						"La suppression de la session s'est effectuée correctement.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				refreshModelSession();
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
	
	private DefaultListModel<Session> modelSessions = new DefaultListModel<Session>();
	private JList<Session> listSessions = new JList<Session>(modelSessions);
	
	private JPanel pnlModSession = new JPanel(new VerticalLayout());
	private JPanel pnlListSessions = new JPanel(new VerticalLayout());
	private JPanel pnlModDate = new JPanel(new VerticalLayout());
	private JPanel pnlModButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	private JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	
	private DateComboBox dcbDateDebut = new DateComboBox();
	private DateComboBox dcbDateFin = new DateComboBox();
	private JLabel lblName = new JLabel("Nom de la session");
	private JLabel lblDateDebut = new JLabel("Début de la session");
	private JLabel lblDateFin = new JLabel("Fin de la session");
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
