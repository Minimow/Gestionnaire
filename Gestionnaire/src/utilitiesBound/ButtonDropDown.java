package utilitiesBound;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import utilities.Observer;
import utilities.Subject;

@SuppressWarnings("serial")
public class ButtonDropDown extends JToggleButton implements Subject{
	private ClickAwayDialog dialog;
	private DefaultListModel<JCheckBox> model;// = new DefaultListModel<JCheckBox>();
	private JList<JCheckBox> list;// = new JList<JCheckBox>(model);
	private JPanel panel;
	private JToggleButton thisButton = this;
	private int curOverIndex = -1;
    // Variables pour le patron observer
    private boolean changed;
    private final Object MUTEX= new Object();
    private List<Observer> observers = new ArrayList<Observer>();
	
	@SuppressWarnings("unchecked")
	public ButtonDropDown(String text, DefaultListModel<JCheckBox> model, JPanel pane){
		super(text);
		this.model = model;
		this.panel = pane;
		this.list = new JList<JCheckBox>(this.model);
		this.setFocusPainted(false);
		
	    list.setBorder(BorderFactory.createLineBorder(Color.black));
		list.setSelectionModel(new DefaultListSelectionModel() {
		    boolean gestureStarted = false;

		    @Override
		    public void setSelectionInterval(int index0, int index1) {
		        if(!gestureStarted){
		            if (isSelectedIndex(index0)) {
		                super.removeSelectionInterval(index0, index1);
		            } else {
		                super.addSelectionInterval(index0, index1);
		            }
		        }
		        gestureStarted = true;
		    }

		    @Override
		    public void setValueIsAdjusting(boolean isAdjusting) {
		        if (isAdjusting == false) {
		            gestureStarted = false;
		        }
		    }

		});
		list.setCellRenderer(new CheckListRenderer());
		/*list.addMouseListener(new MouseAdapter() {  
			@Override
			public void mouseClicked(MouseEvent e) {  
				int index = list.locationToIndex(e.getPoint());  
				JCheckBox item = (JCheckBox)list.getModel().getElementAt(index);  
				item.setSelected(! item.isSelected());  
				Rectangle rect = list.getCellBounds(index, index);  
				list.repaint(rect);
				changed = true;
				notifyObservers();
			}  
			@Override
			public void mouseMoved(MouseEvent e) {
				int index = list.locationToIndex(e.getPoint());
				System.out.println(index);
				JCheckBox item = (JCheckBox)list.getModel().getElementAt(index);
				item.setSelected(! item.isSelected());
				Rectangle rect = list.getCellBounds(index, index);  
				list.repaint(rect);
			}
		}); */
		MouseAdapt mouseAdapt = new MouseAdapt();
		list.addMouseListener(mouseAdapt);
		list.addMouseMotionListener(mouseAdapt);

        this.addActionListener(afficherList);
		JFrame f = (JFrame)this.getTopLevelAncestor();
		dialog = new ClickAwayDialog(f);  
        dialog.setUndecorated(true);  
        dialog.getContentPane().add(list);  
        dialog.pack();
	}
	
	public DefaultListModel<JCheckBox> getListModel(){
		return model;
	}
	
	public void setListModel(DefaultListModel<JCheckBox> listModel){
		model = listModel;
		list.setModel(model);
		
	}
	
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
    public DefaultListModel<JCheckBox> getUpdate(Observer obj) {
    		return this.model;
    }

	private AbstractAction afficherList = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
        	ButtonDropDown button = (ButtonDropDown)e.getSource();  
            Point p = button.getLocation();  
            //JFrame f = (JFrame)button.getTopLevelAncestor();  
            SwingUtilities.convertPointToScreen(p, panel); 
            dialog.setLocation(p.x, p.y+(button.getHeight()));
            dialog.pack();
            dialog.setVisible(true); 
            
        }
    };
	
    private class MouseAdapt extends MouseAdapter {  
		@Override
		public void mouseClicked(MouseEvent e) {  
			int index = list.locationToIndex(e.getPoint());  
			JCheckBox item = (JCheckBox)list.getModel().getElementAt(index);  
			item.setSelected(! item.isSelected());  
			Rectangle rect = list.getCellBounds(index, index);  
			list.repaint(rect);
			changed = true;
			notifyObservers();
		}  
		@Override
		public void mouseMoved(MouseEvent e) {
			int index = list.locationToIndex(e.getPoint());
			curOverIndex = index;
			list.repaint();
		}
	} 
    
	@SuppressWarnings("rawtypes")
	private class CheckListRenderer extends JCheckBox implements ListCellRenderer { 
		   
	    public CheckListRenderer() {  
	      setBackground(UIManager.getColor("List.textBackground"));  
	      setForeground(UIManager.getColor("List.textForeground"));  
	    }  

	    public Component getListCellRendererComponent( JList list, Object value,  
	    		int index, boolean isSelected, boolean hasFocus) {  
			boolean isMouseOver = false;
	    	if(curOverIndex != -1){
	    		isMouseOver = ((JCheckBox)value) == ((JCheckBox)list.getModel().getElementAt(curOverIndex));
	    	}
	    	if (((JCheckBox)value).isSelected() || isMouseOver) {  
	    		setBackground(list.getSelectionBackground());  
	    		setForeground(list.getSelectionForeground());  
	    	} else {  
	    		setBackground(list.getBackground());  
	    		setForeground(list.getForeground());
	    	}

	    	setEnabled(list.isEnabled());  
	    	setSelected(((JCheckBox)value).isSelected());  
	    	setFont(list.getFont());  
	    	setText(((JCheckBox)value).getText());  
	    	return this;  
	    }  
} 
	
	private class ClickAwayDialog extends JDialog {

		public ClickAwayDialog(final JFrame owner) {
			super(owner);

			this.addWindowFocusListener(new WindowFocusListener() {

				public void windowGainedFocus(WindowEvent e) {
					//do nothing
				}

				public void windowLostFocus(WindowEvent e) {
					// Si on clique en dehors du programme
					if(e.getOppositeWindow() == null){
						return;
					}
					// Si on clique sur un sous widget du dialog
					if (SwingUtilities.isDescendingFrom(e.getOppositeWindow(), ClickAwayDialog.this)) {
						return;
					}
					ClickAwayDialog.this.setVisible(false);
					thisButton.setSelected(false);
				}

			});
		}
	}
}