package com.domin.card.intrigue;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Ironworks extends Card {

	private static final long serialVersionUID = 1L;

	public Ironworks() {
		super("Ironworks", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addGain(4, 0);
		player.enableStatusEffect(getName());
		
	}
	
}
