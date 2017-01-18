package com.gillbus.steward.bean;

import java.math.BigDecimal;
import java.util.Date;

public class Trip {

	private String tripId;
	private String tripNumber;
	private String route;
	private String startTime;
	private String freeBuzyPlaces;
	private BigDecimal totalCost;
	private Date startDate;
	
	public Trip() {
		
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getTripNumber() {
		return tripNumber;
	}

	public void setTripNumber(String tripNumber) {
		this.tripNumber = tripNumber;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFreeBuzyPlaces() {
		return freeBuzyPlaces;
	}

	public void setFreeBuzyPlaces(String freeBuzyPlaces) {
		this.freeBuzyPlaces = freeBuzyPlaces;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
}
