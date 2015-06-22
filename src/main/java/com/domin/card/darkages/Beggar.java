package com.domin.card.darkages;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Copper;
import com.domin.card.base.Silver;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;

public class Beggar extends Card {

	private static final long serialVersionUID = 1L;

	public Beggar() {
		super("Beggar", CardSet.DARK_AGES, new CardType[] {CardType.ACTION, CardType.REACTION}, new CardCost(2));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		Card card = new Copper();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		int toGain = Integer.parseInt(cardCount.get()) < 3 ? Integer.parseInt(cardCount.get()) : 3;  
			
		for(int i = 0; i < toGain; i++) {
			player.getHand().add(card);	
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}		
		
	}
	
	public void reactionEffect(Player player, DominClient connection) {
		
		connection.send(new CardEventPacket(this, CardEventType.DISCARD));
		player.discard(this);
		
		Card card = new Silver();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDeck().addToTop(card);		
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
		cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDiscardPile().add(card);
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
		
	}

}
