package com.domin.card.alchemy;

import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Duchy;
import com.domin.card.base.Gold;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class Transmute extends Card {

	private static final long serialVersionUID = 1L;

	public Transmute() {
		super("Transmute", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(0, 1));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);		
		
		List<Card> trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card trashed : trashList) {
			
			if (trashed.isOfType(CardType.ACTION)) {
				Card card = new Duchy();
				StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
				if (Integer.parseInt(cardCount.get()) > 0) {
					player.getDiscardPile().add(card);		
					connection.send(new CardEventPacket(card, CardEventType.GAIN));
					connection.send(new SupplyModPacket(card, -1));
				}
			}
			if (trashed.isOfType(CardType.TREASURE)) {
				Card card = new Transmute();
				StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
				if (Integer.parseInt(cardCount.get()) > 0) {
					player.getDiscardPile().add(card);		
					connection.send(new CardEventPacket(card, CardEventType.GAIN));
					connection.send(new SupplyModPacket(card, -1));
				}				
			}			
			if (trashed.isOfType(CardType.VICTORY)) {
				Card card = new Gold();
				StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
				if (Integer.parseInt(cardCount.get()) > 0) {
					player.getDiscardPile().add(card);		
					connection.send(new CardEventPacket(card, CardEventType.GAIN));
					connection.send(new SupplyModPacket(card, -1));
				}				
			}
			player.trash(trashed);
		}
		
	}

}
