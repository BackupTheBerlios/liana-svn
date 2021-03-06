package org.papernapkin.liana.util;

/**
 * An unsynchronized implementation of {@link IHolder}
 * 
 * @author pchapman
 */
public class Holder<T> implements IHolder<T>
{
	private T value;
	/**
	 * @see org.papernapkin.liana.util.IHolder#get()
	 */
	public T get() {
		return value;
	}
	/**
	 * @see org.papernapkin.liana.util.IHolder#set(T)
	 */
	public void set(T value) {
		this.value = value;
	}
}
