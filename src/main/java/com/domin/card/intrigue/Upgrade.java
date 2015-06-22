package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Upgrade extends Card {

	private static final long serialVersionUID = 1L;

	public Upgrade() {
		super("Upgrade", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	public void play(Player player, DominClient connection) {
		player.draw();
		
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for(Card card : trashedCards) {
			player.addExactGain(card.getCost().getCoins() + 1, card.getCost().getPotions());
			player.trash(card);
		}
	}

}
