package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Workshop extends Card {

	private static final long serialVersionUID = 1L;

	public Workshop() {
		super("Workshop", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.addGain(4, 0);
	}
}
