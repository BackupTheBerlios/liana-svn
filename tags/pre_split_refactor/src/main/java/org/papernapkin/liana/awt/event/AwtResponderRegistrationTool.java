package org.papernapkin.liana.awt.event;

import java.awt.Component;
import java.awt.Container;

import java.lang.reflect.Method;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JMenu;

import org.papernapkin.liana.awt.event.ActionFor;
import org.papernapkin.liana.awt.event.ActionListenerEventHandler;
import org.papernapkin.liana.awt.event.FocusGainedFor;
import org.papernapkin.liana.awt.event.FocusListenerEventHandler;
import org.papernapkin.liana.awt.event.FocusLostFor;
import org.papernapkin.liana.awt.event.WindowActivatedFor;
import org.papernapkin.liana.awt.event.WindowClosedFor;
import org.papernapkin.liana.awt.event.WindowClosingFor;
import org.papernapkin.liana.awt.event.WindowDeactivatedFor;
import org.papernapkin.liana.awt.event.WindowDeiconifiedFor;
import org.papernapkin.liana.awt.event.WindowIconifiedFor;
import org.papernapkin.liana.awt.event.WindowListenerEventHandler;
import org.papernapkin.liana.awt.event.WindowOpenedFor;
import org.slf4j.LoggerFactory;

/**
 * A tool that will bind the methods of responders to the appropriate component
 * events based on annotations.
 * 
 * @author pchapman
 */
public class AwtResponderRegistrationTool
{
	private static final AwtResponderRegistrationTool instance = new AwtResponderRegistrationTool();
	
	/**
	 * This class may not be instantiated outside this class.
	 */
	protected AwtResponderRegistrationTool() {}

	/**
	 * Registers the responder with the component and/or its children if any
	 * based on annotations.
	 * @param responder The responder.
	 * @param component The component.
	 */
	public static void register(Object responder, Component component)
	{
		instance._register(responder, component);
	}
	
	protected void _register(Object responder, Component component) {
		Set<Object>sources;
		Class<?> claz = responder.getClass();
		for (Method m: claz.getDeclaredMethods()) {
			//TODO add other event annotations
			if (m.isAnnotationPresent(ActionFor.class)) {
				ActionFor annot = (ActionFor)m.getAnnotation(ActionFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerActionFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(FocusGainedFor.class)) {
				FocusGainedFor af = m.getAnnotation(FocusGainedFor.class);
				sources = locateSources(af.componentNames(), component, new HashSet<Object>());
				registerFocusGainedFor(sources, responder, m, af);
			} else if (m.isAnnotationPresent(FocusLostFor.class)) {
				FocusLostFor af = m.getAnnotation(FocusLostFor.class);
				sources = locateSources(af.componentNames(), component, new HashSet<Object>());
				registerFocusLostFor(sources, responder, m, af);
			} else if (m.isAnnotationPresent(MouseClickedFor.class)) {
				MouseClickedFor annot = (MouseClickedFor)m.getAnnotation(MouseClickedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerMouseClickedFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(MouseDoubleClickedFor.class)) {
				MouseDoubleClickedFor annot = (MouseDoubleClickedFor)m.getAnnotation(MouseDoubleClickedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerMouseDoubleClickedFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(MousePopupClickedFor.class)) {
				MousePopupClickedFor annot = (MousePopupClickedFor)m.getAnnotation(MousePopupClickedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerMousePopupClickedFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowActivatedFor.class)) {
				WindowActivatedFor annot = (WindowActivatedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowClosedFor.class)) {
				WindowClosedFor annot = (WindowClosedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowClosingFor.class)) {
				WindowClosingFor annot = (WindowClosingFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowDeactivatedFor.class)) {
				WindowDeactivatedFor annot = (WindowDeactivatedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowDeiconifiedFor.class)) {
				WindowDeiconifiedFor annot = (WindowDeiconifiedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowIconifiedFor.class)) {
				WindowIconifiedFor annot = (WindowIconifiedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			} else if (m.isAnnotationPresent(WindowOpenedFor.class)) {
				WindowOpenedFor annot = (WindowOpenedFor)m.getAnnotation(WindowActivatedFor.class);
				sources = locateSources(annot.componentNames(), component, new HashSet<Object>());
				registerWindowListenerFor(sources, responder, m, annot);
			}
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerActionFor(
			Set<Object>sources, Object responder, Method m, ActionFor a
		)
	{
		for (Object o : sources) {
			ActionListenerEventHandler.bindActionEventHandler(
					o, responder, m.getName(), a.bindActionCommand()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerFocusGainedFor(
			Set<Object>sources, Object responder, Method m, FocusGainedFor a
		)
	{
		for (Object o : sources) {
			FocusListenerEventHandler.bindFocusGainedEventHandler(
					o, responder, m.getName(), a.bindIsTemporary()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerFocusLostFor(
			Set<Object>sources, Object responder, Method m, FocusLostFor a
		)
	{
		for (Object o : sources) {
			FocusListenerEventHandler.bindFocusLostEventHandler(
					o, responder, m.getName(), a.bindIsTemporary()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerMouseClickedFor(
			Set<Object>sources, Object responder, Method m, MouseClickedFor a
		)
	{
		for (Object o : sources) {
			MouseListenerEventHandler.bindMouseClickedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerMouseDoubleClickedFor(
			Set<Object>sources, Object responder, Method m, MouseDoubleClickedFor a
		)
	{
		for (Object o : sources) {
			MouseListenerEventHandler.bindMouseDoubleClickedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of action events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerMousePopupClickedFor(
			Set<Object>sources, Object responder, Method m, MousePopupClickedFor a
		)
	{
		for (Object o : sources) {
			MouseListenerEventHandler.bindMousePopupClickedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowActivatedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowActivatedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowClosedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowClosedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowClosingFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowClosingEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowDeactivatedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowDeactivatedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowDeiconifiedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowDeiconifiedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowIconifiedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowIconifiedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Registers the responder for the given event sources.
	 * @param sources The sources of events.
	 * @param responder The responder to react to the events.
	 * @param m The method called in response to the event.
	 * @param a The annotation on the method.
	 */
	protected void registerWindowListenerFor(
			Set<Object>sources, Object responder, Method m, WindowOpenedFor a
		)
	{
		for (Object o : sources) {
			WindowListenerEventHandler.bindWindowOpenedEventHandler(
					o, responder, m.getName()
				);
		}
	}
	
	/**
	 * Adds the component to the set if its name is in the list of names.
	 * Also, if the component is a container, its children are also queried
	 * to be matched with the list of names.
	 * @param nameList The list of names.
	 * @param component The component to test.
	 * @param sources The sources to add the component to.
	 * @return The set of sources (for chaining)
	 */
	protected Set<Object> locateSources(
			String[] nameList, Component component, Set<Object>sources
		)
	{
		if (component == null) {
			LoggerFactory.getLogger(getClass()).warn("Asked to locate sources for a null component");
		} else {
			String name = component.getName();
			if (name != null && name.length() > 0) {
				for (String n : nameList) {
					if (n.equals(name)) {
						sources.add(component);
					}
				}
			}
			if (component instanceof JMenu) {
				// JMenu doesn't follow the normal rules for children
				JMenu menu = (JMenu)component;
				for (int i = 0; i < menu.getItemCount(); i++) {
					locateSources(nameList, menu.getItem(i), sources);
				}
			}
			if (component instanceof Container) {
				for (Component comp : ((Container)component).getComponents()) {
					// Recursively call this function to process the component
					locateSources(nameList, comp, sources);
				}
			}
		}
		return sources;
	}
}
