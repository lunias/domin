package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Ambassador extends Card {

	private static final long serialVersionUID = 1L;

	public Ambassador() {
		super("Ambassador", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(3));
	}
	
	public void play(Player player, DominClient connection) {
		
		StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Return Card(s) to Supply", CardLocation.HAND, -1, false);
		
	}

}
