package com.domin.card.darkages;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Scavenger extends Card {

	private static final long serialVersionUID = 1L;

	public Scavenger() {
		super("Scavenger", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addCoins(2);		
		
		String[] choices = {"Put deck into discard pile"};
		
		List<Integer> selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Put deck into discard pile?", choices, 1, true);
		
		if (!selectedList.isEmpty()) {
			player.discardDeck();
		}
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put on top?", CardLocation.DISCARD, 1, false);		

		for (Card card : toTop) {
			player.getDiscardPile().remove(card);
			player.addToTopOfDeck(card);
		}
	}

}
