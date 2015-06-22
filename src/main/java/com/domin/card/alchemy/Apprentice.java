package com.domin.card.alchemy;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Apprentice extends Card {

	private static final long serialVersionUID = 1L;

	public Apprentice() {
		super("Apprentice", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		List<Card> trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card card : trashList) {
			player.draw(card.getCost().getCoins());
			
			if (card.getCost().getPotions() > 0) {
				player.draw(2);
			}
			
			player.trash(card);					
		}		
		
	}
	
}
