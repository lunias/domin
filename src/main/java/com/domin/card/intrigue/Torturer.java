package com.domin.card.intrigue;

import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
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
import com.domin.ui.util.StageManager;

public class Torturer extends Card {

	private static final long serialVersionUID = 1L;

	public Torturer() {
		super("Torturer", CardSet.INTRIGUE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(3);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = Torturer.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {
				
				List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(Player.INSTANCE, "Choice", "Choose one", new String[] {"Discard 2 Cards", "Gain a Curse"}, 1, false, Torturer.this.getImage());
								
				if (choiceList.get(0) == 0) {
					List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(Player.INSTANCE, "Discard", "Discard 2 Cards", CardLocation.HAND, 2, false, Torturer.this.getImage());
					for(Card card : discardList) {
						Player.INSTANCE.discard(card);
						connection.send(new CardEventPacket(card, CardEventType.ATTACK_DISCARD));
					}
				} else {					
					Card card = new Curse();					
					StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
					if (Integer.parseInt(cardCount.get()) > 0) {
						connection.send(new SupplyModPacket(card, -1));					
						Player.INSTANCE.getDiscardPile().add(card);
					}											
				}
				
				return null;
			}
			
			
		}, this.getImageName()));
		
	}

}
