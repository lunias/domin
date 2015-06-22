package com.domin.card.alchemy;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Herbalist extends Card {

	private static final long serialVersionUID = 1L;

	public Herbalist() {
		super("Herbalist", CardSet.ALCHEMY, new CardType[] {CardType.ACTION}, new CardCost(2));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addBuys(1);
		player.addCoins(1);
		
	}

	public void endOfTurnEffect(Player player, DominClient connection) {
		
		List<Card> playedTreasures = new ArrayList<Card>();
		
		for (Card card : player.getPlayedCards().getAsList()) {
			if (card.isOfType(CardType.TREASURE)) {
				playedTreasures.add(card);
			}
		}
		
		List<Card> toTop = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Put a Treasure on top of your deck", playedTreasures, 1, true, this.getImage());
		
		for (Card card : toTop) {
			player.getPlayedCards().remove(card);
			player.addToTopOfDeck(card);
		}
		
	}
	
}
