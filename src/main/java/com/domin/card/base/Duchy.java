package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Duchy extends Card {

	private static final long serialVersionUID = 1L;

	public Duchy() {
		super("Duchy", CardSet.BASE, new CardType[] {CardType.VICTORY, CardType.BASIC}, new CardCost(5));
	}

	@Override
	public int givesVictory() {
		return 3;
	}

}
