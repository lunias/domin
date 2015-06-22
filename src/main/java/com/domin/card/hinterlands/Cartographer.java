package com.domin.card.hinterlands;

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

public class Cartographer extends Card {

	private static final long serialVersionUID = 1L;

	public Cartographer() {
		super("Cartographer", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		List<Card> popList = player.popDeck(4);
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard Cards", popList, -1, false);		
		for (Card card : discardList) {
			popList.remove(card);
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			player.getDiscardPile().add(card);
		}
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Cards on top of your deck", popList, -1, false);
		for (Card card : toTop) {
			player.addToTopOfDeck(card);
		}
		
	}

}
