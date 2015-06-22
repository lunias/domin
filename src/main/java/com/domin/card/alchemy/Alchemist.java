package com.domin.card.alchemy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Alchemist extends Card {

	private static final long serialVersionUID = 1L;

	public Alchemist() {
		super("Alchemist", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(3, 1));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw(2);
		
	}
	
	public void endOfTurnEffect(Player player, DominClient connection) {		
		
		boolean playedPotion = false;
		for (Card card : player.getPlayedCards().getAsList()) {
			if (card instanceof Potion) {
				playedPotion = true;
				break;
			}
		}
		
		if (playedPotion) {
			List<Card> alchemistsPlayed = new ArrayList<Card>();
			for (Card card : player.getPlayedCards().getAsList()) {
				if (card instanceof Alchemist) {
					alchemistsPlayed.add(card);
				}
			}
			
			List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put an Alchemist's on top?", alchemistsPlayed, alchemistsPlayed.size(), true, this.getImage());
			for (Card card : toTop) {
				player.getPlayedCards().remove(card);
				player.addToTopOfDeck(card);
			}
			
			Iterator<Card> playedCardsIter = player.getPlayedCards().getAsList().iterator();
			while(playedCardsIter.hasNext()) {
				Card card;
				if ((card = playedCardsIter.next()) instanceof Alchemist) {
					player.getDiscardPile().add(card);
					playedCardsIter.remove();
				}
			}	
		}
		
	}
	
}
