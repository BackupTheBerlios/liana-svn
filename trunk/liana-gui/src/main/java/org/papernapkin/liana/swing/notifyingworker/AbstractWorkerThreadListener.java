package org.papernapkin.liana.swing.notifyingworker;

import org.slf4j.LoggerFactory;

/**
 * An empty implementation of WorkerThreadListener which can be extended by
 * listeners that want to listen to only certain callbacks without having to
 * implement empty methods.
 *
 * @author <a href="mail:pchapman@pcsw.us">Philip A. Chapman</a>
 */
public abstract class AbstractWorkerThreadListener implements WorkerThreadListener
{
	/**
	 * Unlike other methods, which are simply empty implementations, this
	 * implementation logs the reported error, but otherwise does nothing else.
	 *
	 * @param event An object that gives the listener status information.
	 */
	@Override
	public void errorNotified(WorkerThreadEvent event) {
		LoggerFactory.getLogger(getClass()).error("Error executing worker thread job", event.getThrowable());
	}

	@Override
	public void messageNotified(WorkerThreadEvent event) {}

	@Override
	public void progressNotified(WorkerThreadEvent event) {}

	@Override
	public void startNotified(WorkerThreadEvent event) {}

	@Override
	public void stopNotified(WorkerThreadEvent event) {}
}
