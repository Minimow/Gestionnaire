package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.DBConnection;
import utilities.IconLoader;
import utilities.DateComboBox;
import utilities.Observer;
import utilities.Subject;
import dao.EmployeQualificationsDAO;
import dao.QualificationDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAODeleteException;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.EmployeQualifications;
import dao.daoPOJO.Qualification;

public class ModifierQualifDialog extends JDialog  implements Subject{

	private static final long serialVersionUID = 1L;

	public ModifierQualifDialog(JFrame parent, String title, boolean modal,
			int numEmploye, String acronyme) {
		super(parent, title, modal);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		lblQualif.setText(acronyme);
		_numEmploye = numEmploye;
		_acronyme = acronyme;
		initComponent();
		this.pack();
	}
	
	public void setNewQualif(int noEmployee, String acronyme){
		lblQualif.setText(acronyme);
		_numEmploye = noEmployee;
		_acronyme = acronyme;
		
		ImageIcon icone = IconLoader.createImageIcon("/qualifs/" + _acronyme
				+ "_icone.jpg", new Dimension(50,50));
		lblQualif.setIcon(icone);
	}

	private void initComponent() {
		btnValider.addActionListener(modifierQualif);
		chkDelete.addActionListener(actionChkDelete);
		btnAnnuler.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});

		JPanel rowHeader = new JPanel();
		rowHeader.setAlignmentX(LEFT_ALIGNMENT);
		rowHeader.setLayout(new BoxLayout(rowHeader, BoxLayout.LINE_AXIS));
		rowHeader.add(lblDateQualif);
		rowHeader.add(Box.createHorizontalGlue());
		rowHeader.add(cmbDate);
		
		JPanel rowDelete = new JPanel();
		rowDelete.setAlignmentX(LEFT_ALIGNMENT);
		rowDelete.setLayout(new BoxLayout(rowDelete, BoxLayout.LINE_AXIS));
		rowDelete.add(lblDelete);
		rowDelete.add(Box.createHorizontalGlue());
		rowDelete.add(chkDelete);
		rowDelete.add(btnReset);

		JPanel col1 = new JPanel();
		col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
		col1.add(rowHeader);
		col1.add(rowDelete);

		JPanel rowBtn = new JPanel();
		rowBtn.add(btnValider);
		rowBtn.add(btnAnnuler);

		if(_acronyme != null){
			ImageIcon icone = IconLoader.createImageIcon("/qualifs/" + _acronyme
					+ "_icone.jpg", new Dimension(50,50));
			lblQualif.setIcon(icone);
		}
		this.add(lblQualif, BorderLayout.NORTH);
		this.add(col1, BorderLayout.CENTER);
		this.add(rowBtn, BorderLayout.SOUTH);
	}

	private AbstractAction actionChkDelete = new AbstractAction(){

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent ae) {
			
			if(chkDelete.isSelected()){
				btnValider.setForeground(Color.red);
				btnValider.setText("Supprimer");
			}
			else{
				btnValider.setForeground(Color.black);
				btnValider.setText("Valider");
			}
		}
		
	};
	
	private AbstractAction modifierQualif = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			Calendar dateQualif = cmbDate.getDate();

			QualificationDAO qualifDAO = new QualificationDAO(
					DBConnection.getInstance());
			Qualification qualif;
			try {
				qualif = qualifDAO.findByAcronyme(_acronyme);
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_FIND, e.getMessage());
				err.setVisible(true);
				return;
			}
			empQualif = new EmployeQualifications(
					_numEmploye, dateQualif, null);
			empQualif.setId(qualif.getId());
			empQualif.setName(qualif.getName());
			empQualif.setAcronyme(qualif.getAcronyme());
			empQualif.setYearsValid(qualif.getYearsValid());
			empQualif.setPriority(qualif.getPriority());
			EmployeQualificationsDAO empQualifDAO = new EmployeQualificationsDAO(
					DBConnection.getInstance());
			
			// Si la case delete est checked
			if(chkDelete.isSelected()){
				try {
					int n = JOptionPane.showOptionDialog(null,
						    "Êtes-vous sur de vouloir supprimer la qualification?",
						    "Confirmation",
						    JOptionPane.YES_NO_OPTION,
						    JOptionPane.QUESTION_MESSAGE,
						    IconLoader.createImageIcon("/warningIcon.png", new Dimension(64,64)),     //do not use a custom Icon
						    null,
						    null);
					if(n == 0){//Yes
						if(empQualifDAO.delete(empQualif)){
							JOptionPane.showMessageDialog(null,
									"L'employé n'est maintenant plus qualifié en tant que : " + empQualif.getName() + ".",
								    DBConnection.MESSAGE_SUCCESS,
								    JOptionPane.INFORMATION_MESSAGE,
								    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
							changed = true;
							notifyObservers();
							dispose();
						}
						else{ // No
							JOptionPane.showMessageDialog(null,
									"La suppression ne s`est pas effectuée correctement.",
								    DBConnection.MESSAGE_SUCCESS,
								    JOptionPane.INFORMATION_MESSAGE,
								    IconLoader.createImageIcon("/warningIcon.png", new Dimension(64,64)));
						}
						
					}
					
				} catch (DAODeleteException e) {
					ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
							DAOException.TYPE_DELETE, e.getMessage());
					err.setVisible(true);
					return;
				}
				return;
			}
			
			boolean updated = false;
			try {
				if(empQualifDAO.update(empQualif)){
					updated = true;
					JOptionPane.showMessageDialog(null,
							"La qualification a bien été mise à jour.",
						    DBConnection.MESSAGE_SUCCESS,
						    JOptionPane.INFORMATION_MESSAGE,
						    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
					changed = true;
					notifyObservers();
				}
			} catch (DAOUpdateException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_UPDATE, e.getMessage());
				err.setVisible(true);
				return;
			}
			try {
				if(!updated){
					empQualifDAO.create(empQualif);
					JOptionPane.showMessageDialog(null,
							"La qualification a bien été créée.",
						    DBConnection.MESSAGE_SUCCESS,
						    JOptionPane.INFORMATION_MESSAGE,
						    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
					changed = true;
					notifyObservers();
				}
			} catch (DAOCreateException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_CREATE, e.getMessage());
				err.setVisible(true);
				return;
			}
			dispose();
		}
	};
	
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
	public EmployeQualifications getUpdate(Observer obj) {
		return empQualif;
	}

	private JLabel lblQualif = new JLabel(), lblDateQualif = new JLabel(
			"Date de qualification :");
	private JLabel lblDelete = new JLabel("Supprimer la qualification :");
	private JCheckBox chkDelete = new JCheckBox();
	private DateComboBox cmbDate = new DateComboBox(2000, 2015);
	private JButton btnValider = new JButton("Valider"),
			btnAnnuler = new JButton("Annuler");
	private JButton btnReset = new JButton("Reset");
	private String _acronyme;
	private EmployeQualifications empQualif;
	private int _numEmploye;
	
	// Variables for the Observer pattern
	private boolean changed;
	private final Object MUTEX = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}