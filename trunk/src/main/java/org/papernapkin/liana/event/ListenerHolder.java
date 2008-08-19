package org.papernapkin.liana.event;

import java.lang.ref.SoftReference;
import java.lang.reflect.Proxy;

/**
 * A holder which holds a reference to a listener.  If the listener is an
 * anonymous or proxy class, the reference will be strong.  Else, it'll be
 * soft, to prevent memory leaks. We cannot hold a soft reference to an
 * anonymous or proxy object as it will likey have no strong references to it
 * and will be reaped.
 * 
 * @author pchapman
 */
class ListenerHolder<T> {
	private T strongref;
	private SoftReference<T> softref;
	
	ListenerHolder(T listener) {
		if (
				listener.getClass().isAnonymousClass() ||
				listener instanceof Proxy
			)
		{
			strongref = listener;
		} else {
			softref = new SoftReference<T>(listener);
		}
	}
	
	T get() {
		if (softref == null) {
			return strongref;
		} else {
			return softref.get();
		}
	}
}
