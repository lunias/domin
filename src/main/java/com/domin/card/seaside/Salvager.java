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

public class Salvager extends Card {

	private static final long serialVersionUID = 1L;

	public Salvager() {
		super("Salvager", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addBuys(1);
		
		List<Card> toTrash = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card card : toTrash) {
			player.trash(card);
			player.addCoins(card.getCost().getCoins());
		}
				
	}

}
