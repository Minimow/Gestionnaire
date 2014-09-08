package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.DBConnection;
import utilities.AutoCommitFormatter;
import utilities.IconLoader;
import utilities.DateComboBox;
import utilities.AlphaFormattedTextField;
import utilities.Observer;
import utilities.Subject;
import dao.CoordonneesDAO;
import dao.EmployeDAO;
import dao.StatutDAO;
import dao.daoException.DAOCreateException;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoException.DAOUpdateException;
import dao.daoPOJO.Coordonnees;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Statut;

public class AjouterEmployeDialog extends JDialog implements Subject {

	private static final long serialVersionUID = 1L;

	public AjouterEmployeDialog(JFrame parent, int numEmploye) {
		super(parent, "Modification d'un employé", true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		initComponent();
		this.pack();
		this.setLocationRelativeTo(null);
		setIsModeAdd(numEmploye);
	}

	public void initComponent() {
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		
		jtfLastName.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfFirstName.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfNoEmploye.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfNoSDeS.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfEmail.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfNoCivic.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfExt1.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfExt2.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfPhone1.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfPhone2.addPropertyChangeListener("value", new FieldsValidatorListener());
		jtfStreetName.addPropertyChangeListener("value", new FieldsValidatorListener());

		modelSex.addElement("M");
		modelSex.addElement("F");
		
		refreshStatusModel();

		initPanes();
		GridBagConstraints gbc = new GridBagConstraints();
		pnlMiddle.setLayout(new GridBagLayout());

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 2;
		gbc.gridwidth = 4;
		pnlMiddle.add(pnlName, gbc);
		// ---------------------------------------------
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = 6;
		gbc.gridwidth = 4;
		pnlMiddle.add(pnlImage, gbc);
		// ---------------------------------------------
		gbc.gridx = 4;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		pnlMiddle.add(pnlCoord, gbc);
		// ---------------------------------------------
		gbc.gridx = 4;
		gbc.gridy = 4;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = 2;
		pnlMiddle.add(pnlInfo, gbc);
		
		pnlButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnlButtons.add(btnCreate);
		pnlButtons.add(btnCancel);
		
		this.setLayout(new BorderLayout());
		this.add(pnlMiddle, BorderLayout.CENTER);
		this.add(pnlButtons, BorderLayout.SOUTH);
	}

	/*
	 * Place components in their respective layout.
	 */
	public void initPanes() {
		GroupLayout nomPrenomLayout = new GroupLayout(pnlName);
		pnlName.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Nom de l'employé"));
		pnlName.setLayout(nomPrenomLayout);
		nomPrenomLayout.setAutoCreateGaps(true);
		nomPrenomLayout.setAutoCreateContainerGaps(true);
		nomPrenomLayout.setHorizontalGroup(nomPrenomLayout
				.createSequentialGroup()
				.addGroup(
						nomPrenomLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblName).addComponent(lblFirstName))
				.addGroup(
						nomPrenomLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(jtfLastName).addComponent(jtfFirstName)));
		nomPrenomLayout.setVerticalGroup(nomPrenomLayout
				.createSequentialGroup()
				.addGroup(
						nomPrenomLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblName).addComponent(jtfLastName))
				.addGroup(
						nomPrenomLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblFirstName)
								.addComponent(jtfFirstName)));

		GroupLayout profilLayout = new GroupLayout(pnlImage);
		pnlImage.setPreferredSize(new Dimension(235, 350));
		pnlImage.setLayout(profilLayout);
		profilLayout.setAutoCreateGaps(true);
		profilLayout.setAutoCreateContainerGaps(true);
		profilLayout.setHorizontalGroup(profilLayout.createSequentialGroup()
				.addGroup(
						profilLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblImageProfil)
								.addComponent(btnChangeImage)));
		profilLayout.setVerticalGroup(profilLayout.createSequentialGroup()
				.addComponent(lblImageProfil).addComponent(btnChangeImage));

		GroupLayout coordLayout = new GroupLayout(pnlCoord);
		pnlCoord.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Coordonnées"));
		pnlCoord.setLayout(coordLayout);
		coordLayout.setAutoCreateGaps(true);
		coordLayout.setAutoCreateContainerGaps(true);
		coordLayout.setHorizontalGroup(coordLayout
				.createSequentialGroup()
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblEmail)
								.addComponent(lblPhone1).addComponent(lblPhone2)
								.addComponent(lblStreetName)
								.addComponent(lblCity)
								.addComponent(lblZipCode))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(jtfEmail)
								.addComponent(jtfPhone1)
								.addComponent(jtfPhone2)
								.addGroup(
										coordLayout.createSequentialGroup()
												.addComponent(jtfNoCivic)
												.addComponent(jtfStreetName))
								.addComponent(jtfVille)
								.addComponent(jtfCodePostal))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblExt1).addComponent(lblExt2)
								.addComponent(lblApt))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(jtfExt1)
								.addComponent(jtfExt2).addComponent(jtfApt)));
		coordLayout.setVerticalGroup(coordLayout
				.createSequentialGroup()
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblEmail)
								.addComponent(jtfEmail))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblPhone1).addComponent(jtfPhone1)
								.addComponent(lblExt1).addComponent(jtfExt1))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblPhone2).addComponent(jtfPhone2)
								.addComponent(lblExt2).addComponent(jtfExt2))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblStreetName)
								.addComponent(jtfNoCivic)
								.addComponent(jtfStreetName).addComponent(lblApt)
								.addComponent(jtfApt))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblCity).addComponent(jtfVille))
				.addGroup(
						coordLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblZipCode)
								.addComponent(jtfCodePostal)));

		GroupLayout genInfoLayout = new GroupLayout(pnlInfo);
		pnlInfo.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black), "Informations"));
		pnlInfo.setLayout(genInfoLayout);
		genInfoLayout.setAutoCreateGaps(true);
		genInfoLayout.setAutoCreateContainerGaps(true);
		genInfoLayout.setHorizontalGroup(genInfoLayout
				.createSequentialGroup()
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblNoEmployee)
								.addComponent(lblDateOfBirth)
								.addComponent(lblSex).addComponent(lblStatus)
								.addComponent(lblHiringDate)
								.addComponent(lblRankHiring)
								.addComponent(lblNoSDeS))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(jtfNoEmploye)
								.addComponent(cmbDateOfBirth)
								.addComponent(cmbSex).addComponent(cmbStatus)
								.addComponent(cmbHiringDate)
								.addComponent(jtfRankHiring)
								.addComponent(jtfNoSDeS)));
		genInfoLayout.setVerticalGroup(genInfoLayout
				.createSequentialGroup()
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblNoEmployee)
								.addComponent(jtfNoEmploye))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblDateOfBirth)
								.addComponent(cmbDateOfBirth))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblSex).addComponent(cmbSex))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblStatus)
								.addComponent(cmbStatus))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblHiringDate)
								.addComponent(cmbHiringDate))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblRankHiring)
								.addComponent(jtfRankHiring))
				.addGroup(
						genInfoLayout
								.createParallelGroup(
										GroupLayout.Alignment.LEADING)
								.addComponent(lblNoSDeS)
								.addComponent(jtfNoSDeS)));

		// Set thw width of the coord and info panel to match the highest of the 2. For a better visual
		if(pnlCoord.getPreferredSize().getWidth() > pnlInfo.getPreferredSize().width){
			pnlInfo.setPreferredSize(new Dimension(pnlCoord.getPreferredSize().width, 220));
		}
		else{
			pnlCoord.setPreferredSize(new Dimension(pnlInfo.getPreferredSize().width, 200));
		}
	}

	/*
	 * Call the database to update the model with all current status.
	 */
	public void refreshStatusModel(){
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
		modelStatus.removeAllElements();
		for (int i = 0; i < arrayStatut.size(); i++) {
			modelStatus.addElement(arrayStatut.get(i));
		}
	}
	
	/*
	 * Set the mode of this JDialog. If noEmployee is 0, it means that we are creating one, if it is any other number
	 * it means we are updating one. It also calls the method setFieldsValue(boolean isModeAdd).
	 */
	public void setIsModeAdd(int noEmploye) {
		_noEmploye = noEmploye;
		newEmployee = null;
		btnCreate.setAction(modifyEmployee);
		if (_noEmploye == 0) {
			btnCreate.setText("Ajouter");
			setFieldsValue(true);
			jtfNoEmploye.setEditable(true);
			jtfNoEmploye.setEnabled(true);
			isModeAdd = true;
		} else {
			btnCreate.setText("Valider");
			setFieldsValue(false);
			jtfNoEmploye.setEditable(false);
			jtfNoEmploye.setEnabled(false);
			isModeAdd = false;
		}
	}
	
	/*
	 * Add or modify the employe to the database. If isModeAdd is true then the employee will be added to the datebase.
	 * If not, it will be modify. Once the update has been made, this dialog is closed and it notifies its observers.
	 */
	private AbstractAction modifyEmployee = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			
			// If the required fields are not filled, we do not continue the update.
			if (!isAllRequiredFieldsFilled()) {
				return;
			}
			Employe empFromFields = getEmployeFromFields();
			EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
			CoordonneesDAO coordDAO = new CoordonneesDAO(DBConnection.getInstance());
			Coordonnees coordFromFields = getCoordFromFields();
			coordFromFields.setNoEmploye(empFromFields.getNoEmploye());
			
			// We set the autoCommit mode to false, to make sure to update both the table employe and coordonnees. They
			// must be both updated, not only one of them.
			try {
				DBConnection.getInstance().setAutoCommit(false);
				if(isModeAdd){
					employeDAO.create(empFromFields);
					coordDAO.create(coordFromFields);
				}
				else{
					employeDAO.update(empFromFields);
					// If there was already an coord row for the employee; we update it. Otherwise we create one.
					if(!coordDAO.update(coordFromFields)){
						coordDAO.create(coordFromFields);
					}
				}
				// There was no error, so we commit the change, and change back the autocommit mode for further queries.
				DBConnection.getInstance().commit();
				DBConnection.getInstance().setAutoCommit(true);
				
				// Update this dialog and notify the observers.
				JOptionPane.showMessageDialog(null,
						"Les modifications sur l'employé ont bien été appliquées.",
					    DBConnection.MESSAGE_SUCCESS,
					    JOptionPane.INFORMATION_MESSAGE,
					    IconLoader.createImageIcon("/greenCheck.png", new Dimension(64,64)));
				newEmployee = empFromFields;
				changed = true;
				notifyObservers();
				dispose();
			}
			catch (SQLException | DAOUpdateException | DAOCreateException e ){
				{
					// Rollback the quries to make sure both tables are not updated.
					try{
					DBConnection.getInstance().rollback();
					DBConnection.getInstance().setAutoCommit(true);
					}
					catch(SQLException e2){
						ErrorDialog err = new ErrorDialog(null, DAOException.TITRE, "Erreur MySQL", e2.getMessage());
						err.setVisible(true);
					}
					ErrorDialog err = new ErrorDialog(null,
							DAOException.TITRE, DAOException.TYPE_UPDATE + " " + DAOException.TYPE_CREATE,
							e.getMessage());
					err.setVisible(true);
					return;
				}
			}
		}
	};

	/*
	 * Verify that all fields required are filled. If not, their background color changes to the errorColor.
	 */
	private boolean isAllRequiredFieldsFilled() {
		boolean allFieldsFilled = true;
		if (jtfLastName.getText().isEmpty()) {
			setBackgroundColor(jtfLastName, false);
			allFieldsFilled = false;
		}
		if (jtfFirstName.getText().isEmpty()) {
			setBackgroundColor(jtfFirstName, false);
			allFieldsFilled = false;
		}

		if (jtfNoEmploye.getText().isEmpty()) {
			setBackgroundColor(jtfNoEmploye, false);
			allFieldsFilled = false;
		}
		
		// Si une extension est présente sans numéro de téléphone
		if (jtfPhone1.getText().isEmpty() && !jtfExt1.getText().isEmpty()) {
			setBackgroundColor(jtfPhone1, false);
			allFieldsFilled = false;
		}
		
		// Si une extension est présente sans numéro de téléphone
		if (jtfPhone2.getText().isEmpty() && !jtfExt2.getText().isEmpty()) {
			setBackgroundColor(jtfPhone2, false);
			allFieldsFilled = false;
		}
		
		// Si une adresse est présente sans numéro civic
		if (!jtfStreetName.getText().isEmpty() && jtfNoCivic.getText().isEmpty()) {
			setBackgroundColor(jtfNoCivic, false);
			allFieldsFilled = false;
		}
		
		// Si un numéro civic est présent sans adresse
		if (jtfStreetName.getText().isEmpty() && !jtfNoCivic.getText().isEmpty()) {
			setBackgroundColor(jtfStreetName, false);
			allFieldsFilled = false;
		}

		return allFieldsFilled;

	}

	/*
	 * Return an employee created with the values of the fields.
	 */
	public Employe getEmployeFromFields() {
		String nom = jtfLastName.getText();
		String prenom = jtfFirstName.getText();
		String sexe = (String) cmbSex.getSelectedItem();
		Statut statut = (Statut) cmbStatus.getSelectedItem();
		int numEmploye;
		try {
			Number num = NumberFormat.getInstance().parse(
					jtfNoEmploye.getText());
			numEmploye = num.intValue();
		} catch (ParseException e) {
			numEmploye = 0;
		}
		
		int rank;
		try {
			Number num = NumberFormat.getInstance().parse(
					jtfRankHiring.getText());
			rank = num.intValue();
		} catch (ParseException e) {
			rank = 1;
		}

		String numSDeS;
		try {
			Number num = NumberFormat.getInstance().parse(jtfNoSDeS.getText());
			numSDeS = num.toString();
		} catch (ParseException e) {
			numSDeS = null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		Calendar dateEmbauche = Calendar.getInstance(), dateNaissance = Calendar
				.getInstance();
		sdf.setLenient(false);

		dateEmbauche = cmbHiringDate.getDate();
		dateNaissance = cmbDateOfBirth.getDate();

		Employe emp = new Employe(numEmploye, nom, prenom, sexe, dateEmbauche,
				dateNaissance, numSDeS);
		emp.setStatus(statut);
		emp.setHiringRank(rank);

		return emp;
	}

	/*
	 * Return a coord created with the values of the fields.
	 */
	public Coordonnees getCoordFromFields() {
		String courriel = jtfEmail.getText();
		String rue = jtfStreetName.getText();
		String tel1 = jtfPhone1.getText();
		String tel2 = jtfPhone2.getText();
		String ville = jtfVille.getText();
		String codePostal = jtfCodePostal.getText();
		
		if (courriel.isEmpty()){
			courriel = null;
		}
		
		if (codePostal.isEmpty()){
			codePostal = null;
		}
		
		if (tel1.isEmpty()){
			tel1 = null;
		}
		
		if (tel2.isEmpty()){
			tel2 = null;
		}

		String apt;
		try {
			Number num = NumberFormat.getInstance().parse(jtfApt.getText());
			apt = num.toString();
		} catch (ParseException e) {
			apt = null;
		}

		String ext1;
		try {
			Number num = NumberFormat.getInstance().parse(jtfExt1.getText());
			ext1 = num.toString();
		} catch (ParseException e) {
			ext1 = null;
		}

		String ext2;
		try {
			Number num = NumberFormat.getInstance().parse(jtfExt2.getText());
			ext2 = num.toString();
		} catch (ParseException e) {
			ext2 = null;
		}

		int numCivic;
		try {
			Number num = NumberFormat.getInstance()
					.parse(jtfNoCivic.getText());
			numCivic = num.intValue();
		} catch (ParseException e) {
			numCivic = 0;
		}

		Coordonnees coord = new Coordonnees(0, courriel, numCivic, rue, apt,
				ville, codePostal);
		coord.setPhone1(tel1);
		coord.setExt1(ext1);
		coord.setPhone2(tel2);
		coord.setExt2(ext2);

		return coord;
	}

	/*
	 * Use the noEmploye to set the fields values according to the mode.
	 */
	private void setFieldsValue(boolean modeAdd) {
		if (!modeAdd) {
			EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
			CoordonneesDAO coordDAO = new CoordonneesDAO(DBConnection.getInstance());
			StatutDAO statusDAO = new StatutDAO(DBConnection.getInstance());
			Employe emp;
			Coordonnees coord;
			Statut status;
			try {
				emp = employeDAO.find(_noEmploye);
				coord = coordDAO.find(_noEmploye);
				status = statusDAO.findByNom(emp.getStatus().getName());
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_FIND, e.getMessage());
				err.setVisible(true);
				dispose();
				return;
			}

			// If a employee if found then we set the fields with the employee values.
			if (emp != null) {
				jtfLastName.setText(emp.getLastName());
				jtfFirstName.setText(emp.getFirstName());
				jtfNoEmploye.setText(Integer.toString(_noEmploye));
				cmbSex.setSelectedItem(emp.getSex());
				jtfNoSDeS.setText(emp.getNoSDeS());
				cmbDateOfBirth.setDate(emp.getDateOfBirth());
				cmbHiringDate.setDate(emp.getHiringDate());
				jtfRankHiring.setText(Integer.toString(emp.getHiringRank()));
				
				// To make sure we get the same status, sometimes the object status is not the same.
				for(int i = 0; i < modelStatus.getSize(); i++){
					if(modelStatus.getElementAt(i).getName().equals(status.getName())){
						cmbStatus.setSelectedIndex(i);
						break;
					}
				}
			}
			// If not we set the fields to empty
			else{
				setEmptyFieldsEmploye();
			}

			// If the coord is not null then we set the fiels with the coord values.
			if (coord != null) {
				jtfEmail.setText(coord.getEmail());
				int noCivic = (Integer)coord.getNoCivic();
				
				// By default a noCivic = 0 means that the field is empty. (We put the 0 to avoid the Null Pointer).
				if(noCivic == 0){
					jtfNoCivic.setText("");
				}
				else{
					jtfNoCivic.setText(Integer.toString(noCivic));
				}
				jtfStreetName.setText(coord.getStreetName());
				jtfApt.setText(coord.getNoApt());
				jtfVille.setText(coord.getCity());
				jtfCodePostal.setText(coord.getZipCode());
				jtfPhone1.setText(coord.getPhone1());
				jtfPhone2.setText(coord.getPhone2());
				jtfExt1.setText(coord.getExt1());
				jtfExt2.setText(coord.getExt2());
			}
			else{
				setEmptyFieldsCoordonnees();
			}
		// If we are not updating an employee, we put empty fields.
		} else {
			setEmptyFieldsEmploye();
			setEmptyFieldsCoordonnees();
		}
		
		// Here we update the value of all the textField, to make sure it is up to date. It also refresh the
		// FieldValidatorListener so the color is updated.
		jtfLastName.setValue(jtfLastName.getText());
		jtfFirstName.setValue(jtfFirstName.getText());
		jtfNoEmploye.setValue(jtfNoEmploye.getText());
		jtfNoSDeS.setValue(jtfNoSDeS.getText());
		jtfRankHiring.setValue(jtfRankHiring.getText());
	
		jtfEmail.setValue(jtfEmail.getText());
		jtfNoCivic.setValue(jtfNoCivic.getText());
		jtfStreetName.setValue(jtfStreetName.getText());
		jtfApt.setValue(jtfApt.getText());
		jtfVille.setValue(jtfVille.getText());
		jtfCodePostal.setValue(jtfCodePostal.getText());
		jtfPhone1.setValue(jtfPhone1.getText());
		jtfPhone2.setValue(jtfPhone2.getText());
		jtfExt1.setValue(jtfExt1.getText());
		jtfExt2.setValue(jtfExt2.getText());
	}
	
	/*
	 * Set all employee fields to empty and the comboBox to the index 0.
	 */
	private void setEmptyFieldsEmploye(){
		jtfLastName.setText("");
		jtfFirstName.setText("");
		jtfNoEmploye.setText("");
		cmbSex.setSelectedItem("M");
		cmbStatus.setSelectedIndex(0);
		jtfNoSDeS.setText("");
		cmbDateOfBirth.setDate(Calendar.getInstance());
		cmbHiringDate.setDate(Calendar.getInstance());
		jtfRankHiring.setText("");
	}
	
	/*
	 * Set all coord fields to empty.
	 */
	private void setEmptyFieldsCoordonnees(){
		jtfEmail.setText("");
		jtfNoCivic.setText("");
		jtfStreetName.setText("");
		jtfApt.setText("");
		jtfVille.setText("");
		jtfCodePostal.setText("");
		jtfPhone1.setText("");
		jtfPhone2.setText("");
		jtfExt1.setText("");
		jtfExt2.setText("");
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
	public Employe getUpdate(Observer obj) {
		return newEmployee;
	}

	/*
	 * Validate every fields to make sure it contains only valid chracters. If a field is does not contain the right
	 * value, the ok button is disabled and the errorColor.
	 */
	private class FieldsValidatorListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent e) {
			Object source = e.getSource();
			boolean valid;
			boolean isBtnValidEnabled = true;
			if (source == jtfLastName) {
				valid = EmployeDAO.isNameValid((String) jtfLastName.getText())
						|| jtfLastName.getText().isEmpty();
				setBackgroundColor(jtfLastName, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			if (source == jtfFirstName) {
				valid = EmployeDAO.isNameValid((String) jtfFirstName.getText())
						|| jtfFirstName.getText().isEmpty();
				setBackgroundColor(jtfFirstName, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			if (source == jtfNoEmploye) {
				valid = EmployeDAO.isNumEmployeValid((String) jtfNoEmploye
						.getText()) || jtfNoEmploye.getText().isEmpty();
				setBackgroundColor(jtfNoEmploye, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			if (source == jtfNoSDeS) {
				valid = EmployeDAO
						.isNumSDeSValid((String) jtfNoSDeS.getText())
						|| jtfNoSDeS.getText().isEmpty();
				setBackgroundColor(jtfNoSDeS, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfEmail) {
				valid = CoordonneesDAO
						.isCourrielValid((String) jtfEmail.getText())
						|| jtfEmail.getText().isEmpty();
				setBackgroundColor(jtfEmail, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfPhone1) {
				valid = CoordonneesDAO
						.isPhoneNumberValid((String) jtfPhone1.getText())
						|| jtfPhone1.getText().isEmpty();
				setBackgroundColor(jtfPhone1, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfPhone2) {
				valid = CoordonneesDAO
						.isPhoneNumberValid((String) jtfPhone2.getText())
						|| jtfPhone2.getText().isEmpty();
				setBackgroundColor(jtfPhone2, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfExt1) {
				valid = CoordonneesDAO
						.isExtensionValid((String) jtfExt1.getText())
						|| jtfExt1.getText().isEmpty();
				setBackgroundColor(jtfExt1, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfExt2) {
				valid = CoordonneesDAO
						.isExtensionValid((String) jtfExt2.getText())
						|| jtfExt2.getText().isEmpty();
				setBackgroundColor(jtfExt2, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfNoCivic) {
				valid = CoordonneesDAO
						.isNumCivicValid((String) jtfNoCivic.getText())
						|| jtfNoCivic.getText().isEmpty();
				setBackgroundColor(jtfNoCivic, valid);
				if (!valid) {
					isBtnValidEnabled = false;
				}
			}
			
			if (source == jtfStreetName) {
				setBackgroundColor(jtfStreetName, true);
			}

			// if there is at least 1 error we disabled the button.
			btnCreate.setEnabled(isBtnValidEnabled);
		}
	}

	/*
	 * Set the background color of a field according to its validity.
	 */
	private void setBackgroundColor(JFormattedTextField field, boolean valid) {
		if (valid) {
			field.setBackground(Color.white);
		} else {
			field.setBackground(colorError);
		}
	}

	private JLabel lblName = new JLabel("Nom :");
	private JLabel lblFirstName = new JLabel("Prenom :");
	private JLabel lblStatus = new JLabel("Statut :");
	private JLabel lblDateOfBirth = new JLabel("Date de naissance (JJ-MM-YYYY):");
	private JLabel lblHiringDate = new JLabel("Date d'embauche (JJ-MM-YYYY)");
	private JLabel lblNoEmployee = new JLabel("Numero d'employé");
	private JLabel lblEmail = new JLabel("Courriel :");
	private JLabel lblPhone1 = new JLabel("Téléphone 1 :");
	private JLabel lblPhone2 = new JLabel("Téléphone 2 :");
	private JLabel lblStreetName = new JLabel("Adresse :");
	private JLabel lblCity = new JLabel("Ville :");
	private JLabel lblZipCode = new JLabel("Code postal : ");
	private JLabel lblExt1 = new JLabel("Ext :");
	private JLabel lblExt2 = new JLabel("Ext :");
	private JLabel lblApt = new JLabel("Apt :");
	private JLabel lblSex = new JLabel("Sexe :");
	private JLabel lblNoSDeS = new JLabel("Numéro de la société de sauvetage");
	private JLabel lblRankHiring = new JLabel("Rang :");
	
	private Dimension dimProfilImage = new Dimension(225,255);
	private ImageIcon icon = IconLoader.createImageIcon("/profil/profil_template.jpg", dimProfilImage);
	private JLabel lblImageProfil = new JLabel(icon);

	private AutoCommitFormatter autoCommitFormatter = new AutoCommitFormatter();
	private AlphaFormattedTextField jtfLastName = new AlphaFormattedTextField(autoCommitFormatter, 15);
	private AlphaFormattedTextField jtfFirstName = new AlphaFormattedTextField(autoCommitFormatter, 15);
	private AlphaFormattedTextField jtfNoEmploye = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfNoSDeS = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfEmail = new AlphaFormattedTextField(autoCommitFormatter, 17);
	private AlphaFormattedTextField jtfStreetName = new AlphaFormattedTextField(autoCommitFormatter, 8);
	private AlphaFormattedTextField jtfVille = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfApt = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfNoCivic = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfPhone1 = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfPhone2 = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfExt1 = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfExt2 = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfCodePostal = new AlphaFormattedTextField(autoCommitFormatter);
	private AlphaFormattedTextField jtfRankHiring = new AlphaFormattedTextField(autoCommitFormatter);

	private JPanel pnlName = new JPanel();
	private JPanel pnlCoord = new JPanel();
	private JPanel pnlImage = new JPanel();
	private JPanel pnlInfo = new JPanel();
	private JPanel pnlButtons = new JPanel();
	private JPanel pnlMiddle = new JPanel();
	
	private DefaultComboBoxModel<String> modelSex = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<Statut> modelStatus = new DefaultComboBoxModel<Statut>();
	private JComboBox<Statut> cmbStatus = new JComboBox<Statut>(modelStatus);
	private JComboBox<String> cmbSex = new JComboBox<String>(modelSex);
	
	private DateComboBox cmbDateOfBirth = new DateComboBox(1920, Calendar.getInstance().get(Calendar.YEAR) + 1);
	private DateComboBox cmbHiringDate = new DateComboBox(1980, Calendar.getInstance().get(Calendar.YEAR) + 1);
	private Employe newEmployee;

	private JButton btnChangeImage = new JButton("Changer d'image");
	private JButton btnCreate = new JButton("Créer");
	private JButton btnCancel = new JButton("Annuler");
	private int _noEmploye;
	private boolean isModeAdd;
	private Color colorError = new Color(255, 0, 0, 140);

	// Variables for the Observer pattern
	private boolean changed;
	private final Object MUTEX = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}
