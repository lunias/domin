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

public class Island extends Card {

	private static final long serialVersionUID = 1L;

	public Island() {
		super("Island", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.VICTORY}, new CardCost(4));
	}

	@Override
	public int givesVictory() {
		return 2;
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> setAsideList = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Set aside a Card", CardLocation.HAND, 1, false);
		
		for (Card card : setAsideList) {
			player.getHand().remove(card);
			player.addIslandCard(card);
			return;
		}
		
	}
}
