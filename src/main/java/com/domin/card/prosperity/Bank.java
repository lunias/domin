package com.domin.card.prosperity;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Bank extends Card {

	private static final long serialVersionUID = 1L;

	public Bank() {
		super("Bank", CardSet.PROSPERITY, new CardType[] {CardType.TREASURE}, new CardCost(7));
	}

	public void play(Player player, DominClient connection) {
		
		int numTreasures = 0;
		for(Card card : player.getPlayedCards().getAsList()) {
			if (card.isOfType(CardType.TREASURE)) {
				numTreasures++;
			}
		}

		player.addCoins(numTreasures);
		
	}
	
}
