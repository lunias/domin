package com.domin.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.domin.card.Card;

public class DiscardPile {
		
	Deque<Card> discard;
	
	private StringProperty sizeProperty;
	
	public DiscardPile() {
		
		discard = new ArrayDeque<Card>();
		
		sizeProperty = new SimpleStringProperty();
		sizeProperty.set("0");
		
	}
	
	public List<Card> getAsList() {
		return new ArrayList<Card>(discard);
	}

	public Card peek() {
		return discard.peek();
	}
	
	public void add(Card card) {
		discard.addFirst(card);
		sizeProperty.set(String.valueOf(size()));
	}
	
	public boolean addAll(List<Card> cardList) {
		boolean changed = discard.addAll(cardList);
		sizeProperty.set(String.valueOf(size()));
		return changed;
	}
	
	public boolean remove(Card card) {
		boolean changed = discard.remove(card);
		sizeProperty.set(String.valueOf(size()));
		return changed;
	}
	
	public List<Card> getShuffledList() {
		List<Card> cardList = getAsList();
		Collections.shuffle(cardList);
		return cardList;
	}
	
	public void clear() {
		discard.clear();
		sizeProperty.set(String.valueOf(size()));
	}
	
	public int size() {
		return discard.size();
	}
	
	public StringProperty getSizeProperty() {
		return sizeProperty;
	}

}
