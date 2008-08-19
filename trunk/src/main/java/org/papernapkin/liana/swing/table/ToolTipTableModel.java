package org.papernapkin.liana.swing.table;

/**
 * A model which can be set for ExtJTable and will provide tooltip text for
 * cells.
 * 
 * @see ExtJTable#getToolTipTableModel()
 * @see ExtJTable#setToolTipTableModel(ToolTipTableModel)
 * 
 * @author pchapman
 */
public interface ToolTipTableModel
{
	/**
	 * Returns the tooltip text to be shown for the cell in the given row and
	 * column.
	 * @param row The index of the row.
	 * @param col The index of the column.
	 * @return The tooltip text.
	 */
	public String getToolTipText(int row , int col);
}
