package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class GreatHall extends Card {

	private static final long serialVersionUID = 1L;

	public GreatHall() {
		super("Great Hall", CardSet.INTRIGUE, new CardType[] {CardType.ACTION, CardType.VICTORY}, new CardCost(3));
	}
	
	public int givesVictory() {
		return 1;
	}
	
	public void play(Player player, DominClient connection) {
		player.draw();
	}

}
