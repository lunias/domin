package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Province extends Card {

	private static final long serialVersionUID = 1L;

	public Province() {
		super("Province", CardSet.BASE, new CardType[] {CardType.VICTORY, CardType.BASIC}, new CardCost(8));
	}

	@Override
	public int givesVictory() {
		return 6;
	}

}
