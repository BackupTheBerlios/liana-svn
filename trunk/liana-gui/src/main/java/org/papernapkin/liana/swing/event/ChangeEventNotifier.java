package org.papernapkin.liana.swing.event;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.papernapkin.liana.event.GenericEventNotifier;

/**
 * A GenericEventNotifier which will notify ChangeListener implementors of
 * change events.
 * 
 * @see ChangeListener
 * @see ChangeEvent
 * 
 * @author pchapman
 */
public class ChangeEventNotifier extends GenericEventNotifier<ChangeListener>
{
	/**
	 * @param listenerClass
	 */
	public ChangeEventNotifier() {
		super(ChangeListener.class, InvocationThread.EDT);
	}

	public void stateChanged(ChangeEvent event) {
		super.notifyListeners("stateChanged", new Class<?>[]{ChangeEvent.class}, new Object[]{event});
	}
}
