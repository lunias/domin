package com.domin.card.alchemy;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Copper;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Apothecary extends Card {

	private static final long serialVersionUID = 1L;

	public Apothecary() {
		super("Apothecary", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(2, 1));
	}

	public void play(Player player, DominClient connection) {
		
		player.draw();
		
		List<Card> poppedList = player.popDeck(4);
		
		for(int i = poppedList.size() - 1; i >= 0; i--) {
			Card card = poppedList.get(i);
			connection.send(new CardEventPacket(card, CardEventType.REVEAL));
			if (card instanceof Copper || card instanceof Potion) {
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
