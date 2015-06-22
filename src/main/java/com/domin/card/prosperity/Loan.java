package com.domin.card.prosperity;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Loan extends Card {

	private static final long serialVersionUID = 1L;

	public Loan() {
		super("Loan", CardSet.PROSPERITY, new CardType[] {CardType.TREASURE}, new CardCost(3));
	}

	public void play(Player player, DominClient connection) {
		
		player.addCoins(1);
		
		List<Card> revealedCards = new ArrayList<Card>();
		
		Card card;
		while((card = player.popDeck()) != null) {
			if (card.isOfType(CardType.TREASURE)) {
				break;
			}
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			revealedCards.add(card);
		}

		player.getDiscardPile().addAll(revealedCards);
		
		if (card != null) {
			int choice = StageManager.INSTANCE.createChoiceView(player, "Choice", "Discard or Trash?", new String[] {"Discard", "Trash"}, 1, false, card.getImage()).get(0);
			
			if (choice == 0) {
				player.getDiscardPile().add(card);
				connection.send(new CardEventPacket(card, CardEventType.DISCARD));
			} else {
				connection.send(new CardEventPacket(card, CardEventType.TRASH));		
			}
		}
		
	}
	
}
