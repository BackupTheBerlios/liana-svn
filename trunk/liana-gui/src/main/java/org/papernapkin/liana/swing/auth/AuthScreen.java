package org.papernapkin.liana.swing.auth;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.EventQueue;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import org.papernapkin.liana.swing.layout.Bag;

public class AuthScreen extends JComponent
{
	private static final long serialVersionUID = 1L;

	private final AuthScreenCallBack callBack;
	private final JFrame aFrame;
	private final String loginMessage;
	
	private Map<String, JTextField> additionalTextFields;
	private JLabel titleLabel;
	private JTextField userField;
	private JPasswordField passwordField;
	
	private boolean firstTime = true , showCancel , emptyPass = true;
	private int counter = 0;
	private Timer timer;
	private JButton go,cancel;
	
	private final Component oldGlassPane;
	
	public AuthScreen(
			JFrame aFrame, String loginMsg, AuthScreenCallBack callback,
			boolean showCancel, Map<String, String> additionalFields
		)
	{
		this(aFrame, loginMsg, callback, showCancel, additionalFields, "OK");
	}
	
	public AuthScreen(
			JFrame aFrame, String loginMsg, AuthScreenCallBack callback,
			boolean showCancel, Map<String, String> additionalFields,
			String okLabel
		)
	{
		super();
		this.aFrame = aFrame;
		this.callBack = callback;
		this.loginMessage = loginMsg;
		this.showCancel = showCancel;
		
		if(aFrame == null)
		{
			throw new RuntimeException("AuthScreen: JFrame can not be null");
		}
		
		initGui(additionalFields, okLabel);
		
		oldGlassPane = aFrame.getGlassPane();
	}
	
	public void dispose()
	{
		if(timer != null)
		{
			timer.stop();
		}
		aFrame.setGlassPane(oldGlassPane);
		aFrame.getGlassPane().setVisible(false);
	}
	
	public void setAllowsEmptyPass(boolean b)
	{
		emptyPass = b;
	}
	
	public boolean getAllowsEmptyPass()
	{
		return emptyPass;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		
		if(firstTime)
		{
			firstTime = false;
			
			timer = new Timer(20 , new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					counter+=10;
					repaint();
				}
			});
			
