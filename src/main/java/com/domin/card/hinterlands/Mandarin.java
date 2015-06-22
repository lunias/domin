package com.domin.card.hinterlands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Mandarin extends Card {

	private static final long serialVersionUID = 1L;

	public Mandarin() {
		super("Mandarin", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.addCoins(3);
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put a Card on top of your deck", CardLocation.HAND, 1, false);
		
		for (Card card : toTop) {
			player.getHand().remove(card);
			player.addToTopOfDeck(card);
		}				
		
	}
	
	public void gainEffect(Player player, DominClient connection) {
		
		List<Card> playedTreasures = new ArrayList<Card>();

		Iterator<Card> playedCardsIter = player.getPlayedCards().getAsList().iterator();
		while(playedCardsIter.hasNext()) {
			Card card = playedCardsIter.next();
			if (card.isOfType(CardType.TREASURE)) {
				playedTreasures.add(card);
				playedCardsIter.remove();
			}
			
		}
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Treasures on top", playedTreasures, playedTreasures.size(), false, getImage());
		
		for (Card card : toTop) {
			player.addToTopOfDeck(card);
		}
		
	}
	
}
