package org.papernapkin.liana.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;

import org.papernapkin.liana.swing.model.ToolTipTableModel;

/**
 * An improved version of JTable.  This version fixes a problem with JTable
 * where data entered into cells is not saved when focus is lost due to mouse
 * events (such as clicking on a JButton).  This class also allows the user
 * to choose for the ExtJTable to be adapted for Excel.  When this option is
 * used, ExtJTable will be copy/paste friendly with data going to and coming
 * from Microsoft Excel.
 *
 * @author pchapman
 */
public class ExtJTable extends JTable
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	// CONSTRUCTORS FROM SUPER
	/**
	 * 
	 */
	public ExtJTable()
	{
		this(false);
	}
	
	/**
	 * @param numRows
	 * @param numColumns
	 */
	public ExtJTable(int numRows, int numColumns)
	{
		this(numRows, numColumns, false);
	}
	
	/**
	 * @param rowData
	 * @param columnNames
	 */
	public ExtJTable(Object[][] rowData, Object[] columnNames)
	{
		this(rowData, columnNames, false);
	}
	
	/**
	 * @param rowData
	 * @param columnNames
	 */
	@SuppressWarnings("unchecked")
	public ExtJTable(Vector rowData, Vector columnNames)
	{
		this(rowData, columnNames, false);
	}
	
	/**
	 * @param dm
	 */
	public ExtJTable(TableModel dm)
	{
		this(dm, false);
	}
	
	/**
	 * @param dm
	 * @param cm
	 */
	public ExtJTable(TableModel dm, TableColumnModel cm)
	{
		this(dm, cm, false);
	}
	
	/**
	 * @param dm
	 * @param cm
	 * @param sm
	 */
	public ExtJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	{
		this(dm, cm, sm, false);
	}

	// EXT CONSTRUCTORS
	
	/**
	 * 
	 */
	public ExtJTable(boolean adaptForExcel)
	{
		super();
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param numRows
	 * @param numColumns
	 */
	public ExtJTable(int numRows, int numColumns, boolean adaptForExcel)
	{
		super(numRows, numColumns);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param rowData
	 * @param columnNames
	 */
	public ExtJTable(Object[][] rowData, Object[] columnNames, boolean adaptForExcel)
	{
		super(rowData, columnNames);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param rowData
	 * @param columnNames
	 */
	@SuppressWarnings("unchecked")
	public ExtJTable(Vector rowData, Vector columnNames, boolean adaptForExcel)
	{
		super(rowData, columnNames);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param dm
	 */
	public ExtJTable(TableModel dm, boolean adaptForExcel)
	{
		super(dm);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param dm
	 * @param cm
	 */
	public ExtJTable(TableModel dm, TableColumnModel cm, boolean adaptForExcel)
	{
		super(dm, cm);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	/**
	 * @param dm
	 * @param cm
	 * @param sm
	 */
	public ExtJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm, boolean adaptForExcel)
	{
		super(dm, cm, sm);
		if (adaptForExcel) {
			excelAdapter = new JTableExcelAdapter(this);
		}
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	
	
	// MEMBERS
	
//	protected JTableCellEditPatch cellEditPatch = null;
	protected JTableExcelAdapter excelAdapter = null;
	
	protected boolean alternateRowColors = false;
	private Color rowColor;
	private Color altRowColor;
	
	/**
	 * @return the alternateRowColors
	 */
	public boolean isAlternateRowColors() {
		return alternateRowColors;
	}

	/**
	 * @param alternateRowColors the alternateRowColors to set
	 */
	public void setAlternateRowColors(boolean alternateRowColors) {
		this.alternateRowColors = alternateRowColors;
	}

	/**
	 * @return the rowColor
	 */
	public Color getRowColor() {
		if (rowColor == null) {
			return UIManager.getColor("Table.background");
		} else {
			return rowColor;
		}
	}

	/**
	 * @param rowColor the rowColor to set
	 */
	public void setRowColor(Color rowColor) {
		this.rowColor = rowColor;
	}

	/**
	 * @return the altRowColor
	 */
	public Color getAltRowColor() {
		if (altRowColor == null) {
			altRowColor = new Color(204, 255, 204);
		}
		return altRowColor;
	}

	/**
	 * @param altRowColor the altRowColor to set
	 */
	public void setAltRowColor(Color altRowColor) {
		alternateRowColors = altRowColor != null;
		this.altRowColor = altRowColor;
	}

	// METHODS

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component c = super.prepareRenderer(renderer, row, column);
		if (alternateRowColors && ! isCellSelected(row, column)) {
			if (row % 2 == 0) {
				c.setBackground(getAltRowColor());
			} else {
				c.setBackground(getRowColor());
			}
		}
		return c;
	}
	
	//  Select the text when the cell starts editing
	//  a) text will be replaced when you start typing in a cell
	//  b) text will be selected when you use F2 to start editing
	//  c) text will be selected when double clicking to start editing
	// removed this override and moved this behaviour into <code>prepareEditor</code>
	// to avoid thread issues it SwingUtilities#invokeLater(Runnable) and the rest
	// of the GUI updated involved in switching to the TableCellEditor
	// mattorantimatt 2009.01.16
//	public boolean editCellAt(int row, int column, EventObject e)
//	{
//		boolean result = super.editCellAt(row, column, e);
//		final Component editor = getEditorComponent();
//
//		if (editor != null && editor instanceof JTextComponent)
//		{
//			if (e == null)
//			{
//				((JTextComponent)editor).selectAll();
//			}
//			else
//			{
//				SwingUtilities.invokeLater(new Runnable()
//				{
//					public void run()
//					{
//						((JTextComponent)editor).selectAll();
//					}
//				});
//			}
//		}
//
//		return result;
//	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JTable#prepareEditor(javax.swing.table.TableCellEditor, int, int)
	 */
	@Override
	public Component prepareEditor(TableCellEditor editor, int row, int column) {
		Component c = super.prepareEditor(editor, row, column);
		
		if (c instanceof JTextComponent) {
			((JTextComponent)c).selectAll();
		}
		return c;
	}


	private ToolTipTableModel tooltipModel;
	public ToolTipTableModel getToolTipTableModel()
	{
		return tooltipModel;
	}
	public void setToolTipTableModel(ToolTipTableModel model)
	{
		this.tooltipModel = model;
	}
	
	/**
	 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent event)
	{
		if(tooltipModel != null) {
			 Point p = event.getPoint();
			 int hitColumnIndex = columnAtPoint(p);
			 int hitRowIndex = rowAtPoint(p);

			 String data = tooltipModel.getToolTipText(hitRowIndex, hitColumnIndex);

			 if(data.length() > 0) {
				 return "<html><body bgcolor=\"#ffffff\" border=\"1\">" + data + "</body></html>";
			 }
		}
		return super.getToolTipText(event); 
	}
	
	public void scrollToVisible(int rowIndex, int vColIndex)
	{
		// If the JTable is not contained in a JScrollPane, no need in
		// continuing.
        if (!(getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport)getParent();
    
        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = getCellRect(rowIndex, vColIndex, true);
    
        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();
    
        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y);
    
        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
	}
}
