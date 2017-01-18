package com.gillbus.steward.component;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class Frame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5025221428400317503L;

	public static final int DEFAULT_WIDTH = 240;
	public static final int DEFAULT_HEIGHT = 320;
	
	private static Frame instance;
	
	private JPanel panel;

	
	private Frame() throws HeadlessException {
		setTitle("GillBus Steward Panel");
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		panel = new JPanel(new BorderLayout());
		JScrollPane pane = new JScrollPane(panel);
		pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(pane);
	}
	
	
	public static Frame getInstance() {
		if (instance == null) {
			instance = new Frame();
		}
		return instance;
	}
	
	
	public void addPanelComponent(Component comp) {
		panel.add(comp, BorderLayout.NORTH);
	}
	
	
	public void removePanelComponent(Component comp) {
		panel.remove(comp);
	}

}
