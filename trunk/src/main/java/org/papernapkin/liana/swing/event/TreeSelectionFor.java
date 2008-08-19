package org.papernapkin.liana.swing.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * TreeSelectionListener event.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TreeSelectionFor
{
	/**
	 * The name of the components for which list selection events will be
	 * bound to the annotated method.
	 */
	String[] componentNames();
	
	/**
	 * If true, the method must have one parameter of type java.lang.Object.
	 * Either the last object in the selected tree path or null will be passed
	 * to the method. @see javax.swing.event.TreeSelectionEvent
	 * @see javax.swing.tree.TreePath
	 */
	boolean passSelectedObject() default false;
}
