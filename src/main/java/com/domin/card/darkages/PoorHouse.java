package com.domin.card.darkages;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class PoorHouse extends Card {

	private static final long serialVersionUID = 1L;

	public PoorHouse() {
		super("Poor House", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(1));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		int toAdd = 4 - player.getHand().getCardsOfType(CardType.TREASURE).size();
		
		if (toAdd > 0) {
			player.addCoins(toAdd);	
		}
		
	}
	
}
