package com.domin.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.base.Copper;
import com.domin.card.base.Estate;

public class Deck {	
	
	private Deque<Card> deck;
	
	private StringProperty sizeProperty;
	
	public Deck() {				
		
		List<Card> deckList = new ArrayList<Card>();		
		
		for (int i = 0; i < 7; i++) {
			deckList.add(new Copper());
		}
		
		for (int i = 0; i < 3; i++) {
			deckList.add(new Estate());
		}
		
		Collections.shuffle(deckList);
		
		sizeProperty = new SimpleStringProperty();
		sizeProperty.set("10");
		
		deck = new ArrayDeque<Card>(deckList);		
	}
	
	public List<Card> getAsList() {
		return new ArrayList<Card>(deck);
	}
	
	public Card draw() {
		Card drawn = deck.poll();
		sizeProperty.set(String.valueOf(size()));
		return drawn;
	}
	
	public Card peek() {
		return deck.peek();		
	}
	
	public Card peekLast() {
		return deck.peekLast();
	}
	
	public void addToTop(Card card) {
		deck.addFirst(card);
		sizeProperty.set(String.valueOf(size()));
	}
	
	public void addToBottom(Card card) {
		deck.addLast(card);
		sizeProperty.set(String.valueOf(size()));
	}
	
	public boolean addAll(List<Card> cardList) {		
		boolean added = deck.addAll(cardList);
		sizeProperty.set(String.valueOf(size()));
		return added;
	}
	
	public Card removeLast() {
		Card card = deck.pollLast();
		sizeProperty.set(String.valueOf(size()));
		return card;
	}
	
	public Card removeFirst() {
		Card card = deck.poll();
		sizeProperty.set(String.valueOf(size()));
		return card;
	}
	
	public List<Card> removeAll() {
		List<Card> removeList = new ArrayList<Card>(deck);
		deck.clear();
		sizeProperty.set(String.valueOf(size()));
		return removeList;
	}
	
	public int size() {
		return deck.size();
	}
	
	public StringProperty getSizeProperty() {
		return sizeProperty;
	}
}
