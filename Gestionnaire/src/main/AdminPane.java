package main;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import utilities.JButtonBgImage;
import utilities.Observer;
import utilities.Subject;
import dialogs.AdminQualification;
import dialogs.AdminRaison;
import dialogs.AdminSession;
import dialogs.AdminStatut;

public class AdminPane extends JPanel implements Observer, Subject{
	
	private static final long serialVersionUID = 1L;

	public AdminPane(){
		initComponent();
		btnAdminSession.addActionListener(actionShowAdminSession);
		btnAdminRaison.addActionListener(actionShowAdminRaison);
		btnAdminStatut.addActionListener(actionShowAdminStatut);
		btnAdminQualif.addActionListener(actionShowAdminQualif);
		
		adminQualif.register(this);
		adminSession.register(this);
		adminRaison.register(this);
		adminStatut.register(this);
	}
	
	private void initComponent(){
		this.setLayout(new BorderLayout());
		this.add(pnlMenu, BorderLayout.CENTER);

		pnlMenu.add(btnAdminQualif);
		pnlMenu.add(btnAdminSession);
		pnlMenu.add(btnAdminStatut);
		pnlMenu.add(btnAdminRaison);
	}
	
	private AbstractAction actionShowAdminSession = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			showAdminSession();
		}
	};
	
	private AbstractAction actionShowAdminRaison = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			showAdminRaison();
		}
	};
	
	private AbstractAction actionShowAdminStatut = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			showAdminStatus();
		}
	};
	
	private AbstractAction actionShowAdminQualif = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			showAdminQualif();
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
	
	/*
	 * Notify the mainWindow that some changes has been made so it can notify EmployeesPane.
	 */
	@Override
	public void update() {
		changed = true;
		notifyObservers();
		
	}
	
	public void showAdminSession(){
		adminSession.setVisible(true);
	}
	
	public void showAdminRaison(){
		adminRaison.setVisible(true);
	}
	
	public void showAdminQualif(){
		adminQualif.setVisible(true);
	}
	
	public void showAdminStatus(){
		adminStatut.setVisible(true);
	}
	
	/*public AdminSession getAdminSession(){
		return adminSession;
	}
	
	public AdminRaison getAdminRaison(){
		return adminRaison;
	}
	
	public AdminQualification getAdminQualif(){
		return adminQualif;
	}
	
	public AdminStatut getAdminStatus(){
		return adminStatut;
	}*/
	
	private JButtonBgImage btnAdminSession = new JButtonBgImage("Sessions", "/sessionIcon.png");
	private JButtonBgImage btnAdminStatut = new JButtonBgImage("Statuts", "/statutIcon.png");
	private JButtonBgImage btnAdminRaison = new JButtonBgImage("Raisons", "/raisonIcon.png");
	private JButtonBgImage btnAdminQualif = new JButtonBgImage("Qualifications", "/qualifIcon.png");
	private AdminSession adminSession = new AdminSession();
	private AdminRaison adminRaison = new AdminRaison();
	private AdminStatut adminStatut = new AdminStatut();
	private AdminQualification adminQualif = new AdminQualification();
	private JPanel pnlMenu = new JPanel(new GridLayout(2,2));
	
	// Variables pour le patron observer
    private boolean changed;
    private final Object MUTEX= new Object();
    private List<Observer> observers = new ArrayList<Observer>();
}
