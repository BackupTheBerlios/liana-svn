package org.papernapkin.liana.swing.notifyingworker;

/**
 * Implemented by an object that wants to listen for events from a
 * NotifyingWorkerThread.
 *
 * @author Philip A. Chapman
 */
public interface WorkerThreadListener
{
	/**
	 * Called to notify the listener of an error.
	 * @param event An object that gives the listener status information.
	 */
	abstract void errorNotified(WorkerThreadEvent event);

	/**
	 * Called to notify the listener of a message.  Typically, a human
	 * readable message which can be relayed to the user.
	 * @param event An object that gives the listener status information.
	 */
	abstract void messageNotified(WorkerThreadEvent event);

	/**
	 * Called to notify the listener of progress.
	 * @param event An object that gives the listener status information.
	 */
	abstract void progressNotified(WorkerThreadEvent event);

	/**
	 * Called to notify the listener that the thread has started.
	 * @param event An object that gives the listener status information.
	 */
	abstract void startNotified(WorkerThreadEvent event);

	/**
	 * Called to notify the listener that the thread has stopped.
	 * @param event An object that gives the listener status information.
	 */
	abstract void stopNotified(WorkerThreadEvent event);
}
