package org.papernapkin.liana.swing.model;

import javax.swing.table.TableModel;

/**
 * An interface implemented by a table model which allows easy access to
 * objects referenced by the model without casting.  This is only useful if
 * each row displays data for a single object, such as each column showing a
 * the value from a different member.
 * 
 * <p><strong>Warning</strong>Use care when using implementations of this
 * interface.  It can be misused so as to allow bad application design.  Models
 * are not meant to be used as data structures, but as a way of providing GUI
 * components with the needed elements from within an already existing data
 * structure.  However, there are times when the only data structure that
 * exists is in support of the UI.  In those instances, it may be desirable to
 * use an implementation of this interface.
 *
 * @param <T> The type of objects referenced by the model.
 *
 * @author pchapman
 */
public interface GenericTableModel<T> extends TableModel
{
	/**
	 * Gets the object at the given row.
	 * 
	 * @param row The row from which the item is to be returned.
	 * @return The object at the given index.
	 */
	public T getObjectAt(int row);
}
