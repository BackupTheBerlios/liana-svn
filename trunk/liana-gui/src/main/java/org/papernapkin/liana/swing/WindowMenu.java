package org.papernapkin.liana.swing;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * A menu which shows all open windows.
 * 
 * @author Philip A. Chapman
 */
public class WindowMenu extends JMenu
	implements ContainerListener, InternalFrameListener, WindowListener
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	private JDesktopPane deskTop;
	private Vector<Component> windows = new Vector<Component>();
	private WindowButtonGroup buttonGroup = new WindowButtonGroup();

	/**
	 * Constructor which creates a WindowMenu with the text "Window" and
	 * mnemonic 'W'.
	 */	
	public WindowMenu()
	{
		this("Window");
		setMnemonic('W');
	}
	
	/**
	 * Constructor which creates a WinowMenu with the text "Window" and
	 * mnemonic 'W'.
	 * @param deskTop A JDesktopPane to which this class should listen for
	 *                the creation of new JInternalFrames.
	 */
	public WindowMenu(JDesktopPane deskTop)
	{
		this("Window", deskTop);
		setMnemonic('W');
	}
	
	/**
	 * Constructor which creates a WindowMenu with the given text
	 * and no default mnemonic 'W'.
	 * @param text The text for the menu.
	 */
	public WindowMenu(String text)
	{
		this(text, null);
	}
	
	/**
	 * Constructor which creates a WindowMenu with the given text
	 * and no default mnemonic 'W'.
	 * @param text The text for the menu.
	 * @param deskTop A JDesktopPane to which this class should listen for
	 *                the creation of new JInternalFrames.
	 */
	public WindowMenu(String text, JDesktopPane deskTop)
	{
		super(text);
		if (deskTop != null) {
			this.deskTop = deskTop;
			deskTop.addContainerListener(this);
			if (deskTop instanceof MDIDesktopPane) {
				// Add Cascade menu item.
				JMenuItem item = new JMenuItem("Cascade");
				item.setMnemonic('C');
				item.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							((MDIDesktopPane)WindowMenu.this.deskTop).cascadeFrames();
						}
					}
				);
				add(item);
				
				// Add Tile menu item.
				item = new JMenuItem("Tile");
				item.setMnemonic('T');
				item.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							((MDIDesktopPane)WindowMenu.this.deskTop).tileFrames();
						}
					}
				);
				add(item);
				
				addSeparator();
			}
		}
	}

	/**
	 * Adds a JDialog to be enumerated in the menu and tracked.
	 * @param dialog The JDialog to enumerate.
	 */	
	public void addDialog(JDialog dialog)
	{
		addListedComponent(dialog);
	}
	
	/**
	 * Adds a JFrame to be enumerated in the menu and tracked.
	 * @param frame The JFrame to enumerate.
	 */
	public void addFrame(JFrame frame)
	{
		addListedComponent(frame);
	}
	
	/**
	 * Adds a JInternalFrame to be enumerated in the menu and tracked.
	 * @param iFrame The JInternalFrame to enumerate.
	 */
	public void addInternalFrame(JInternalFrame iFrame)
	{
		addListedComponent(iFrame);
	}

	/**
	 * Internal method to add a JDialog, JFrame, or JInternalFrame to be
	 * enumerated in the menu and tracked.
	 * @param comp The component to enumerate.
	 */
	private void addListedComponent(Component comp)
	{
		if (! windows.contains(comp)) {
			boolean selected = false;
			if (comp instanceof JInternalFrame) {
				((JInternalFrame)comp).addInternalFrameListener(this);
				if (((JInternalFrame)comp).isSelected()) {
					selected = true;
				}
			} else {
				((Window)comp).addWindowListener(this);
				if (((Window)comp).isActive()) {
					selected = true;
				}
			}
			windows.add(comp);
			JMenuItem menuItem = new JRadioButtonMenuItem();
			menuItem.setVisible(false);
			AbstractAction action = new WindowAction(comp);
			menuItem.setAction(action);
			add(menuItem);
			buttonGroup.add(menuItem);
			refreshItems();
			if (selected) {
				selectListedComponent(comp);
			}
		}
	}
	
    /**
     * @see java.awt.event.ContainerListener#componentAdded(java.awt.event.ContainerEvent)
     * If the component is a JInternalFrame, it is added to the list of
     * components to enumerate.
     */
    public void componentAdded(ContainerEvent e)
    {
    	Component child = e.getChild();
        if (child instanceof JInternalFrame)  {
        	addListedComponent(child);
        }
    }

    /**
     * @see java.awt.event.ContainerListener#componentRemoved(java.awt.event.ContainerEvent)
     */
    public void componentRemoved(ContainerEvent e)
    {
    	// The JDesktop container is constantly removing and readding
    	// JInternalFrames as they are selected and moved.  Therefore, rely on
    	// the InternalFrame events internalFrameClosed and
    	// internalFrameClosing to be called before removing the JInternalFrame.
    }
    
    /**
     * Try to ascertain a good name for the component to be displayed in the
     * menu item's caption.  We try Window.getTitle() or
     * JInternalFrame.getTitle().  If neither give us a non-null value, we
     * call Object.toString(); 
     * @param comp The component to name.
     * @return A name for the component.
     */
    private String getComponentName(Component comp)
    {
    	String name = null;
		// No name was provided, so try to use the title.
		if (comp instanceof JInternalFrame) {
			name = ((JInternalFrame)comp).getTitle();
		} else if (comp instanceof JDialog) {
			name = ((JDialog)comp).getTitle();
		} else if (comp instanceof JFrame) {
			name = ((JFrame)comp).getTitle();
		}
		if (name == null) {
			// No title!  Yikes.
			name = "Window " + comp.toString();
		}
		return name;
    }
    
    public Iterator<Component> getManagedWindows()
    {
    	return Collections.unmodifiableList(windows).iterator();
    }
    
	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
	 * Selects the related menu item.
	 */
	public void internalFrameActivated(InternalFrameEvent e)
	{
		selectListedComponent(e.getInternalFrame());
	}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
	 * Removes the related menu item.
	 */
	public void internalFrameClosed(InternalFrameEvent e)
	{
		JInternalFrame f = e.getInternalFrame();
		f.removeInternalFrameListener(this);
		removeListedComponent(f);
	}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
	 * Removes the related menu item.
	 */
	public void internalFrameClosing(InternalFrameEvent e)
	{
		JInternalFrame f = e.getInternalFrame();
		f.removeInternalFrameListener(this);
		removeListedComponent(f);
	}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
	 */
	public void internalFrameDeactivated(InternalFrameEvent e) {}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
	 */
	public void internalFrameDeiconified(InternalFrameEvent e) {}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
	 */
	public void internalFrameIconified(InternalFrameEvent e) {}

	/**
	 * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
	 */
	public void internalFrameOpened(InternalFrameEvent e) {}
    
	/**
	 * Removes the indicated JDialog from the components that are enumerated
	 * in the menu.
	 * @param dialog The JDialog to remove.
	 */
    public void removeDialog(JDialog dialog)
    {
    	removeListedComponent(dialog);
    }
    
	/**
	 * Removes the indicated JFrame from the components that are enumerated in
	 * the menu.
	 * @param dialog The JFrame to remove.
	 */
    public void removeFrame(JFrame frame)
    {
    	removeListedComponent(frame);
    }
    
    /**
     * Removes the indicated JInternalFrame from the components that are
     * enumerated in the menu.
     * @param iFrame
     */
    public void removeInternalFrame(JInternalFrame iFrame)
    {
    	removeListedComponent(iFrame);
    }
    
    /**
     * Internal method which removes the indicated JDialog, JFrame, or
     * JInternalFrame from the components that are enumerated in the menu.
     * @param comp
     */
    private void removeListedComponent(Component comp)
    {
    	int index = windows.indexOf(comp);
    	if (index > -1) {
    		windows.remove(index);
    		if (comp instanceof JInternalFrame) {
    			((JInternalFrame)comp).removeInternalFrameListener(this);
    		} else {
    			((Window)comp).removeWindowListener(this);
    		}
    		JMenuItem item;
    		for (int i = 0; i < getItemCount(); i++) {
    			item = getItem(i);
				if (item != null && item.getAction() instanceof WindowAction) {
					Component comp2 = ((WindowAction)item.getAction()).getComponent();
					if (comp.equals(comp2)) {
						remove(item);
						break;
					}
				}
    		}
			refreshItems();
    	}
    }

	/**
	 * Refreshes (re-numbers and re-titles) JMenuItems under the Window menu.
	 * This method is called internally when windows are added or removed.
	 * This method could also be called to re-title the items if a window's
	 * title changes, but that's not done by default and would have to come
	 * from an external source.
	 */
	public void refreshItems()
	{
		JMenuItem item;
		int i = 0;
		int itemCount = 0;
		String itemCountS;
		while (i < getItemCount() && itemCount < 9) { // TODO create a sub-menu for overflow
			item = getItem(i);
			if (item != null && item.getAction() instanceof WindowAction) {
				Component comp = ((WindowAction)item.getAction()).getComponent();
				String name = getComponentName(comp);
				itemCountS = String.valueOf(++itemCount);
				item.setText(itemCountS + ' ' + name);
				item.setMnemonic(itemCountS.charAt(0));
				item.setVisible(true);
			}
			i++;
		}
	}
    
    /**
     * Selects the menu item related to the component.  This is called from
     * event listener methods when a JDialog, JFrame, or JInternalFrame is
     * activated.
     * @param comp The component to be marked as selected.
     */
    private void selectListedComponent(Component comp)
    {
    	Component comp2;
    	JMenuItem item;
    	for (int i = 0; i < getItemCount(); i++) {
    		item = getItem(i);
    		if (item != null && item.getAction() instanceof WindowAction) {
    			comp2 = ((WindowAction)item.getAction()).getComponent();
    			if (comp == comp2) {
    				item.setSelected(true);
    				break;
    			}
    		}
    	}
    }
    
	/**
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 * Selects the related menu item.
	 */
	public void windowActivated(WindowEvent e)
	{
		selectListedComponent(e.getWindow());
	}

    /**
     * @see java.awt.event.WindowListener#windowCloseded(java.awt.event.WindowEvent)
     * Removes the related menu item.
     */
    public void windowClosed(WindowEvent e)
    {
    	Window w = e.getWindow();
		w.removeWindowListener(this);
    	removeListedComponent(w);
    }
    
    /**
     * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
     * Removes the related menu item.
     */
    public void windowClosing(WindowEvent e)
    {
    	Window w = e.getWindow();
    	w.removeWindowListener(this);
    	removeListedComponent(w);
    }

	/**
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	public void windowDeactivated(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	public void windowDeiconified(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	public void windowIconified(WindowEvent e) {}

	/**
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	public void windowOpened(WindowEvent e) {}
	
    /**
     * @see java.lang.Object#finalize()
     * Unregisters this class from all the JDesktop, JDialogs, JFrame, and
     * JInternalFrames it listens to.
     */
    protected void finalize() throws Throwable
    {
    	// Remove myself from the listeners for all JInternalFrames, JDialogs,
    	// and JFrames.
    	Component comp;
    	JMenuItem item;
		for (int i = 0; i < getItemCount(); i++) {
			item = getItem(i);
			if (item != null && item.getAction() instanceof WindowAction) {
				comp = ((WindowAction)item.getAction()).getComponent();
				if (comp instanceof JInternalFrame) {
					((JInternalFrame)comp).removeInternalFrameListener(this);
				} else if (comp instanceof Window) {
					((Window)comp).removeWindowListener(this);
				}
			}
		}
		// Remove myself from the listeners for the deskTop.
		if (deskTop != null) {
			deskTop.removeContainerListener(this);
		}
        super.finalize();
    }

}

