package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class Nobles extends Card {

	private static final long serialVersionUID = 1L;

	public Nobles() {
		super("Nobles", CardSet.INTRIGUE, new CardType[] {CardType.ACTION, CardType.VICTORY}, new CardCost(6));
	}
	
	@Override
	public int givesVictory() {
		return 2;
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose one", new String[] {"+3 Cards", "+2 Actions"}, 1, false);
		
		if (choiceList.get(0) == 0) {
			player.draw(3);			
		} else {
			player.addActions(2);
		}
		
	}

}
