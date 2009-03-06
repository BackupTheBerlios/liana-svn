package org.papernapkin.liana.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/**
 * A generified table model which holds a list of objects which can be easily
 * retrieved without casting.  Each row contains a unique object.  Neither the
 * list, nor this class are synchronized.  User code should ensure that this
 * model's list is only modified on the EDT.
 * 
 * <p>This model should only be used if the items held in this model are not
 * contained in a usable data structure elsewhere.  See the warning in the
 * javadoc of {@link GenericTableBoxModel} for more detail.
 * 
 * @author pchapman
 */
@SuppressWarnings("serial")
public abstract class ListTableModel<T> extends AbstractTableModel
	implements GenericTableModel<T>
{
	/**
	 * @see org.papernapkin.liana.swing.model.GenericTableModel#getItemAt(int)
	 */
	@Override
	public T getObjectAt(int row) {
		if (row > -1 && row < data.size()) {
			return data.get(row);
		}
		return null;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.size();
	}

	
	/**
	 * Creates a new model with an empty list of elements.
	 */
	public ListTableModel()
	{
		super();
		this.data = new Vector<T>();
	}
	
	/**
	 * Creates a new model wich contains the elements in the collection.  A new
	 * list with a reference to all the items in the collection is made.
	 * Therefore, if the list passed in as a parameter is modified, the changes
	 * will not be reflected in this model.
	 * 
	 * @param values The values to be displayed.  The values are added to the
	 *               internal list using the collection's iterator.  Therefore,
	 *               if you pass in a collection that guarantees order, the
	 *               order will be maintained in the resulting model. 
	 */
	public ListTableModel(Collection<T> values)
	{
		this();
		for (T value : values) {
			this.data.add(value);
		}
	}

	// MEMBERS
	
	protected List <T>data;

	
	/**
	 * Returns a copy of the list of items held in this model.  Changes made to
	 * the returned list will not be reflected in this model.
	 * 
	 * @return The copy.
	 */
	public List<T> getObjects() {
		return new ArrayList<T>(this.data);
	}
	
	// METHODS
	
	/**
	 * Adds the given item at the end of the list.
	 */
	public void addObject(T item)
	{
		if (! data.contains(item)) {
			data.add(item);
			int index = data.size() - 1;
			fireTableRowsInserted(index, index);
		}
	}

	/**
	 * Adds all the items in the collection to the end of the list.
	 * @param items The items to add.  The values are added to the internal
	 *              list using the collection's iterator.  Therefore, if you
	 *              pass in a collection that guarantees order, the order of
	 *              the added items will be maintained in the resulting model.
	 */
	public void addObjects(Collection<T> items)
	{
		int index1 = data.size();
		for (T value : items) {
			this.data.add(value);
		}
		int index2 = data.size() - 1;
		if ((index2 - index1) > -1) {
			fireTableRowsInserted(index1, index2);
		}
	}
	
	/**
	 * Clears all items from the list.
	 */
	public void clear() {
		if (this.data.size() > 0) {
			int i = this.data.size() - 1;
			this.data.clear();
			fireTableRowsDeleted(0, i);
		}
	}
	
	public void fireObjectEdited(T item) {
		int i = this.data.indexOf(item);
		if (i > -1) {
			fireTableRowsUpdated(i, i);
		}
	}
	
	/**
	 * Inserts the item into the indicated index, pushing all existing items
	 * from that index on higher in the list.
	 * @param item The item to insert.
	 * @param index The index at which the item is to be inserted.
	 */
	public void insertObject(T item, int index) {
		this.data.add(index, item);
		fireTableRowsInserted(index, index);
	}

	/**
	 * Removes the item at the given index from the list.
	 * @param index The index at which the item to be removed is located.
	 */
	public void removeObject(int index) {
		this.data.remove(index);
		fireTableRowsDeleted(index, index);
	}
	
	/**
	 * Removes the given item from the list.  Nothing happens if the item is
	 * not found in the list.
	 * @param item The item to remove from the list.
	 */
	public void removeObject(T item) {
		int index = this.data.indexOf(item);
		if (index > -1) {
			removeObject(index);
		}
	}

	/**
	 * Sorts the data in the list using the given comparator.
	 * 
	 * @param comparator The comparator to use when sorting the list.
	 */
	public void sort(Comparator<T> comparator) {
		if (this.data.size() > 1) {
			Collections.sort(this.data, comparator);
			fireTableDataChanged();
		}
	}
}
