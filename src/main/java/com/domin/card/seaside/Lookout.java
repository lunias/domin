package com.domin.card.seaside;

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

public class Lookout extends Card {

	private static final long serialVersionUID = 1L;

	public Lookout() {
		super("Lookout", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	public void play(Player player, DominClient connection) {

		List<Card> popList = player.popDeck(3);
		
		List<Card> choiceList = StageManager.INSTANCE.createCardChoiceView(player, "Lookout", "Trash one; discard one; put one on top", popList, 3, false);
		
		int i = 0;
		for (Card card : choiceList) {
			switch(i) {
			case 0:
				connection.send(new CardEventPacket(card, CardEventType.TRASH));		
				break;
			case 1:
				player.getDiscardPile().add(card);
				break;
			case 2:
				player.addToTopOfDeck(card);
				break;
			}
			i++;
		}
		
	}
	
}
