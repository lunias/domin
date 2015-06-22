package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Bazaar extends Card {

	private static final long serialVersionUID = 1L;

	public Bazaar() {
		super("Bazaar", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);
		player.addCoins(1);
		
	}

}
