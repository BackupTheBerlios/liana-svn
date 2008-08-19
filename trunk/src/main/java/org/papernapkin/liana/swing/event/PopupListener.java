package org.papernapkin.liana.swing.event;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

/**
 * A MouseAdapter suitable for showing a JPopupMenu when a component that this
 * class is registered with is right-clicked.
 * 
 * @author pchapman
 */
public class PopupListener extends MouseAdapter
{
	private JPopupMenu menu;
	
	/**
	 * Creates a new PopupListener which will show the given menu. 
	 */
	public PopupListener(JPopupMenu menu)
	{
		super();
		this.menu = menu;
	}
	
    public void mousePressed(MouseEvent e)
    {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e)
    {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
    	if (e.isPopupTrigger() && menu.isEnabled()) {
    		menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
