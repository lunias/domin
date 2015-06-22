package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Smithy extends Card {

	public Smithy() {
		super("Smithy", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.draw(3);
	}

}
