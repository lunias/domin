package com.domin.card.darkages;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Ironmonger extends Card {

	private static final long serialVersionUID = 1L;

	public Ironmonger() {
		super("Ironmonger", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();

		Card topCard = player.popDeck();
		if (topCard != null) {
			List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Discard?", new String[] {"Yes", "No"}, 1, false, topCard.getImage());
			if (choiceList.get(0) == 0) {
				player.getDiscardPile().add(topCard);
				connection.send(new CardEventPacket(topCard, CardEventType.DISCARD));
			} else {
				player.getDeck().addToTop(topCard);
			}
						
			if (topCard.isOfType(CardType.ACTION)) {
				player.addActions(1);			
			}
			if (topCard.isOfType(CardType.TREASURE)) {
				player.addCoins(1);			
			}
			if (topCard.isOfType(CardType.VICTORY)) {
				player.draw();
			}
		}		
		
	}
	
}
