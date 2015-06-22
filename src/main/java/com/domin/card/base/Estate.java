package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Estate extends Card {

	private static final long serialVersionUID = 1L;

	public Estate() {
		super("Estate", CardSet.BASE, new CardType[] {CardType.VICTORY, CardType.BASIC}, new CardCost(2));
	}

	@Override
	public int givesVictory() {
		return 1;
	}

}
