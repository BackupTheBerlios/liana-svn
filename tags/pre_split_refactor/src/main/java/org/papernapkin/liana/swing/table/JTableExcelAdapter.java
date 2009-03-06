package org.papernapkin.liana.swing.table;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables. The
 * clipboard data format used by the adapter is compatible with the clipboard
 * format used by Excel. This provides for clipboard interoperability between
 * enabled JTables and Excel.
 *
 * @author pchapman
 */
public class JTableExcelAdapter implements ActionListener
{
	private static final Logger logger = LoggerFactory.getLogger(JTableExcelAdapter.class);
	private KeyStroke copy;
	private KeyStroke paste;
	private Clipboard system;
	private StringSelection stsel;
	private JTable jTable1;
	
	/**
	 * The Excel Adapter is constructed with a JTable on which it enables
	 * Copy-Paste and acts as a Clipboard listener.
	 */
	public JTableExcelAdapter(JTable myJTable)
	{
		jTable1 = myJTable;
		copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK, false);
		// Identifying the copy KeyStroke user can modify this
		// to copy on some other Key combination.
		paste = KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK, false);
		// Identifying the Paste KeyStroke user can modify this
		//to copy on some other Key combination.
		jTable1.registerKeyboardAction(this, "Copy", copy,
				JComponent.WHEN_FOCUSED);
		
		jTable1.registerKeyboardAction(this, "Paste", paste,
				JComponent.WHEN_FOCUSED);
		system = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/**
	 * Public Accessor methods for the Table on which this adapter acts.
	 */
	public JTable getJTable()
	{
		return jTable1;
	}
	
	public void setJTable(JTable jTable1)
	{
		this.jTable1 = jTable1;
	}
	
	/**
	 * This method is activated on the Keystrokes we are listening to in this
	 * implementation. Here it listens for Copy and Paste ActionCommands.
	 * Selections comprising non-adjacent cells result in invalid selection and
	 * then copy action cannot be performed. Paste is done by aligning the upper
	 * left corner of the selection with the 1st element in the current
	 * selection of the JTable.
	 * 
	 * The original version used a StringTokenizer to parse the pasted data.
	 * I made changes to use the String.split(String, int) method because the
	 * StringTokenizer version ignored data copied from empty cells.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand().compareTo("Copy")==0)
		{
			StringBuffer sbf=new StringBuffer();
			// Check to ensure we have selected only a contiguous block of
			// cells
			int numcols=jTable1.getSelectedColumnCount();
			int numrows=jTable1.getSelectedRowCount();
			int[] rowsselected=jTable1.getSelectedRows();
			int[] colsselected=jTable1.getSelectedColumns();
			if (!((numrows-1==rowsselected[rowsselected.length-1]-rowsselected[0] &&
					numrows==rowsselected.length) &&
					(numcols-1==colsselected[colsselected.length-1]-colsselected[0] &&
							numcols==colsselected.length)))
			{
				JOptionPane.showMessageDialog(null, "Invalid Copy Selection",
						"Invalid Copy Selection",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (int i=0;i<numrows;i++)
			{
				for (int j=0;j<numcols;j++)
				{
					sbf.append(jTable1.getValueAt(rowsselected[i],colsselected[j]));
					if (j<numcols-1) sbf.append("\t");
				}
				sbf.append("\n");
			}
			stsel  = new StringSelection(sbf.toString());
			system = Toolkit.getDefaultToolkit().getSystemClipboard();
			system.setContents(stsel,stsel);
		}
		if (e.getActionCommand().compareTo("Paste")==0)
		{
			if (logger.isDebugEnabled()) {
				logger.debug("Trying to Paste");
			}
			int[] selectedRows = jTable1.getSelectedRows();
			int[] selectedColumns = jTable1.getSelectedColumns();
			
			int startRow = 0;
			int startCol = 0;
			if (selectedRows.length > 0) {
				startRow=selectedRows[0];
			}
			if (selectedColumns.length > 0) {
				startCol=selectedColumns[0];
			}
			
			try
			{
				String trstring= (String)(system.getContents(this).getTransferData(DataFlavor.stringFlavor));
				if (logger.isDebugEnabled()) {
					logger.debug("String is:"+trstring);
				}
				
				String[] s;
				String[] rows = trstring.split("\n", -1);
				Pattern p = Pattern.compile("\t");
				for (int i = 0; i < rows.length; i++) {
					if (rows[i].length() == 0) {
						continue;
					}
					s = p.split(rows[i]);
					for (int j = 0; j < s.length ; j++) {
						jTable1.setValueAt(s[j], startRow + i, startCol + j);
					}
				}
			} catch(Exception ex){
				logger.error("Error pasting data", ex);
			}
		}
	}
}
