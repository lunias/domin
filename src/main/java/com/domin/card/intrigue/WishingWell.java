package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.CardManager;
import com.domin.ui.util.StageManager;

public class WishingWell extends Card {

	private static final long serialVersionUID = 1L;

	public WishingWell() {
		super("Wishing Well", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();		
		
		List<Card> allCardsList = CardManager.INSTANCE.getBasicSupplyPiles();
		for (Card card : connection.getSupplyCardList()) {
			allCardsList.add(card);
		}
		
		List<Card> namedCardList = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Name a Card", allCardsList, 1, false);
		
		Card peeked = player.peekAtDeck(true);
		
		connection.send(new CardEventPacket(peeked, CardEventType.REVEAL));
		
		for (Card card : namedCardList) {
			if (peeked.equals(card)) {
				player.draw();
			}
		}
		
	}
	
}
