package com.gillbus.steward.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.gillbus.steward.bean.Trip;

public class TripTableModel implements TableModel{
	
	private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
	private List<Trip> trips;
	
	

	public TripTableModel(List<Trip> trips) {
		this.trips = trips;
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
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "<html>№<br/>рейса</html>";
		case 1:
			return "<html>Час<br/>відпр.</html>";
		case 2:
			return "<html>Кіл.<br/>місць</html>";
		default:
			return "";
		}
	}

	@Override
	public int getRowCount() {
		return trips.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (trips.get(rowIndex) != null) {
			switch (columnIndex) {
			case 0:
				return trips.get(rowIndex).getTripNumber();
			case 1:
				return trips.get(rowIndex).getStartTime();
			case 2:
				return trips.get(rowIndex).getFreeBuzyPlaces();
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
