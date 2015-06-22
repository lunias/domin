package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Bridge extends Card {

	public Bridge() {
		super("Bridge", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addBuys(1);
		player.addCoins(1);		
		
		player.incrementStatusEffectValue(getName());
		
		player.incrementReducedCostProperty();
		
	}
}
