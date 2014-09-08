package utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class JButtonBgImage extends JButton{

	private static final long serialVersionUID = 1L;

	public JButtonBgImage(String path){
		super();
		imgPath = path;
		initComponent();
	}
	
	public JButtonBgImage(String text, String path){
		super();
		this.text = text;
		imgPath = path;
		initComponent();
	}
	
	private void initComponent(){
		Font fontButtons = new Font("Courrier", Font.BOLD + Font.ITALIC, 70);
		this.setFont(fontButtons);
		this.setForeground(Color.red);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon icon = IconLoader.createImageIcon(imgPath, null);
		g.drawImage(icon.getImage(), 0, 0, null);
		int stringLen = (int) g.getFontMetrics().getStringBounds(getText(), g).getWidth();
		g.drawString(text, this.getWidth()/2 - stringLen/2, this.getHeight()/2);
	}
	
	private String imgPath;
	private String text;
}