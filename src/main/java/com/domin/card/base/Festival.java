package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Festival extends Card {

	private static final long serialVersionUID = 1L;

	public Festival() {
		super("Festival", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		player.addActions(1);
		player.addBuys(1);
		player.addCoins(2);
	}

}
