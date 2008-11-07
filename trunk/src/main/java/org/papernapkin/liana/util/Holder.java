package org.papernapkin.liana.util;

/**
 * An object which holds a hard reference to another object.
 * 
 * @author pchapman
 */
public class Holder<T>
{
	private T value;
	/** Returns the value being hold by this object.  May be null. */
	public T get() {
		return value;
	}
	/** Sets the value being held by this object.  May be null. */
	public void set(T value) {
		this.value = value;
	}
}
