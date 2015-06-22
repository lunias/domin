package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Woodcutter extends Card {

	private static final long serialVersionUID = 1L;

	public Woodcutter() {
		super("Woodcutter", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	@Override
	public int givesBuys() {
		return 1;
	}
	
	@Override
	public int givesCoins() {
		return 2;
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.addBuys(1);
		player.addCoins(2);
	}
	
}
