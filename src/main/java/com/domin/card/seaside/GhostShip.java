package com.domin.card.seaside;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.AttackCallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;
import com.domin.ui.util.StageManager;

public class GhostShip extends Card {

	private static final long serialVersionUID = 1L;

	public GhostShip() {
		super("Ghost Ship", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(2);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = GhostShip.serialVersionUID;
			
			public Void callWithConnection(DominClient connection) {
				int handSize = Player.INSTANCE.getHand().size();
				if (handSize < 4) {
					return null;
				}
				
				List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(Player.INSTANCE, "Choice", "Put cards on top of deck", CardLocation.HAND, handSize - 3, false, GhostShip.this.getImage());
				
				for (Card card : toTop) {					
					Player.INSTANCE.getHand().remove(card);
					Player.INSTANCE.addToTopOfDeck(card);
				}
				
				return null;
			}
			
		}, true, this.getImageName()));
		
	}
	
}
