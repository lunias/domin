package com.domin.card.darkages;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Curse;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Vagrant extends Card {

	private static final long serialVersionUID = 1L;

	public Vagrant() {
		super("Vagrant", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(2));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		Card peeked = player.peekAtDeck(true);
		
		connection.send(new CardEventPacket(peeked, CardEventType.REVEAL));
		
		if (peeked.isOfType(CardType.RUINS) || peeked.isOfType(CardType.SHELTER) || peeked.isOfType(CardType.VICTORY) || peeked instanceof Curse) {
			player.getHand().add(player.getDeck().removeFirst());
		}
		
	}

}
