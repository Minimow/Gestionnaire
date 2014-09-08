package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import utilities.IconLoader;
import utilities.Observer;

public class MainWindow extends JFrame implements Observer{
	
	private static final long serialVersionUID = 1L;
	
	public MainWindow() {	
		this.setTitle("Gestionnaire");
		this.setMinimumSize(new Dimension(1024,768));
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		pnlAdmin.register(this);
		
		initComponent();
	}
	
	private void initComponent(){
		initMenu();
		initButtonsPane();
		
		pnlContainer.setLayout(cardLayout);
		pnlContainer.add(pnlAdmin, "cardHome");
		pnlContainer.add(pnlEmploye, "cardEmploye");
		pnlContainer.add(pnlStatistics, "cardStatistics");
		
		// Default view
		tglEmploye.setSelected(true);
		tglLastClicked = tglEmploye;
		cardLayout.show(pnlContainer, "cardEmploye");		
		
		// We put the container in a scrollPane in case the pane is bigger than the resolution
		JScrollPane scrollPane = new JScrollPane(pnlContainer);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	private void initMenu(){
		// Add action to the menuItem
		menuItemViewHome.addActionListener(new actionShowViewHome());
		menuItemViewEmployes.addActionListener(new actionShowViewEmploye());
		menuItemViewStatistics.addActionListener(new actionShowViewStatistics());
		menuItemClose.addActionListener(new actionClose());
		menuItemManageSession.addActionListener(new actionShowAdminSession());
		menuItemManageQualification.addActionListener(new actionShowAdminQualif());
		menuItemManageRaison.addActionListener(new actionShowAdminRaison());
		menuItemManageStatus.addActionListener(new actionShowAdminStatus());
		

		
		// Add menuItem to the menu
		menuFichier.add(menuItemConnect);
		menuFichier.add(menuItemDisconnect);
		menuFichier.addSeparator();
		menuFichier.add(menuItemReload);
		menuFichier.addSeparator();
		menuFichier.add(menuItemClose);
		
		menuEdition.add(menuItemUndo);
		menuEdition.add(menuItemRedo);
		menuEdition.addSeparator();
		menuEdition.add(menuItemPreferences);
		
		menuTools.add(menuManage);
		menuManage.add(menuItemManageQualification);
		menuManage.add(menuItemManageStatus);
		menuManage.add(menuItemManageSession);
		menuManage.add(menuItemManageRaison);
		
		menuViews.add(menuItemViewHome);
		menuViews.add(menuItemViewEmployes);
		menuViews.add(menuItemViewStatistics);
		
		menuHelp.add(menuItemHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuItemAbout);
		
		
		// Add menu to the menuBar
		menuBar.add(menuFichier);
		menuBar.add(menuEdition);
		menuBar.add(menuViews);
		menuBar.add(menuTools);
		menuBar.add(menuHelp);
		
		this.setJMenuBar(menuBar);
	}
	
	private void initButtonsPane(){	
		Font fontButtons = new Font("Courrier", Font.BOLD + Font.ITALIC, 30);
		Color colorButtons = new Color(50, 100, 255);

		// Add action to the buttons
		tglAdminTools.addActionListener(new actionShowViewHome());
		tglAdminTools.setFont(fontButtons);
		tglAdminTools.setForeground(colorButtons);
		
		tglEmploye.addActionListener(new actionShowViewEmploye());
		tglEmploye.setFont(fontButtons);
		tglEmploye.setForeground(colorButtons);
		
		tglStatistics.addActionListener(new actionShowViewStatistics());
		tglStatistics.setFont(fontButtons);
		tglStatistics.setForeground(colorButtons);
		
		tglAdminTools.setFocusPainted(false);
		tglEmploye.setFocusPainted(false);
		tglStatistics.setFocusPainted(false);
		
		// Add items to the buttonsPane
		pnlPanes.setLayout(new GridLayout(1,3));
		pnlPanes.add(tglEmploye);
		pnlPanes.add(tglStatistics);
		pnlPanes.add(tglAdminTools);
		
		this.add(pnlPanes, BorderLayout.NORTH);
	}
	
	private class actionShowAdminSession extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			pnlAdmin.showAdminSession();
	    }
	}
	
	private class actionShowAdminRaison extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			pnlAdmin.showAdminRaison();
	    }
	}
	
	private class actionShowAdminQualif extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			pnlAdmin.showAdminQualif();
	    }
	}
	
	private class actionShowAdminStatus extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			pnlAdmin.showAdminStatus();
	    }
	}
	
	private class actionShowViewHome extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			cardLayout.show(pnlContainer, "cardHome");
			tglLastClicked.setSelected(false);
			tglLastClicked = tglAdminTools;
			tglAdminTools.setSelected(true);
	    }
	}
	
	private class actionShowViewEmploye extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			cardLayout.show(pnlContainer, "cardEmploye");
			tglLastClicked.setSelected(false);
			tglLastClicked = tglEmploye;
			tglEmploye.setSelected(true);
		}
	}

	private class actionShowViewStatistics extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			cardLayout.show(pnlContainer, "cardStatistics");
			tglLastClicked.setSelected(false);
			tglLastClicked = tglStatistics;
			tglStatistics.setSelected(true);
		}
	}
	
	private class actionClose extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {			
			int option = JOptionPane.showConfirmDialog(null, "Êtes-vous sur de vouloir quitter l'application ?",
					"Fermeture de l'application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						
			if(option == JOptionPane.OK_OPTION){
			  dispose();		
			}
		}
	}
	
	@Override
	public void update(){
		pnlEmploye.getListBox().getOptionBox().refreshQualif();
		pnlEmploye.getListBox().getOptionBox().refreshStatus();
		pnlEmploye.update();
		pnlEmploye.getListBox().update();
	}
			
     
	private JPanel pnlContainer = new JPanel();
	private EmployeesPane pnlEmploye = new EmployeesPane();
	private AdminPane pnlAdmin = new AdminPane();
	private StatsPane pnlStatistics = new StatsPane();
	private CardLayout cardLayout = new CardLayout();
	
	// Dimension and ImageIcon for the menu
	private Dimension dimMenuIcon = new Dimension(16,16);
	private ImageIcon iconMenuHome = IconLoader.createImageIcon("/menu/menu_accueil_icone.png", dimMenuIcon);
	private ImageIcon iconMenuEmploye = IconLoader.createImageIcon("/menu/menu_employes_icone.png", dimMenuIcon);
	private ImageIcon iconMenuStatistics = IconLoader.createImageIcon("/menu/menu_stats_icone.png", dimMenuIcon);
	private ImageIcon iconMenuConnect = IconLoader.createImageIcon("/menu/menu_connexion_icone.png", dimMenuIcon);
	private ImageIcon iconMenuDisconnect = IconLoader.createImageIcon("/menu/menu_deconnexion_icone.png", dimMenuIcon);
	private ImageIcon iconMenuReload = IconLoader.createImageIcon("/menu/menu_redemarrer_icone.png", dimMenuIcon);
	private ImageIcon iconMenuClose = IconLoader.createImageIcon("/menu/menu_fermer_icone.png", dimMenuIcon);
	private ImageIcon iconMenuUndo = IconLoader.createImageIcon("/menu/menu_undo_icone.png", dimMenuIcon);
	private ImageIcon iconMenuRedo = IconLoader.createImageIcon("/menu/menu_redo_icone.png", dimMenuIcon);
	private ImageIcon iconMenuHelp = IconLoader.createImageIcon("/menu/menu_aide_icone.png", dimMenuIcon);
	
	// Dimension and ImageIcon for the toolbar
	private Dimension dimToolbarIcon = new Dimension(48,48);
	private ImageIcon iconToolbarHome = IconLoader.createImageIcon("/menu/menu_accueil_icone.png", dimToolbarIcon);
	private ImageIcon iconToolbarEmploye = IconLoader.createImageIcon("/menu/menu_employes_icone.png", dimToolbarIcon);
	private ImageIcon iconToolbarStatistics = IconLoader.createImageIcon("/menu/menu_stats_icone.png", dimToolbarIcon);
	
	// Items for the menu
	private JMenuItem menuItemConnect = new JMenuItem("Connexion", iconMenuConnect);
	private JMenuItem menuItemDisconnect = new JMenuItem("Déconnexion", iconMenuDisconnect);
	private JMenuItem menuItemReload = new JMenuItem("Redémarrer", iconMenuReload);
	private JMenuItem menuItemClose = new JMenuItem("Fermer", iconMenuClose);
	
	private JMenuItem menuItemUndo = new JMenuItem("Annuler opération", iconMenuUndo);
	private JMenuItem menuItemRedo = new JMenuItem("Refaire dernière opération", iconMenuRedo);
	private JMenuItem menuItemPreferences = new JMenuItem("Preferences");
	
	private JMenuItem menuItemManageQualification = new JMenuItem("Qualifications");
	private JMenuItem menuItemManageStatus = new JMenuItem("Statuts");
	private JMenuItem menuItemManageSession = new JMenuItem("Session");
	private JMenuItem menuItemManageRaison = new JMenuItem("Raison");
	
	private JMenuItem menuItemViewHome = new JMenuItem("Accueil", iconMenuHome);
	private JMenuItem menuItemViewEmployes = new JMenuItem("Employés", iconMenuEmploye);
	private JMenuItem menuItemViewStatistics = new JMenuItem("Statistiques", iconMenuStatistics);
	
	private JMenuItem menuItemHelp = new JMenuItem("Aide", iconMenuHelp);
	private JMenuItem menuItemAbout = new JMenuItem("À propos");
	
	// Menus of the menuBar
	private JMenu menuFichier = new JMenu("Fichier");
	private JMenu menuEdition = new JMenu("Edition");
	private JMenu menuViews = new JMenu("Vues");
	private JMenu menuTools = new JMenu("Outils");
	private JMenu menuManage = new JMenu("Gérer");
	private JMenu menuHelp = new JMenu("?");
	
	// JToggleButton of the toolbar
	private JToggleButton tglAdminTools = new JToggleButton("Outils d'administration", iconToolbarHome);
	private JToggleButton tglEmploye = new JToggleButton("Employés", iconToolbarEmploye);
	private JToggleButton tglStatistics = new JToggleButton("Statistiques", iconToolbarStatistics);
	private JToggleButton tglLastClicked = new JToggleButton();
	
	// Menu and ToolBar
	private JMenuBar menuBar = new JMenuBar();
	private JPanel pnlPanes = new JPanel();
}
