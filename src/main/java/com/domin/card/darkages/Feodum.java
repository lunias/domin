package com.domin.card.darkages;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Silver;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;

public class Feodum extends Card {

	private static final long serialVersionUID = 1L;

	public Feodum() {
		super("Feodum", CardSet.DARK_AGES, new CardType[] {CardType.VICTORY}, new CardCost(4));
	}
	
	public int givesVictory() {
		int silverCount = 0;
		for(Card card : Player.INSTANCE.getDeck().getAsList()) {
			if (card instanceof Silver) {
				silverCount++;
			}
		}
		
		return silverCount / 3;
	}
	
	public void trashEffect(Player player, DominClient connection) {
		
		Card card = new Silver();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		int toGain = Integer.parseInt(cardCount.get()) < 3 ? Integer.parseInt(cardCount.get()) : 3;  
			
		for(int i = 0; i < toGain; i++) {
			player.getDiscardPile().add(card);	
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}		
		
	}

}
