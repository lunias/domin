package com.domin.player;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;

public class PlayedCards {
	
	List<Card> playedCards;
	
	public PlayedCards() {
		this.playedCards = new ArrayList<Card>();
	}
	
	public List<Card> getAsList() {
		return playedCards;
	}
	
	public boolean add(Card card) {
		return playedCards.add(card);
	}
	
	public boolean remove(Card card) {
		return playedCards.remove(card);
	}
	
	public List<Card> removeAll() {
		List<Card> removeList = new ArrayList<Card>(playedCards);
		playedCards.clear();
		return removeList;
	}
	
	public int size() {
		return playedCards.size();
	}
	
}
