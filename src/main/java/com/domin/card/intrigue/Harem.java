package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Harem extends Card {

	private static final long serialVersionUID = 1L;

	public Harem() {
		super("Harem", CardSet.INTRIGUE, new CardType[] {CardType.TREASURE, CardType.VICTORY}, new CardCost(6));
	}
	
	@Override
	public int givesVictory() {
		return 2;
	}
	
	public void play(Player player, DominClient connection) {
		
		player.addCoins(2);
		
	}

}
