package org.papernapkin.liana.swing.notifyingworker;

/**
 * An event used to report events in the NotifyingWorkerThread as it works.
 *
 * @author Philip A. Chapman
 */
public class WorkerThreadEvent
{
	// CONSTANTS
	
	public enum Type {
		/** Indicates an error was encountered. */
		WORK_ERROR("errorNotified"),
		/** Indicates a message has been sent to the listeners. */
		WORK_MESSAGE("messageNotified"),
		/** Indicates some progress has been made. */
		WORK_PROGRESS("progressNotified"),
		/** Indicates that work has started. */
		WORK_START("startNotified"),
		/** Indicates that work has stopped. */
		WORK_STOP("stopNotified");
		
		private String listenerMethod;
		private Type(String listenerMethod) {
			this.listenerMethod = listenerMethod;
		}
		
		String getListenerMethod() {
			return listenerMethod;
		}
	}
	
	// CONSTRUCTORS
	
	/**
	 * Builds a new message type event that will contain the message for the
	 * listener.  The message is usually a human-readable message which is
	 * intended to be relayed to the user.
	 * @param source The source of the event.
	 * @param message The message to pass.
	 */
	public WorkerThreadEvent(NotifyingWorkerThread source, String message)
	{
		this(source, Type.WORK_MESSAGE);
		setMessage(message);
	}
	
	/**
	 * Builds a new progress type event that will contain a maximum value for
	 * progress and the current value of progress.  For instance, if the max
	 * value is 100 and the current value of progress is 25, the work is 25%
	 * done.
	 * @param source The source of the event.
	 * @param maxProgress The maximum value which will be reached upon
	 *                    completion.
	 * @param currentProgress The current value which indicates progress.
	 */
	public WorkerThreadEvent(NotifyingWorkerThread source, int maxProgress, int currentProgress)
	{
		this(source, Type.WORK_PROGRESS);
		setMaxProgressValue(maxProgress);
		setCurrentProgressValue(currentProgress);
	}

	/**
	 * Creates a new error type event which will notify the listener of an
	 * error.
	 * @param source The source of the event.
	 * @param t The exception which was encountered.
	 */
	public WorkerThreadEvent(NotifyingWorkerThread source, Throwable t)
	{
		this(source, Type.WORK_ERROR);
		setThrowable(t);
	}
	
	/**
	 * Creates a new event of the given type.
	 * @param source The source of the event.
	 * @param eventType The type of event.
	 */
	public WorkerThreadEvent(NotifyingWorkerThread source, Type eventType)
	{
		super();
		setEventType(eventType);
		setSource(source);
	}
	
	// MEMBERS
	
	private int currentProgressValue = -1;
	/**
	 * Gets a value indicating current progress.  Only valid if the event is of
	 * type @see {@link Type#WORK_PROGRESS}
	 */
	public int getCurrentProgressValue()
	{
		return currentProgressValue;
	}
	/**
	 * Sets value indicating current progress.  Only valid if the event is of
	 * type @see {@link Type#WORK_PROGRESS}
	 */
	public void setCurrentProgressValue(int value)
	{
		this.currentProgressValue = value;
	}
	
	private Type eventType;
	/** Gets the type of event.  @see Type */
	public Type getEventType()
	{
		return eventType;
	}
	/** Sets the type of event.  @see Type */
	public void setEventType(Type eventType)
	{
		this.eventType = eventType;
	}
	
	private int maxProgressValue = -1;
	/**
	 * Gets the maximum value indicating progress.  This will be the value of
	 * current progress once all work is done.
	 */
	public int getMaxProgressValue()
	{
		return maxProgressValue;
	}
	/**
	 * Sets the maximum value indicating progress.  This will be the value of
	 * current progress once all work is done.
	 */
	public void setMaxProgressValue(int value)
	{
		this.maxProgressValue = value;
	}
	
	private String message;
	/**
	 * Gets a message about the the work being done.  Usually a human
	 * readable message to be relayed to the user.
	 */
	public String getMessage()
	{
		return message;
	}
	/**
	 * Sets a message about the the work being done.  Usually a human
	 * readable message to be relayed to the user.
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	private NotifyingWorkerThread source;
	/** Gets the source of the vent. */
	public NotifyingWorkerThread getSource()
	{
		return source;
	}
	/** Sets the source of the vent. */
	public void setSource(NotifyingWorkerThread source)
	{
		this.source = source;
	}
	
	private Throwable throwable;
	/** Gets the exception which occurred while doing the work. */
	public Throwable getThrowable()
	{
		return throwable;
	}
	/** Sets the exception which occurred while doing the work. */
	public void setThrowable(Throwable t)
	{
		this.throwable = t;
	}
}
