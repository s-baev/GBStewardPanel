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
import com.gillbus.steward.bean.Passenger;
import com.gillbus.steward.bean.Select;
import com.gillbus.steward.component.Frame;
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.component.WorkPanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;
import com.gillbus.steward.model.PassengerTableModel;

public class Register implements ActionListener {
	
	private HttpConnector httpConnector;
	private WorkPanel workPanel;
	private String tripPassengerId;
	private SalePanel salePanel;

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			httpConnector = HttpConnector.getInstance();
			workPanel = WorkPanel.getInstance();
			salePanel = SalePanel.getInstance();
			if (workPanel.isActive()) {
				tripPassengerId = workPanel.getPassengers().get(
						workPanel.getPassengersTable().getSelectedRow()).getTripPassengerId();
			} else {
				tripPassengerId = null;
			}
			sendRequest();
			getResponse();
			rewriteComponents();
		} catch (IOException e) {
		} 
	}
	
	
	private void rewriteComponents() {
		if (workPanel.isActive()) {
			workPanel.getPassengersTable().setModel(
					new PassengerTableModel(workPanel.getPassengers()));
			workPanel.invalidate();
			workPanel.validate();
		} else {
			workPanel.setActive(true);
			Frame frame = Frame.getInstance();
			frame.removePanelComponent(salePanel);
			frame.addPanelComponent(workPanel);
			frame.invalidate();
			frame.validate();
		}
	}
	
	
	private void getResponse() throws IOException {
		List<Passenger> passengers = workPanel.getPassengers();
		passengers.clear();
		try {
			byte[] bytes = httpConnector.getBytes();
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			NodeList passengersByTag = documentBuilder.parse(new ByteArrayInputStream(bytes))
											.getDocumentElement().getElementsByTagName("PASSENGER");
			for (int i = 0; i < passengersByTag.getLength(); i++) {
		        Element thisPassenger = (Element)passengersByTag.item(i);
		        Passenger passenger = new Passenger();
		        passenger.setTripPassengerId(
		        		Utils.getElementsByTagName(thisPassenger, "TRIP_PASSENGER_ID"));
		        passenger.setBarCode(
		        		Utils.getElementsByTagName(thisPassenger, "TICKET_BARCODE"));
		        passenger.setEndPointName(
		        		Utils.getElementsByTagName(thisPassenger, "END_CITY_NAME"));
		        passenger.setNumberSeat(
		        		Utils.getElementsByTagName(thisPassenger, "NUMBER_SEAT"));
		        passenger.setOperation(
		        		Utils.getElementsByTagName(thisPassenger, "TICKETING_METHOD_NAME"));
		        passenger.setOperationId(
		        		Utils.getElementsByTagName(thisPassenger, "TICKETING_METHOD_ID"));
		        passenger.setStatus(
		        		Utils.getElementsByTagName(thisPassenger, "STATUS_NAME"));
		        passenger.setStatusId(
		        		Utils.getElementsByTagName(thisPassenger, "STATUS_ID"));
		        passengers.add(passenger);
			}
			//TODO commands parser
			workPanel.getGain().setText(DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(new ByteArrayInputStream(bytes))
					.getDocumentElement().getElementsByTagName("TRIP_GAIN").item(0)
					.getFirstChild().getNodeValue());
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.REGISTER_PASSENGER);
		if (tripPassengerId != null 
				&& !tripPassengerId.isEmpty()) {
			httpConnector.addParameter("tripPassengerId", tripPassengerId);
		} else {
			httpConnector.addParameter("lastName", 
					salePanel.getTicketCost().getText());
			httpConnector.addParameter("seatId", 
					((Select)salePanel.getSeatsBox().getSelectedItem()).getValue());
			httpConnector.addParameter("tariffId", 
					((Select)salePanel.getTariffsBox().getSelectedItem()).getValue());
			httpConnector.addParameter("endId", 
					((Select)salePanel.getEndStoppingsBox().getSelectedItem()).getValue());
		}
		httpConnector.executeMethod();
	}

}
