package org.papernapkin.liana.example;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.papernapkin.liana.awt.event.ActionListenerEventHandler;
import org.papernapkin.liana.swing.event.SwingResponderRegistrationTool;

/**
 * Test for simple App.
 */
public class BindingEventsExample
{
    /**
     * Cannot be instantiated.  We just have a main method that does work
     * for us.
     */
    private BindingEventsExample()
    {
        super();
    }

    public static void main(String[] args)
    {
    	// Initial setup of window
    	
        JFrame frame = new JFrame("A Test of JDI");
        frame.getContentPane().setLayout(new GridLayout(2, 2));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create the object that will respond to swing events.
        
        Responder responder = new Responder();

		
        // Manual Binding.  The action event of two buttons are bound to the
        // responder class by passing the class and method names to a static
        // method.  This method of binding is clean, but requires that you
		// provide the name of the method as a string.  This is bad for
		// refactoring.  However, you can unregister the binding using the
		// returned ActionListenerEventHandler.
        
        JButton button = new JButton("Stuff 1");
        ActionListenerEventHandler.bindActionEventHandler(
        		button, responder, "doStuff1", false
        	);
        frame.getContentPane().add(button);
        
        button = new JButton("Stuff 2");
        button.setActionCommand("A command 2");
        ActionListenerEventHandler.bindActionEventHandler(
        		button, responder, "doStuff2", true
        	);
        frame.getContentPane().add(button);


        // Annotations Binding.  Once the components are set up, a static call
        // to SwingResponderRegisterationTool is used to bindActionEventHandler the controls by
        // declared annotation.  This method is nicer as it is relatively safe
		// for refactoring, but it relies on setting the name of each component.
		// You cannot unregister the listeners.
        
        button = new JButton("Stuff 3");
        button.setName("stuff3");
        frame.getContentPane().add(button);
                
        button = new JButton("Stuff 4");
        button.setActionCommand("A command 4");
        button.setName("stuff4");
        frame.getContentPane().add(button);
        
        // The registration tool will look through all children of the given
        // java.awt.Container for components to bindActionEventHandler.  Therefore, you only have
        // to call this once passing in top level parent.
        SwingResponderRegistrationTool.register(responder, frame);

		
		// Proxy-enabled programmatic bindings.  A registration proxy is
		// created and then re-used for each binding.  This method is
		// cleanest, is refactor proof, and does not rely on magic strings of
		// any sort; including component names.  However, it does require that
		// an Interface be used to define the responder methods of the
		// controller.  Not a bad idea, really.
		// You cannot unregister the listeners.
		IResponder registrationProxy = SwingResponderRegistrationTool.createRegistrationProxy(IResponder.class, responder);

		button = new JButton("Stuff 5");
		frame.getContentPane().add(button);
		ActionListenerEventHandler.bindActionEventHandler(button, registrationProxy, false).doStuff5();

		button = new JButton("Stuff 6");
		button.setActionCommand("A command 6");
		frame.getContentPane().add(button);
		ActionListenerEventHandler.bindActionEventHandler(button, registrationProxy, true).doStuff6(null);

        // Finish show UI
        
        frame.pack();
        frame.setVisible(true);
    }
}
