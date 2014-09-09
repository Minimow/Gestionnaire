package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import utilities.IconLoader;

public class LoginPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private Image _imgBackground;
	
	LoginPanel() {
		_imgBackground = IconLoader.createImageIcon("/loginLogo.jpg", null).getImage();
		setBackground(Color.WHITE);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(_imgBackground, 0, 0, null);
	}
}
