package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utilities.IconLoader;
import utilities.Observer;
import boxes.CoordonneesBox;
import boxes.InfoBox;
import boxes.ListEmployesBox;
import boxes.QualificationsBox;
import boxes.RemplacementsBox;
import dao.CoordonneesDAO;
import dao.daoException.DAOException;
import dao.daoException.DAOFindException;
import dao.daoPOJO.Employe;
import dialogs.ErrorDialog;

public class EmployeesPane extends JPanel implements Observer{

	private static final long serialVersionUID = 1L;

	public EmployeesPane() {
		initComponent();

		this.addComponentListener(new FrameResizeListener());
		
        // Register observers to the subject
        listEmployesBox.register(this);
        listEmployesBox.getOptionBox().getAjoutEmployeDialog().register(this);
        qualifBox.getModQualifDialog().register(this);
        
        // Default employee
        changeEmploye(listEmployesBox.getList().getModel().getElementAt(0));
	}
	
	public ListEmployesBox getListBox(){
		return listEmployesBox;
	}
	
	@Override
	public void update() {
		Employe emp = (Employe) listEmployesBox.getUpdate(this);
		if(emp != null){
			changeEmploye(emp);
		}
		rempBox.getAjoutModRempDialog().updateComboBoxes();
	}
    
	/*
	 * Adjust the pane to show the employee's info.
	 */
	private void changeEmploye(Employe employe){
		Color col;
		if(employe.getSex().equals("M")){
			col = new Color(0, 162,232);
		}
		else{
			col = new Color(255,50,255);
		}
		lblTitleName.setForeground(col);
		lblTitleName.setText(employe.getLastName() + ", " + employe.getFirstName());
		
		infoBox.setInfoEmploye(employe);
		qualifBox.setQualifEmploye(employe);
		rempBox.setEmploye(employe);
		lblProfilImage.setIcon(IconLoader.createImageIcon(employe.getImagepath(), dimProfilImage));
		
		CoordonneesDAO coordDAO = new CoordonneesDAO(DBConnection.getInstance());
		try {
			coordBox.setCoordEmploye(coordDAO.find(employe.getNoEmploye()));
		} catch (DAOFindException e) {
			ErrorDialog err = new ErrorDialog(null, DAOException.TITRE, DAOException.TYPE_FIND, e.getMessage());
			err.setVisible(true);
		}
	}

	private void initComponent(){
		Font fontTitleName = new Font("Serif", Font.BOLD, 48);
		lblTitleName.setFont(fontTitleName);
		
		// InfoBox, ProfilImage and CoordBox
		GroupLayout layout = new GroupLayout(pnlProfil);
		pnlProfil.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addComponent(lblProfilImage)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(infoBox)
						.addComponent(coordBox))
				);

		layout.setVerticalGroup(layout.createParallelGroup()
				.addComponent(lblProfilImage)
				.addGroup(layout.createSequentialGroup()
						.addComponent(infoBox)
						.addComponent(coordBox))
				);
		
		JPanel line1 = new JPanel();
		line1.setLayout(new BoxLayout(line1, BoxLayout.X_AXIS));
		line1.setAlignmentX(LEFT_ALIGNMENT);
		line1.add(lblTitleName);

		JPanel line2 = new JPanel();
		line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
		line2.setAlignmentX(LEFT_ALIGNMENT);
		line2.add(pnlProfil);
		line2.add(qualifBox);
		
		pnlEmployee.setLayout(new BoxLayout(pnlEmployee, BoxLayout.PAGE_AXIS));
		pnlEmployee.add(line1);
		pnlEmployee.add(line2);

		pnlPane.add(rempBox, BorderLayout.CENTER);
		pnlPane.add(scrPnlEmployee, BorderLayout.NORTH);
		
		this.setLayout(new BorderLayout());
		this.add(listEmployesBox, BorderLayout.WEST);
		this.add(pnlPane, BorderLayout.CENTER);
	}
	
	private class FrameResizeListener implements ComponentListener {
		@Override
		public void componentHidden(ComponentEvent e) {
			
		}
		
		@Override
		public void componentMoved(ComponentEvent e) {
			
		}
		
		@Override
		public void componentResized(ComponentEvent e) {   
			Component c = (Component)e.getSource();
			rempBox.getTable().setPreferredScrollableViewportSize(new Dimension(c.getWidth() - 300,
					200));
			rempBox.revalidate();

			// height is the value of the title + highest height of the profil pane
			scrPnlEmployee.setPreferredSize(new Dimension(c.getWidth() - 300, 335));
			scrPnlEmployee.revalidate();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			
		}
	}

	private JPanel pnlPane = new JPanel(new BorderLayout());
	private JPanel pnlProfil = new JPanel();
	private JPanel pnlEmployee = new JPanel();
	private JScrollPane scrPnlEmployee = new JScrollPane(pnlEmployee);
	
	private ListEmployesBox listEmployesBox = new ListEmployesBox();
	private InfoBox infoBox = new InfoBox();
	private QualificationsBox qualifBox = new QualificationsBox();
	private CoordonneesBox coordBox = new CoordonneesBox();
	private RemplacementsBox rempBox = new RemplacementsBox();

	private JLabel lblTitleName = new JLabel();
	private Dimension dimProfilImage = new Dimension(225, 225);
	private ImageIcon iconProfil = IconLoader.createImageIcon("/profil/profil_template.jpg", dimProfilImage);
	private JLabel lblProfilImage = new JLabel(iconProfil);
}