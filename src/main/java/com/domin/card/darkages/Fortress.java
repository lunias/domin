package com.domin.card.darkages;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Fortress extends Card {

	private static final long serialVersionUID = 1L;

	public Fortress() {
		super("Fortress", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);				
		
	}
	
	public void trashEffect(Player player, DominClient connection) {
		
		connection.send(new CardEventPacket(this, CardEventType.UNTRASH));
		
		player.getHand().add(this);
		
	}

}
