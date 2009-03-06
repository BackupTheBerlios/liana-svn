package org.papernapkin.liana.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SelectionByComponentDialog extends JDialog
{
	// CONSTANTS
	
	private static final long serialVersionUID = 1L;
	
	// CONSTRUCTORS
	
	/** Creates a new dialog which will display the given edit dialog. */
	public SelectionByComponentDialog(
			Frame owner, String title, String msg, boolean modal,
			Component valueSelectionComponent
	)
		throws HeadlessException
	{
		super(owner, title, modal);
		init(valueSelectionComponent, msg, owner);
	}

	/** Creates a new dialog which will display the given edit dialog. */
	public SelectionByComponentDialog(
			Dialog owner, String title, String msg, boolean modal,
			Component valueSelectionComponent
		)
		throws HeadlessException
	{
		super(owner, title, modal);
		init(valueSelectionComponent, msg, owner);
	}

	// MEMBERS
	
	private boolean cancelled = true;
	/** Returns true if the user canceled input. */
	public boolean isCancelled()
	{
		return cancelled;
	}
	private void isCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
	
	// methods
	
	private void init(
			Component valueSelectionComponent, String msg, Container owner
		)
	{
		JPanel content = new JPanel(new BorderLayout(10, 10));
		content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		getContentPane().add(content);
		
		// Icon label
		URL res = getClass().getClassLoader().getResource("org/papernapkin/liana/resources/images/quiz.png");
		Icon icon = new ImageIcon(res);
		JLabel label = new JLabel(icon);
		label.setVerticalAlignment(JLabel.TOP);
		content.add(label, BorderLayout.WEST);
		
		// Message label
		JTextArea msgArea = new JTextArea();
		msgArea.setText(msg);
		msgArea.setEditable(false);
		msgArea.setFocusable(false);
		msgArea.setLineWrap(true);
		msgArea.setOpaque(false);
		msgArea.setWrapStyleWord(true);
		msgArea.setPreferredSize(new Dimension(300, 64));
		content.add(msgArea, BorderLayout.CENTER);

		// The Input Calendar
		Container panel2 = new JPanel(new FlowLayout());
		panel2.add(valueSelectionComponent);
		content.add(panel2, BorderLayout.EAST);

		// OK Button
		panel2 = Box.createHorizontalBox();
		panel2.add(Box.createHorizontalGlue());
		JButton okButton = new JButton("OK");
		okButton.setMnemonic('O');
		getRootPane().setDefaultButton(okButton);
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isCancelled(false);
				setVisible(false);
			}
		});
		panel2.add(okButton);
		
		// Cancel button
		panel2.add(Box.createHorizontalStrut(10));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setMnemonic('C');
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				isCancelled(true); // Just in case
				setVisible(false);
			}
		});
		panel2.add(cancelButton);

		// I want the two buttons to be the same size.
		okButton.setMinimumSize(cancelButton.getMaximumSize());
		okButton.setPreferredSize(cancelButton.getPreferredSize());
		content.add(panel2, BorderLayout.SOUTH);
		
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// Try to center owner.
		Rectangle ownerBounds = owner.getBounds();
		Rectangle dBounds = getBounds();
		dBounds.x = ownerBounds.x + ownerBounds.width / 2 - dBounds.width / 2;
		dBounds.y = ownerBounds.y + ownerBounds.height / 2 - dBounds.height / 2; 
		
		// Ensure we don't go off screen
		if (dBounds.width > screenSize.width) {
			dBounds.width = screenSize.width - 10;
		}
		if (dBounds.x >= screenSize.width) {
			dBounds.x = screenSize.width - dBounds.width;
		}
		if (dBounds.height > screenSize.height) {
			dBounds.height = screenSize.height - 10;
		}
		if (dBounds.y >= screenSize.height) {
			dBounds.y = screenSize.height - dBounds.height;
		}
		if (dBounds.x < 0) {
			dBounds.x = 0;
		}
		if (dBounds.y < 0) {
			dBounds.y = 0;
		}
		setBounds(dBounds);
	}
	
	/**
	 * Opens a dialog with the given component for input modally.
	 * @param owner The owner of the dialog.
	 * @param title The title of the dialog.
	 * @param msg The prompt to show the user.
	 * @param comp The component used for input.
	 * @return True if the user clicked OK.  False if the user clicked Cancel.
	 */
	public static boolean showSelectionByComponentDialog(
			JComponent owner, String title, String msg, Component comp
		)
	{
		SelectionByComponentDialog dialog = null;
		Container container = owner.getTopLevelAncestor();
		if (container instanceof Dialog) {
			dialog = new SelectionByComponentDialog((Dialog)container, title, msg, true, comp);
		} else if (container instanceof Frame) {
			dialog = new SelectionByComponentDialog((Frame)container, title, msg, true, comp);
		} else {
			dialog = new SelectionByComponentDialog((Frame)null, title, msg, true, comp);
		}
		dialog.setVisible(true);
		return ! dialog.isCancelled();
	}
}
