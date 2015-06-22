package com.domin.card.prosperity;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;

public class Colony extends Card {

	private static final long serialVersionUID = 1L;

	public Colony() {
		super("Colony", CardSet.PROSPERITY, new CardType[] {CardType.VICTORY, CardType.BASIC}, new CardCost(11));
	}
	
	@Override
	public int givesVictory() {
		return 10;
	}	
}
