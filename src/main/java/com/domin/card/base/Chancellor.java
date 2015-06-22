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

public class Chancellor extends Card {

	private static final long serialVersionUID = 1L;

	public Chancellor() {
		super("Chancellor", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	@Override
	public int givesCoins() {
		return 2;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.addCoins(2);
		
		String[] choices = {"Put deck into discard pile"};
		
		List<Integer> selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Put deck into discard pile?", choices, 1, true);
		
		if (!selectedList.isEmpty()) {
			player.discardDeck();
		}		
	}

}
