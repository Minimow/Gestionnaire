package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTable;

import utilities.VerticalLayout;
import utilities.WrapLayout;
import utilitiesBound.ButtonListTypeStats;
import boxes.StatsBox;
import dao.EmployeDAO;
import dao.RemplacementDAO;
import dao.SessionDAO;
import dao.StatutDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoException.DAOGetAllException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Remplacement;
import dao.daoPOJO.Session;
import dao.daoPOJO.Statut;
import dialogs.ErrorDialog;

public class StatsPane extends JPanel {

	private static final long serialVersionUID = 1L;

	public StatsPane() {
		initComponent();
	}

	private void initComponent() {
		
		initList();
		initMostRempBox();
		initListAnciennetee();
		initListRemplacementsBox();
		initListRemplacementsParSessionBox();
		initMostRempPreneurBox();
		initRempByEmployeBySession();
		initAge();
		initSex();
		initEmpParAnnee();
		initListAncienneteeActive();
		
		this.setLayout(new BorderLayout());
		
		pnlStatsBoxes = new JPanel();
		pnlStatsBoxes.setLayout(new WrapLayout(FlowLayout.LEFT));
		listStatsBox.add(mostRempBox);
		listStatsBox.add(listAncienneteBox);
		listStatsBox.add(listRemplacementsBox);
		listStatsBox.add(listRemplacementsParSessionBox);
		listStatsBox.add(mostRempPreneurBox);
		listStatsBox.add(rempByEmployeBySession);
		listStatsBox.add(statsBoxAge);
		listStatsBox.add(statsBoxSex);
		listStatsBox.add(statsEmpParAnnee);
		listStatsBox.add(statsBoxListAncienneteActive);
		
		this.add(pnlListStats, BorderLayout.WEST);
		this.add(pnlStatsBoxes, BorderLayout.CENTER);
		
		// Default view
		tglStatsTypeGeneral.setSelected(true);
		tglLastClicked = tglStatsTypeGeneral;
		setStatsBox(StatsBox.TAG.GENERAL);		
	}

