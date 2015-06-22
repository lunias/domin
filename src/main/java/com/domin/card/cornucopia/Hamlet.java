package com.domin.card.cornucopia;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Hamlet extends Card {

	private static final long serialVersionUID = 1L;

	public Hamlet() {
		super("Hamlet", CardSet.CORNUCOPIA, new CardType[] {CardType.ACTION}, new CardCost(2));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard a Card?", CardLocation.HAND, 1, true);
		
		for (Card card : discardList) {			
			player.getHand().remove(card);
			player.getDiscardPile().add(card);
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			player.addActions(1);			
		}
		
		discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard a Card?", CardLocation.HAND, 1, true);
		
		for (Card card : discardList) {
			player.getHand().remove(card);
			player.getDiscardPile().add(card);
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			player.addBuys(1);
		}
				
		
	}
	
}
