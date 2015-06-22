package com.domin.card.darkages;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class Rats extends Card {

	private static final long serialVersionUID = 1L;

	public Rats() {
		super("Rats", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		Card card = new Rats();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getDiscardPile().add(card);		
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));
		}
		
		List<Card> trashList = new ArrayList<Card>();
		for (Card trashCandidate : player.getHand().getAsList()) {
			if (!(trashCandidate instanceof Rats)) {
				trashList.add(trashCandidate);
			}
		}
		
		trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", trashList, 1, false);
		for (Card trashCard : trashList) {
			player.trash(trashCard);
		}
		
	}
	
	public void trashEffect(Player player, DominClient connection) {
		
		player.draw();
		
	}
	
}