	private void initList(){
		Dimension dimButtonList = new Dimension(350,75);
		
		tglStatsTypeEmploye = new ButtonListTypeStats("Employés");
		tglStatsTypeEmploye.setPreferredSize(dimButtonList);
		tglStatsTypeEmploye.setFocusPainted(false);
		tglStatsTypeEmploye.addActionListener(new actionShowTypeEmploye());
		
		tglStatsTypeSession = new ButtonListTypeStats("Sessions");
		tglStatsTypeSession.setPreferredSize(dimButtonList);
		tglStatsTypeSession.setFocusPainted(false);
		tglStatsTypeSession.addActionListener(new actionShowTypeSession());
		
		tglStatsTypeRemplacement = new ButtonListTypeStats("Remplacements");
		tglStatsTypeRemplacement.setPreferredSize(dimButtonList);
		tglStatsTypeRemplacement.setFocusPainted(false);
		tglStatsTypeRemplacement.addActionListener(new actionShowTypeRemplacement());
		
		tglStatsTypeGeneral = new ButtonListTypeStats("Général");
		tglStatsTypeGeneral.setPreferredSize(dimButtonList);
		tglStatsTypeGeneral.setFocusPainted(false);
		tglStatsTypeGeneral.addActionListener(new actionShowTypeGeneral());
		
		pnlListStats = new JPanel(new VerticalLayout());
		pnlListStats.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 4, new Color(50, 150, 200)));
		pnlListStats.add(tglStatsTypeGeneral);
		//pnlListStats.add(tglStatsTypeEmploye);
		pnlListStats.add(tglStatsTypeSession);
		pnlListStats.add(tglStatsTypeRemplacement);
	}
	
	private void setStatsBox(StatsBox.TAG tag){
		pnlStatsBoxes.removeAll();
		
		for(int i = 0; i < listStatsBox.size(); i++){
			if(listStatsBox.get(i).getTag() == tag){
				pnlStatsBoxes.add(listStatsBox.get(i));
			}
		}
		pnlStatsBoxes.revalidate();
		pnlStatsBoxes.repaint();
	}

	// Actions for the buttons
	private class actionShowTypeGeneral extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			tglLastClicked.setSelected(false);
			tglLastClicked = tglStatsTypeGeneral;
			tglStatsTypeGeneral.setSelected(true);
			setStatsBox(StatsBox.TAG.GENERAL);
		}
	}
	
	private class actionShowTypeEmploye extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			tglLastClicked.setSelected(false);
			tglLastClicked = tglStatsTypeEmploye;
			tglStatsTypeEmploye.setSelected(true);
			setStatsBox(StatsBox.TAG.EMPLOYES);
		}
	}
	
	private class actionShowTypeRemplacement extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			tglLastClicked.setSelected(false);
			tglLastClicked = tglStatsTypeRemplacement;
			tglStatsTypeRemplacement.setSelected(true);
			setStatsBox(StatsBox.TAG.REMPLACEMENT);
		}
	}
	
	private class actionShowTypeSession extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			tglLastClicked.setSelected(false);
			tglLastClicked = tglStatsTypeSession;
			tglStatsTypeSession.setSelected(true);
			setStatsBox(StatsBox.TAG.SESSION);
		}
	}

	@SuppressWarnings("unchecked")
	private void initMostRempBox() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("#");
		colNames.add("Nom");
		colNames.add("Count");
		mostRempBox = new StatsBox("Nombre de remplacements par employé",
				colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		Map<Employe, Integer> mapEmp;
		try {
			mapEmp = employeDAO.getMostRemp();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		int i = 1;
		for (Map.Entry<Employe, Integer> entry : mapEmp.entrySet()) {
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(i);
			rowData.add(entry.getKey());
			rowData.add(entry.getValue());
			mostRempBox.getModel().addRow(rowData);
			i++;

		}

		Dimension mostRempBoxSize = new Dimension(300, 200);
		mostRempBox.getTable().setPreferredScrollableViewportSize(
				mostRempBoxSize);
		mostRempBox.getTable().getColumnModel().getColumn(0).setMaxWidth(25);
		mostRempBox.getTable().getColumnModel().getColumn(2).setMaxWidth(55);
		
		// Set tag
		mostRempBox.setTag(StatsBox.TAG.REMPLACEMENT);
	}

	@SuppressWarnings("unchecked")
	private void initListAnciennetee() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("#");
		colNames.add("Nom");
		colNames.add("Année");
		listAncienneteBox = new StatsBox("Liste d'anciennetée", colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		ArrayList<Employe> alEmp;
		try {
			alEmp = employeDAO.getAll(EmployeDAO.EMPLOYE_SORT.DATE_EMBAUCHE,
					null, null, false, false, false);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		for (int i = 0; i < alEmp.size(); i++) {
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(i+1);
			rowData.add(alEmp.get(i));
			rowData.add(sdf.format(alEmp.get(i).getHiringDate().getTime()));
			listAncienneteBox.getModel().addRow(rowData);
		}

		Dimension mostRempBoxSize = new Dimension(300, 200);
		listAncienneteBox.getTable().setPreferredScrollableViewportSize(
				mostRempBoxSize);
		listAncienneteBox.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		
		// Set tag
		listAncienneteBox.setTag(StatsBox.TAG.GENERAL);
	}

	@SuppressWarnings("unchecked")
	private void initListRemplacementsBox() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("#");
		colNames.add("Demandeur");
		colNames.add("Date");
		colNames.add("Preneur");
		listRemplacementsBox = new StatsBox("Remplacements", colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		RemplacementDAO rempDAO = new RemplacementDAO(DBConnection.getInstance());
		ArrayList<Remplacement> alRemp;
		try {
			alRemp = rempDAO.getAll();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
		for (int i = 0; i < alRemp.size(); i++) {
			try {
				@SuppressWarnings("rawtypes")
				ArrayList rowData = new ArrayList();
				rowData.add(i);
				rowData.add(employeDAO.find(alRemp.get(i).getRequester()));
				rowData.add(sdf.format(alRemp.get(i).getBeginDate().getTime()));
				rowData.add(employeDAO.find(alRemp.get(i).getTaker()));
				listRemplacementsBox.getModel().addRow(rowData);
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_FIND, e.getMessage());
				err.setVisible(true);
				return;
			}
		}

		Dimension mostRempBoxSize = new Dimension(600, 200);
		listRemplacementsBox.getTable().setPreferredScrollableViewportSize(
				mostRempBoxSize);
		listRemplacementsBox.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		
		// Set tag
		listRemplacementsBox.setTag(StatsBox.TAG.REMPLACEMENT);
	}

	@SuppressWarnings("unchecked")
	private void initListRemplacementsParSessionBox() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Session");
		colNames.add("Count");
		listRemplacementsParSessionBox = new StatsBox(
				"Remplacements par session", colNames);
		RemplacementDAO rempDAO = new RemplacementDAO(DBConnection.getInstance());
		Map<String, Integer> mapSession;
		try {
			mapSession = rempDAO.getRempParSession();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		for (Map.Entry<String, Integer> entry : mapSession.entrySet()) {
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(entry.getKey());
			rowData.add(entry.getValue());
			listRemplacementsParSessionBox.getModel().addRow(rowData);
		}

		Dimension mostRempBoxSize = new Dimension(300, 200);
		listRemplacementsParSessionBox.getTable()
				.setPreferredScrollableViewportSize(mostRempBoxSize);
		listRemplacementsParSessionBox.getTable().getColumnModel().getColumn(1)
				.setMaxWidth(55);
		
		// Set tag
		listRemplacementsParSessionBox.setTag(StatsBox.TAG.SESSION);
	}

	@SuppressWarnings("unchecked")
	private void initMostRempPreneurBox() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("#");
		colNames.add("Nom");
		colNames.add("Count");
		mostRempPreneurBox = new StatsBox(
				"Nombre de fois que l'employé a effectué un remplacement",
				colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		Map<Employe, Integer> mapEmp;
		try {
			mapEmp = employeDAO.getMostPreneur();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		int i = 1;
		for (Map.Entry<Employe, Integer> entry : mapEmp.entrySet()) {
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(i);
			rowData.add(entry.getKey());
			rowData.add(entry.getValue());
			mostRempPreneurBox.getModel().addRow(rowData);
			i++;

		}

		Dimension mostPreneurBoxSize = new Dimension(300, 200);
		mostRempPreneurBox.getTable().setPreferredScrollableViewportSize(
				mostPreneurBoxSize);
		mostRempPreneurBox.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		mostRempPreneurBox.getTable().getColumnModel().getColumn(2)
				.setMaxWidth(55);
		
		// Set tag
		mostRempPreneurBox.setTag(StatsBox.TAG.REMPLACEMENT);
	}
	
	@SuppressWarnings("unchecked")
	private void initRempByEmployeBySession() {
		ArrayList<String> colNames = new ArrayList<String>();
		boolean descSession = false;
		SessionDAO sessionDAO = new SessionDAO(DBConnection.getInstance());
		ArrayList<Session> arraySession;
		try {
			arraySession = sessionDAO.getAll(descSession);
		} catch (DAOGetAllException e1) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e1.getMessage());
			err.setVisible(true);
			return;
		}
		
		colNames.add("Nom");
		for(int i = 0; i < arraySession.size(); i++){
			colNames.add(arraySession.get(i).getName() + " " + arraySession.get(i).getBeginDate().get(Calendar.YEAR));
		}
		rempByEmployeBySession = new StatsBox(
				"Nombre de remplacement par employé par session",
				colNames);
		RemplacementDAO rempDAO = new RemplacementDAO(DBConnection.getInstance());
		Map<Integer, List<Integer>> mapEmp;
		try {
			mapEmp = rempDAO.getRempSessionEmp(descSession);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE +"slkfj",
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		//Ajoute les employes dans la table
		EmployeDAO empDAO = new EmployeDAO(DBConnection.getInstance());
		for (Map.Entry<Integer, List<Integer>> entry : mapEmp.entrySet()) {
			Employe emp;
			try {
				emp = empDAO.find(entry.getKey());
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
						DAOException.TYPE_FIND, e.getMessage());
				err.setVisible(true);
				return;
			}
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(emp.getFullName());
			
			ArrayList<Integer> arrayCountSession = (ArrayList<Integer>) entry.getValue();
			for(int j = 0; j < arrayCountSession.size(); j++){
				rowData.add(arrayCountSession.get(j));
			}
			rempByEmployeBySession.getModel().addRow(rowData);
		}

		Dimension mostPreneurBoxSize = new Dimension(500, 200);
		rempByEmployeBySession.getTable().setPreferredScrollableViewportSize(
				mostPreneurBoxSize);
		// Sil y a plus de 5 colonne, on rajoute la scrollbar (en enlevant le auto resize la scroll bar apparait)
		// Sil y en a moins, on peut le laisse ajuster question de beaute
		if(colNames.size() > 6){
			rempByEmployeBySession.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		
		// Set tag
		rempByEmployeBySession.setTag(StatsBox.TAG.REMPLACEMENT);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initAge() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Tranche d'âge");
		colNames.add("Count");
		statsBoxAge = new StatsBox(
				"Âge des employés",
				colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		Map<Integer, Integer> mapAge;
		float avgAge = 0f;
		try {
			mapAge = employeDAO.getTranchesAge();
			avgAge = employeDAO.getAgeMoyen();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		ArrayList firstRowData = new ArrayList();
		firstRowData.add("Moyenne d'âge");
		firstRowData.add((int)avgAge);
		statsBoxAge.getModel().addRow(firstRowData);
		
		for (Map.Entry<Integer, Integer> entry : mapAge.entrySet()) {
			ArrayList rowData = new ArrayList();
			rowData.add(entry.getKey());
			rowData.add(entry.getValue());
			statsBoxAge.getModel().addRow(rowData);

		}

		Dimension AgeBoxSize = new Dimension(300, 200);
		statsBoxAge.getTable().setPreferredScrollableViewportSize(
				AgeBoxSize);
		/*statsBoxAge.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		statsBoxAge.getTable().getColumnModel().getColumn(1)
				.setMaxWidth(55);*/
		
		// Set tag
		statsBoxAge.setTag(StatsBox.TAG.GENERAL);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initSex() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Hommes");
		colNames.add("Femmes");
		colNames.add("Total");
		statsBoxSex = new StatsBox(
				"Hommes et Femmes",
				colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		ArrayList<Integer> alSex;
		try {
			alSex = employeDAO.getSexRatio();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
		
		ArrayList rowData = new ArrayList();
		for(int i = 0; i < alSex.size(); i++){
			rowData.add(alSex.get(i));
		}
		statsBoxSex.getModel().addRow(rowData);

		Dimension AgeBoxSize = new Dimension(300, 200);
		statsBoxSex.getTable().setPreferredScrollableViewportSize(
				AgeBoxSize);
		/*statsBoxSex.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		statsBoxSex.getTable().getColumnModel().getColumn(1)
				.setMaxWidth(55);*/
		
		// Set tag
		statsBoxSex.setTag(StatsBox.TAG.GENERAL);
	}
	
	private void initEmpParAnnee() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("Année");
		colNames.add("Count");
		statsEmpParAnnee = new StatsBox(
				"Employés embauchés par année",
				colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		Map<Integer, Integer> mapEmpAnnee;
		try {
			mapEmpAnnee = employeDAO.getEmpParAnneeEmbauche();
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}
		
		for (Map.Entry<Integer, Integer> entry : mapEmpAnnee.entrySet()) {
			ArrayList rowData = new ArrayList();
			rowData.add(entry.getKey());
			rowData.add(entry.getValue());
			statsEmpParAnnee.getModel().addRow(rowData);

		}

		Dimension AgeBoxSize = new Dimension(300, 200);
		statsEmpParAnnee.getTable().setPreferredScrollableViewportSize(
				AgeBoxSize);
		/*statsBoxAge.getTable().getColumnModel().getColumn(0)
			statsEmpParAnnee.setMaxWidth(25);
		statsEmpParAnnee.getTable().getColumnModel().getColumn(1)
				.setMaxWidth(55);*/
		
		// Set tag
		statsEmpParAnnee.setTag(StatsBox.TAG.GENERAL);
	}
	
	private void initListAncienneteeActive() {
		ArrayList<String> colNames = new ArrayList<String>();
		colNames.add("#");
		colNames.add("Nom");
		colNames.add("Année");
		statsBoxListAncienneteActive = new StatsBox("Liste d'anciennetée active", colNames);
		EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
		ArrayList<Employe> alEmp;
		
		StatutDAO statusDAO = new StatutDAO(DBConnection.getInstance());
		ArrayList<Statut> alStatus = new ArrayList<Statut>();
		ArrayList<String> alStatusString = new ArrayList<String>();
		
		try {
			alStatus = statusDAO.getAll();
			for(int i = 0; i < alStatus.size(); i++){
				if(alStatus.get(i).getId() != 3 && alStatus.get(i).getId() != 4){ // Demission ou Mis a pied
					alStatusString.add(alStatus.get(i).getName());
				}
			}
			alEmp = employeDAO.getAll(EmployeDAO.EMPLOYE_SORT.DATE_EMBAUCHE,
					alStatusString, null, false, false, false);
		} catch (DAOGetAllException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE,
					DAOException.TYPE_GETALL, e.getMessage());
			err.setVisible(true);
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		for (int i = 0; i < alEmp.size(); i++) {
			@SuppressWarnings("rawtypes")
			ArrayList rowData = new ArrayList();
			rowData.add(i+1);
			rowData.add(alEmp.get(i));
			rowData.add(sdf.format(alEmp.get(i).getHiringDate().getTime()));
			statsBoxListAncienneteActive.getModel().addRow(rowData);
		}

		Dimension mostRempBoxSize = new Dimension(300, 400);
		statsBoxListAncienneteActive.getTable().setPreferredScrollableViewportSize(
				mostRempBoxSize);
		statsBoxListAncienneteActive.getTable().getColumnModel().getColumn(0)
				.setMaxWidth(25);
		
		// Set tag
		statsBoxListAncienneteActive.setTag(StatsBox.TAG.GENERAL);
	}

	private JPanel pnlListStats;
	private JPanel pnlStatsBoxes;
	private ButtonListTypeStats tglStatsTypeEmploye;
	private ButtonListTypeStats tglStatsTypeSession;
	private ButtonListTypeStats tglStatsTypeRemplacement;
	private ButtonListTypeStats tglStatsTypeGeneral;
	private ButtonListTypeStats tglLastClicked;
	private StatsBox mostRempBox;
	private StatsBox listAncienneteBox;
	private StatsBox listRemplacementsBox;
	private StatsBox listRemplacementsParSessionBox;
	private StatsBox mostRempPreneurBox;
	private StatsBox rempByEmployeBySession;
	private StatsBox statsBoxAge;
	private StatsBox statsBoxSex;
	private StatsBox statsEmpParAnnee;
	private StatsBox statsBoxListAncienneteActive;
	private ArrayList<StatsBox> listStatsBox = new ArrayList<StatsBox>();
}
