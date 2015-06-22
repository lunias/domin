package com.domin.card.base;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.AttackCallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.DominCallable;
import com.domin.ui.util.StageManager;

public class Bureaucrat extends Card {

	private static final long serialVersionUID = 1L;

	public Bureaucrat() {
		super("Bureaucrat", CardSet.BASE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(4));
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		
		Card card = new Silver();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDeck().addToTop(card);		
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
		connection.send(new AttackCallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = Bureaucrat.serialVersionUID;			
			
			@Override
			public Void callWithConnection(DominClient connection) {
				List<Card> victoryCards = new ArrayList<Card>();
				for (Card card : Player.INSTANCE.getHand().getAsList()) {
					if (card.isOfType(CardType.VICTORY)) {
						victoryCards.add(card);
					}
				}

				List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(Player.INSTANCE, "Choice", "Choose a Victory card", victoryCards, 1, false, Bureaucrat.this.getImage());
				for (Card card : toTop) {
					Player.INSTANCE.getHand().remove(card);
					Player.INSTANCE.addToTopOfDeck(card);
					connection.send(new CardEventPacket(card, CardEventType.REVEAL));
				}
				
				return null;
			}					
			
		}, true, this.getImageName()));
	}

}
