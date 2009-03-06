package org.papernapkin.liana.swing;

import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * A JMenuItem with constructors that I find more helpful.
 * 
 * @author Philip A. Chapman
 */
public class ExtMenuItem extends JMenuItem
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	/**
	 * Takes care of the redundant code for creating JMenuItems.
	 * @param caption The caption to be used by the menu item.
	 * @param mnemonic The mnemonic to be used by the menu item.
	 * @param parentMenuItem The menu item which is to be this item's parent.
	 *                       This parameter can be null.
	 * @param listener The object which will be listening for action events.
	 *                 This parameter can be null.
	 */
	public ExtMenuItem
		(String caption, char mnemonic, JMenuItem parentMenuItem,
		 ActionListener listener)
	{
		super(caption);
		if (listener != null) {
			addActionListener(listener);
		}
		setMnemonic(mnemonic);
		if (parentMenuItem != null) {
			parentMenuItem.add(this);
		}
	}
	
	/**
	 * Takes care of the redundant code for creating JMenuItems with images.
	 * @param caption The caption to be used by the menu item.
	 * @param mnemonic The mnemonic to be used by the menu item.
	 * @param parentMenuItem The menu item which is to be this item's parent.
	 *                       This parameter can be null.
	 * @param listener The object which will be listening for action events.
	 *                 This parameter can be null.
	 * @param icon The icon to be used by the menu item.
	 */
	public ExtMenuItem
		(String caption, char mnemonic, JMenuItem parentMenuItem,
		 ActionListener listener, Icon icon)
	{
		super(caption, icon);
		if (listener != null) {
			addActionListener(listener);
		}
		setMnemonic(mnemonic);
		if (parentMenuItem != null) {
			parentMenuItem.add(this);
		}
	}
	
	/**
	 * Takes care of the redundant code for creating JMenuItems with images.
	 * @param caption The caption to be used by the menu item.
	 * @param mnemonic The mnemonic to be used by the menu item.
	 * @param parentMenuItem The menu item which is to be this item's parent.
	 *                       This parameter can be null.
	 * @param listener The object which will be listening for action events.
	 *                 This parameter can be null.
	 * @param iconURL The URL from which the icon's image can be loaded.
	 */
	public ExtMenuItem
		(String caption, char mnemonic, JMenuItem parentMenuItem,
		 ActionListener listener, URL iconURL)
	{
		this(caption, mnemonic, parentMenuItem, listener);
		if (iconURL != null) {
			setIcon(new ImageIcon(iconURL));
		}
	}
}
