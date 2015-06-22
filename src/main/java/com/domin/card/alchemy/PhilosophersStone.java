package com.domin.card.alchemy;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class PhilosophersStone extends Card {

	private static final long serialVersionUID = 1L;

	public PhilosophersStone() {
		super("Philosopher's Stone", CardSet.ALCHEMY, new CardType[] {CardType.TREASURE}, new CardCost(3, 1));
	}

	public void play(Player player, DominClient connection) {
						
		player.addCoins((player.getDeck().size() + player.getDiscardPile().size()) / 5);
		
	}
	
}
