package boxes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import dao.daoPOJO.Employe;

public class StatsBox extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static enum TAG {GENERAL, EMPLOYES, REMPLACEMENT, SESSION};

	public StatsBox(String title, ArrayList<String> columnNames) {
		lblTitle.setText("<html><div style=\"text-align: center;\">" + title + "</html>");
		this.colNames = columnNames;
		tableModel = new MyTableModel();
		initComponent();

		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(lblTitle, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		
		scroll = new JScrollPane(table);
		Border border = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		scroll.setViewportBorder(border);
		scroll.setBorder(border);
		this.add(scroll, gbc);

		// Box invisible pour quon voit la border south
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(Box.createRigidArea(new Dimension(5, 3)), gbc);
	}

	public MyTableModel getModel() {
		return tableModel;
	}

	public JTable getTable() {
		return table;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension arcs = new Dimension(15, 15); // Border corners arcs
												// {width,height}, change this
												// to whatever you want
		int width = getWidth();
		int height = getHeight();
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Draws the rounded panel with borders.
		graphics.setColor(getBackground());
		graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width,
				arcs.height);// paint background
		graphics.setColor(getForeground());
		graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width,
				arcs.height);// paint border
	}

	private void initComponent() {
		lblTitle.setBackground(new Color(50, 150, 255));
		lblTitle.setMaximumSize(new Dimension(300, 50));
		lblTitle.setHorizontalAlignment(JLabel.CENTER);
		//lblTitle.setForeground(Color.WHITE);
		Font myFont = new Font("Serif", Font.BOLD + Font.ITALIC, 18);
		lblTitle.setFont(myFont);

		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
	        {
			/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus, int row,
					int column) {
	            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					if (row == 0) {
						setBackground(color1st);
					} else if (row == 1) {
						setBackground(color2nd);
					} else if (row == 2) {
						setBackground(color3rd);
					} else {
						setBackground(table.getBackground());
					}
				} else {
					c.setForeground(table.getSelectionForeground());
					c.setBackground(table.getSelectionBackground());
				}
				if (hasFocus) {
					setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				} else {
					setBorder(noFocusBorder);
				}
				return c;
			}
	        });
		table.setDefaultRenderer(Integer.class, new DefaultTableCellRenderer()
        {
			private static final long serialVersionUID = 1L;

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (!isSelected) {
				if (row == 0) {
					setBackground(color1st);
				} else if (row == 1) {
					setBackground(color2nd);
				} else if (row == 2) {
					setBackground(color3rd);
				} else {
					setBackground(table.getBackground());
				}
			} else {
				c.setForeground(table.getSelectionForeground());
				c.setBackground(table.getSelectionBackground());
			}
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
			} else {
				setBorder(noFocusBorder);
			}
			return c;
		}
        });
		table.setAutoCreateRowSorter(true);
		table.setModel(tableModel);
		Color color = table.getGridColor();
		table.getTableHeader().setBorder(new MatteBorder(0, 1, 0, 0, color));
		table.setBorder(new MatteBorder(0, 1, 0, 0, color));
	}

	
	private ArrayList<String> colNames;
	private MyTableModel tableModel;
    private int alpha = 80;
	private Color color1st = new Color(255,0,0, alpha),
    		color2nd = new Color(100,100,255, alpha),
    		color3rd = new Color(255,255,0, alpha);

	private JLabel lblTitle = new JLabel() {

		private static final long serialVersionUID = 1L;

		@Override
		protected void paintComponent(Graphics g) {
			Dimension arcs = new Dimension(15, 15); // Border corners arcs
													// {width,height}, change
													// this to whatever you want
			int width = getWidth();
			int height = getHeight();
			GradientPaint gp = new GradientPaint(0, 0, getBackground(), 0,
					height, getBackground().brighter().brighter(), true);

			Graphics2D graphics = (Graphics2D) g;
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics.setPaint(gp);
			// graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			graphics.fillRoundRect(1, 1, width - 2, height - 1, arcs.width,
					arcs.height);

			super.paintComponent(g);
		}
	};

	private JScrollPane scroll;
	private TAG tag;
	
	public TAG getTag(){
		return tag;
	}
	
	public void setTag(TAG tag){
		this.tag = tag;
	}
	
	
	private JTable table = new JTable() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void setPreferredScrollableViewportSize(Dimension size) {
			// -4 pour que la table soit plus petite que le panel pour quon voit
			// la border
			super.setPreferredScrollableViewportSize(new Dimension(
					size.width - 4, size.height));
			if (size != null) {
				if(scroll != null && scroll.getVerticalScrollBar().isVisible()){
					lblTitle.setPreferredSize(new Dimension(size.width + 20, 50));
				}
				else
				{
					lblTitle.setPreferredSize(new Dimension(size.width, 50));
				}
				// Pas sur que c'est nécessaire, à vérifier
				//lblTitle.revalidate();
			}
		}
	};
	
	public class MyTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private List<String> columnNames = colNames;
		@SuppressWarnings("rawtypes")
		private List<List> data = new ArrayList<List>();
		private ArrayList<Employe> arrayEmp = new ArrayList<Employe>();

		/*{
			columnNames.add("#");
			columnNames.add("Nom");
			columnNames.add("Count");
		}*/


		@SuppressWarnings("rawtypes")
		public void addRow(List rowData) {
			data.add(rowData);
			fireTableRowsInserted(data.size() - 1, data.size() - 1);
		}

		public void removeRow(int row) {
			// remove a row from your internal data structure
			data.remove(row);
			arrayEmp.remove(row);
			fireTableRowsDeleted(row, row);
		}

		public void removeAllRows() {
			int rows = this.getRowCount();
			for (int i = rows - 1; i >= 0; i--) {
				this.removeRow(i);
			}
			arrayEmp.clear();
		}

		public ArrayList<Employe> getBruteData() {
			return arrayEmp;
		}

		public int getColumnCount() {
			return columnNames.size();
		}

		public int getRowCount() {
			return data.size();
		}

		public String getColumnName(int col) {
			try {
				return columnNames.get(col);
			} catch (Exception e) {
				return null;
			}
		}

		public Object getValueAt(int row, int col) {
			return data.get(row).get(col);
		}

		/*
		 * JTable uses this method to determine the default renderer/
		 * 
		 * editor for each cell. If we didn't implement this method, then the
		 * last column would contain text ("true"/"false"), rather than a check
		 * box.
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
			//return getValueAt(0, c).getClass();
			Object o = getValueAt(0, c);

			// Si la premiere ligne est null, on ne detecte pas bien le type de la colonne.
			// a modifier ulterieurement, nest pas safe, si on veut des checkbox ou autre mais que la premiere row est null
            if (o != null)
            {
                return o.getClass();
            }
            else{
            	return Object.class;
            }
		}

		/*
		 * Don't need to implement this method unless your table's editable.
		 */
		public boolean isCellEditable(int row, int col) {
			// Note that the data/cell address is constant,
			// no matter where the cell appears onscreen.
			/*
			 * if (col < 2) { return false; } else { return true; }
			 */
			return false;
		}

		/*
		 * Don't need to implement this method unless your table's data can
		 * change.
		 */
		/*
		 * public void setValueAt(Object value, int row, int col) { if (DEBUG) {
		 * System.out.println("Setting value at " + row + "," + col + " to " +
		 * value + " (an instance of " + value.getClass() + ")"); }
		 * 
		 * data[row][col] = value; fireTableCellUpdated(row, col);
		 * 
		 * if (DEBUG) { System.out.println("New value of data:");
		 * printDebugData(); } }
		 * 
		 * private void printDebugData() { int numRows = getRowCount(); int
		 * numCols = getColumnCount();
		 * 
		 * for (int i=0; i < numRows; i++) { System.out.print("    row " + i +
		 * ":"); for (int j=0; j < numCols; j++) { System.out.print("  " +
		 * data[i][j]); } System.out.println(); }
		 * System.out.println("--------------------------"); }
		 */
	}
}
