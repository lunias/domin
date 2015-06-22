package com.domin.card.darkages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Forager extends Card {

	private static final long serialVersionUID = 1L;

	public Forager() {
		super("Forager", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(3));
	}

	public void play(Player player, DominClient connection) {
		
		player.addBuys(1);
		
		List<Card> toTrash = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash a Card", CardLocation.HAND, 1, false);
		
		for (Card card : toTrash) {			
			player.trash(card);						
		}
		
		Set<Card> treasuresInTrash = new HashSet<Card>();
		for (Card trashedCard : StageManager.INSTANCE.getTrashList()) {
			if (trashedCard.isOfType(CardType.TREASURE)) {
				treasuresInTrash.add(trashedCard);
			}
		}
		
		player.addCoins(treasuresInTrash.size());		
		
	}
	
}
