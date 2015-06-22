package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Adventurer extends Card {

	private static final long serialVersionUID = 1L;

	public Adventurer() {
		super("Adventurer", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(6));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> cardList = player.popUntil(CardType.TREASURE, 2);
		
		for (Card card : cardList) {
			if (card.isOfType(CardType.TREASURE)) {
				player.getHand().add(card);
				connection.send(new CardEventPacket(card, CardEventType.HAND));	
			} else {
				player.getDiscardPile().add(card);
				connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			}
		}
		
	}
	
}
