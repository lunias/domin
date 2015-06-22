package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.CardChoiceView;
import com.domin.ui.util.StageManager;

public class Chapel extends Card {

	private static final long serialVersionUID = 1L;

	public Chapel() {
		super("Chapel", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(2));
	}
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash up to four cards", CardLocation.HAND, 4, true);
		for (Card card : trashedCards) {
			player.trash(card);
		}
	}

}
