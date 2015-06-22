package com.domin.card.base;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class ThroneRoom extends Card {
	
	private static final long serialVersionUID = 1L;

	public ThroneRoom() {
		super("Throne Room", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(4));		
	}
	
	@Override
	public void play(final Player player, final DominClient connection) {
		player.removeActions(1);
		
		List<Card> actionCardsInHand = new ArrayList<Card>();
		for (Card card : player.getHand().getAsList()) {
			if (card.isOfType(CardType.ACTION)) {
				actionCardsInHand.add(card);
			}
		}
		
		List<Card> chosenCards = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Choose an Action card", actionCardsInHand, 1, false);
		for (final Card card : chosenCards) {
			connection.send(new CardEventPacket(card, CardEventType.PLAY));
			
			if (card.isOfType(CardType.ATTACK)) {
				player.waiting(true);
			}
			
			player.addActions(1);
			player.play(card);					
			
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					while(Player.INSTANCE.isWaiting()) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) { }
					}
					connection.send(new CardEventPacket(card, CardEventType.PLAY_COPY));												
					player.addActions(1);
					player.play(card, true);					
				}
				
			});			
		}
	}

}
