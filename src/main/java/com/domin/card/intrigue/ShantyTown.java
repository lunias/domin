package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class ShantyTown extends Card {

	private static final long serialVersionUID = 1L;

	public ShantyTown() {
		super("Shanty Town", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	public void play(Player player, DominClient connection) {
		player.addActions(1);
		
		for (Card card : player.getHand().getAsList()) {
			if (card.isOfType(CardType.ACTION)) {
				return;
			}
		}
		
		player.draw(2);
	}

}
