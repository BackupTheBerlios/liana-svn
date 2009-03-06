package org.papernapkin.liana.swing;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * An extension of JInternalFrame with utility methods.
 * 
 * @author Philip A. Chapman
 */
public class ExtInternalFrame extends JInternalFrame
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;
	
	// CONSTRUCTORS

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame()
     */
    public ExtInternalFrame() {
        super();
    }

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame(String)
     */
    public ExtInternalFrame(String title) {
        super(title);
    }

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame(String boolean)
     */
    public ExtInternalFrame(String title, boolean resizable) {
        super(title, resizable);
    }

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame(String, boolean, boolean)
     */
    public ExtInternalFrame(
        String title,
        boolean resizable,
        boolean closable) {
        super(title, resizable, closable);
    }

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame(String, boolean, boolean, boolean)
     */
    public ExtInternalFrame(
        String title,
        boolean resizable,
        boolean closable,
        boolean maximizable) {
        super(title, resizable, closable, maximizable);
    }

    /**
     * @see javax.swing.JInternalFrame#JInternalFrame(String, boolean, boolean, boolean, boolean)
     */
    public ExtInternalFrame(
        String title,
        boolean resizable,
        boolean closable,
        boolean maximizable,
        boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

	/**
	 * Center the frame on the desktop.
	 */
	public void center()
	{
		center(getSize());
	}

	/**
	 * Resize the frame and center it on the desktop.
	 * @param newSize The new size.
	 */
	public void center(Dimension newSize)
	{
		center(newSize.width, newSize.height);
	}

	/**
	 * Resize the frame and center it on the desktop.
	 * @param newWidth The new width.
	 * @param newHeight The new Height.
	 */
	public void center(int newWidth,int newHeight)
	{
		JDesktopPane parent = getDesktopPane();
		Rectangle myBounds = getBounds();
		if (parent == null) {
			// Can't center the frame.  We  don't know the size of the desktop
			myBounds.width = newWidth;
			myBounds.height = newHeight;
		} else {
			// Try to place the frame in the center of the desktop
			Rectangle parentBounds = parent.getBounds();
			myBounds.x = parentBounds.width / 2 - myBounds.width / 2;
			myBounds.y = parentBounds.height / 2 - myBounds.height / 2;
			// Try not to disappear off the desktop
			if (myBounds.x > parentBounds.width) {
				myBounds.x = parentBounds.width - myBounds.width;
			}
			if (myBounds.x < 0) {
				myBounds.x = 0;
			}
			if (myBounds.y > parentBounds.height) {
				myBounds.y = parentBounds.height - myBounds.height;
			}
			if (myBounds.y < 0) {
				myBounds.y = 0;
			}
		}
		setBounds(myBounds);
	}
	
    /**
     * Ensure that the frame's size is not larger than the desktop.
     * @param iFrame
     */
    public void ensureSize(JDesktopPane deskTop)
    {
    	Dimension parentSize = deskTop.getSize();
    	Dimension childSize = this.getSize();
    	if (childSize.height > parentSize.height) {
    		childSize.height = parentSize.height;
    	}
    	if (childSize.width > parentSize.width) {
    		childSize.width = parentSize.width;
    	}
    	this.setSize(childSize);
    }
}
