package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Village extends Card {

	private static final long serialVersionUID = 1L;

	public Village() {
		super("Village", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	@Override
	public int givesCards() {
		return 1;
	}
	
	@Override
	public int givesActions() {
		return 2;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.draw();
		player.addActions(1);
	}
}
