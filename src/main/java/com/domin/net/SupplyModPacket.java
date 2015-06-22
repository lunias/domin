package com.domin.net;

import java.io.Serializable;

import com.domin.card.Card;

public class SupplyModPacket implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Card card;
	private int mod;
	
	public SupplyModPacket(Card card, int mod) {
		
		this.card = card;
		this.mod = mod;
		
	}
	
	public Card getCard() {
		return card;
	}
	
	public String getCardName() {
		return card.getName();
	}
	
	public int getMod() {
		return mod;
	}
	
}
