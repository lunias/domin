package com.domin.player;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.domin.card.Card;
import com.domin.card.CardType;

public class Hand {	
	
	private ObservableList<Card> hand;
	
	public Hand() {
		this.hand = FXCollections.<Card> observableArrayList();
	}
	
	public ObservableList<Card> getAsList() {
		return hand;
	}
	
	public void add(Card card) {
		hand.add(card);
		FXCollections.sort(hand);
	}
	
	public void addAll(List<Card> cardList) {
		hand.addAll(cardList);
		FXCollections.sort(hand);
	}
	
	public boolean remove(Card card) {
		boolean removed = hand.remove(card);
		FXCollections.sort(hand);
		return removed;
	}
	
	public Card remove(int index) {
		Card removed = hand.remove(index);
		FXCollections.sort(hand);
		return removed;
	}
	
	public List<Card> removeAll() {
		List<Card> removeList = new ArrayList<Card>(hand);
		hand.clear();
		return removeList;
	}
	
	public List<Card> getCardsOfType(CardType type) {
		List<Card> typeList = new ArrayList<Card>(hand.size());
		for (Card card : hand) {
			if (card.isOfType(type)) {
				typeList.add(card);	
			}
		}
		return typeList;
	}
	
	public List<Card> getCardsNotOfType(CardType type) {
		List<Card> typeList = new ArrayList<Card>(hand.size());
		for (Card card : hand) {
			if (!card.isOfType(type)) {
				typeList.add(card);
			}
		}
		return typeList;
	}
	
	public void clear() {
		hand.clear();
	}
	
	public int size() {
		return hand.size();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Card card : hand) {
			sb.append(card.getName() + ", ");
		}
		return sb.toString();
	}
}
