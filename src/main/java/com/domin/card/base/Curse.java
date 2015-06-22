package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Curse extends Card {

	private static final long serialVersionUID = 1L;

	public Curse() {
		super("Curse", CardSet.BASE, new CardType[] {CardType.CURSE, CardType.BASIC}, new CardCost(0));
	}

	@Override
	public int givesVictory() {
		return -1;
	}

	@Override
	public boolean mentionsCurse() {
		return true;
	}

}
