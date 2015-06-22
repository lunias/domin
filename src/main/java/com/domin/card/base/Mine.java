package com.domin.card.base;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Mine extends Card {

	private static final long serialVersionUID = 1L;

	public Mine() {
		super("Mine", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		
		List<Card> treasureList = new ArrayList<Card>();
		
		for (Card card : player.getHand().getAsList()) {
			if (card.isOfType(CardType.TREASURE)) {
				treasureList.add(card);
			}
		}
		
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Treasure card from your hand", treasureList, 1, false);
		for (Card card : trashedCards) {
			player.trash(card);
			player.setGainType(CardType.TREASURE);
			player.setGainLoc(CardLocation.HAND);
			player.addGain(card.getCost().getCoins() + 3, card.getCost().getPotions());
		}
	}
}

