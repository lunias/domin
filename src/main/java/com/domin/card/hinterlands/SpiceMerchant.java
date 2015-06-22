package com.domin.card.hinterlands;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class SpiceMerchant extends Card {

	private static final long serialVersionUID = 1L;

	public SpiceMerchant() {
		super("Spice Merchant", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> treasureList = player.getHand().getCardsOfType(CardType.TREASURE);
		
		List<Card> trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", treasureList, 1, true);
		
		for (Card card : trashList) {
			player.trash(card);
			
			List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose one", new String[] {"+2 Cards; +1 Action", "+2 Coins; +1 Buy"}, 1, false);
			
			if (choiceList.get(0) == 0) {
				player.draw(2);
				player.addActions(1);
			} else {
				player.addCoins(2);
				player.addBuys(1);
			}
		}
		
	}
	
}
