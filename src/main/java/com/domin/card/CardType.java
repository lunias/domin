package com.domin.card;

import java.io.Serializable;

public enum CardType implements Serializable {
	
	ACTION("Action"), REACTION("Reaction"), ATTACK("Attack"),
	VICTORY("Victory"), TREASURE("Treasure"), DURATION("Duration"),
	PRIZE("Prize"), RUINS("Ruins"), SHELTER("Shelter"), LOOTER("Looter"),
	KNIGHT("Knight"), CURSE("Curse"), BASIC("basic");
	
	private final String name;
	
	private CardType(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}	

}
