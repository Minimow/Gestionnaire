package utilitiesBound;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import utilities.Observer;
import utilities.Subject;

public class ToggleButtonSubject extends JToggleButton implements Subject{

	private static final long serialVersionUID = 1L;
	
	public ToggleButtonSubject(){
		super();
		this.addActionListener(actionClick);
	}
	
	public ToggleButtonSubject(String text){
		super(text);
		this.addActionListener(actionClick);
	}
	
	public ToggleButtonSubject(ImageIcon icon){
		super(icon);
		this.addActionListener(actionClick);
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
	public Boolean getUpdate(Observer obj) {
		return this.isSelected();
	}
	
	private AbstractAction actionClick = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent ae) {
			changed = true;
			notifyObservers();
		}
	};
	
	// Variables pour le patron Observer
	private boolean changed;
	private final Object mutex = new Object();
	private List<Observer> observers = new ArrayList<Observer>();
}