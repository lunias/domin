package com.domin.card.intrigue;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Estate;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class Baron extends Card {

	private static final long serialVersionUID = 1L;

	public Baron() {
		super("Baron", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addBuys(1);
		
		List<Card> estateList = new ArrayList<Card>();		
		for (Card card : player.getHand().getAsList()) {
			if ("Estate".equals(card.getName())) {
				estateList.add(card);
			}
		}
		
		List<Card> discardCards = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard an Estate?", estateList, 1, true);
		
		for (Card card : discardCards) {
			player.discard(card);
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			player.addCoins(4);
			return;
		}		
		
		Card card = new Estate();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDiscardPile().add(card);		
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
	}
	
}
