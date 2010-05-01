package org.papernapkin.liana.util;

/**
 * A synchronized implementation of {@link IHolder}
 * 
 * @author pchapman
 */
public class SynchronizedHolder<T> implements IHolder<T>
{
	private T value;
	/**
	 * @see org.papernapkin.liana.util.IHolder#get()
	 */
	public synchronized T get() {
		return value;
	}
	/**
	 * @see org.papernapkin.liana.util.IHolder#set(T)
	 */
	public synchronized void set(T value) {
		this.value = value;
	}
}
