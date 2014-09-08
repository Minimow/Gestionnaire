package utilities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Calendar;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/*
 * Create 2 JComboBox to define a time with thet following format HH MM.
 * You can use the getTime method to return the time wrapped into the Calendar
 * class.
 */
public class TimeComboBox extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * Default constructor.
	 */
	public TimeComboBox(){
		initComponent();
		Calendar curDate = Calendar.getInstance();
		int heure = curDate.get(Calendar.HOUR_OF_DAY);
		int min = curDate.get(Calendar.MINUTE);
		min = (int) Math.ceil(min / 5d) * 5;
		cmbHeure.setSelectedItem(String.format("%02d", heure));
		cmbMin.setSelectedItem(String.format("%02d", min));
	}
	
	/*
	 * Set the time to the current time
	 */
	public void setToCurrentTime(){
		Calendar curDate = Calendar.getInstance();
		int heure = curDate.get(Calendar.HOUR_OF_DAY);
		int min = curDate.get(Calendar.MINUTE);
		min = (int) Math.ceil(min / 5d) * 5;
		cmbHeure.setSelectedItem(String.format("%02d", heure));
		cmbMin.setSelectedItem(String.format("%02d", min));
		resetModifiedColor();
	}
	
	/*
	 * Return the time, as shown by the 2 JComboBox, as a Calendar object.
	 */
	public Calendar getTime(){
		Calendar date = Calendar.getInstance();
		date.setLenient(false);
		date.set(Calendar.HOUR_OF_DAY, Integer.parseInt((String)cmbHeure.getSelectedItem()));
		date.set(Calendar.MINUTE, Integer.parseInt((String)cmbMin.getSelectedItem()));
		return date;
	}
	
	/*
	 * Set the 2 JComboBox to fit the time passed by reference
	 */
	public void setTime(Calendar date){
		cmbHeure.setSelectedItem(String.format("%02d", date.get(Calendar.HOUR_OF_DAY)));
		cmbMin.setSelectedItem(String.format("%02d", date.get(Calendar.MINUTE)));
		resetModifiedColor();
	}
	
	public void setModifiedColor(Color color) {
		this.colorModified = color;
	}

	public Color getModifiedColor() {
		return this.colorModified;
	}

	public void setNotModifiedColor(Color color) {
		this.colorNotModified = color;
	}

	public Color getNotModifiedColor() {
		return this.colorNotModified;
	}

	/*
	 * Reset the foreground to the default color.
	 */
	public void resetModifiedColor() {
		cmbHeure.setForeground(colorNotModified);
		cmbMin.setForeground(colorNotModified);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean) In case you want to
	 * disable the component, every sub-components are also disabled,
	 */
	@Override
	public void setEnabled(boolean enable){
		cmbHeure.setEnabled(enable);
		cmbMin.setEnabled(enable);
	}
	
	/*
	 * Initialize the model of the 2 JComboBox.
	 */
	private void initComponent(){
		cmbHeure = new JComboBox<String>(heureModel);
		cmbHeure.addActionListener(actionTimeModified);
		for(int i = 0; i < 24; i++){
			heureModel.addElement(String.format("%02d", i));
		}
		
		cmbMin = new JComboBox<String>(minModel);
		cmbMin.addActionListener(actionTimeModified);
		for(int i = 0; i < 60; i+=5){
			minModel.addElement(String.format("%02d", i));
		}
		
		this.add(cmbHeure);
		this.add(cmbMin);

	}
	
	/*
	 * If the user use one of the JComboBox, its foreground changes to the
	 * modifiedColor to indicate that it may not be the dafault value.
	 */
	private AbstractAction actionTimeModified = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		public void actionPerformed(ActionEvent e) {
			((JComboBox) e.getSource()).setForeground(colorModified);
		}
	};
	
	// The 2 JComboBox
	private JComboBox<String> cmbHeure;
	private JComboBox<String> cmbMin;
	
	// The 2 model of the JComboBox
	private DefaultComboBoxModel<String> heureModel = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> minModel = new DefaultComboBoxModel<String>();
	
	// The color of the foreground
	private Color colorModified = new Color(0, 0, 255);
	private Color colorNotModified = new Color(0, 0, 0);
}