package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Steward extends Card {

	private static final long serialVersionUID = 1L;

	public Steward() {
		super("Steward", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(3));
	}
	
	@Override
	public int givesCards() {
		return 2;
	}
	
	@Override
	public int givesCoins() {
		return 2;
	}	
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		String[] choices = {"+2 Cards", "+2 Coins", "Trash 2 Cards from your hand"};
		
		List<Integer> selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose an option:", choices, 1, false);
		
		for (Integer choice : selectedList) {
			switch(choice) {
			case 0:
				player.draw(2);
				break;
			case 1:
				player.addCoins(2);
				break;
			case 2:
				List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash two cards", CardLocation.HAND, 2, false);
				for (Card card : trashedCards) {
					player.trash(card);
				}
				break;
			default:
				System.out.println("Invalid choice for Steward.");
			}
		}		
		
	}

}
