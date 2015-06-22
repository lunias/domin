package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Cellar extends Card {

	private static final long serialVersionUID = 1L;

	public Cellar() {
		super("Cellar", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(2));
	}

	@Override
	public int givesActions() {
		return 1;
	}

	@Override
	public int givesCards() {
		return -1;
	}

	@Override
	public void play(Player player, DominClient connection) {
		List<Card> discardCards = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard X cards; Draw X cards", CardLocation.HAND, -1, false);
		for (Card card : discardCards) {
			player.discard(card);
			connection.send(new CardEventPacket(card, CardEventType.DISCARD));												
		}
		player.draw(discardCards.size());		
	}
	
}
