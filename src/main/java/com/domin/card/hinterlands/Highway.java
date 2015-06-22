package com.domin.card.hinterlands;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Highway extends Card {

	private static final long serialVersionUID = 1L;

	public Highway() {
		super("Highway", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();		
		
		player.incrementStatusEffectValue(getName());
		
		player.incrementReducedCostProperty();
		
	}
	
}
