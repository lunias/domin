package com.domin.card.darkages;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;

public class Hovel extends Card {

	private static final long serialVersionUID = 1L;

	public Hovel() {
		super("Hovel", CardSet.DARK_AGES, new CardType[] {CardType.REACTION, CardType.SHELTER, CardType.BASIC}, new CardCost(1));
	}

}
