package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Feast extends Card {

	private static final long serialVersionUID = 1L;

	public Feast() {
		super("Feast", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}	
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.trashImmediately(this);
		player.addGain(5, 0);
	}
	
}
