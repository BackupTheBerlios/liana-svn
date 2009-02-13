package org.papernapkin.liana.swing.notifyingworker;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.papernapkin.liana.awt.event.MouseClickedFor;
import org.papernapkin.liana.swing.event.SwingResponderRegistrationTool;

/**
 * A component which will show the status of a NotifyingWorkerThread.  This
 * component does not manage the thread in any way.  Therefore, the thread
 * will not be started by this component.
 * 
 * @author pchapman
 */
public class WorkNotificationPanel
{
	private static final String PROGRESS_BAR = "progress.bar";

	private JLabel label;
	private JPanel panel;
	private JProgressBar progressbar;
	private Color defaultforeground;
	private NotifyingWorkerThread thread;
	private boolean running = false;
	private String text;
	private WorkNotificationWindow wnwindow;

	/** Creates a new instance with all default values.  No label and no thread to monitor. */
	public WorkNotificationPanel() {
		this("", null);
	}
	
	/** Creates a new instance with the given label and no thread to monitor. */
	public WorkNotificationPanel(String labeltext) {
		this(labeltext, null);
	}
	
	/** Creates a new instance with the given label that will monitor the given thread. */
	public WorkNotificationPanel(String labeltext, NotifyingWorkerThread thread) {
	}
	
	WorkNotificationPanel(WorkNotificationWindow wnwindow, String labeltext, NotifyingWorkerThread thread) {
		this.wnwindow = wnwindow;
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		label = new JLabel(labeltext == null ? "" : labeltext);
		panel.add(label);
		progressbar = new JProgressBar();
		progressbar.setName(PROGRESS_BAR);
		progressbar.setStringPainted(true);
		defaultforeground = progressbar.getForeground();
		panel.add(progressbar);
		SwingResponderRegistrationTool.register(this, panel);
		setNotifyingWorkerThread(thread);
	}

	/** Gets the actual GUI component to be displayed. */
	public JComponent getComponent() {
		return panel;
	}

	/** Gets the thread being monitored. */
	public NotifyingWorkerThread getNotifyingWorkerThread() {
		return thread;
	}
	/**
	 * Sets the thread to be monitored.  This should never be called if an
	 * active thread is already being monitored.  Furthermore, this method
	 * should always be called from the EDT.
	 */
	public void setNotifyingWorkerThread(NotifyingWorkerThread thread) {
		this.thread = thread;
		progressbar.setString("");
		if (thread.isAlive()) {
			startOccurred();
		} else {
			stopOccurred();
		}
		WorkerThreadListenerEventHandler.register(this, thread);
	}
	
	public String getLabelText() {
		return label.getText();
	}
	public void setLabelText(String text) {
		this.label.setText(text);
	}
	
	protected void setStatusText(String text, boolean error) {
		this.text = text;
		progressbar.setString(text);
		if (error) {
			progressbar.setForeground(Color.RED);
		} else {
			progressbar.setForeground(defaultforeground);
		}
		if (text != null && text.trim().length() > 0) {
			progressbar.setToolTipText("Click for more information.");
		} else {
			progressbar.setToolTipText(null);
		}
	}
	
	public boolean isThreadStopped() {
		return thread == null || (!thread.isAlive());
	}
	
	public boolean isThreadStoppedWithError() {
		return thread != null && !thread.isAlive() && !thread.isSuccessful(); 
	}
	
	@WorkerThreadErrorFor
	protected void errorOccurred(Throwable t) {
		if (running) {
			setStatusText(t.getLocalizedMessage(), true);
		}
	}
	
	@WorkerThreadMessageFor
	protected void messageOccurred(String msg) {
		if (running) {
			setStatusText(msg, false);
		}
	}
	
	@WorkerThreadProgressFor
	protected void progressOccurred(int max, int cur) {
		if (running) {
			if (progressbar.isIndeterminate()) {
				progressbar.setIndeterminate(false);
			}
			if (progressbar.getMaximum() != max) {
				progressbar.setMaximum(max);
			}
			progressbar.setValue(cur);
		}
	}
	
	@MouseClickedFor(componentNames={PROGRESS_BAR})
	protected void showMessage() {
		if (text != null && text.length() > 0) {
			JOptionPane.showMessageDialog(panel, text, "Thread Message", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	@WorkerThreadStartFor
	protected void startOccurred() {
		running = true;
		progressbar.setString(null);
		progressbar.setIndeterminate(true);
		progressbar.setString("Working...");
	}
	
	@WorkerThreadStopFor
	protected void stopOccurred() {
		running = false;
		progressbar.setMaximum(Integer.MAX_VALUE);
		progressbar.setValue(Integer.MAX_VALUE);
		if (progressbar.getForeground().equals(Color.RED)) {
			progressbar.setString("Aborted: " + progressbar.getString());
		} else {
			progressbar.setString("Done");
		}
		progressbar.setIndeterminate(false);
		if (wnwindow != null) {
			wnwindow.removeThread(thread);
		}
	}
}
