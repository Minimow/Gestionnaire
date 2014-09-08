package utilitiesBound;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import utilities.IconLoader;
import utilities.Observer;
import utilities.Subject;

public class ComboDropDown extends JPanel implements Subject{
	private ClickAwayDialog dialog;
	private DefaultListModel<JCheckBox> model;// = new DefaultListModel<JCheckBox>();
	private JList<JCheckBox> list;// = new JList<JCheckBox>(model);
	private JPanel panel;
	private int curOverIndex = -1;
	private JTextField lblNom = new JTextField();
	private JToggleButton btnDown = new JToggleButton();
	private JCheckBox chkAll = new JCheckBox("Tout sélectionner");
	private JCheckBox chkSeparator = new JCheckBox("SEPARATOR");
	private String nom;
    JScrollPane scroll = new JScrollPane(list);
   
	// Variables pour le patron observer
    private boolean changed;
    private final Object MUTEX= new Object();
    private List<Observer> observers = new ArrayList<Observer>();
	
	public ComboDropDown(String nom, DefaultListModel<JCheckBox> model, JPanel pane){
		this.model = model;
		this.panel = pane;
		this.nom = nom;
		this.list = new JList<JCheckBox>(this.model);
		
		initComponent();
	}
	
	private void initComponent(){
		model.insertElementAt(chkAll, 0);
		model.insertElementAt(chkSeparator, 1);
		
		MouseAdapt mouseAdapt = new MouseAdapt();
	    list.setBorder(BorderFactory.createLineBorder(Color.black));
		list.setSelectionModel(new ListSelectionModel());
		list.setCellRenderer(new CheckListRenderer());
		list.addMouseListener(mouseAdapt);
		list.addMouseMotionListener(mouseAdapt);

		CompoundBorder border = BorderFactory.createCompoundBorder(
		          BorderFactory.createMatteBorder(1, 1, 1, 0, new Color(122, 138, 153)),
		          BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(184, 207, 229))
		          );
		lblNom.setText(nom);
		lblNom.setBorder(border);
		lblNom.setEditable(false);
		lblNom.setHorizontalAlignment(JLabel.CENTER);
		lblNom.setForeground(UIManager.getColor("Label.foreground"));               
		lblNom.setFont(UIManager.getFont("Label.font"));
		lblNom.setPreferredSize(new Dimension(lblNom.getPreferredSize().width + 10, 25));
		lblNom.addMouseListener(new MouseAdaptText());
		
		btnDown.setPreferredSize(new Dimension(20,25));
		btnDown.setIcon(IconLoader.createImageIcon("/downarrow.png", new Dimension(10,10)));
		btnDown.setFocusPainted(false);
		btnDown.addActionListener(actionAfficherList);
		
		// Remove spaces between components
		this.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		this.add(lblNom);
		this.add(btnDown);
 
        scroll = new JScrollPane(list);
		Border emptyBorder = BorderFactory.createEmptyBorder(0,0,0,0);
        scroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
        scroll.setViewportBorder(emptyBorder);
        
		int visibleRow = 10;
		if(model.getSize() < visibleRow){
			list.setVisibleRowCount(model.getSize() - 1);
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		}
		else{
			list.setVisibleRowCount(visibleRow);
	        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.black));
		}
        
		JFrame f = (JFrame)this.getTopLevelAncestor();
		dialog = new ClickAwayDialog(f);  
        dialog.setUndecorated(true); 
        dialog.getContentPane().add(scroll);  
        dialog.pack();
	}
	
	public DefaultListModel<JCheckBox> getListModel(){
		model.removeElement(chkAll);
		model.removeElement(chkSeparator);
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

    private void afficherList(){
    	btnDown.setSelected(true);
        Point p = this.getLocation();
        SwingUtilities.convertPointToScreen(p, panel);
        dialog.setLocation(p.x, (p.y + this.getHeight()));
        dialog.pack();
        dialog.setVisible(true); 
    }
    
	private AbstractAction actionAfficherList = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
        	afficherList();
        }
    };
	
    private class MouseAdapt extends MouseAdapter {  
		@Override
		public void mouseClicked(MouseEvent e) {  
			int index = list.locationToIndex(e.getPoint());  
			JCheckBox item = (JCheckBox)list.getModel().getElementAt(index);   
			
			if(index == 0){
				boolean selected = false;
				if(!item.isSelected()){
					selected = true;
				}
				for(int i = 0; i < list.getModel().getSize(); i++){
					list.getModel().getElementAt(i).setSelected(selected);
				}
				list.repaint();
			}
			else{
				item.setSelected(! item.isSelected());  
				Rectangle rect = list.getCellBounds(index, index); 
				list.getModel().getElementAt(0).setSelected(false);
				Rectangle rect2 = list.getCellBounds(0, 0);
				list.repaint(rect2);
				list.repaint(rect);
			}
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
    
    private class MouseAdaptText extends MouseAdapter {  
		@Override
		public void mouseClicked(MouseEvent e) {  
        	afficherList();
		}  
	} 
	@SuppressWarnings("rawtypes")
	private class CheckListRenderer extends JCheckBox implements ListCellRenderer { 
		   
		final String SEPARATOR = "SEPARATOR";
	    public CheckListRenderer() {  
	      setBackground(UIManager.getColor("List.textBackground"));  
	      setForeground(UIManager.getColor("List.textForeground")); 
	    }  

	    public Component getListCellRendererComponent( JList list, Object value,  
	    		int index, boolean isSelected, boolean hasFocus) { 
	    	
	    	String str = ((JCheckBox)value).getText();
	    	if(SEPARATOR.equals(str)){
	    		return new JSeparator(JSeparator.HORIZONTAL);
	    	}
	    	
	    	
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
					btnDown.setSelected(false);
				}
			});
		}
	}
	
	private class ListSelectionModel extends DefaultListSelectionModel {
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

	}
}