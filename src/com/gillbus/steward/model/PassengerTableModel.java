package com.gillbus.steward.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.gillbus.steward.bean.Passenger;

public class PassengerTableModel implements TableModel {
	
	private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
	private List<Passenger> passengers;
	

	public PassengerTableModel(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	@Override
	public void addTableModelListener(TableModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "<html>№ м.<br/>&nbsp;</html>";
		case 1:
			return "<html>Док.<br/>&nbsp;</html>";
		case 2:
			return "<html>Пункт<br/>призн.</html>";
		case 3:
			return "<html>Статус<br/>&nbsp;</html>";
		default:
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return passengers.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (passengers.get(rowIndex) != null) {
			switch (columnIndex) {
			case 0:
				return passengers.get(rowIndex).getNumberSeat();
			case 1:
				return passengers.get(rowIndex).getOperation();
			case 2:
				return passengers.get(rowIndex).getEndPointName();
			case 3:
				return passengers.get(rowIndex).getStatus();
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}
	
}
