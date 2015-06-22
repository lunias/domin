package com.domin.card.prosperity;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class WorkersVillage extends Card {

	private static final long serialVersionUID = 1L;

	public WorkersVillage() {
		super("Worker's Village", CardSet.PROSPERITY, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);
		player.addBuys(1);
		
	}
	
}
