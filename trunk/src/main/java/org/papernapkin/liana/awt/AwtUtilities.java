package org.papernapkin.liana.awt;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Utility methods for use with AWT and Swing components.
 * 
 * @author pchapman
 */
public class AwtUtilities {
	private AwtUtilities() { super(); }

	/**
	 * Centers the dialog over it's parent, or the screen if there is no
	 * parent.  It is centered based on size, not preferred size.
	 */
	public static void center(Dialog dialog) {
		_center(dialog);
	}
	
	/**
	 * Centers the frame on the screen.  It is centered based on size, not
	 * preferred size.
	 */
	public static void center(Frame frame) {
		_center(frame);
	}
	
	/**
	 * Centers the frame on the window.  It is centered based on size, not
	 * preferred size.
	 */
	public static void center(Window window) {
		_center(window);
	}
	
	private static void _center(Component comp) {
		// Get screen and component sizes
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle bounds = comp.getBounds();
		// Ensure the component isn't larger than the screen
		if (bounds.height > screensize.height) {
			bounds.height = screensize.height;
		}
		if (bounds.width > screensize.width) {
			bounds.width = screensize.width;
		}
		// Center the component
		bounds.y = (int)Math.floor((screensize.height - bounds.height) / 2);
		bounds.x = (int)Math.floor((screensize.width - bounds.width) / 2);
		comp.setBounds(bounds);
	}
}
