package org.papernapkin.liana.swing.table;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTable;

/**
 * A class used to fix a cell edit bug in JTable where edited data is not
 * saved if the JTable looses focus due to mouse actions.
 *
 * @author pchapman
 */
public class JTableCellEditPatch extends FocusAdapter
{
	// CONSTRUCTORS
	
	public JTableCellEditPatch(JTable table)
	{
		super();
		this.table = table;
		table.addFocusListener(this);
	}
	
	// MEMBERS
	
	private JTable table;
	
	// METHODS
	
	public void focusLost(FocusEvent e)
	{
		if (table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
	}
}
