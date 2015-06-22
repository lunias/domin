package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class MiningVillage extends Card {

	private static final long serialVersionUID = 1L;

	public MiningVillage() {
		super("Mining Village", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}

	public void play (Player player, DominClient connection) {
		
		player.draw();
		player.addActions(1);
		
		List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Trash this Card?", new String[] {"Yes", "No"}, 1, true);
		
		for (Integer choice : choiceList) {
			if (choice == 0) {
				player.addCoins(2);
				player.trashImmediately(this);
			}
		}
		
	}
}
