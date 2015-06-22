package com.domin.card;

import java.io.Serializable;

public enum CardSet implements Serializable {		
	
	BASE("Base"), INTRIGUE("Intrigue"), SEASIDE("Seaside"),
	ALCHEMY("Alchemy"), PROSPERITY("Prosperity"), CORNUCOPIA("Cornucopia"),
	HINTERLANDS("Hinterlands"), DARK_AGES("Dark Ages"), PROMO("Promo");
	
	private String name;
	
	private CardSet(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
