package com.gillbus.steward.connection;

import com.gillbus.steward.Steward;

/**
 * 
 * @author Kashpur Artem
 *
 */
public class ServletURL {
	
	public final static String LOGIN = Steward.BASE_URL + "steward/login";
	
	public final static String SEARCH_TRIPS = Steward.BASE_URL + "steward/searchTrips";
	
	public final static String CHANGE_TRIP = Steward.BASE_URL + "steward/changeTrip";
	
	public final static String CHANGE_START_STOPPING = Steward.BASE_URL + "steward/changeStartStopping";
	
	public final static String CHANGE_END_STOPPING = Steward.BASE_URL + "steward/changeEndStopping";
	
	public final static String CHANGE_TARIFF = Steward.BASE_URL + "steward/changeTariff";
	
	public final static String SEARCH_SEATS = Steward.BASE_URL + "steward/searchSeats";
	
	public final static String PREPARE_SALE = Steward.BASE_URL + "steward/prepareSale";
	
	public final static String REGISTER_PASSENGER = Steward.BASE_URL + "steward/registerPassenger";

}
