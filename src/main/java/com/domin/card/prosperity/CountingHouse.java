package com.domin.card.prosperity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Copper;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class CountingHouse extends Card {

	private static final long serialVersionUID = 1L;

	public CountingHouse() {
		super("Counting House", CardSet.PROSPERITY, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> copperInDiscard = new ArrayList<Card>();
		for (Card card : player.getDiscardPile().getAsList()) {
			if (card instanceof Copper) {
				copperInDiscard.add(card);
			}
		}		
		
		List<Card> toHand = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Copper in your Hand", copperInDiscard, -1, false);
		
		for (Card card : toHand) {
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			player.getDiscardPile().remove(card);
			player.getHand().add(card);			
		}			
		
	}

}
