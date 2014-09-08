package utilities;

import java.awt.Graphics;

import javax.swing.JFormattedTextField;

/*
 * A JFormattedTextField wrapper to use when you want to use color with an
 * alpha value.
 * 
 * @Author Julien Bergeron
 */
public class AlphaFormattedTextField extends JFormattedTextField {

	private static final long serialVersionUID = 1L;

	public AlphaFormattedTextField() {
		super();
		this.setOpaque(false);
	}

	public AlphaFormattedTextField(AbstractFormatter formatter) {
		super(formatter);
		this.setOpaque(false);
	}
	
	public AlphaFormattedTextField(AbstractFormatter formatter, int columns) {
		super(formatter);
		this.setColumns(columns);
		this.setOpaque(false);
	}

	/**
	 * Paint the background using the background Color of the contained
	 * component
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}
}
