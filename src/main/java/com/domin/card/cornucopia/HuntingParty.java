package com.domin.card.cornucopia;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class HuntingParty extends Card {

	private static final long serialVersionUID = 1L;

	public HuntingParty() {
		super("Hunting Party", CardSet.CORNUCOPIA, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		List<Card> toDiscard = new ArrayList<Card>();
		
		Card card;
		while(player.getHand().getAsList().contains((card = player.popDeck()))) {
			 connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			 toDiscard.add(card);
		}

		if (card != null && !player.getHand().getAsList().contains(card)) {
			player.getHand().add(card);
		}

		player.getDiscardPile().addAll(toDiscard);
		
	}
	
}
