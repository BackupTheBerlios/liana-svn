package org.papernapkin.liana.swing.notifyingworker;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * WORK_ERROR type worker thread event.  The bound method must have a single
 * parameter of type java.lang.Throwable.
 * 
 * @See {@link WorkerThreadListenerEventHandler#bindErrorNotifiedEventHandler(Object, Object, String)}
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WorkerThreadErrorFor {}
