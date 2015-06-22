package com.domin.card.prosperity;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;

public class City extends Card {

	private static final long serialVersionUID = 1L;

	public City() {
		super("City", CardSet.PROSPERITY, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);
		
		int emptySupplyCount = 0;
		for (StringProperty supplyCount : DominGameScreen.supplyCardMap.values()) {			
			if ("0".equals(supplyCount.get())) {
				emptySupplyCount++;
			}			
		}
		
		if (emptySupplyCount > 0) {
			player.draw();
		}
		
		if (emptySupplyCount > 1) {
			player.addCoins(1);
			player.addBuys(1);
		}
	}
	
}
