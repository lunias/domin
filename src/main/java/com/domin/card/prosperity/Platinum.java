package com.domin.card.prosperity;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Platinum extends Card {

	private static final long serialVersionUID = 1L;

	public Platinum() {
		super("Platinum", CardSet.PROSPERITY, new CardType[] {CardType.BASIC, CardType.TREASURE}, new CardCost(9));
	}
	
	@Override
	public int givesCoins() {

		return 3;
		
	}

	@Override
	public void play(Player player, DominClient connection) {
		
		player.addCoins(5);
		
	}	
}
