package com.domin.card.cornucopia;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class FarmingVillage extends Card {

	private static final long serialVersionUID = 1L;

	public FarmingVillage() {
		super("Farming Village", CardSet.CORNUCOPIA, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.addActions(1);
		
		List<CardType> cardTypeList = new ArrayList<CardType>(2);
		cardTypeList.add(CardType.ACTION);
		cardTypeList.add(CardType.TREASURE);
		
		List<Card> cardList = player.popUntil(cardTypeList);
		
		for (Card card : cardList) {
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
		}
		
		player.getHand().add(cardList.remove(cardList.size() - 1));
		player.getDiscardPile().addAll(cardList);
		
	}
	
}
