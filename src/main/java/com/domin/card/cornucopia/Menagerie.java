package com.domin.card.cornucopia;

import java.util.HashSet;
import java.util.Set;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Menagerie extends Card {

	private static final long serialVersionUID = 1L;

	public Menagerie() {
		super("Menagerie", CardSet.CORNUCOPIA, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	public void play(Player player, DominClient connection) {
		
		Set<Card> handSet = new HashSet<Card>(player.getHand().getAsList());
		
		if (handSet.size() == player.getHand().getAsList().size()) {
			player.draw(3);			
		} else {
			player.draw();
		}
		
	}

}
