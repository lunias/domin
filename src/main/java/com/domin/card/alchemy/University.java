package com.domin.card.alchemy;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class University extends Card {

	private static final long serialVersionUID = 1L;

	public University() {
		super("University", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(2, 1));
	}

	public void play(Player player, DominClient connection) {
		
		player.addActions(1);
				
		List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Gain?", new String[] {"Yes", "No"}, 1, false);
		if (choiceList.get(0) == 0) {
			player.setGainType(CardType.ACTION);
			player.addGain(5, 0);
		}
		
	}
	
}
