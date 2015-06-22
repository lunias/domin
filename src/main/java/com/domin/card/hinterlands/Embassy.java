package com.domin.card.hinterlands;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;
import com.domin.ui.util.StageManager;

public class Embassy extends Card {

	private static final long serialVersionUID = 1L;

	public Embassy() {
		super("Embassy", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(5);
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard 3 Cards", CardLocation.HAND, 3, false);
		
		for (Card card : discardList) {
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			player.discard(card);
		}				
		
	}
	
	public void gainEffect(Player player, DominClient connection) {
		connection.send(new CallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = Embassy.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) throws Exception {
				
				
				
				return null;
			}
			
		}));
	}
	
}
