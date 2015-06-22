package com.domin.card.seaside;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class PearlDiver extends Card {

	private static final long serialVersionUID = 1L;

	public PearlDiver() {
		super("Pearl Diver", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(2));
	}
	
	@Override
	public int givesCards() {
		return 1;
	}
	
	@Override
	public int givesActions() {
		return 1;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.draw();				
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Click the card to put it on top", player.peekAtDeck(1, false), 1, true);
		
		for (Card card : toTop) {
			player.getDeck().removeLast();
			player.addToTopOfDeck(card);
		}
	}

}
