package com.gillbus.steward.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.gillbus.steward.action.Login;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class LoginPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2164743213460014904L;
	
	private static LoginPanel instance;
	
	private JTextField textLogin;
	private JPasswordField textPswd;
	private boolean logged = false;
	private String userName;

	
	private LoginPanel() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
	    c.insets = new Insets(10, 10, 0, 0);
	    c.weighty = 1.0;
	    c.weightx = 1.0;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.NORTHWEST;
		add(new JLabel("Логін"), c);
		
		c.insets = new Insets(10, 0, 0, 0);
	    c.gridx = 1;
		textLogin = new JTextField();
		textLogin.setPreferredSize(new Dimension(150, 20));
		add(textLogin, c);
		
		c.insets = new Insets(0, 10, 0, 0);
	    c.gridx = 0;
	    c.gridy = 1;
		add(new JLabel("Пароль"), c);
		
		c.insets = new Insets(10, 0, 0, 0);
	    c.gridwidth = 2;
	    c.anchor = GridBagConstraints.CENTER;
	    c.gridx = 0;
	    c.gridy = 2;
		final JButton button = new JButton("Вхід");
		button.addActionListener(new Login());
		add(button, c);
		
		c.insets = new Insets(0, 0, 0, 0);
	    c.gridx = 1;
	    c.gridy = 1;
	    c.anchor = GridBagConstraints.NORTHWEST;
		textPswd = new JPasswordField();
		textPswd.setPreferredSize(new Dimension(150, 20));
		textPswd.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					button.doClick();
				}
			}
		});
		add(textPswd, c);
	}
	
	
	public static LoginPanel getInstance() {
		if (instance == null) {
			instance = new LoginPanel();
		}
		return instance;
	}


	public JTextField getTextLogin() {
		return textLogin;
	}


	public JPasswordField getTextPswd() {
		return textPswd;
	}


	public boolean isLogged() {
		return logged;
	}


	public void setLogged(boolean logged) {
		this.logged = logged;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
