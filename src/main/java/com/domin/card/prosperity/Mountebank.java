package com.domin.card.prosperity;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Copper;
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

public class Mountebank extends Card {

	private static final long serialVersionUID = 1L;

	public Mountebank() {
		super("Mountebank", CardSet.PROSPERITY, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addCoins(2);
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {
			
			private static final long serialVersionUID = Mountebank.serialVersionUID;
			
			@Override
			public Void callWithConnection(DominClient connection) {				
				
				List<Card> curseList = new ArrayList<Card>();
				for (Card card : Player.INSTANCE.getHand().getAsList()) {
					if (card instanceof Curse) {
						curseList.add(card);
					}
				}
				
				List<Card> toDiscard = StageManager.INSTANCE.createCardChoiceView(Player.INSTANCE, "Discard", "Discard a Curse?", curseList, 1, true, Mountebank.this.getImage());				
				if (toDiscard.size() > 0) {					
					Player.INSTANCE.discard(toDiscard.get(0));
					connection.send(new CardEventPacket(toDiscard.get(0), CardEventType.REVEAL));
				} else {
					Card curse = new Curse();
					Card copper = new Copper();
					
					StringProperty cardCount = DominGameScreen.supplyCardMap.get(curse);
					if (Integer.parseInt(cardCount.get()) > 0) {
						connection.send(new SupplyModPacket(curse, -1));
						connection.send(new CardEventPacket(curse, CardEventType.GAIN));
						Player.INSTANCE.getDiscardPile().add(curse);
					}
					
					cardCount = DominGameScreen.supplyCardMap.get(copper);
					if (Integer.parseInt(cardCount.get()) > 0) {
						connection.send(new SupplyModPacket(copper, -1));
						connection.send(new CardEventPacket(copper, CardEventType.GAIN));
						Player.INSTANCE.getDiscardPile().add(copper);
					}					
				}
				
				return null;
			}
		}, this.getImageName()));		
		
	}
	
}
