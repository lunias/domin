package com.domin.card.alchemy;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Potion extends Card {

	private static final long serialVersionUID = 1L;

	public Potion() {
		super("Potion", CardSet.ALCHEMY, new CardType[] {CardType.TREASURE, CardType.BASIC}, new CardCost(4));
	}
	
	@Override
	public int givesPotions() {
		return 1;
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.addPotions(1);
	}
	
}
