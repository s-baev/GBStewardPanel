package com.gillbus.steward;

import java.awt.EventQueue;

import javax.swing.JFrame;

import com.gillbus.steward.component.Frame;
import com.gillbus.steward.component.LoginPanel;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class Steward {
	
	public static String BASE_URL = "http://192.168.2.113:8080/GillBuss/";
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				Frame stewardFrame = Frame.getInstance();
				stewardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				LoginPanel panel = LoginPanel.getInstance();
				stewardFrame.addPanelComponent(panel);
				
				stewardFrame.setLocationRelativeTo(null);
				stewardFrame.setVisible(true);
			}
		});
	}

}
