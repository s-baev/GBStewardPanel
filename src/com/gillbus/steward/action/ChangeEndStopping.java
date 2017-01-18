package com.gillbus.steward.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gillbus.steward.Utils;
import com.gillbus.steward.bean.Select;
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.component.WorkPanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;
import com.gillbus.steward.model.ComboBoxModel;

public class ChangeEndStopping implements ActionListener {

	private HttpConnector httpConnector;
	private WorkPanel workPanel;
	private SalePanel salePanel;
	
	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			httpConnector = HttpConnector.getInstance();
			workPanel = WorkPanel.getInstance();
			salePanel = SalePanel.getInstance();
			sendRequest();
			getResponse();
			rewriteComponents();
			salePanel.invalidate();
			salePanel.validate();
		} catch (IOException e) {
		}
	}

	private void rewriteComponents() {
		salePanel.getSeatsBox().setModel(
				new ComboBoxModel(salePanel.getSeats()));
	}
	
	
	private void getResponse() throws IOException {
		List<Select> seats = salePanel.getSeats();
		seats.clear();
		try {
			byte[] bytes = httpConnector.getBytes();
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			NodeList seatsByTag = documentBuilder.parse(new ByteArrayInputStream(bytes))
					.getDocumentElement().getElementsByTagName("SEAT");
			for (int i = 0; i < seatsByTag.getLength(); i++) {
				Element thisSeat = (Element) seatsByTag.item(i);
				Select seat = new Select();
				seat.setValue(
						Utils.getElementsByTagName(thisSeat, "SEAT_ID"));
				seat.setLabel(
						Utils.getElementsByTagName(thisSeat, "SEAT_NUMBER"));
				seats.add(seat);
			}
			salePanel.getTicketCost().setText(DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new ByteArrayInputStream(bytes))
					.getDocumentElement().getElementsByTagName("TICKET_COST").item(0)
					.getFirstChild().getNodeValue());
			
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.CHANGE_END_STOPPING);
		httpConnector.addParameter("startId", 
				((Select)workPanel.getComboBox().getSelectedItem()).getValue());
		httpConnector.addParameter("endId", 
				((Select)salePanel.getEndStoppingsBox().getSelectedItem()).getValue());
		httpConnector.executeMethod();
	}
}
