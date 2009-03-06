package org.papernapkin.liana.swing;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * Selects all the text in a JTextControl when it gets the focus.  Erases data
 * entry by allowing easy overtype of default values.
 *
 * @author Philip A. Chapman
 */
public class TextSelectionFocusListener implements FocusListener
{
	/**
	 * Select the text when the control gains focus.
	 */
	public void focusGained(FocusEvent event)
	{
		if (! event.isTemporary()) {
			Object o = event.getSource();
			if (o instanceof JFormattedTextField) {
				// This is a major hack.  If I were to do it like a normal
				// Text field, something further down the line would unselect
				// the text after I select it.
				// I don't like having the same code in two places, but I'd
				// rather not implement the hack for al JTextFields
				final JFormattedTextField tf = (JFormattedTextField)o;
		 		Runnable r = new Runnable() {
		 			public void run() {
				 		tf.setSelectionStart(0);
				 		tf.setSelectionEnd(tf.getText().length());
		 			}
		 		};
		 		SwingUtilities.invokeLater(r);
			} else if (o instanceof JTextComponent) {
				JTextComponent tc = (JTextComponent)o;
				tc.setSelectionStart(0);
				tc.setSelectionEnd(tc.getText().length());
			}
		}
	}
	
	/**
	 * Unselect the text so that it doesn't look conspicuous. 
	 */
	public void focusLost(FocusEvent event)
	{
		if ((! event.isTemporary())) {
			Object o = event.getSource();
			if (o instanceof JTextField) {
				JTextField tf = (JTextField)o;
				int i = tf.getText().length();
				tf.setSelectionStart(i);
				tf.setSelectionEnd(i);
			}
		}
	}
}
