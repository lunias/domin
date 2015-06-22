package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Conspirator extends Card {

	private static final long serialVersionUID = 1L;

	public Conspirator() {
		super("Conspirator", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.addCoins(2);
		
		int actionCount = 0;
		for (Card card : player.getPlayedCards().getAsList()) {			
			if (card.isOfType(CardType.ACTION)) {
				actionCount++;
			}
		}
		
		for (Card card : player.getDurationCards().getAsList()) {
			if (card.isOfType(CardType.ACTION)) {
				actionCount++;
			}
		}
		
		if (actionCount > 2) {
			player.draw();
		} else {
			player.removeActions(1);
		}
		
	}

}
