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
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.component.WorkPanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;
import com.gillbus.steward.model.ComboBoxModel;
import com.gillbus.steward.model.PassengerTableModel;

public class ChangeStartStopping implements ActionListener {

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
			workPanel.invalidate();
			workPanel.validate();
		} catch (IOException e) {
		}
	}
	
	
	private void rewriteComponents() {
		workPanel.getPassengersTable().setModel(
				new PassengerTableModel(workPanel.getPassengers()));
		salePanel.getEndStoppingsBox().setModel(
				new ComboBoxModel(salePanel.getEndStoppings()));
	}
	
	
	private void getResponse() throws IOException {
		List<Passenger> passengers = workPanel.getPassengers();
		List<Select> endStoppings = salePanel.getEndStoppings();
		passengers.clear();
		endStoppings.clear();
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
			NodeList endStoppingsByTag = documentBuilder.parse(new ByteArrayInputStream(bytes))
					.getDocumentElement().getElementsByTagName("END_STOPPING");
			for (int i = 0; i < endStoppingsByTag.getLength(); i++) {
				Element thisEndStopping = (Element) endStoppingsByTag.item(i);
				Select endStopping = new Select();
				endStopping.setValue(
						Utils.getElementsByTagName(thisEndStopping, "STOPPING_ID"));
				endStopping.setLabel(
						Utils.getElementsByTagName(thisEndStopping, "STOPPING_NAME"));
				endStoppings.add(endStopping);
			}
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.CHANGE_START_STOPPING);
		httpConnector.addParameter("startId", 
				((Select)workPanel.getComboBox().getSelectedItem()).getValue());
		httpConnector.executeMethod();
	}

}
