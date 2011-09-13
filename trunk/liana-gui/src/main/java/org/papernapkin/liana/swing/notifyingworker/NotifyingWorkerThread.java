package org.papernapkin.liana.swing.notifyingworker;

import org.papernapkin.liana.event.GenericEventNotifier;
import org.papernapkin.liana.locale.Translation;
import org.slf4j.LoggerFactory;

/**
 * A class that does some work and notifies the listeners.  Notification is
 * done from within the GUI thread, so UI components can be safely manipulated
 * from within the listener's called methods.
 *
 * @author Philip A. Chapman
 */
public abstract class NotifyingWorkerThread extends Thread
{
	public NotifyingWorkerThread() {
		super();
		this.dependency = null;
	}
	
	public NotifyingWorkerThread(String name) {
		super(name);
		this.dependency = null;
	}
	
	public NotifyingWorkerThread(NotifyingWorkerThread dependency) {
		super();
		this.dependency = dependency;
	}
	
	public NotifyingWorkerThread(String name, NotifyingWorkerThread dependency) {
		super(name);
		this.dependency = dependency;
	}
	
	private WorkerThreadEventNotifier notifier = new WorkerThreadEventNotifier();

	private NotifyingWorkerThread dependency;
	/**
	 * Sets a dependency thread which must complete sucessfully before this will complete.
	 * Note that this will only have an effect if the dependency is set before calling
	 * Thread.start().
	 */
	public void setDependency(NotifyingWorkerThread dependency) {
		this.dependency = dependency;
	}

	/**
	 * Adds a new listener to be notified of status.
	 * @param listener The listener to add.
	 */
	public void addWorkerThreadListener(WorkerThreadListener listener) {
		notifier.addListener(listener);
	}
	
	private Boolean success = null;
	/**
	 * Returns true of the thread has completed successfully, null if the
	 * thread has not yet completed, and false if the thread has completed,
	 * but not with success.  It is not recommended that this method not be
	 * used to determine the state of the thread, only to verify success or
	 * failure after the thread has terminated.
	 */
	public synchronized Boolean isSuccessful() {
		return success;
	}
	private synchronized void setSuccessful(Boolean success) {
		this.success = success;
	}
	
	@Override
	public int hashCode() {
		return Long.valueOf(getId()).hashCode();
	}

	/**
	 * Notifies listeners of status.
	 * @param event The event to pass to listeners.
	 */
	protected void notifyListeners(WorkerThreadEvent event) {
		notifier.notify(event);
	}
	
	/**
	 * Removes the listener from the list to be notified of status.
	 * @param listener The listener to remove.
	 */
	public void removeWorkerThreadListener(WorkerThreadListener listener) {
		notifier.removeListener(listener);
	}
	
	private boolean test(Boolean value) {
		return value != null && value;
	}
	
	/**
	 * Method which notifies listeners of WorkerThreadEvent.WORK_START, calls
	 * work, then notifies listeners of WorkerThreadEvent.WORK_STOP.
	 */
	public void run() {
		notifyListeners(new WorkerThreadEvent(this, WorkerThreadEvent.Type.WORK_START));
		if (dependency == null || test(dependency.isSuccessful())) {
			execute();
		} else {
			notifyListeners(
				new WorkerThreadEvent(
					this, Translation.getTranslation(NotifyingWorkerThread.class).getString("label.waiting.dependency", dependency.getName())
				)
			);
			if (!dependency.isAlive()) {
				dependency.start();
				try {
					dependency.join();
				} catch (InterruptedException e) {}
			}
			if (test(dependency.isSuccessful())) {
				execute();
			} else {
				this.success = false;
				notifyListeners(new WorkerThreadEvent(this, WorkerThreadEvent.Type.WORK_STOP));
			}
		}
	}
	
	private void execute() {
		try {
			boolean result = work();
			setSuccessful(result);
		} catch (Throwable t) {
			// Ideally, the subclass would handle all errors.  In case it does
			// not, deal with any uncaught errors.
			LoggerFactory.getLogger(getClass()).error("Error executing thread", t);
			notifyListeners(new WorkerThreadEvent(this, t));
		}
		notifyListeners(new WorkerThreadEvent(this, WorkerThreadEvent.Type.WORK_STOP));
	}
	
	/**
	 * Method called in the run method in order to do work.
	 * @return true if the work completed successfully.
	 */
	public abstract boolean work();
	
	//////////////////////////////////////////////////////////////////////////
	
	private class WorkerThreadEventNotifier extends GenericEventNotifier<WorkerThreadListener>
	{
		WorkerThreadEventNotifier() {
			super(WorkerThreadListener.class, GenericEventNotifier.InvocationThread.EDT);
		}
		
		void notify(WorkerThreadEvent event) {
			super.notifyListeners(event.getEventType().getListenerMethod(), new Object[]{event});
		}
	}

	/**
	 * At times, it may be desirable for one NotifyingWorkerThread to delegate
	 * work to another NotifyingWorkerThread.  This listener can be attached to
	 * that NotifyingWorkerThread.  It will change the event source in the
	 * event and re-fire it to the listeners of the enclosing instance.  Note
	 * that start and stop events are not fired.  Only error, notification, and
	 * progress, if requested.
	 * 
	 * @author pchapman
	 */
	protected class ProxyingWorkerThreadListener implements WorkerThreadListener {
		private boolean notifyProgress;
		
		/**
		 * Creates a new instance.
		 * 
		 * @param notifyProgress True if progress notifications should be
		 *                       re-written and sent to listeners.
		 */
		public ProxyingWorkerThreadListener(boolean notifyProgress) {
			super();
			this.notifyProgress = notifyProgress;
		}
		
		@Override
		public void errorNotified(WorkerThreadEvent event) {
			event.setSource(NotifyingWorkerThread.this);
			notifyListeners(event);
		}

		@Override
		public void messageNotified(WorkerThreadEvent event) {
			event.setSource(NotifyingWorkerThread.this);
			notifyListeners(event);
		}

		@Override
		public void progressNotified(WorkerThreadEvent event) {
			if (notifyProgress) {
				event.setSource(NotifyingWorkerThread.this);
				notifyListeners(event);
			}
		}

		@Override
		public void startNotified(WorkerThreadEvent event) {}

		@Override
		public void stopNotified(WorkerThreadEvent event) {}
	}
}
