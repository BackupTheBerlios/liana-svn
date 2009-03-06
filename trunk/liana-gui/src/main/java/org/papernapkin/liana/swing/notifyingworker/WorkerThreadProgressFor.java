package org.papernapkin.liana.swing.notifyingworker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * WORK_PROGRESS type worker thread event.  The bound method must have two
 * parameters of type java.lang.Integer or int.  The first parameter will be
 * populated with the maximum progress value.  The second parameter will be
 * populated with the current progress value.
 * 
 * @See {@link WorkerThreadListenerEventHandler#bindProgressNotifiedEventHandler(Object, Object, String)}
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkerThreadProgressFor {}
