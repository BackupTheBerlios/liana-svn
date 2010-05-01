package org.papernapkin.liana.util;

public interface IHolder<T> {

	/** Returns the value being hold by this object.  May be null. */
	public abstract T get();

	/** Sets the value being held by this object.  May be null. */
	public abstract void set(T value);

}