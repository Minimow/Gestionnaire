package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import utilities.AlphaContainer;
import dao.EmployeDAO;
import dao.daoException.DAOFindException;
import dao.daoPOJO.Employe;
import dao.daoPOJO.Remplacement;
import dialogs.ErrorDialog;

public class TableRemplacements extends JTable {
	private static final long serialVersionUID = 1L;

	public TableRemplacements() {
        //this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.setModel(_model);
        this.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getTableCellRendererComponent(JTable table, Object value,
            		boolean isSelected, boolean hasFocus, int row, int column)
            {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                	if (_model.getBruteData().get(row).getSession().equals("Hiver")){
                        setBackground(colorHiver);
                	}
                	else if (_model.getBruteData().get(row).getSession().equals("Printemps")){
                		setBackground(colorPrintemps);
                	}
                	else if (_model.getBruteData().get(row).getSession().equals("Été")){
                		setBackground(colorEte);
                	}
                	else if (_model.getBruteData().get(row).getSession().equals("Automne")){
                		setBackground(colorAutomne);
                	}
                    else{
                    	setBackground(colorInconnue);
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
        
        this.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
        
        this.getColumnModel().getColumn(0).setMinWidth(125);
        //this.getColumnModel().getColumn(0).setMaxWidth(125);
        
        this.getColumnModel().getColumn(1).setMinWidth(50);
        this.getColumnModel().getColumn(1).setMaxWidth(50);
        
        this.getColumnModel().getColumn(2).setMinWidth(50);
        this.getColumnModel().getColumn(2).setMaxWidth(50);
        
        this.getColumnModel().getColumn(3).setPreferredWidth(150);
        //this.getColumnModel().getColumn(3).setMaxWidth(150);
        
        this.getColumnModel().getColumn(4).setPreferredWidth(125);
        //this.getColumnModel().getColumn(4).setMaxWidth(125);
        
        this.getColumnModel().getColumn(5).setPreferredWidth(125);
       // this.getColumnModel().getColumn(5).setMaxWidth(125);
        
        this.getColumnModel().getColumn(6).setPreferredWidth(60);
        this.getColumnModel().getColumn(6).setMaxWidth(60);
        
    }
    public MyTableModel getModel() {
    	return _model;
    }

    private MyTableModel _model = new MyTableModel();
    
    // Color for the table, each session has a color associated.
    private int alpha = 80;
    private Color colorHiver = new Color(0,0,255, alpha);
    private Color colorPrintemps = new Color(255,0,0, alpha);
    private Color colorEte = new Color(0,255,0, alpha);
    private Color colorAutomne = new Color(255,255,0, alpha);
    private Color colorInconnue = new Color(120,120,120, alpha);
    
    public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer {

		private static final long serialVersionUID = 1L;
		private final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public BooleanCellRenderer() {
            setLayout(new GridBagLayout());
            setMargin(new Insets(0, 0, 0, 0));
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        	if (value instanceof Boolean) {
                setSelected((Boolean) value);
            }
            if (!isSelected) { 
            	if (_model.getBruteData().get(row).getSession().equals("Hiver")){
                    setBackground(colorHiver);
            	}
            	else if (_model.getBruteData().get(row).getSession().equals("Printemps")){
            		setBackground(colorPrintemps);
            	}
            	else if (_model.getBruteData().get(row).getSession().equals("Été")){
            		setBackground(colorEte);
            	}
            	else if (_model.getBruteData().get(row).getSession().equals("Automne")){
            		setBackground(colorAutomne);
            	}
                else{
                	setBackground(colorInconnue);
                }
            } else {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            }
            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(noFocusBorder);
            }
            return new AlphaContainer(this);
        }
    }

    public class MyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		private List<String> columnNames = new ArrayList<String>();
        @SuppressWarnings("rawtypes")
		private List<List> data = new ArrayList<List>();
        private ArrayList<Remplacement> arrayRemp = new ArrayList<Remplacement>();

        {
        	//columnNames.add("");
            columnNames.add("Date");
            columnNames.add("Début");
            columnNames.add("Fin");
            columnNames.add("Preneur");
            columnNames.add("Type");
            columnNames.add("Raison");
            columnNames.add("Approuvé");
            columnNames.add("Details");
        }

        @SuppressWarnings("rawtypes")
		public void addRow(List rowData)
        {
            data.add(rowData);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
        
        public void removeRow(int row){
        	// remove a row from your internal data structure
        	data.remove(row);
        	arrayRemp.remove(row);
            fireTableRowsDeleted(row, row);
        }
        
        public void removeAllRows(){
        	int rows = _model.getRowCount(); 
        	for(int i = rows - 1; i >=0; i--)
        	{
        	   _model.removeRow(i); 
        	}
        	arrayRemp.clear();
        }
        
        public ArrayList<Remplacement> getBruteData(){
        	return arrayRemp;
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		public void addRow(Remplacement remp){
        	SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM yyyy (EEEE)");
        	SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm");
        	EmployeDAO employeDAO = new EmployeDAO(DBConnection.getInstance());
        	Employe preneur;
			try {
				preneur = employeDAO.find(remp.getTaker());
			} catch (DAOFindException e) {
				ErrorDialog err = new ErrorDialog(null, "Erreur dans la base de données", "La récupération de l'employé preneur a échouée", 
						e.getMessage());
				err.setVisible(true);
				return;
			}

			ArrayList rowData = new ArrayList();
        	rowData.add(sdfDate.format(remp.getBeginDate().getTime()));
        	rowData.add(sdfHeure.format(remp.getBeginDate().getTime()));
        	rowData.add(sdfHeure.format(remp.getEndDate().getTime()));
        	if(preneur == null){
        		rowData.add("");
        	}
        	else{
            	rowData.add(preneur.getFullName());
        	}
        	rowData.add(remp.getType());
        	rowData.add(remp.getReason());
        	rowData.add(remp.isApproved());
        	if(remp.getDetails() == null){
            	rowData.add("");
        	}
        	else{
            	rowData.add(remp.getDetails());
        	}
        	
        	data.add(rowData);
        	arrayRemp.add(remp);
            fireTableRowsInserted(data.size() - 1, data.size() - 1);
        }
        
        
        public int getColumnCount() {
            return columnNames.size();
        }
 
        public int getRowCount() {
            return data.size();
        }
 
        public String getColumnName(int col)
        {
            try
            {
                return columnNames.get(col);
            }
            catch(Exception e)
            {
                return null;
            }
        }

        public Object getValueAt(int row, int col)
        {
            return data.get(row).get(col);
        }
 
        /*
         * JTable uses this method to determine the default renderer/

         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
 
        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
        	
        	// Pour que la premiere colonne avec le button delete soit clickable
        	/*if(col == 0){
        		return true;
        	}*/
        	return false;
        }
    }    
}