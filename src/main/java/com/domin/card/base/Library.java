package com.domin.card.base;

import java.util.ArrayList;
import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Library extends Card {

	private static final long serialVersionUID = 1L;

	public Library() {
		super("Library", CardSet.BASE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> setAsideCards = new ArrayList<Card>();
		
		while(player.getHand().size() < 7) {
			Card toDraw = player.popDeck();
			if (toDraw == null) {
				break;
			}
			if (toDraw.isOfType(CardType.ACTION)) {
				List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Set Card aside?", new String[] {"Yes", "No"}, 1, false, toDraw.getImage());
				if (choiceList.get(0) == 0) {
					setAsideCards.add(toDraw);
				} else {
					player.getHand().add(toDraw);
				}
			} else {
				player.getHand().add(toDraw);
			}
		}
		
		player.getDiscardPile().addAll(setAsideCards);
		
	}
	
}
