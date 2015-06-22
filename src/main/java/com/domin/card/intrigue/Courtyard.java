package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.CardChoiceView;
import com.domin.ui.util.StageManager;

public class Courtyard extends Card {

	private static final long serialVersionUID = 1L;

	public Courtyard() {
		super("Courtyard", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(2));
	}
	
	@Override
	public int givesCards() {
		return 3;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.draw(3);
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put a card from your hand on top of your deck", CardLocation.HAND, 1, false);
		
		for (Card card : toTop) {
			player.getHand().remove(card);
			player.addToTopOfDeck(card);
		}		
	}

}
