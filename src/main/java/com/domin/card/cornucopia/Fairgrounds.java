package com.domin.card.cornucopia;

import java.util.HashSet;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.player.Player;

public class Fairgrounds extends Card {

	private static final long serialVersionUID = 1L;

	public Fairgrounds() {
		super("Fairgrounds", CardSet.CORNUCOPIA, new CardType[] {CardType.VICTORY}, new CardCost(6));
	}

	@Override
	public int givesVictory() {
		return 2 * (new HashSet<Card>(Player.INSTANCE.getDeck().getAsList()).size() / 5);		
	}
	
}