			timer.start();
			
		}
		
		
		Graphics2D g2d = (Graphics2D)g;
		
		Composite comps = g2d.getComposite();
		
		float alpha = .6f;
		
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

		g2d.setColor(Color.BLACK);

		int height = (getHeight() / 100 + 1) * (counter + 1);
		
		GradientPaint gradient = new GradientPaint(getX(), getY(), new Color(0,0,0,255), getX() , height , new Color(0,0,0,0) , false);
		
		Paint p = g2d.getPaint();
		
		g2d.setPaint(gradient);
		
		g2d.fill(new Rectangle(0, 0, getWidth(), getHeight()));
		
		g2d.setComposite(comps);
		
		g2d.setPaint(p);
		
		if(counter == 120) timer.stop(); 
	}





	private boolean alertCallBack()
	{
		final Map<String, String> additionalValues = new HashMap<String, String>();
		for (String key : additionalTextFields.keySet()) {
			additionalValues.put(key, additionalTextFields.get(key).getText());
		}
		AuthRequest request = new AuthRequest()
		{
			public String getPass()
			{
				return new String(passwordField.getPassword());
			}
			
			public String getUsername()
			{
				return userField.getText();
			}
			
			public String getAdditionalValue(String field)
			{
				return additionalValues.get(field);
			}

			private boolean successful;
			public boolean isAuthSuccessful()
			{
				return successful;
			}
			public void setAuthSuccessful(boolean b)
			{
				this.successful = b;
			}
		};
		
		callBack.receiveAuthRequest(request);
		return request.isAuthSuccessful();
	}
	
	
	private void initGui(Map<String, String> additionalFields, String okLabel)
	{
		
		ImageIcon img = new ImageIcon(getClass().getResource("/org/papernapkin/resources/images/auth.png"));
			
		JLabel iconLabel = new JLabel(img);
		
		titleLabel = new JLabel(loginMessage , SwingConstants.CENTER);
		
		titleLabel.setForeground(Color.WHITE);
		
		userField = new JTextField();
		
		passwordField = new JPasswordField();
		
		userField.setBorder(BorderFactory.createEmptyBorder());
		passwordField.setBorder(BorderFactory.createEmptyBorder());
		
		JLabel userLabel = new JLabel("Login");
		JLabel passLabel = new JLabel("Password");
		
		userLabel.setForeground(Color.WHITE);
		passLabel.setForeground(Color.WHITE);
		
		
		
		JPanel panel = new JPanel(new GridBagLayout());
		
		panel.setOpaque(false);
		
		Bag bag = new Bag();
		
		panel.add(iconLabel , bag.inset(2).rowspan(4));
		
		panel.add(titleLabel , bag.nextX().fillX().rowspan(1).colspan(2));
		
		bag.resetX().anchor = Bag.EAST;
		panel.add(userLabel , bag.nextY().fillNone().colspan(1).nextX());
		panel.add(userField , bag.nextX().fillX());
		
		bag.resetX();
		panel.add(passLabel , bag.nextY().fillNone().nextX());
		panel.add(passwordField , bag.nextX().fillX());
		
		JLabel lbl;
		JTextField fld;
		additionalTextFields = new HashMap<String, JTextField>();
		for (String key : additionalFields.keySet()) {
			lbl = new JLabel(additionalFields.get(key));
			lbl.setForeground(Color.WHITE);
			fld = new JTextField();
			fld.setBorder(BorderFactory.createEmptyBorder());
			additionalTextFields.put(key, fld);
			bag.resetX();
			panel.add(lbl , bag.nextY().fillNone().nextX());
			panel.add(fld , bag.nextX().fillX());
		}
		
		panel.add(initActionsPanel(okLabel) , bag.nextY().resetX().colspan(3));
		
		setLayout(new GridBagLayout());
		
		bag = new Bag();
		bag.anchor = GridBagConstraints.CENTER;
		
		add(panel , bag);
		
	}

	
	
	private JPanel initActionsPanel(String okLabel)
	{
		JPanel actionsPanel = new JPanel(new GridBagLayout());
		
		actionsPanel.setOpaque(false);
		
		go = new JButton(okLabel);
		
		cancel = new JButton("Cancel");
		
		go.setMargin(new Insets(1,1,1,1));
		cancel.setMargin(new Insets(1,1,1,1));
		
		go.setForeground(Color.WHITE);
		cancel.setForeground(Color.WHITE);
		
		go.setOpaque(false);
		cancel.setOpaque(false);
		
		go.setBackground(new Color(0,0,0,0));
		cancel.setBackground(new Color(0,0,0,0));
		
		go.setBorderPainted(false);
		cancel.setBorderPainted(false);
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelAuthentication();
			}
		});
		
		
		
		
		go.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				attemptAuthentication();
			}
		});
		
		Bag bag = new Bag();
		
		actionsPanel.add(Bag.spacer() , bag.fillX());
		
		actionsPanel.add(go , bag.nextX().fillNone());
		
		if(showCancel)
		{
			actionsPanel.add(cancel , bag.nextX());
		}
		
		return actionsPanel;
		
	}
	
	private void attemptAuthentication()
	{
		if(!emptyPass)
		{
			if(passwordField.getPassword().length == 0)
			{
				titleLabel.setText("Enter a password");
				
				passwordField.requestFocusInWindow();
				
				Timer t = new Timer(4000 , new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						titleLabel.setText(loginMessage);
					}
				});
				
				t.setRepeats(false);
				t.start();
				return;
			}
		}
		userField.setEnabled(false);
		passwordField.setEnabled(false);
		go.setEnabled(false);
		cancel.setEnabled(false);
		titleLabel.setText("Checking Login");
		if (alertCallBack()) {
			dispose();
		} else {
			titleLabel.setText("Login Failed");
			userField.setEnabled(true);
			passwordField.setEnabled(true);
			go.setEnabled(true);
			cancel.setEnabled(true);
		}
	}
	
	private void cancelAuthentication()
	{
		dispose();
		callBack.receiveAuthCancellation();
	}
	
	public void showAuthScreen()
	{
		aFrame.setGlassPane(this);
		
		aFrame.getGlassPane().setVisible(true);
		
		EventQueue.invokeLater(new Runnable(){
			public void run()
			{
				userField.requestFocusInWindow();
			}
		});
	}
}
