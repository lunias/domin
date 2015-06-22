package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.ChoiceView;
import com.domin.ui.util.StageManager;

public class Pawn extends Card {

	private static final long serialVersionUID = 1L;

	public Pawn() {
		super("Pawn", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(2));
	}

	@Override
	public int givesCards() {
		return 1;
	}
	
	@Override
	public int givesActions() {
		return 1;
	}
	
	@Override
	public int givesBuys() {
		return 1;
	}
	
	@Override
	public int givesCoins() {
		return 1;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		
		String[] choices = {"+1 Card", "+1 Action", "+1 Buy", "+1 Coin"};
		
		List<Integer> selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose two options:", choices, 2, false);
		
		for (Integer choice : selectedList) {
			switch(choice) {
			case 0:
				player.draw();
				break;
			case 1:
				player.addActions(1);
				break;
			case 2:
				player.addBuys(1);
				break;
			case 3:
				player.addCoins(1);
				break;
			default:
				System.out.println("Invalid choice for Pawn.");
			}
		}
	}
}
