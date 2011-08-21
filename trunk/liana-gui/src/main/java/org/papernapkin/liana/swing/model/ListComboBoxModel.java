package org.papernapkin.liana.swing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractListModel;

/**
 * A generified combo box and list model which holds a list of objects
 * which can be easily retrieved without casting.
 * 
 * <p>This model should only be used if the items held in this model are not
 * contained in a usable data structure elsewhere.  See the warning in the
 * javadoc of {@link GenericComboBoxModel} for more detail.
 * 
 * @author pchapman
 */
public class ListComboBoxModel<T> extends AbstractListModel
	implements GenericComboBoxModel<T>
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;
	
	// CONSTRUCTORS
	
	/**
	 * Creates a new model with an empty list of elements.
	 */
	public ListComboBoxModel()
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
	public ListComboBoxModel(Collection<T> values)
	{
		this();
		setObjects(values);
	}

	// MEMBERS
	
	protected List <T>data;
	
	private Object selectedItem;
	@Override
	public void setSelectedItem(Object anItem)
	{
		this.selectedItem = anItem;
	}
	@Override
	public Object getSelectedItem()
	{
		return selectedItem;
	}

	@Override
	public int getSize()
	{
		return data.size();
	}

	@Override
	public Object getElementAt(int index)
	{
		if (index > -1 && index < data.size()) {
			return data.get(index);
		}
		return null;
	}

	/**
	 * Gets the item at the given index.  Same as getElementAt(index) except
	 * that no casting is required.
	 * 
	 * @param index The index from which the item is to be returned.
	 * @return The item at the given index.
	 */
	public T getObjectAt(int index)
	{
		return data.get(index);
	}
	
	/**
	 * Returns a copy of the list of items held in this model.  Changes made to
	 * the returned list will not be reflected in this model.
	 * 
	 * @return The copy.
	 */
	public List<T> getObjects() {
		return new ArrayList<T>(this.data);
	}
	
	/**
	 * Changes the data in the list backing this model.  The Collection passed
	 * in is not used.  As a result, changes to the collection passed in will
	 * not be reflected in this model.
	 * 
	 * @param objects The items to insert.
	 */
	public void setObjects(Collection<T> objects) {
		clear();
		addObjects(objects);
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
			fireIntervalAdded(this, index, index);
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
		this.data.addAll(items);
		int index2 = data.size() - 1;
		if ((index2 - index1) > -1) {
			fireIntervalAdded(this, index1, index2);
		}
	}
	
	/**
	 * Clears all items from the list.
	 */
	public void clear() {
		if (this.data.size() > 0) {
			int i = this.data.size() - 1;
			this.data.clear();
			fireIntervalRemoved(this, 0, i);
		}
	}
	
	public void fireObjectChanged(T item) {
		int i = indexOf(item);
		if (i > -1) {
			fireContentsChanged(this, i, i);
		}
	}
	
	public int indexOf(T item) {
		return this.data.indexOf(item);
	}
	
	/**
	 * Inserts the item into the indicated index, pushing all existing items
	 * from that index on higher in the list.
	 * @param item The item to insert.
	 * @param index The index at which the item is to be inserted.
	 */
	public void insertObject(T item, int index) {
		this.data.add(index, item);
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes the item at the given index from the list.
	 * @param index The index at which the item to be removed is located.
	 */
	public void removeObject(int index) {
		this.data.remove(index);
		fireIntervalRemoved(this, index, index);
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
	 * Removes all current data and replaces it with the given data.
	 *
	 * @param data The collection of data to hold in the model.
	 */
	public void replaceObjects(Collection<T> data) {
		clear();
		addObjects(data);
	}

	/**
	 * Sorts the data in the list using the given comparator.
	 * 
	 * @param comparator The comparator to use when sorting the list.
	 */
	public void sort(Comparator<T> comparator) {
		if (this.data.size() > 1) {
			Collections.sort(this.data, comparator);
			fireContentsChanged(this, 0, this.data.size() - 1);
		}
	}
}
