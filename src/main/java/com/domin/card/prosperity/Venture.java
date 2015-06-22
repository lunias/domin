package com.domin.card.prosperity;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Venture extends Card {

	private static final long serialVersionUID = 1L;

	public Venture() {
		super("Venture", CardSet.PROSPERITY, new CardType[] {CardType.TREASURE}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.addCoins(1);
		
		List<Card> cardList = player.popUntil(CardType.TREASURE, 1);
		
		for (Card card : cardList) {
			if (card.isOfType(CardType.TREASURE)) {
				connection.send(new CardEventPacket(card, CardEventType.PLAY));												
				player.getPlayedCards().add(card);
				player.setLastPlayedCard(card);
				card.play(player, connection);
			} else {
				player.getDiscardPile().add(card);
				connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			}
		}		
		
	}

}
