package utilities;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/*
 * Create 3 JComboBox to define a date with the following format DD MM YYYY.
 * You can use the getDate method to return the date wrapped into the Calendar
 * class.
 */
public class DateComboBox extends JPanel {

	private static final long serialVersionUID = 1L;

	/*
	 * Default constructor, the interval varies from 2013 to 2017.
	 */
	public DateComboBox() {
		initComponent(2013, 2017);
		setToTodaysDate();
	}

	/*
	 * Constructor where the date interval is determined by the parameters.
	 */
	public DateComboBox(int minYear, int maxYear) {
		initComponent(minYear, maxYear);
		setToTodaysDate();
	}

	/*
	 * Set the date to the current date
	 */
	public void setToTodaysDate() {
		Calendar curDate = Calendar.getInstance();
		cmbJours.setSelectedItem(curDate.get(Calendar.DAY_OF_MONTH));
		cmbMois.setSelectedItem(fromInt(curDate.get(Calendar.MONTH)));
		cmbAnnee.setSelectedItem(curDate.get(Calendar.YEAR));
		resetModifiedColor();
	}

	/*
	 * Return the date, as shown by the 3 JComboBox, as a Calendar object.
	 */
	public Calendar getDate() {
		Calendar date = Calendar.getInstance();
		date.setLenient(false);
		date.set((int) cmbAnnee.getSelectedItem(),
				((Mois) cmbMois.getSelectedItem()).value,
				(int) cmbJours.getSelectedItem());
		return date;
	}

	/*
	 * Set the 3 JComboBox to fit the date passed by reference
	 */
	public void setDate(Calendar date) {
		cmbJours.setSelectedItem(date.get(Calendar.DAY_OF_MONTH));
		cmbMois.setSelectedItem(fromInt(date.get(Calendar.MONTH)));
		cmbAnnee.setSelectedItem(date.get(Calendar.YEAR));
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
		cmbJours.setForeground(colorNotModified);
		cmbMois.setForeground(colorNotModified);
		cmbAnnee.setForeground(colorNotModified);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean) In case you want to
	 * disable the component, every sub-components are also disabled,
	 */
	@Override
	public void setEnabled(boolean enable) {
		super.setEnabled(enable);
		cmbJours.setEnabled(enable);
		cmbMois.setEnabled(enable);
		cmbAnnee.setEnabled(enable);
	}

	/*
	 * Initialize the model of the 3 JComboBox.
	 */
	private void initComponent(int minAnnee, int maxAnnee) {
		cmbJours = new JComboBox<Integer>(joursModel);
		cmbJours.addActionListener(actionDateModified);
		for (int i = 1; i < 32; i++) {
			joursModel.addElement(i);
		}

		cmbMois = new JComboBox<Mois>(moisModel);
		cmbMois.addActionListener(ajusterMaxJours);
		cmbMois.addActionListener(actionDateModified);

		for (int i = minAnnee; i < maxAnnee; i++) {
			anneeModel.addElement(i);
		}
		cmbAnnee = new JComboBox<Integer>(anneeModel);
		cmbAnnee.addActionListener(ajusterMaxJours);
		cmbAnnee.addActionListener(actionDateModified);

		this.add(cmbJours);
		this.add(cmbMois);
		this.add(cmbAnnee);
	}

	/*
	 * Action that is fired when the month is changed. For example if the day
	 * was at 31 and the newly month selected is April, then the day JComboBox
	 * will automatically change its value to 30.
	 */
	private AbstractAction ajusterMaxJours = new AbstractAction() {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			int joursSelect = (int) cmbJours.getSelectedItem();
			int mois = ((Mois) cmbMois.getSelectedItem()).value;
			int annee = (int) cmbAnnee.getSelectedItem();
			Calendar date = Calendar.getInstance();
			date.set(annee, mois, 1);
			int maxJours = date.getActualMaximum(Calendar.DAY_OF_MONTH);

			// Variable that store the last day value and color
			int oldDay = (int) cmbJours.getSelectedItem();
			Color oldColor = cmbJours.getForeground();

			joursModel.removeAllElements();
			for (int i = 1; i <= maxJours; i++) {
				joursModel.addElement(i);
			}
			if (joursSelect <= maxJours) {
				cmbJours.setSelectedItem(joursSelect);
			} else {
				cmbJours.setSelectedItem(maxJours);
			}

			// Adjust the foreground if the value has changed depending on the
			// month
			// If it didn't change then we set it to its last value
			if (oldDay == (int) cmbJours.getSelectedItem()) {
				cmbJours.setForeground(oldColor);
			} else {
				cmbJours.setForeground(colorModified);
			}
		}
	};

	/*
	 * If the user use one of the JComboBox, its foreground changes to the
	 * modifiedColor to indicate that it may not be the dafault value.
	 */
	private AbstractAction actionDateModified = new AbstractAction() {

		private static final long serialVersionUID = 1L;

		@SuppressWarnings("rawtypes")
		public void actionPerformed(ActionEvent e) {
			((JComboBox) e.getSource()).setForeground(colorModified);
		}
	};

	/*
	 * Enum with every month. It is used for the model of the month JComboBox.
	 */
	private enum Mois {
		JANVIER(0), FEVRIER(1), MARS(2), AVRIL(3), MAI(4), JUIN(5), JUILLET(6), AOUT(
				7), SEPTEMBRE(8), OCTOBRE(9), NOVEMBRE(10), DECEMBRE(11);

		private final int value;

		private Mois(int value) {
			this.value = value;
		}
	}

	private static final Map<Integer, Mois> intToTypeMap = new HashMap<Integer, Mois>();
	static {
		for (Mois type : Mois.values()) {
			intToTypeMap.put(type.value, type);
		}
	}

	private static Mois fromInt(int i) {
		Mois type = intToTypeMap.get(Integer.valueOf(i));
		if (type == null)
			return Mois.JANVIER;
		return type;
	}

	// The 3 JComboBox
	private JComboBox<Integer> cmbJours;
	private JComboBox<Integer> cmbAnnee;
	private JComboBox<Mois> cmbMois;
	
	// The 3 model of the JComboBox
	private DefaultComboBoxModel<Mois> moisModel = new DefaultComboBoxModel<Mois>(
			Mois.values());
	private DefaultComboBoxModel<Integer> joursModel = new DefaultComboBoxModel<Integer>();
	private DefaultComboBoxModel<Integer> anneeModel = new DefaultComboBoxModel<Integer>();

	// The color of the foreground
	private Color colorModified = new Color(0, 0, 255);
	private Color colorNotModified = new Color(0, 0, 0);
}