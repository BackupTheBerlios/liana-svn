package org.papernapkin.liana.awt;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
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
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = comp.getSize();
		Point location = new Point();
		location.y = (int)Math.floor((screensize.height - size.height) / 2);
		location.x = (int)Math.floor((screensize.width - size.width) / 2);
		comp.setLocation(location);
	}
}
