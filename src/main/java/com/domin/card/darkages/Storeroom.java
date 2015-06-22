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

public class Storeroom extends Card {

	private static final long serialVersionUID = 1L;

	public Storeroom() {
		super("Storeroom", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);		
		player.addBuys(1);
		
		List<Card> toDiscard = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard Cards?", CardLocation.HAND, -1, false);
		
		for (Card card : toDiscard) {
			player.discard(card);
			player.draw();
		}
		
		toDiscard = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard Cards?", CardLocation.HAND, -1, false);
		
		for (Card card : toDiscard) {
			player.discard(card);
			player.addCoins(1);
		}		
			
	}
}