/**
 * WindowAction
 * -
 * An action which makes the given window or JInternalFrame active.
 * 
 * <P><B>Revision History:</B><UL>
 * <LI>Mar 30, 2004 Class Created By pchapman.</LI>
 * </UL></P>
 */
class WindowAction extends AbstractAction
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	private static final String WINDOW = "window";
	
	WindowAction(Component comp)
	{
		putValue(WINDOW, comp);
	}

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     * Selects the related component (JInternalFrame or Window).
     */
    public void actionPerformed(ActionEvent e)
    {
    	Object o = getValue(WINDOW);
        if (o instanceof JInternalFrame) {
        	try {
        		JInternalFrame iFrame = (JInternalFrame)o;
        		iFrame.setSelected(true);
	        	if (iFrame.isIcon()) {
	        		iFrame.setIcon(false);
	        	}
        	} catch (PropertyVetoException pve) {}
        } else if (o instanceof Window) {
        	((Window)o).toFront();
        }
    }

    /**
     * The component which will be made active when the actionPerformed method
     * is called.
     * @return The component.
     */
    public Component getComponent()
    {
    	return (Component)getValue(WINDOW);
    }
}

/**
 * pac.swing.WindowButtonGroup
 * -
 * Provides the mutual exclusive properties of the Window menu items.
 *
 * <P>Revision History:
 * <BR>03/30/2004 This class was created.
 *
 * @author Philip A. Chapman
 */
class WindowButtonGroup extends javax.swing.ButtonGroup
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	/**
	 * Indicates if the object is one of the Window menu items managed by this
	 * instance of the LookAndFeelButtonGroup class.
	 * @param obj The object to test.
	 * @return True if the object is one of the managed Window menu items or
	 *         False if it is not.
	 */
	boolean contains(Object obj)
	{
		return buttons.contains(obj);
	}

	/**
	 * Enables or disables all the Window menu items at once.  I'm not sure
	 * why you'd want to do that, but I put the functionallity here anyway.
	 * @param enabled When true will cause all the Window menu items to
	 *                be enabled.  When false, they will all be disabled.
	 */
	void setItemsEnabled(boolean enabled)
	{
		for (Enumeration<?> enm = buttons.elements(); enm.hasMoreElements();) {
			((JMenuItem)enm.nextElement()).setEnabled(enabled);
		}
	}
}