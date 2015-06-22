package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;

public class Moat extends Card {

	private static final long serialVersionUID = 1L;

	public Moat() {
		super("Moat", CardSet.BASE, new CardType[] {CardType.ACTION, CardType.REACTION}, new CardCost(2));
	}
	
	public int givesCards() {
		return 2;
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(2);
		
	}
	
	public void reactionEffect(Player player, DominClient connection) {
		
		connection.send(new CardEventPacket(this, CardEventType.REVEAL));
		player.setBlockedAttack(true);
		
	}
	
}
