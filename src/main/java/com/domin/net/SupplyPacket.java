package com.domin.net;

import java.io.Serializable;
import java.util.List;

import com.domin.card.Card;

public class SupplyPacket implements Serializable {

	private static final long serialVersionUID = 1L;	

	List<Card> supplyList;
	List<Card> basicSupplyList;
	
	public SupplyPacket(List<Card> supplyList, List<Card> basicSupplyList) {
		this.supplyList = supplyList; 
		this.basicSupplyList = basicSupplyList;
	}
	
	public List<Card> getSupplyList() {
		return supplyList;
	}
	
	public List<Card> getBasicSupplyList() {
		return basicSupplyList;
	}
}
