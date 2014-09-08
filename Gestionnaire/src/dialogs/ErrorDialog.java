package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ErrorDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	public ErrorDialog(JPanel parent, String title, String erreur, ArrayList<String> types, ArrayList<String> details){
		this.setLayout(new BorderLayout());
		this.setTitle(title);
		this.setModal(true);
		
		initComponent();
		erreurSimple.setText(erreur);
		
		StyledDocument doc = erreurDetails.getStyledDocument();
		Style style = erreurDetails.addStyle("Erreur", null);
		if(types.size() == details.size()){
			for(int i = 0; i < types.size(); i ++){
				Color col;
				if(i % 2 == 0) col = Color.red;
				else col = Color.magenta;
				StyleConstants.setForeground(style, col);
				try { doc.insertString(doc.getLength(), types.get(i),style); }
				catch (BadLocationException e){}

				StyleConstants.setForeground(style, Color.black);
				try { doc.insertString(doc.getLength(), details.get(i),style); }
				catch (BadLocationException e){}
			}
		}
		else{
			StyleConstants.setForeground(style, Color.red);
			try { doc.insertString(doc.getLength(), "Aucun détail n'est disponible",style); }
			catch (BadLocationException e){}
		}
		erreurDetails.setCaretPosition(0);
		this.pack();
		this.setLocationRelativeTo(parent);
	}

	public ErrorDialog(JPanel parent, String title, String erreur, String errDetails){
		this.setLayout(new BorderLayout());
		this.setTitle(title);
		this.setModal(true);

		initComponent();
		erreurSimple.setText(erreur);

		StyledDocument doc = erreurDetails.getStyledDocument();
		Style style = erreurDetails.addStyle("Erreur", null);
		StyleConstants.setForeground(style, Color.red);
		try { doc.insertString(doc.getLength(), errDetails,style); }
		catch (BadLocationException e){}
		erreurDetails.setCaretPosition(0);
		this.pack();
		this.setLocationRelativeTo(parent);
	}
	
	public void initComponent(){
		
		btnDetails.setFocusPainted(false);
		btnOk.setFocusPainted(false);
		
		// NORTH
		ImageIcon icon = createImageIcon("/erreurDialog.png", "");
		imgErreur.setIcon(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
		erreurSimple.setSize(new Dimension(400, 60));
		erreurSimple.setLineWrap(true);
		erreurSimple.setWrapStyleWord(true);
		erreurSimple.setOpaque(false);
		erreurSimple.setEditable(false);
		JPanel erreurSimplePane = new JPanel();
		erreurSimplePane.add(imgErreur);
		erreurSimplePane.add(erreurSimple);
		this.add(erreurSimplePane, BorderLayout.NORTH);
		
		// Middle
		Dimension detailsSize = new Dimension(600,600);
		erreurDetails.setSize(detailsSize);
		JScrollPane scrollPaneArea = new JScrollPane(erreurDetails);
		erreurDetails.setEditable(false);
		scrollPaneArea.setPreferredSize(new Dimension(400, 100));
		middlePane.add(scrollPaneArea);
		this.add(middlePane, BorderLayout.CENTER);
		middlePane.setVisible(false);
		
		// SOUTH
		JPanel buttonsPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPane.add(btnOk);
		buttonsPane.add(btnDetails);
		this.add(buttonsPane, BorderLayout.SOUTH);
		
		btnDetails.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(middlePane.isVisible()){
					middlePane.setVisible(false);
					btnDetails.setText("Détails >>");
				}
				else{
					middlePane.setVisible(true);	
					btnDetails.setText("Détails <<");
				}
				thisDialog.pack();
			}
		});
		
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				dispose();
			}
		});
	}

	protected ImageIcon createImageIcon(String path,
			String description) {
		URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
	private JLabel imgErreur = new JLabel();
	private JTextPane erreurDetails = new JTextPane()
	{

		private static final long serialVersionUID = 1L;

		public boolean getScrollableTracksViewportWidth()
	    {
	        return getUI().getPreferredSize(this).width 
	            <= getParent().getSize().width;
	    }
	};
	private JTextArea erreurSimple = new JTextArea();
	private JButton btnOk = new JButton("OK"),
			btnDetails = new JButton("Détails >>");
	private JPanel	middlePane = new JPanel();
	private JDialog thisDialog = this;
}