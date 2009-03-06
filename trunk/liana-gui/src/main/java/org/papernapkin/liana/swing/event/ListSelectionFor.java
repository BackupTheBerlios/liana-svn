package org.papernapkin.liana.swing.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates a method that is to be used to respond to a
 * ListSelectionListener event.
 * 
 * @author pchapman
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ListSelectionFor
{
	/**
	 * The name of the components for which list selection events will be
	 * bound to the annotated method.
	 */
	String[] componentNames();
	
	/**
	 * If true, this method must have three parameters of type
	 * java.lang.Integer, java.lang.Integer, and java.lang.Boolean.  The first
	 * integer will contain the first row whos selection may have been changed.
	 * The second integer is the last row whos selection may have changed.  The
	 * third is whether this is one of multiple change events.
	 */
	boolean passEventMembers() default false;
}
