package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Gardens extends Card {

	private static final long serialVersionUID = 1L;

	public Gardens() {
		super("Gardens", CardSet.BASE, new CardType[] {CardType.VICTORY} , new CardCost(4));
	}
	
	@Override
	public int givesVictory() {
		return Player.INSTANCE.getDeck().size() / 10;
	}
	
}
