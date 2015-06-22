package com.domin.card.hinterlands;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Crossroads extends Card {

	private static final long serialVersionUID = 1L;

	public Crossroads() {
		super("Crossroads", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(2));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		player.draw(player.getHand().getCardsOfType(CardType.VICTORY).size());
		
		if (player.getStatusEffectValue(getName()) == 0) {
			player.addActions(3);
			player.enableStatusEffect(getName());
		}
		
	}
	
}
