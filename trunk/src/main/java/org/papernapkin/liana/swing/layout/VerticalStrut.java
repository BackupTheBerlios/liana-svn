package org.papernapkin.liana.swing.layout;

import java.awt.Dimension;


/**
 * An invisible component which takes up no horizontal space and a fixed
 * vertical space.
 *
 * @author Philip A. Chapman
 */
public class VerticalStrut extends FillerComponent
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new vertical strut with a height of 0.
	 */
	public VerticalStrut() {
		this(0);
	}
	
	/**
	 * Creates a new vertical strut with the given height.
	 * @param height The height of the strut.
	 */
	public VerticalStrut(int height) {
		super(
				new Dimension(0, height), new Dimension(0, height),
				new Dimension(Short.MAX_VALUE, height)
			);
	}
}
