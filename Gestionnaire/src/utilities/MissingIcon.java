package utilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

/**
 * The "missing icon" is a white box with a black border and a red x. It's used
 * to display something when there are issues loading an icon from an external
 * location.
 * 
 * @author Collin Fagan & Julien Bergeron
 */
public class MissingIcon extends ImageIcon {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int icon_width;
	private int icon_height;

	private BasicStroke stroke = new BasicStroke(4);

	public MissingIcon(int width, int height){
		super();
		icon_width = width;
		icon_height = height;
	}
	
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		// Contour de l'icone
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x + 1, y + 1, icon_width - 2, icon_height - 2);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(x + 1, y + 1, icon_width - 2, icon_height - 2);
		
		// le X au milieu
		g2d.setColor(Color.WHITE);
		g2d.fillRect(icon_width/4 + 1, icon_height/4 + 1, icon_width/2 - 2, icon_height/2 - 2);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(icon_width/4 + 1, icon_height/4 + 1, icon_width/2 - 2, icon_height/2 - 2);

		g2d.setColor(Color.RED);

		g2d.setStroke(stroke);
		g2d.drawLine(icon_width/4 + 10, icon_height/4 + 10, 3*icon_width/4 - 10, 3*icon_height/4 - 10);// \
		g2d.drawLine(icon_width/4 + 10, 3*icon_height/4 - 10, 3*icon_width/4 - 10, icon_height/4 + 10);// /

		g2d.dispose();
	}

	public int getIconWidth() {
		return icon_width;
	}

	public int getIconHeight() {
		return icon_height;
	}
}