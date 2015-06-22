package com.domin.card.prosperity;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Expand extends Card {

	private static final long serialVersionUID = 1L;

	public Expand() {
		super("Expand", CardSet.PROSPERITY, new CardType[] {CardType.ACTION}, new CardCost(7));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> toTrash = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card card : toTrash) {
			player.trash(card);
			player.addGain(card.getCost().getCoins() + 3, card.getCost().getPotions());
		}
		
	}

}
