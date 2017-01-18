package com.gillbus.steward.model;

import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import com.gillbus.steward.bean.Select;

public class ComboBoxModel extends AbstractListModel implements MutableComboBoxModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7153856399811668913L;
	
	private List<Select> items;
	private Select selectedItem;
	

	public ComboBoxModel(List<Select> newItems) {
		items = newItems;
		if (items != null && items.size() > 0) {
			selectedItem = items.get(0);
		}
	}


	@Override
	public Object getElementAt(int index) {
		if ( index >= 0 && index < items.size() )
            return items.get(index);
        else
            return null;
	}

	
	@Override
	public int getSize() {
		return items.size();
	}


	@Override
	public void addElement(Object newObj) {
		if (newObj != null && newObj instanceof Select) {
			items.add((Select)newObj);
	        fireIntervalAdded(this, items.size()-1, items.size()-1);
	        if ( items.size() == 1 && selectedItem == null && newObj != null ) {
	            setSelectedItem(newObj);
	        }
		}
	}


	@Override
	public void insertElementAt(Object newObj, int index) {
		if (newObj != null && newObj instanceof Select) {
			items.add(index, (Select)newObj);
			fireIntervalAdded(this, index, index);
		}
	}


	@Override
	public void removeElement(Object obj) {
		if (obj != null && obj instanceof Select) {
			int index = items.indexOf(obj);
			if (index != -1) {
				removeElementAt(index);
			}
		}
	}


	@Override
	public void removeElementAt(int index) {
		if (getElementAt(index) == selectedItem) {
			if (index == 0) {
				setSelectedItem(getSize() == 1 ? null : getElementAt(index + 1));
			} else {
				setSelectedItem(getElementAt(index - 1));
			}
		}

		items.remove(index);

		fireIntervalRemoved(this, index, index);
	}


	@Override
	public Object getSelectedItem() {
		return selectedItem;
	}


	@Override
	public void setSelectedItem(Object anItem) {
		if (((selectedItem != null && !selectedItem.equals(anItem))
				|| selectedItem == null && anItem != null) 
				&& anItem instanceof Select) {
			selectedItem = (Select)anItem;
			fireContentsChanged(this, -1, -1);
		}
	}

}
