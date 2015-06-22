package com.domin.card.cornucopia;

import java.util.HashSet;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Harvest extends Card {

	private static final long serialVersionUID = 1L;

	public Harvest() {
		super("Harvest", CardSet.CORNUCOPIA, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> poppedList = player.popDeck(4);
		
		for (Card card : poppedList) {
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
		}
		
		player.addCoins(new HashSet<Card>(poppedList).size());
		
		player.getDiscardPile().addAll(poppedList);
		
	}
	
}
