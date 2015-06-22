package com.domin.card.hinterlands;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class SilkRoad extends Card {

	private static final long serialVersionUID = 1L;

	public SilkRoad() {
		super("Silk Road", CardSet.HINTERLANDS, new CardType[] {CardType.VICTORY}, new CardCost(4));
	}
	
	public int givesVictory() {
		
		int victoryCount = 0;
		
		for (Card card : Player.INSTANCE.getDeck().getAsList()) {
			if (card.isOfType(CardType.VICTORY)) {
				victoryCount++;
			}
		}
		
		return victoryCount / 4;		
		
	}

}
