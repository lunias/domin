package com.domin.card.hinterlands;

import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Silver;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class JackOfAllTrades extends Card {

	private static final long serialVersionUID = 1L;

	public JackOfAllTrades() {
		super("Jack Of All Trades", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		Card card = new Silver();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDiscardPile().add(card);
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
		Card topCard = player.popDeck();
		if (topCard != null) {
			List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Discard or put on top?", new String[] {"Discard", "Put on top"}, 1, false, topCard.getImage());
			if (choiceList.get(0) == 0) {
				player.getDiscardPile().add(topCard);
				connection.send(new CardEventPacket(topCard, CardEventType.DISCARD));
			} else {
				player.getDeck().addToTop(topCard);
			}
		}
		
		player.draw(5 - player.getHand().size());
		
		List<Card> trashOptions = player.getHand().getCardsNotOfType(CardType.TREASURE);
		
		List<Card> trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", trashOptions, 1, true);
		
		for (Card toTrash : trashList) {
			player.trash(toTrash);
		}
		
	}
	
}
