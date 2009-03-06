package org.papernapkin.liana.example;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.papernapkin.liana.awt.event.ActionListenerEventHandler;
import org.papernapkin.liana.swing.event.SwingResponderRegistrationTool;

/**
 * Test for simple App.
 */
public class AppTest
{
    /**
     * Cannot be instantiated.  We just have a main method that does work
     * for us.
     *
     * @param testName name of the test case
     */
    private AppTest()
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
        // method.
        
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
        // to SwingResponderRegisterationTool is used to bind the controls by
        // declared annotation.
        
        button = new JButton("Stuff 3");
        button.setName("stuff3");
        frame.getContentPane().add(button);
                
        button = new JButton("Stuff 4");
        button.setActionCommand("A command 4");
        button.setName("stuff4");
        frame.getContentPane().add(button);
        
        // The registration tool will look through all children of the given
        // java.awt.Container for components to bind.  Therefore, you only have
        // to call this once passing in top level parent.
        SwingResponderRegistrationTool.register(responder, frame);
        
        // Finish show UI
        
        frame.pack();
        frame.setVisible(true);
    }
}
