package org.papernapkin.liana.swing;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JWindow;

/**
 * Contains miscellaneous helper methods.
 *
 * <P><B>Revision History:</B><UL>
 * <LI>Sep 14, 2004 This class was created by pchapman.</LI>
 * </UL></P>
 */
public class SwingToolbox
{
	/**
	 * Finds the JDialog in the component's parent heirarchy, or null if one
	 * does not exist.
	 * @param comp The component's heirarchy to search.
	 * @return The JDialog, or null.
	 */
	public static JDialog findParentJDialog(Component comp)
	{
		while (comp != null && ! (comp instanceof JDialog))
		{
			comp = comp.getParent();
		}
		
		return (JDialog)comp;
	}
	
	/**
	 * Finds the JFrame in the component's parent heirarchy, or null if one
	 * does not exist.
	 * @param comp The component's heirarchy to search.
	 * @return The JFrame, or null.
	 */
	public static JFrame findParentJFrame(Component comp)
	{
		while (comp != null && ! (comp instanceof JFrame))
		{
			comp = comp.getParent();
		}
		
		return (JFrame)comp;
	}
	
	
	/**
	 * Finds the JWindow in the component's parent heirarchy, or null if one
	 * does not exist.
	 * @param comp The component's heirarchy to search.
	 * @return The JWindow, or null.
	 */
	public static JWindow findParentJWindow(Component comp)
	{
		while (comp != null && ! (comp instanceof JWindow))
		{
			comp = comp.getParent();
		}
		
		return (JWindow)comp;
	}
	
	/**
	 * Finds the JInternalFrame in the component's parent heirarchy, or null if one
	 * does not exist.
	 * @param comp
	 * @return
	 */
	public static JInternalFrame findParentJInternalFrame(Component comp) {
		while (comp != null && ! (comp instanceof JInternalFrame))
		{
			comp = comp.getParent();
		}
		
		return (JInternalFrame)comp;
	}
}
