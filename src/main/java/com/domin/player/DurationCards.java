package com.domin.player;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;

public class DurationCards {

	List<Card> durationCards;
	
	public DurationCards() {
		this.durationCards = new ArrayList<Card>();
	}
	
	public List<Card> getAsList() {
		return durationCards;
	}
	
	public boolean add(Card card) {
		return durationCards.add(card);
	}
	
	public List<Card> removeAll() {
		List<Card> removeList = new ArrayList<Card>(durationCards);
		durationCards.clear();		
		return removeList;
	}

	public int size() {
		return durationCards.size();
	}
	
}
