package org.papernapkin.liana.swing.notifyingworker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;

import org.papernapkin.liana.awt.event.ActionFor;
import org.papernapkin.liana.locale.Translation;
import org.papernapkin.liana.swing.event.SwingResponderRegistrationTool;
import org.slf4j.LoggerFactory;

/**
 * A modal window without decoration which shows the status of multiple
 * threads.  Any type of thread may be modified.  However,
 * NotifyingWorkerThread instances will allow for more fine-grained feedback.
 * For other threads, the window can only tell when one has started and ended.
 * Once the window is shown, all threads which have not already been started
 * will be started.
 * 
 * @author pchapman
 */
public class WorkNotificationWindow
{
	// CONSTANTS
	
	private static final String DEFAULT_ICON_PATH = "org/papernapkin/liana/resources/images/work.png";
	private static final String CLOSE_ICON_PATH = "org/papernapkin/liana/resources/images/close.png";
	private static final String CLOSE_WINDOW = "close.button";
	
	// CONSTRUCTORS
	
	/**
	 * Creates a new instance.  Any threads to be monitored should be passed
	 * as a parameter before it is started.  The threads will be started
	 * automatically once the dialog's GUI has been realized. 
	 * @param owner The owner of the dialog.
	 * @param icon The icon to show on the left of the dialog.  The icon must
	 *             be 32x32 pixels in size.  If null, a default icon will be
	 *             used.
	 * @param workerThreads The runnables to monitor.  If the runnable is not
	 *                      a Thread, it will be wrapped in a thread.
	 */
	public WorkNotificationWindow(
			Dialog owner, String title, Icon icon, Collection<? extends Runnable> runnables
		)
	{
		initGUI(owner, title, icon);
		addRunnables(runnables);
		startWorkerThreads();
	}

	/**
	 * Creates a new instance.  Any threads to be monitored should be passed
	 * as a parameter before it is started.  The threads will be started
	 * automatically once the dialog's GUI has been realized. 
	 * @param owner The owner of the dialog.
	 * @param icon The icon to show on the left of the dialog.  The icon must
	 *             be 32x32 pixels in size.  If null, a default icon will be
	 *             used.
	 * @param workerThreads The runnables to monitor.  If the runnable is not
	 *                      a Thread, it will be wrapped in a thread.
	 */
	public WorkNotificationWindow(
			Frame owner, String title, Icon icon, Collection<? extends Runnable> runnables
		)
	{
		this.displayIcon = icon;
		initGUI(owner, title, icon);
		addRunnables(runnables);
		startWorkerThreads();
	}
	
	// MEMBERS
	
	private JButton closeButton;
	private JDialog dialog;
	private Icon displayIcon;
	private JLabel displayIconLabel;
	private JPanel displayPanel;
	private JScrollPane displayScrollPane;
	private List<WorkNotificationPanel> notificationPanels = new LinkedList<WorkNotificationPanel>();
	private Set<NotifyingWorkerThread> threads = new HashSet<NotifyingWorkerThread>();
	
	/** Whether the view is visible. */
	public boolean isVisible() {
		return dialog.isVisible();
	}
	/** Makes the view visible or invisible. */
	public void setVisible(boolean visible) {
		if (dialog != null)
			dialog.setVisible(visible);
	}
	
	// METHODS
	
	private void initGUI(Component owner, String title, Icon icon)
	{
		if (owner instanceof Dialog) {
			dialog = new JDialog((Dialog)owner, title, true);
		} else {
			dialog = new JDialog((Frame)owner, title, true);
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle myBounds = new Rectangle(350, 180);
		myBounds.x = (screenSize.width / 2) - (myBounds.width / 2);
		myBounds.y = (screenSize.height / 2) - (myBounds.height / 2);
		dialog.setBounds(myBounds);
		dialog.setBackground(new Color(.85f,.85f,1f));
		dialog.setLayout(null);
		dialog.setUndecorated(true);
		// The Alloy LAF does not honor setUndecorated(true) when using JDialog.setDefaultLookAndFeelDecorated(true).
		// The below ensures that the LAF does not decorate the dialog
		dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		((JComponent)dialog.getContentPane()).setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(1, 1, 0, 0, new Color(.85f,.85f,1f)),
						BorderFactory.createRaisedBevelBorder()
					)
			);
		
