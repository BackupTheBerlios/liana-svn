package org.papernapkin.liana.swing;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 * Autocompleting JComboBox.  this code taken from Java forums. See 
 * http://forum.java.sun.com/thread.jsp?forum=57&thread=167852&start=15&range=15&hilite=false&q=,
 * bottom of page 2.  Subsequent modifications have been made by
 * Philip A. Chapman.
 * 
 * @author Jack Greenbaum (j.greenbaum@computer.org)
 * @author Philip A. Chapman
 */
public class ExtComboBox extends JComboBox
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;

	protected boolean fullSelectOnFocus = true;
	
	/**
	 * This class listens for events and implements autocompletion.  It also
	 * will select the entire text of the edit control when the control gains
	 * focus.
	 *
	 * @author pchapman
	 */
	private class ExtComboBoxListener extends Object
		implements FocusListener
	{
		private ExtComboBox comboBox;
		
		ExtComboBoxListener(ExtComboBox comboBox)
		{
			this.comboBox = comboBox;
		}
		
		/**
		 * Select the text when the control gains focus.
		 */
		public void focusGained(FocusEvent event)
		{
			if ((! event.isTemporary()) && comboBox.getFullSelectOnFocus()) {
				JTextField tf = (JTextField)comboBox.getEditor().getEditorComponent();
				if (tf != null) {
					tf.setSelectionStart(0);
					tf.setSelectionEnd(tf.getText().length());
				}
			}
		}
		
		/**
		 * Deselect the text when the control looses focus.
		 */
		public void focusLost(FocusEvent event)
		{
			if ((! event.isTemporary()) && comboBox.getFullSelectOnFocus()) {
				JTextField tf = (JTextField)comboBox.getEditor().getEditorComponent();
				if (tf != null) {
					int i = tf.getText().length();
					tf.setSelectionStart(i);
					tf.setSelectionEnd(i);
				}
			}
		}
	}

	/**
	 * @see javax.swing.JComboBox#JComboBox(ComboBoxModel)
	 */
	public ExtComboBox(ComboBoxModel aModel) 
	{
		super(aModel);
		initializeAutocomplete();
	}
	
	/**
	 * @see javax.swing.JComboBox#JComboBox()
	 */
	public ExtComboBox()
	{
		super();
		initializeAutocomplete();
	}
	
	/**
	 * @see javax.swing.JComboBox#JComboBox(Object[])
	 */
	public ExtComboBox(Object[] items)
	{
		super(items);
		initializeAutocomplete();
	}
	
	/**
	 * @see javax.swing.JComboBox#JComboBox(Vector)
	 */
	@SuppressWarnings("unchecked")
	public ExtComboBox(Vector items)
	{
		super(items);
		initializeAutocomplete();
	}


	/**
	 * Instantiates a new ExtComboBox with the given FullSelectOnFocus setting.
	 * @see #setFullSelectOnFocus(boolean)
	 * @see javax.swing.JComboBox#JComboBox(ComboBoxModel)
	 */
	public ExtComboBox(ComboBoxModel aModel, boolean fullSelectOnFocus) 
	{
		super(aModel);
		this.fullSelectOnFocus = fullSelectOnFocus;
		initializeAutocomplete();
	}
	
	/**
	 * Instantiates a new ExtComboBox with the given FullSelectOnFocus setting.
	 * @see #setFullSelectOnFocus(boolean)
	 * @see javax.swing.JComboBox#JComboBox()
	 */
	public ExtComboBox(boolean fullSelectOnFocus)
	{
		super();
		this.fullSelectOnFocus = fullSelectOnFocus;
		setEditable(true); // Otherwise, the autocomplete feature is rather useless.
		initializeAutocomplete();
	}
	
	/**
	 * Instantiates a new ExtComboBox with the given FullSelectOnFocus setting.
	 * @see #setFullSelectOnFocus(boolean)
	 * @see javax.swing.JComboBox#JComboBox(Object[])
	 */
	public ExtComboBox(Object[] items, boolean fullSelectOnFocus)
	{
		super(items);
		this.fullSelectOnFocus = fullSelectOnFocus;
		initializeAutocomplete();
	}
	
	/**
	 * Instantiates a new ExtComboBox with the given FullSelectOnFocus setting.
	 * @see #setFullSelectOnFocus(boolean)
	 * @see javax.swing.JComboBox#JComboBox(Vector)
	 */
	@SuppressWarnings("unchecked")
	public ExtComboBox(Vector items, boolean fullSelectOnFocus)
	{
		super(items);
		this.fullSelectOnFocus = fullSelectOnFocus;
		initializeAutocomplete();
	}

	/**
	 * Indicates whether the full text of the control should be selected when
	 * The control gains focus. 
	 * @return True if the full text of the control should be selected when
	 *         the control gains focus, else false.
	 */
	public boolean getFullSelectOnFocus()
	{
		return fullSelectOnFocus;
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if (getModel().getSelectedItem() == null && ae.getActionCommand() != "comboBoxEdited") {
			super.actionPerformed(ae);
		} else {
			// Do not allow super to set the selected item to the editor's text.
			// Simply fire the comboBoxEdited command as the super would.
			String oldCommand = getActionCommand();
			setActionCommand("comboBoxEdited");
			fireActionEvent();
			setActionCommand(oldCommand);
		}
	}
	
	/**
	 * Called by all constructors to set up the components needed for
	 * autocompletion.
	 */
	private void initializeAutocomplete()
	{
	 	// If the combo box is not editable, the following code is useless.
	 	this.setEditable(true);
	 	ExtComboBoxListener listener = new ExtComboBoxListener(this);
	 	AutoCompletion.enable(this);
	 	addFocusListener(listener);
	}
	
	/**
	 * Indicates whether the full text of the control should be selected when
	 * The control gains focus. 
	 * @param fullSelect True if the full text of the control should be
	 *                   selected when the control gains focus, else false.
	 */
	public void setFullSelectOnFocus(boolean fullSelect)
	{
		this.fullSelectOnFocus = fullSelect;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComboBox#selectedItemChanged()
	 */
	@Override
	protected void selectedItemChanged() {
		super.selectedItemChanged();
		getEditor().setItem(getSelectedItem());
	}

	
}  	
