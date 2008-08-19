package org.papernapkin.liana.swing.layout;

import java.awt.Dimension;

/**
 * An invisible component which takes up no vertical space, and as much
 * horizontal space as possible.
 *
 * @author Philip A. Chapman
 */
public class HorizontalGlue extends FillerComponent
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

    /**
     * Creates a horizontal glue component.
     *
     * @return the component
     */
    public HorizontalGlue()
    {
    	super(new Dimension(0,0), new Dimension(0,0), 
			  new Dimension(Short.MAX_VALUE, 0));
    }
}