		if (displayIcon == null) {
			URL url = getClass().getClassLoader().getResource(DEFAULT_ICON_PATH);
			if (url == null) {
				LoggerFactory.getLogger(getClass()).error("Unable to load default icon.");
			} else {
				displayIcon = new ImageIcon(url);
			}
		}
		displayIconLabel = new JLabel(displayIcon);
		displayIconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		displayIconLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		displayIconLabel.setBounds(10, 10, 32, 32);
		dialog.add(displayIconLabel);
		
		Translation trans = Translation.getTranslation(getClass());
		displayIcon = new ImageIcon(getClass().getClassLoader().getResource(CLOSE_ICON_PATH));
		closeButton = new JButton(displayIcon);
		Dimension size = closeButton.getPreferredSize();
		Point point = new Point(10, 180 - 10 - size.height);
		closeButton.setBounds(new Rectangle(point, size));
		closeButton.setName(CLOSE_WINDOW);
		closeButton.setToolTipText(trans.getString(CLOSE_WINDOW));
		closeButton.setVisible(false);
		dialog.add(closeButton);
		
		displayScrollPane = new JScrollPane();
		displayScrollPane.setBounds(new Rectangle(size.width + 20, 10, (350 - size.width - 30), 160));
		displayScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dialog.add(displayScrollPane);
		
		displayPanel = new JPanel();
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
		displayScrollPane.setViewportView(displayPanel);
		
