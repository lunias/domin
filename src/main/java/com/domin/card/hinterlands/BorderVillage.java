package com.domin.card.hinterlands;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class BorderVillage extends Card {

	private static final long serialVersionUID = 1L;

	public BorderVillage() {
		super("Border Village", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(6));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);
		
	}

	public void gainEffect(Player player, DominClient connection) {
		
		player.addGain(getCost().getCoins() - 1, 0);
		
	}
	
}
