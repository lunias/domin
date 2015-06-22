package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Tactician extends Card {

	private static final long serialVersionUID = 1L;

	public Tactician() {
		super("Tactician", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		if (player.getHand().size() > 0) {
			player.enableStatusEffect(getName());
			player.discardAll();
		}		
		
	}
	
	@Override
	public void durationEffect(Player player, DominClient connection) {
		
		if (player.getStatusEffectValue(getName()) > 0) {
			player.draw(5);
			player.addBuys(1);
			player.addActions(1);
			player.disableStatusEffect(getName());
		}
		
	}	
	
}
