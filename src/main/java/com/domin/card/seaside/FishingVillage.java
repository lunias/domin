package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class FishingVillage extends Card {

	private static final long serialVersionUID = 1L;

	public FishingVillage() {
		super("Fishing Village", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(3));
	}

	public void play(Player player, DominClient connection) {		
		player.addActions(1);
		player.addCoins(1);				
	}
	
	@Override
	public void durationEffect(Player player, DominClient connection) {
		player.addActions(1);
		player.addCoins(1);
	}
	
}
