package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Caravan extends Card {

	private static final long serialVersionUID = 1L;

	public Caravan() {
		super("Caravan", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
	}
	
	@Override
	public void durationEffect(Player player, DominClient connection) {
		
		player.draw();
		
	}
	
}
