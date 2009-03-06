package org.papernapkin.liana.awt.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.papernapkin.liana.event.GenericEventNotifier;

/**
 * A helper class which will track and notify ActionListener instances of
 * ActionEvents.
 * 
 * @author pchapman
 */
public class ActionEventNotifier extends GenericEventNotifier<ActionListener>
{
	public ActionEventNotifier() {
		super(ActionListener.class, InvocationThread.EDT);
	}
	
	public void notifyActionPerformed(ActionEvent event) {
		super.notifyListeners("actionPerformed", new Object[]{event});
	}
}
