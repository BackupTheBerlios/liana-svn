package org.papernapkin.liana.swing.layout;

import java.awt.Dimension;

/**
 * An invisible component which takes up no horizontal space, and as much
 * vertical space as possible.
 *
 * @author Philip A. Chapman
 */
public class VerticalGlue extends FillerComponent
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

    /**
     * Creates a vertical glue component.
     *
     * @return the component
     */
    public VerticalGlue()
	{
    	super(new Dimension(0,0), new Dimension(0,0), 
			  new Dimension(0, Short.MAX_VALUE));
    }
}
