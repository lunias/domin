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

public class Warehouse extends Card {

	private static final long serialVersionUID = 1L;

	public Warehouse() {
		super("Warehouse", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw(3);
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard 3 Cards", CardLocation.HAND, 3, false);
		
		for (Card card : discardList) {
			player.discard(card);
		}
		
	}

}
