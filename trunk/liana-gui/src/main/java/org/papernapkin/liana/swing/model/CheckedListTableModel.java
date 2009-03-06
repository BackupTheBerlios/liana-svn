package org.papernapkin.liana.swing.model;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides two columns.  The left column is a checkbox indicating whether the
 * item is selected.  The right column is a label displaying the item using the
 * Object.toString() method.
 * 
 * @author pchapman
 */
public class CheckedListTableModel<T>
	extends AbstractTableModel
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/** The column names that should be displayed. */
	protected String[] columnNames = {"",""};
	
	/** The vector which holds all the selectable items. */
	protected Vector<SelectableItem<T>> selectItems = new Vector<SelectableItem<T>>();

	/** Constructs a new CheckedListTableModel. */
	public CheckedListTableModel()
	{
		super();
	}

	/**
	 * Constructs a new CheckedListTableModel that uses the indicated column
	 * names.
	 * @param checkedColumnName The column name to be used for the check (left)
	 *                           column.
	 * @param itemColumnName The column name to be used for the item (right)
	 *                        column.
	 */
	public CheckedListTableModel(String checkColumnName, String itemColumnName)
	{
		super();
		columnNames[0] = checkColumnName;
		columnNames[1] = itemColumnName;
	}

	/**
	 * Constructs a new CheckedListTableModel and initializes it with the given
	 * values.
	 * @param items An array of items to display in the table.
	 */
	public CheckedListTableModel(T[] items)
	{
		super();
		for (int i = 0; i < items.length; i++) {
			add(items[i]);
		}
	}

	/**
	 * Constructs a new CheckedListTableModel that uses the indicated column
	 * names and initializes it with the given values..
	 * @param checkedColumnName The column name to be used for the check (left)
	 *                           column.
	 * @param itemColumnName The column name to be used for the item (right)
	 *                        column.
	 */
	public CheckedListTableModel(T[] items, String checkColumnName, String itemColumnName)
	{
		this(items);
		columnNames[0] = checkColumnName;
		columnNames[1] = itemColumnName;		
	}

	/** Adds an unselected item to the end of the list. */
	public void add(T item)
	{
		add(item, false);
	}
	
	/**
	 * Adds an item to the end of the list.
	 * @param item The item to add.
	 * @param selected Whether the item starts its life in the list selected.
	 */
	public void add(T item, boolean selected)
	{
		// I make use of the fact that SelectableItem's equals method cheats
		// and uses the internal object's equals method.
		if (! selectItems.contains(item)) {
			selectItems.add(new SelectableItem<T>(item, selected));
			fireTableRowsInserted(selectItems.size() - 1, selectItems.size() - 1);
		}
	}

	/** Adds an item into the list at the given index. */
	public void add(int index, T item)
	{
		add(index, item, false);
	}
	
	/** Adds an item into the list at the given index. */
	public void add(int index, T item, boolean selected)
	{
		// I make use of the fact that SelectableItem's equals method cheats
		// and uses the internal object's equals method.
		if (! selectItems.contains(item)) {
			selectItems.add(index ,new SelectableItem<T>(item, selected));
			fireTableRowsInserted(index, index);
		}
	}

	/** Removes the specified item from the list. */
	public void remove(T item)
	{
		// I make use of the fact that SelectableItem's equals method cheats
		// and uses the internal object's equals method.
		int i = selectItems.indexOf(item);
		if (i > -1) {
			selectItems.remove(i);
			fireTableRowsDeleted(i, i);
		}
	}
	
	/** Removes all items from the list. */
	public void removeAllElements()
	{
		selectItems.removeAllElements();
		fireTableDataChanged();
	}

	/** @see javax.swing.table.TableModel#getColumnCount() */
	public int getColumnCount()
	{
		return 2;
	}

	/** @see javax.swing.table.TableModel#getRowCount() */
	public int getRowCount()
	{
		return selectItems.size();
	}

	/**
	 * Gets the item at the indicated index.  (The value displayed in the
	 * right column.)
	 */
	public T getItemAt(int rowIndex)
	{
		return selectItems.elementAt(rowIndex).getItem();
	}

	/** @see javax.swing.table.TableModel#getValueAt(int, int) */
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			SelectableItem<T> si = selectItems.elementAt(rowIndex);
			if (columnIndex == 0) {
				// return true or false
				return si.isSelected();
			} else if (columnIndex == 1) {
			    return si.getItem();
			} else {
				if (logger.isDebugEnabled()) {
				    logger.debug("CheckedListTableModel.getValueAt(int,int) : returning null for row {}, column {}.", rowIndex, columnIndex);
				    logger.debug("Row Count: {}", getRowCount());
				}
				return null;
			}
		} catch (Throwable t) {
			if (logger.isDebugEnabled()) {
			    logger.debug("CheckedListTableModel.getValueAt(int,int) : returning null for row {}, column {}.", rowIndex, columnIndex);
			    logger.debug("Row Count: {}", getRowCount());
			}
			logger.error("Error encountered", t);
		    return null;
		}
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex)
	{
		if (columnIndex == 0) {
			return columnNames[0];
		} else {
			return columnNames[1];
		}
	}

	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex == 0;
	}

	/**
	 * Indicates whether the item at the indicated index is selected.  (The
	 * value displayed in the left column.)
	 */
	public boolean isSelected(int rowIndex)
	{
		return selectItems.elementAt(rowIndex).isSelected();
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(Object, int, int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			selectItems.elementAt(rowIndex).setSelected((Boolean)aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}
	/**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Boolean.class;
		} else {
			return Object.class;
		}
	}

	/**
	 * Encapsulates an item which can be selected using the CheckedListTableModel.
	 * 
	 * @author pchapman
	 */
	protected class SelectableItem<K> extends Object
	{
		/**
		 * The item which is to be displayed.
		 */
		K item = null;
		
		/**
		 * Whether the item is selected.
		 */
		boolean selected = false;
		
		/**
		 * Construct a new SelectableItem.
		 * @param item The item to be displayed.
		 */
		public SelectableItem(K item)
		{
			this(item, false);
		}
	
		/**
		 * Construct a new SelectableItem.
		 * @param item The item to be displayed.
		 */
		public SelectableItem(K item, boolean selected)
		{
			this.item = item;
			this.selected = selected;
		}
		
		/**
		 * @see java.lang.Object#equals(Object)
		 */
		public boolean equals(Object obj) {
			return item.equals(obj);
		}
		
		/**
		 * Get the item that is displayed.
		 */
		public K getItem()
		{
			return item;
		}
		
		/**
		 * Return whether the item is selected.
		 */
		public boolean isSelected()
		{
			return selected;
		}
		
		/**
		 * Set whether the item is selected.
		 */
		public void setSelected(boolean selected)
		{
			this.selected = selected;
		}
	
		/**
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return item.toString();
		}
	}
}
