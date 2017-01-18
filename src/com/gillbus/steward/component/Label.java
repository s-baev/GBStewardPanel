package com.gillbus.steward.component;

import javax.swing.JLabel;

public class Label extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165522061787564096L;

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		super.setText("<html><table cellpadding=\"0\" cellspacing=\"0\" width=\"130\"><tr><td>"
	    		+ text
	    		+ "</td></tr></html>");
	}
	
}
