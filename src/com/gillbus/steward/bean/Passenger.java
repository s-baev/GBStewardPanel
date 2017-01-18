package com.gillbus.steward.bean;

public class Passenger implements Cloneable{

	private String tripPassengerId;
	private String operationId;
	private String operation;
	private String statusId;
	private String status;
	private String endPointName;
	private String numberSeat;
	private String barCode;
	
	public Passenger() {
		
	}
	
	public Passenger clone() throws CloneNotSupportedException {
		return (Passenger) super.clone();
	}
	
	public String getTripPassengerId() {
		return tripPassengerId;
	}

	public void setTripPassengerId(String tripPassengerId) {
		this.tripPassengerId = tripPassengerId;
	}

	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEndPointName() {
		return endPointName;
	}

	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}

	public String getNumberSeat() {
		return numberSeat;
	}

	public void setNumberSeat(String numberSeat) {
		this.numberSeat = numberSeat;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
}
