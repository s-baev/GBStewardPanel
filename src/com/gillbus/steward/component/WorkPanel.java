package com.gillbus.steward.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gillbus.steward.Utils;
import com.gillbus.steward.action.ChangeStartStopping;
import com.gillbus.steward.action.ChangeTrip;
import com.gillbus.steward.action.PrepareSale;
import com.gillbus.steward.action.Register;
import com.gillbus.steward.bean.Passenger;
import com.gillbus.steward.bean.Select;
import com.gillbus.steward.bean.Trip;
import com.gillbus.steward.connection.HttpConnector;
import com.gillbus.steward.connection.ServletURL;
import com.gillbus.steward.model.ComboBoxModel;
import com.gillbus.steward.model.PassengerTableModel;
import com.gillbus.steward.model.TripTableModel;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class WorkPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3428522665920952667L;

	private static WorkPanel instance;
	private HttpConnector httpConnector;
	private List<Trip> trips = new ArrayList<Trip>();
	private List<Passenger> passengers = new ArrayList<Passenger>();
	private List<Select> startStoppings = new ArrayList<Select>();
	private boolean active = true;
	
	private JTextField barCode = new JTextField();
	private JLabel tripNumber = new Label();
	private JLabel route = new Label();
	private JLabel startTime = new Label();
	private JLabel gain = new Label();
	private JLabel freeBuzySeats = new Label();
	private JComboBox comboBox;
	private JTable passengersTable;
	private JButton register;
	
	
	private WorkPanel() {
		setLayout(new GridBagLayout());
		
		JPanel tripInfo = new JPanel(new GridBagLayout());
		Border border = BorderFactory.createTitledBorder("Вибраний рейс");
		tripInfo.setBorder(border);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 0, 0);
	    c.weighty = 1.0;
	    c.weightx = 1.0;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    tripInfo.add(new JLabel("№ рейса:"), c);
	    
	    c.insets = new Insets(5, 5, 0, 0);
	    c.gridy = 1;
	    tripInfo.add(new JLabel("Маршрут:"), c);
	    
	    c.gridy = 2;
	    tripInfo.add(new JLabel("Час відпр.:"), c);
	    
	    c.gridy = 3;
	    tripInfo.add(new JLabel("Кіл. місць:"), c);
	    
	    c.gridy = 4;
	    tripInfo.add(new JLabel("Виручка:"), c);
	    
	    c.gridy = 5;
	    tripInfo.add(new JLabel("Зупинка:"), c);
	    
	    c.gridy = 6;
	    tripInfo.add(new JLabel("Штрихкод:"), c);
	    
	    c.insets = new Insets(0, 5, 0, 0);
	    c.gridx = 1;
	    c.gridy = 0;
	    tripInfo.add(tripNumber, c);
	    
	    c.insets = new Insets(5, 5, 0, 0);
	    c.gridy = 1;
	    tripInfo.add(route, c);
	    
	    c.gridy = 2;
	    tripInfo.add(startTime, c);
	    
	    c.gridy = 3;
	    tripInfo.add(freeBuzySeats, c);
	    
	    c.gridy = 4;
	    tripInfo.add(gain, c);
	    
	    c.gridy = 5;
	    comboBox = new JComboBox(new ComboBoxModel(startStoppings));
	    comboBox.setPreferredSize(new Dimension(130, 20));
	    comboBox.addActionListener(new ChangeStartStopping());
	    tripInfo.add(comboBox, c);
	    
	    c.gridy = 6;
	    barCode.setPreferredSize(new Dimension(130, 20));
	    barCode.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Passenger newPassenger = getPassengerByBarCode(
												((JTextField)e.getSource()).getText());
					if (newPassenger != null) {
						int index = passengers.indexOf(newPassenger);
						passengersTable.getSelectionModel().setSelectionInterval(index, index);
						register.doClick();
					}
				}
			}
		});
		tripInfo.add(barCode, c);
		
		GridBagConstraints mainC = new GridBagConstraints();
		mainC.insets = new Insets(0, 0, 0, 0);
	    mainC.weighty = 1.0;
	    mainC.weightx = 1.0;
	    mainC.gridx = 0;
	    mainC.gridy = 0;
	    mainC.anchor = GridBagConstraints.NORTHWEST;
		add(tripInfo, mainC);
	}
	
	
	public static WorkPanel getInstance() {
		if (instance == null) {
			instance = new WorkPanel();
		}
		return instance;
	}
	
	
	public void initWorkSpace() throws IOException {
		httpConnector = HttpConnector.getInstance();
		sendInitRequest();
		getInitResponse();
		
		//создание таблицы с рейсами
		JTable tripTable = new JTable(new TripTableModel(trips));
		tripTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		tripTable.setRowSelectionAllowed(true);
		tripTable.setColumnSelectionAllowed(false);
		
		ChangeTrip action = new ChangeTrip(tripTable);
		tripTable.getSelectionModel().addListSelectionListener(action);
		tripTable.getColumnModel().getSelectionModel().addListSelectionListener(action);
		
		TableColumn column = tripTable.getColumnModel().getColumn(0);
		column.setPreferredWidth(170);
		
		JScrollPane tripPane = new JScrollPane(tripTable);
		tripPane.setPreferredSize(new Dimension(210, 100));
		
		GridBagConstraints mainC = new GridBagConstraints();
		mainC.insets = new Insets(0, 0, 0, 0);
	    mainC.weighty = 1.0;
	    mainC.weightx = 1.0;
	    mainC.gridx = 0;
	    mainC.gridy = 1;
	    mainC.anchor = GridBagConstraints.NORTHWEST;
		add(tripPane, mainC);
		
		//добавляем кнопку перехода на оформление
		JButton toSale = new JButton("На оформлення");
		toSale.setPreferredSize(new Dimension(210, 20));
		toSale.addActionListener(new PrepareSale());
		mainC.insets = new Insets(0, 0, 0, 0);
		mainC.gridy = 2;
		add(toSale, mainC);
		
		//создание таблицы с пассажирами
		passengersTable = new JTable(new PassengerTableModel(passengers));
		passengersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		passengersTable.setRowSelectionAllowed(true);
		passengersTable.setColumnSelectionAllowed(false);
		
		JScrollPane pasPane = new JScrollPane(passengersTable);
		pasPane.setPreferredSize(new Dimension(210, 100));
		mainC.insets = new Insets(10, 0, 0, 0);
		mainC.gridy = 3;
		add(pasPane, mainC);
		
		//добавляем кнопку регистрации пассажира
		register = new JButton("Реєструвати пасажира");
		register.setPreferredSize(new Dimension(210, 20));
		register.addActionListener(new Register());
		mainC.insets = new Insets(0, 0, 0, 0);
		mainC.gridy = 4;
		add(register, mainC);
	}
	
	
	private void sendInitRequest() throws IOException {
		httpConnector.newMethod(ServletURL.SEARCH_TRIPS);
		httpConnector.executeMethod();
	}
	
	
	private void getInitResponse() throws IOException {
		try {
			trips.clear();
			NodeList tripsByTag = DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(httpConnector.getInputStream()).getDocumentElement().getElementsByTagName("TRIP");
			for (int i = 0; i < tripsByTag.getLength(); i++) {
		        Element thisTrip = (Element)tripsByTag.item(i);
		        Trip trip = new Trip();
		        trip.setTripId(
		        		Utils.getElementsByTagName(thisTrip, "TRIP_ID"));
		        trip.setTripNumber(
		        		Utils.getElementsByTagName(thisTrip, "TRIP_NUMBER"));
		        trip.setRoute(
		        		Utils.getElementsByTagName(thisTrip, "DISPATCH_CITY_NAME")
		        		+ " - " + Utils.getElementsByTagName(thisTrip, "ARRIVAL_CITY_NAME"));
		        trip.setStartDate(
		        		Utils.makeDate(
		        				Utils.getElementsByTagName(thisTrip, "DISPATCH_DATE")));
		        trip.setStartTime(
		        		Utils.getElementsByTagName(thisTrip, "DISPATCH_TIME"));
		        trip.setTotalCost(
		        		Utils.returnBigDecimal(
		        				Utils.getElementsByTagName(thisTrip, "TRIP_TOTAL_COST")));
		        trip.setFreeBuzyPlaces(
		        		Utils.getElementsByTagName(thisTrip, "FREE_BUSY_PLACES"));
		        trips.add(trip);
			}
		} catch (SAXException e) {
		} catch (ParserConfigurationException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			httpConnector.releaseMethod();
		}
	}
	
	
	private Passenger getPassengerByBarCode(String barCode) {
		for (Passenger passenger : passengers) {
			if (passenger.getBarCode().equals(barCode)) {
				return passenger;
			}
		}
		return null;
	}


	public List<Passenger> getPassengers() {
		return passengers;
	}


	public List<Select> getStartStoppings() {
		return startStoppings;
	}

	
	public List<Trip> getTrips() {
		return trips;
	}


	public JLabel getTripNumber() {
		return tripNumber;
	}


	public JLabel getRoute() {
		return route;
	}


	public JLabel getStartTime() {
		return startTime;
	}


	public JLabel getGain() {
		return gain;
	}


	public JLabel getFreeBuzySeats() {
		return freeBuzySeats;
	}


	public JComboBox getComboBox() {
		return comboBox;
	}


	public JTable getPassengersTable() {
		return passengersTable;
	}


	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

}
