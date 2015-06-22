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

public class JunkDealer extends Card {

	private static final long serialVersionUID = 1L;

	public JunkDealer() {
		super("Junk Dealer", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw(1);
		player.addCoins(1);
		
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card card : trashedCards) {
			player.trash(card);
		}
		
	}
	
}