		SwingResponderRegistrationTool.register(this, dialog);
	}
	
	@ActionFor(componentNames={CLOSE_WINDOW})
	protected void closeWindow() {
		setVisible(false);
	}
	
	/**
	 * Shows the work notification window.  This window is modal, this
	 * method does not return until all runnables have been run.
	 * @param owner The owner component.  Must not be null.
	 * @param workerThreads The runnables to run.  Any threads should not be
	 *                      started before they are passed in to this method.
	 *                      Any Runnable may be passed, but only subclasses of
	 *                      NotifyingWorkerThread can be used to provide
	 *                      metered feedback to the user.  This parameter must
	 *                      not be null.
	 */
	public static void showWorkNotificationWindow(
			Component owner, String title, Collection<? extends Runnable> runnables
		)
	{
		showWorkNotificationWindow(owner, title, runnables, null);
	}
	
	/**
	 * Shows the work notification window.  This window is modal, this
	 * method does not return until all runnables have been run.
	 * @param owner The owner component.  Must not be null.
	 * @param workerThreads The runnable to run.  If it is a thread, it should
	 *                      not be started before it is passed into this
	 *                      method.  Any Runnable may be passed, but only
	 *                      subclasses of NotifyingWorkerThread can be used to
	 *                      provide metered feedback to the user.  This
	 *                      parameter must not be null.
	 */
	public static void showWorkNotificationWindow(
			Component owner, String title, Runnable runnable
		)
	{
		showWorkNotificationWindow(owner, title, Arrays.asList(new Runnable[] {runnable}), null);
	}
	
	/**
	 * Shows the work notification window.  This window is modal, this
	 * method does not return until all runnables have been run.
	 * @param owner The owner component.  Must not be null.
	 * @param workerThreads The runnable to run.  If it is a thread, it should
	 *                      not be started before it is passed into this
	 *                      method.  Any Runnable may be passed, but only
	 *                      subclasses of NotifyingWorkerThread can be used to
	 *                      provide metered feedback to the user.  This
	 *                      parameter must not be null.
	 * @param icon The icon to display. May be null.  Icons of sizes other
	 *             than 32x32 are allowed, but they will not show correctly.
	 */
	public static void showWorkNotificationWindow(
			Component owner, String title, Runnable runnable, Icon icon
		)
	{
		showWorkNotificationWindow(owner, title, Arrays.asList(new Runnable[] {runnable}), icon);
	}

	/**
	 * Shows the work notification window.  This window is modal, this
	 * method does not return until all runnables have been run.
	 * @param owner The owner component.  Must not be null.
	 * @param workerThreads The runnables to run.  Any threads should not be
	 *                      started before they are passed in to this method.
	 *                      Any Runnable may be passed, but only subclasses of
	 *                      NotifyingWorkerThread can be used to provide
	 *                      metered feedback to the user.  This parameter must
	 *                      not be null.
	 * @param icon The icon to display. May be null.  Icons of sizes other
	 *             than 32x32 are allowed, but they will not show correctly.
	 */
	public static void showWorkNotificationWindow(
			Component owner, String title, Collection<? extends Runnable> runnables, Icon icon
		)
	{
		WorkNotificationWindow wnw = null;
		for (
				Component comp = owner;
				comp != null && wnw == null;
				comp = comp.getParent()
			)
		{
			if (comp instanceof Frame) {
				wnw = new WorkNotificationWindow((Frame)comp, title, icon, runnables);
			} else if (comp instanceof Dialog) {
				wnw = new WorkNotificationWindow((Dialog)comp, title, icon, runnables);
			}
		}
		if (wnw == null) {
			wnw = new WorkNotificationWindow((Frame)null, title, icon, runnables);
		}
		wnw.setVisible(true);
	}
	
	private WorkerThreadListener listener = new WorkerThreadListener() {
		@Override public void errorNotified(WorkerThreadEvent event) {}

		@Override public void messageNotified(WorkerThreadEvent event) {}

		@Override public void progressNotified(WorkerThreadEvent event) {}

		@Override public void startNotified(WorkerThreadEvent event) {}

		@Override
		public void stopNotified(WorkerThreadEvent event) {
			removeThread(event.getSource());
		}
	};
	
	private void addRunnable(Runnable r) {
		NotifyingWorkerThread thread;
		if (r instanceof NotifyingWorkerThread) {
			thread = (NotifyingWorkerThread)r;
		} else {
			thread = new SimpleNotifyingWorkerThread(r);
		}
		thread.addWorkerThreadListener(listener);
		WorkNotificationPanel panel = new WorkNotificationPanel(thread.getName(), thread);
		notificationPanels.add(panel);
		displayPanel.add(panel.getComponent());
		if (panel.getComponent().getPreferredSize().width > displayScrollPane.getBounds().width) {
			Rectangle scrollBounds = displayScrollPane.getBounds();
			Rectangle dialogBounds = dialog.getBounds();
			int diff = scrollBounds.width;
			scrollBounds.width = panel.getComponent().getPreferredSize().width + 25;
			diff -= scrollBounds.width;
			dialogBounds.width -= diff;
			dialogBounds.x += diff / 2;
			displayScrollPane.setBounds(scrollBounds);
			dialog.setBounds(dialogBounds);
		}
		displayScrollPane.revalidate();
		synchronized(threads) {
			threads.add(thread);
		}
	}
	
	private void addRunnables(Collection<? extends Runnable> rcol) {
		for (Runnable r : rcol) {
			addRunnable(r);
		}
	}
	
	private void removeThread(NotifyingWorkerThread thread) {
		int count = 0;
		synchronized(threads) {
			threads.remove(thread);
			count = threads.size();
		}
		if (count == 0) {
			for (WorkNotificationPanel panel : notificationPanels) {
				// Changed to WorkNotificationPanel#hasError() from
				// WorkNotificationPanel#isThreadStoppedWithError()
				// because thread may not have died by the time we
				// remove it.  -matt 20090213
				if (panel.hasError()) {
					closeButton.setVisible(true);
					return;
				}
			}
			// Everything ended OK.  Go away.
			setVisible(false);
		}
	}

	private void startWorkerThreads()
	{
		Collection<NotifyingWorkerThread> col;
		synchronized(threads) {
			// Copy the threads just in case all some thread stops before we
			// finish starting all.  We don't want to block the removal of
			// threads.
			col = new ArrayList<NotifyingWorkerThread>(threads);
		}
		for (NotifyingWorkerThread thread : col) {
			if (! thread.isAlive()) {
				thread.start();
			}
		}
	}

	//////////////////////////////////////////////////////////////////////////

	/**
	 * Wraps a Runnable in a NotifyingWorkerThread so that it can be
	 * monitored.  Of course, only the starting and ending of the thread
	 * may be monitored.  It is not possible to determine any status
	 * information for providing to listeners.
	 *
	 * @author pchapman.
	 */
	class SimpleNotifyingWorkerThread extends NotifyingWorkerThread
	{
		private Runnable runnable;
		
		SimpleNotifyingWorkerThread(Runnable runnable)
		{
			this.runnable = runnable;
		}
		
		public boolean work()
		{
			try {
				if (runnable instanceof Thread) {
					// Start the thread and wait on it to finish.
					((Thread)runnable).start();
					try {
						((Thread)runnable).join();
					} catch (InterruptedException e) {}
				} else {
					// Run the executable in this thread.
					runnable.run();
				}
				return true;
			} catch (Exception e) {
				super.notifyListeners(new WorkerThreadEvent(this, e));
				return false;
			}
		}
	}
}
