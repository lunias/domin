package com.domin.card.prosperity;

import java.util.List;

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
import com.domin.ui.util.StageManager;

public class Rabble extends Card {

	private static final long serialVersionUID = 1L;

	public Rabble() {
		super("Rabble", CardSet.PROSPERITY, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(3);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {
			
			private static final long serialVersionUID = Rabble.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {				
				
				List<Card> poppedList = Player.INSTANCE.popDeck(3);
		
				for(int i = poppedList.size() - 1; i >= 0; i--) {
					Card card = poppedList.get(i);
					if (card.isOfType(CardType.ACTION) || card.isOfType(CardType.TREASURE)) {
						Player.INSTANCE.getDiscardPile().add(card);
						connection.send(new CardEventPacket(card, CardEventType.DISCARD));
						poppedList.remove(i);						
					} else {
						connection.send(new CardEventPacket(card, CardEventType.REVEAL));
					}
				}
				
				List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(Player.INSTANCE, "Choice", "Put Cards on top of your deck", poppedList, -1, false, Rabble.this.getImage());
				for (Card card : toTop) {
					Player.INSTANCE.addToTopOfDeck(card);
				}
				
				return null;
			}
		}, this.getImageName()));			
		
	}

}
