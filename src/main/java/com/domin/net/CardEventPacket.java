package com.domin.net;

import java.io.Serializable;

import com.domin.card.Card;
import com.domin.player.CardEventType;

public class CardEventPacket implements Serializable {

	private static final long serialVersionUID = 1L;

	private Card card;
	private CardEventType eventType;
	
	public CardEventPacket(Card card, CardEventType eventType) {
		this.card = card;
		this.eventType = eventType;
	}
	
	public Card getCard() {
		return card;
	}
	
	public CardEventType getEventType() {
		return eventType;
	}
}
