package utilities;

import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.UIManager;

/*
 * JTextField that imitates a JLabel. Use it when you want your JLabel to be
 * selectable (ie : to use copy/paste).
 */
public class JLabelSelectable extends JTextField {

	private static final long serialVersionUID = 1L;

	public JLabelSelectable() {
		super();
		setLookJLabel();
	}

	public JLabelSelectable(String text) {
		super(text);
		setLookJLabel();
	}

	private void setLookJLabel() {
		this.setOpaque(false);
		this.setEditable(false);
		this.setBorder(null);
		this.setForeground(UIManager.getColor("Label.foreground"));
		this.setFont(UIManager.getFont("Label.font"));
		this.setCaretPosition(0); // To avoid the little jumping when selected
		this.setMaximumSize(new Dimension(20, 20)); // To scale the width to
													// match the text length
	}
}