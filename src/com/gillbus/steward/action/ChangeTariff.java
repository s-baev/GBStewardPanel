package com.gillbus.steward.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import com.gillbus.steward.bean.Select;
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;

public class ChangeTariff implements ActionListener {

	private HttpConnector httpConnector;
	private SalePanel salePanel;
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			httpConnector = HttpConnector.getInstance();
			salePanel = SalePanel.getInstance();
			sendRequest();
			getResponse();
			salePanel.invalidate();
			salePanel.validate();
		} catch (IOException e) {
		}
	}
	
	
	private void getResponse() throws IOException {
		salePanel.getTicketCost().setText(
				new String(httpConnector.getBytes()));
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.CHANGE_TARIFF);
		httpConnector.addParameter("tariffId", 
				((Select)salePanel.getTariffsBox().getSelectedItem()).getValue());
		httpConnector.executeMethod();
	}

}
