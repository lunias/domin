package com.domin.card.alchemy;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Vineyard extends Card {

	private static final long serialVersionUID = 1L;

	public Vineyard() {
		super("Vineyard", CardSet.ALCHEMY, new CardType[] {CardType.VICTORY}, new CardCost(0, 1));
	}

	@Override
	public int givesVictory() {
		
		int actionCount = 0;
		
		for (Card card : Player.INSTANCE.getDeck().getAsList()) {
			if (card.isOfType(CardType.ACTION)) {
				actionCount++;
			}
		}
		
		return actionCount / 3;
		
	}
	
}
