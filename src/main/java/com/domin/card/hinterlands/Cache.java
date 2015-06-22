package com.domin.card.hinterlands;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Copper;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;

public class Cache extends Card {

	private static final long serialVersionUID = 1L;

	public Cache() {
		super("Cache", CardSet.HINTERLANDS, new CardType[] {CardType.TREASURE}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.addCoins(3);
		
	}
	
	public void gainEffect(Player player, DominClient connection) {
		
		Card card = new Copper();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		int toGain = Integer.parseInt(cardCount.get()) < 2 ? Integer.parseInt(cardCount.get()) : 2;  
			
		for(int i = 0; i < toGain; i++) {
			player.getDiscardPile().add(card);	
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
	}
	
}
