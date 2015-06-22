package com.domin.card.base;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CallablePacket;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;

public class CouncilRoom extends Card {

	private static final long serialVersionUID = 1L;

	public CouncilRoom() {
		super("Council Room", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.addBuys(1);
		
		connection.send(new CallablePacket(new DominCallable() {

			private static final long serialVersionUID = CouncilRoom.serialVersionUID;

			@Override
			public Void call() throws Exception {
				Player.INSTANCE.draw();
				return null;
			}
			
		}));
		
		player.draw(4);
	}
	
}
