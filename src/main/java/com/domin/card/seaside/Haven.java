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

public class Haven extends Card {

	private static final long serialVersionUID = 1L;

	public Haven() {
		super("Haven", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(2));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		List<Card> choiceList = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Set aside a Card", CardLocation.HAND, 1, false);
		
		for (Card card : choiceList) {
			player.getHand().remove(card);
			player.getSetAsideCards().add(card);
		}
		
	}

}
