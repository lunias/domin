package com.domin.player;

import java.io.Serializable;

public enum CardEventType implements Serializable {

	PLAY("P"), GAIN("G"), BUY("B"), TRASH("T"), UNTRASH("UT"), DISCARD("D"), CLEAR("C"), DURATION("D"),
	ATTACK_DISCARD("AD"), ATTACK_TOP("AT"), HAND("H"), PLAY_COPY("PC"), REVEAL("R");
	
	private String abbrev;
	
	private CardEventType(String abbrev) {
		this.abbrev = abbrev;
	}
	
	public String getAbbrev() {
		return abbrev;
	}
}
