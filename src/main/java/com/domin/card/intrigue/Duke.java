package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Duke extends Card {

	private static final long serialVersionUID = 1L;

	public Duke() {
		super("Duke", CardSet.INTRIGUE, new CardType[] {CardType.VICTORY}, new CardCost(5));
	}

	@Override
	public int givesVictory() {
		int duchyCount = 0;
		
		for (Card card : Player.INSTANCE.getDeck().getAsList()) {
			if ("Duchy".equals(card.getName())) {
				duchyCount++;
			}				
		}
		
		return duchyCount;
	}
	
}
