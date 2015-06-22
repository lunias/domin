package com.domin.card.alchemy;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Curse;
import com.domin.net.AttackCallablePacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.DominCallable;

public class Familiar extends Card {

	private static final long serialVersionUID = 1L;

	public Familiar() {
		super("Familiar", CardSet.ALCHEMY, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(3, 1));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {
			
			private static final long serialVersionUID = Familiar.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {				
				
				Card card = new Curse();
				
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
