package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Copper extends Card {

	private static final long serialVersionUID = 1L;

	public Copper() {
		super("Copper", CardSet.BASE, new CardType[] {CardType.TREASURE, CardType.BASIC}, new CardCost(0));
	}

	@Override
	public int givesCoins() {
		return 1;
	}

	@Override
	public void play(Player player, DominClient connection) {
		
		for (int i = 0; i < player.getStatusEffectValue("Coppersmith"); i++) {
			player.addCoins(1);
		}
		
		player.addCoins(1);		
	}

}
