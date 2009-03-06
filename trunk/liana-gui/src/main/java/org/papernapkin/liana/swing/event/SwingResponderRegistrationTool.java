package org.papernapkin.liana.swing.event;

import java.awt.Component;

import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTable;

import org.papernapkin.liana.awt.event.AwtResponderRegistrationTool;

/**
 * A tool that will bind the methods of responders to the appropriate component
 * events based on annotations.
 * 
 * @author pchapman
 */
public final class SwingResponderRegistrationTool extends AwtResponderRegistrationTool
{
	private static final SwingResponderRegistrationTool instance = new SwingResponderRegistrationTool();
	
	/**
	 * This class may not be instantiated.
	 */
	private SwingResponderRegistrationTool() {}

	/**
	 * Registers the responder with the component and/or its children if any
	 * based on annotations.  Because many swing UIs rely also on Awt events,
	 * AwtResponderRegistrationTool.register is also called.  Therefore, only
	 * this one call is required for the use of mixed awt and swing event
	 * handler annotations.
	 * @param responder The responder.
	 * @param component The component.
	 */
	public static void register(Object responder, Component component)
	{
		instance._register(responder, component);
	}
	
	@Override
	protected void _register(Object responder, Component component) {
		super._register(responder, component);
		Class<?> claz = responder.getClass();
		Set<Object>sources;
		for (Method m: claz.getDeclaredMethods()) {
			//TODO add other event annotations
			if (m.isAnnotationPresent(ChangeFor.class)) {
				ChangeFor annot = (ChangeFor)m.getAnnotation(ChangeFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerChangeFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(ListSelectionFor.class)) {
				ListSelectionFor annot = (ListSelectionFor)m.getAnnotation(ListSelectionFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerListSelectionFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(TreeSelectionFor.class)) {
				TreeSelectionFor annot = (TreeSelectionFor)m.getAnnotation(TreeSelectionFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerTreeSelectionFor(sources, responder, m, annot);
			}
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method calld in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerChangeFor(
			Set<Object>sources, Object responder, Method m, ChangeFor a
		)
	{
		for (Object o : sources) {
			ChangeListenerEventHandler.bindChangeEventHandler(o, responder, m.getName());
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method calld in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerListSelectionFor(
			Set<Object>sources, Object responder, Method m, ListSelectionFor a
		)
	{
		for (Object o : sources) {
			if (o instanceof JTable) {
				// JTables are special in that you must get the list selection
				// model and register with it directly.  You cannot add a list
				// selection listener via an addXXX method.
				ListSelectionListenerEventHandler.bindValueChangedEventHandler(
						((JTable)o).getSelectionModel(), responder,
						m.getName(), a.passEventMembers()
					);
			} else {
				ListSelectionListenerEventHandler.bindValueChangedEventHandler(
						o, responder, m.getName(), a.passEventMembers()
					);
			}
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method calld in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerTreeSelectionFor(
			Set<Object>sources, Object responder, Method m, TreeSelectionFor a
		)
	{
		for (Object o : sources) {
			TreeSelectionListenerEventHandler.bindValueChangedEventHandler(
					o, responder, m.getName(), a.passSelectedObject()
				);
		}
	}
}
