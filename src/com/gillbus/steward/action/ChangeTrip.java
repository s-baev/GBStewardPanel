package com.gillbus.steward.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gillbus.steward.Utils;
import com.gillbus.steward.bean.Passenger;
import com.gillbus.steward.bean.Select;
import com.gillbus.steward.bean.Trip;
import com.gillbus.steward.component.SalePanel;
import com.gillbus.steward.component.WorkPanel;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;
import com.gillbus.steward.model.ComboBoxModel;
import com.gillbus.steward.model.PassengerTableModel;

public class ChangeTrip implements ListSelectionListener {
	
	private HttpConnector httpConnector;
	private WorkPanel workPanel;
	private SalePanel salePanel;
	private Trip selectedTrip;
	
	JTable table;

	public ChangeTrip(JTable table) {
		this.table = table;
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getSource() == table.getSelectionModel()
				&& table.getRowSelectionAllowed()
				&& event.getValueIsAdjusting()) {
			try {
				httpConnector = HttpConnector.getInstance();
				workPanel = WorkPanel.getInstance();
				salePanel = SalePanel.getInstance();
				selectedTrip = workPanel.getTrips().get(
									table.getSelectedRow());
				sendRequest();
				getResponse();
				rewriteComponents();
				workPanel.invalidate();
				workPanel.validate();
			} catch (IOException e) {
			}
		} 
	}
	
	
	private void rewriteComponents() {
		workPanel.getRoute().setText(
				selectedTrip.getRoute());
		workPanel.getFreeBuzySeats().setText(
				selectedTrip.getFreeBuzyPlaces());
		workPanel.getTripNumber().setText(
				selectedTrip.getTripNumber());
		workPanel.getGain().setText(
				selectedTrip.getTotalCost().toString());
		workPanel.getStartTime().setText(
				selectedTrip.getStartTime());
		workPanel.getComboBox().setModel(
				new ComboBoxModel(workPanel.getStartStoppings()));
		workPanel.getPassengersTable().setModel(
				new PassengerTableModel(workPanel.getPassengers()));
		salePanel.getEndStoppingsBox().setModel(
				new ComboBoxModel(salePanel.getEndStoppings()));
	}

	
	private void getResponse() throws IOException {
		List<Passenger> passengers = workPanel.getPassengers();
		List<Select> tariffs = salePanel.getTariffs();
		List<Select> startStoppings = workPanel.getStartStoppings();
		List<Select> endStoppings = salePanel.getEndStoppings();
		passengers.clear();
		tariffs.clear();
		startStoppings.clear();
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
			NodeList startStoppingsByTag = documentBuilder.parse(new ByteArrayInputStream(bytes))
							.getDocumentElement().getElementsByTagName("STAR_STOPPING");
			for (int i = 0; i < startStoppingsByTag.getLength(); i++) {
				Element thisStartStopping = (Element)startStoppingsByTag.item(i);
				Select startStopping = new Select();
				startStopping.setValue(
						Utils.getElementsByTagName(thisStartStopping, "STOPPING_ID"));
				startStopping.setLabel(
						Utils.getElementsByTagName(thisStartStopping, "STOPPING_NAME"));
				startStoppings.add(startStopping);
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
			NodeList tariffsByTag = documentBuilder.parse(new ByteArrayInputStream(bytes))
					.getDocumentElement().getElementsByTagName("TARIFF");
			for (int i = 0; i < tariffsByTag.getLength(); i++) {
				Element thisTariff = (Element) tariffsByTag.item(i);
				Select tariff = new Select();
				tariff.setValue(
						Utils.getElementsByTagName(thisTariff, "TARIFF_ID"));
				tariff.setLabel(
						Utils.getElementsByTagName(thisTariff, "TARIFF_NAME"));
				tariffs.add(tariff);
			}
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private void sendRequest() throws IOException {
		httpConnector.newMethod(ServletURL.CHANGE_TRIP);
		httpConnector.addParameter("tripId", selectedTrip.getTripId());
		httpConnector.executeMethod();
	}

}
