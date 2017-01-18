package com.gillbus.steward.component;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.gillbus.steward.action.CancelSale;
import com.gillbus.steward.action.ChangeEndStopping;
import com.gillbus.steward.action.ChangeTariff;
import com.gillbus.steward.action.Register;
import com.gillbus.steward.bean.Select;
import com.gillbus.steward.model.ComboBoxModel;

public class SalePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6600112398380008309L;
	
	private static SalePanel instance;
	
	private List<Select> tariffs = new ArrayList<Select>();
	private List<Select> endStoppings = new ArrayList<Select>();
	private List<Select> seats = new ArrayList<Select>();
	
	private JComboBox endStoppingsBox;
	private JComboBox tariffsBox;
	private JComboBox seatsBox;
	private JTextField lastName;
	private JLabel ticketCost = new JLabel();
	
	
	private SalePanel() {
		setLayout(new BorderLayout());
		
		JPanel saleInfo = new JPanel(new GridBagLayout());
		Border border = BorderFactory.createTitledBorder("Продаж");
		saleInfo.setBorder(border);
		
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 0, 0);
	    c.weighty = 1.0;
	    c.weightx = 1.0;
	    c.gridx = 0;
	    c.gridy = 0;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    saleInfo.add(new JLabel("Прізвище"), c);
	    
	    c.insets = new Insets(5, 5, 0, 0);
	    c.gridy = 1;
	    saleInfo.add(new JLabel("<html>Пункт<br/>призначення</html>"), c);
	    
	    c.gridy = 2;
	    saleInfo.add(new JLabel("№ місця"), c);
	    
	    c.gridy = 3;
	    saleInfo.add(new JLabel("Тариф"), c);
	    
	    c.gridy = 4;
	    saleInfo.add(new JLabel("Вартість"), c);
	    
	    c.insets = new Insets(0, 5, 0, 0);
	    c.gridx = 1;
	    c.gridy = 0;
	    lastName = new JTextField();
	    lastName.setPreferredSize(new Dimension(130, 20));
	    saleInfo.add(lastName, c);
	    
	    c.insets = new Insets(5, 5, 0, 0);
	    c.gridy = 1;
	    endStoppingsBox = new JComboBox(new ComboBoxModel(endStoppings));
	    endStoppingsBox.setPreferredSize(new Dimension(130, 20));
	    endStoppingsBox.addActionListener(new ChangeEndStopping());
	    saleInfo.add(endStoppingsBox, c);
	    
	    c.gridy = 2;
	    seatsBox = new JComboBox(new ComboBoxModel(seats));
	    seatsBox.setPreferredSize(new Dimension(130, 20));
	    saleInfo.add(seatsBox, c);
	    
	    c.gridy = 3;
	    tariffsBox = new JComboBox(new ComboBoxModel(tariffs));
	    tariffsBox.setPreferredSize(new Dimension(130, 20));
	    tariffsBox.addActionListener(new ChangeTariff());
	    saleInfo.add(tariffsBox, c);
	    
	    c.gridy = 4;
	    saleInfo.add(ticketCost, c);
	    
	    c.insets = new Insets(10, 0, 0, 2);
	    c.gridx = 0;
	    c.gridy = 5;
	    c.anchor = GridBagConstraints.NORTHEAST;
	    JButton sale = new JButton("Продаж");
	    sale.addActionListener(new Register());
	    saleInfo.add(sale, c);
	    
	    c.insets = new Insets(10, 2, 0, 0);
	    c.gridx = 1;
	    c.anchor = GridBagConstraints.NORTHWEST;
	    JButton cancel = new JButton("Відміна");
	    cancel.addActionListener(new CancelSale());
	    saleInfo.add(cancel, c);
		
		add(saleInfo, BorderLayout.NORTH);
	}
	
	
	public static SalePanel getInstance() {
		if (instance == null) {
			instance = new SalePanel();
		}
		return instance;
	}


	public List<Select> getTariffs() {
		return tariffs;
	}


	public List<Select> getEndStoppings() {
		return endStoppings;
	}


	public List<Select> getSeats() {
		return seats;
	}


	public JComboBox getEndStoppingsBox() {
		return endStoppingsBox;
	}


	public JComboBox getTariffsBox() {
		return tariffsBox;
	}


	public JComboBox getSeatsBox() {
		return seatsBox;
	}


	public JLabel getTicketCost() {
		return ticketCost;
	}
	
}
