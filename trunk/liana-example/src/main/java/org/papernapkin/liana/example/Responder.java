package org.papernapkin.liana.example;

import org.papernapkin.liana.awt.event.ActionFor;

/**
 * The responder that will respond to events in the window created by BindingEventsExample.
 * 
 * @author pchapman
 */
class Responder implements IResponder {
	/**
	 * Creates a new responder for the BindingEventsExample class.
	 */
	Responder()
	{
		super();
	}
	
	/**
	 * Responds to BindingEventsExample's "Stuff1" button being clicked.
	 */
	public void doStuff1()
	{
		System.out.println("Stuff 1");
	}
	
	/**
	 * Responds to BindingEventsExample's "Stuff2" button being clicked.
	 */
	public void doStuff2(String command)
	{
		System.out.println("Stuff 2\t  Action: " + command);
	}
	
	/**
	 * Responds to BindingEventsExample's "Stuff3" button being clicked.
	 */
	@ActionFor(componentNames={"stuff3"})
	public void doStuff3()
	{
		System.out.println("Stuff 3");
	}
	
	/**
	 * Responds to BindingEventsExample's "Stuff4" button being clicked.
	 */
	@ActionFor(componentNames={"stuff4"}, bindActionCommand=true)
	public void doStuff4(String command)
	{
		System.out.println("Stuff 4\t  Action: " + command);
	}

	/**
	 * Responds to BindingEventsExample's "Stuff5" button being clicked.
	 */
	@Override
	public void doStuff5()
	{
		System.out.println("Stuff 5");
	}

	/**
	 * Responds to BindingEventsExample's "Stuff6" button being clicked.
	 */
	@Override
	public void doStuff6(String command)
	{
		System.out.println("Stuff 6\t  Action: " + command);
	}
}
