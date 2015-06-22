package com.domin.card.hinterlands;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Stables extends Card {

	private static final long serialVersionUID = 1L;

	public Stables() {
		super("Stables", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);				
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard a Treasure?", player.getHand().getCardsOfType(CardType.TREASURE), 1, true);
		
		if (discardList.size() > 0) {			
			player.draw(3);
			player.addActions(1);			
		}
		
	}
	
}
