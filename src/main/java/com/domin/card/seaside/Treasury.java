package com.domin.card.seaside;

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

public class Treasury extends Card {

	private static final long serialVersionUID = 1L;

	public Treasury() {
		super("Treasury", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	
	public void play (Player player, DominClient connection) {
		
		player.draw();
		player.addCoins(1);
		
	}
	
	public void endOfTurnEffect(Player player, DominClient connection) {
		
		boolean boughtVictory = false;
		for (Card card : player.getBoughtCards()) {
			if (card.isOfType(CardType.VICTORY)) {
				boughtVictory = true;
				break;
			}
		}
		
		if (!boughtVictory) {			
			List<Card> treasurysPlayed = new ArrayList<Card>();
			for (Card card : player.getPlayedCards().getAsList()) {
				if (card instanceof Treasury) {
					treasurysPlayed.add(card);
				}
			}
			
			List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Treasury(s) on top?", treasurysPlayed, treasurysPlayed.size(), true, this.getImage());
			for (Card card : toTop) {
				player.getPlayedCards().remove(card);
				player.addToTopOfDeck(card);
			}
			
			Iterator<Card> playedCardsIter = player.getPlayedCards().getAsList().iterator();
			while(playedCardsIter.hasNext()) {
				Card card;
				if ((card = playedCardsIter.next()) instanceof Treasury) {
					player.getDiscardPile().add(card);
					playedCardsIter.remove();
				}
			}			
		}
		
	}
}
