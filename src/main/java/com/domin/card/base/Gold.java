package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Gold extends Card {

	private static final long serialVersionUID = 1L;

	public Gold() {
		super("Gold", CardSet.BASE, new CardType[] {CardType.TREASURE, CardType.BASIC}, new CardCost(6));
	}

	@Override
	public int givesCoins() {

		return 3;
		
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.addCoins(3);		
	}

}
