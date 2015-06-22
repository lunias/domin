package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Remodel extends Card {

	private static final long serialVersionUID = 1L;

	public Remodel() {
		super("Remodel", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a card from your hand", CardLocation.HAND, 1, false);
		for (Card card : trashedCards) {
			player.trash(card);
			player.addGain(card.getCost().getCoins() + 2, card.getCost().getPotions());
		}
	}
}
