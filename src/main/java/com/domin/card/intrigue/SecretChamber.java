package com.domin.card.intrigue;

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

public class SecretChamber extends Card {

	private static final long serialVersionUID = 1L;

	public SecretChamber() {
		super("Secret Chamber", CardSet.INTRIGUE, new CardType[] {CardType.ACTION, CardType.REACTION}, new CardCost(2));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> discardList = StageManager.INSTANCE.createCardChoiceView(player, "Discard", "Discard Cards", CardLocation.HAND, -1, false);
		
		for (Card card : discardList) {
			player.addCoins(1);
			player.discard(card);
		}			
		
	}
	
	public void reactionEffect(Player player, DominClient connection) {
		
		connection.send(new CardEventPacket(this, CardEventType.REVEAL));
		
		player.draw(2);
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put Cards on top?", CardLocation.HAND, 2, true);
		
		for (Card card : toTop) {
			player.getHand().remove(card);
			player.addToTopOfDeck(card);
		}
		
	}

}
