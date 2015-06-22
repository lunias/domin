package com.domin.card.darkages;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Sage extends Card {

	private static final long serialVersionUID = 1L;

	public Sage() {
		super("Sage", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	public void play(Player player, DominClient connection) {
		
		List<Card> poppedList = player.popUntilMoreThan(2);
		
		for (Card card : poppedList) {
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			if (card.getCost().getCoins() > 2) {
				player.getHand().add(card);
				poppedList.remove(card);
				break;
			}
		}
		
		player.getDiscardPile().addAll(poppedList);
		
	}

}
