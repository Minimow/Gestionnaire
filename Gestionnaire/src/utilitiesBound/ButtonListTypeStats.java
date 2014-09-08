package utilitiesBound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class ButtonListTypeStats extends JToggleButton{

	private static final long serialVersionUID = 1L;
	Color pressedBackgroundColor = new Color(35, 35, 135);
    Color pressedBackgroundColor2 = new Color(50, 100, 255);
    Color backgroundColor = new Color(50,150,255);
    Color backgroundColor2 = new Color(50,255,255);
	public ButtonListTypeStats(String texte) {
		super(texte);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setBackground(new Color(50, 200, 100));
		setBorder(BorderFactory.createEtchedBorder());
		setContentAreaFilled(false);
		setFocusPainted(false);
		setForeground(Color.black);
		Font fontButtons = new Font("Courrier", Font.BOLD + Font.ITALIC, 30);
		setFont(fontButtons);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g; 
        if (isSelected()) {
        	GradientPaint gp = new GradientPaint(0, 0, pressedBackgroundColor,
    	    		0, getHeight(), pressedBackgroundColor2, true);
    	    g2d.setPaint(gp);
        }
            else {
            	GradientPaint gp = new GradientPaint(0, 0, backgroundColor,
        	    		0, getHeight(), backgroundColor2, true);
        	    g2d.setPaint(gp);
        }        
	                    
	    g2d.fillRect(0, 0, this.getWidth(), this.getHeight()); 
        super.paintComponent(g);
    }
}
