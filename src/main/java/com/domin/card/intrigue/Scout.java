package com.domin.card.intrigue;

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

public class Scout extends Card {

	private static final long serialVersionUID = 1L;

	public Scout() {
		super("Scout", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		
		List<Card> poppedList = player.popDeck(4);
		
		for(int i = poppedList.size() - 1; i >= 0; i--) {
			Card card = poppedList.get(i);
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			if (card.isOfType(CardType.VICTORY)) {
				player.getHand().add(card);
				poppedList.remove(i);
			}
		}
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Cards on top of your deck", poppedList, poppedList.size(), false);
		
		for (Card card : toTop) {
			player.addToTopOfDeck(card);
		}
		
	}
	
}
