package utilities;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class IconLoader {

	public static ImageIcon createImageIcon(String path, Dimension size) {
		URL imgURL = IconLoader.class.getResource(path);
		if (imgURL != null) {
			if(size != null){
				return new ImageIcon(new ImageIcon(imgURL).getImage()
						.getScaledInstance(size.width, size.height,
								Image.SCALE_SMOOTH));
			}
			else{
				return new ImageIcon(imgURL);
			}
		} else {
			System.err.println("Couldn't find file: " + path);
			if(size != null){
				return new MissingIcon(size.width, size.height);
			}
			else{
				return new MissingIcon(32, 32);
			}
		}
	}
}