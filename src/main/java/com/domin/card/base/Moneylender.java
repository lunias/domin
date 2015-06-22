package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.ChoiceView;
import com.domin.ui.util.StageManager;

public class Moneylender extends Card {

	private static final long serialVersionUID = 1L;

	public Moneylender() {
		super("Moneylender", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public int givesCoins() {
		return 3;
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		
		List<Integer> selectedList = null;
		Card toTrash = null;
		
		for (Card card : player.getHand().getAsList()) {
			if (card.getName() == "Copper") {
				String[] choices = {"Trash a Copper"};				
				selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Trash a Copper from your hand?", choices, 1, true);
				toTrash = card;
				break;
			}
		}
		
		if (toTrash != null && selectedList != null && !selectedList.isEmpty()) {
			player.addCoins(3);
			player.trash(toTrash);
		}
		
	}
	
}
