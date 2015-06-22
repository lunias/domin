package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.AttackCallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;

public class Cutpurse extends Card {

	private static final long serialVersionUID = 1L;

	public Cutpurse() {
		super("Cutpurse", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {

		player.removeActions(1);
		player.addCoins(2);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = Cutpurse.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {
				
				for(Card card : Player.INSTANCE.getHand().getAsList()) {
					if ("Copper".equals(card.getName())) {
						connection.send(new CardEventPacket(card, CardEventType.ATTACK_DISCARD));
						Player.INSTANCE.discard(card);
						break;
					}
				}				
				
				return null;
			}
			
		}, true, this.getImageName()));
		
	}
	
}
