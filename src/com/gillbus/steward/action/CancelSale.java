package com.gillbus.steward.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.gillbus.steward.component.Frame;
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.component.WorkPanel;

public class CancelSale implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SalePanel salePanel = SalePanel.getInstance();
		WorkPanel workPanel = WorkPanel.getInstance();
		workPanel.setActive(true);
		Frame frame = Frame.getInstance();
		frame.removePanelComponent(salePanel);
		frame.addPanelComponent(workPanel);
		frame.invalidate();
		frame.validate();
	}

}
