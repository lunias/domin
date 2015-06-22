package com.domin.card.seaside;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Curse;
import com.domin.net.AttackCallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.DominCallable;

public class SeaHag extends Card {

	private static final long serialVersionUID = 1L;

	public SeaHag() {
		super("Sea Hag", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {
			
			private static final long serialVersionUID = SeaHag.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {				

				Card card = Player.INSTANCE.discardTopCard();
				if (card != null) {
					connection.send(new CardEventPacket(card, CardEventType.DISCARD));					
				}
				
				card = new Curse();
				
				StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
				if (Integer.parseInt(cardCount.get()) > 0) {
					connection.send(new SupplyModPacket(card, -1));					
					Player.INSTANCE.getDiscardPile().add(card);
				}				
				
				return null;
			}
		}, this.getImageName()));		
		
	}
	
}
