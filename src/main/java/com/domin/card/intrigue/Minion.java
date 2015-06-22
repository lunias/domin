package com.domin.card.intrigue;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.AttackCallablePacket;
import com.domin.net.DominClient;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;
import com.domin.ui.util.StageManager;

public class Minion extends Card {

	private static final long serialVersionUID = 1L;

	public Minion() {
		super("Minion", CardSet.INTRIGUE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(5));
	}
	
	public void play(Player player, DominClient connection) {
		
		String[] choices = new String[2];
		choices[0] = "+2 Coins";
		choices[1] = "Discard your hand; +4 Cards;\nAttack effect";
		
		List<Integer> choiceList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose one", choices, 1, false);

		if (choiceList.get(0) == 1) {
			player.discardAll();
			player.draw(4);
			
			connection.send(new AttackCallablePacket(new DominCallable<Void>() {
				
				private static final long serialVersionUID = Minion.serialVersionUID;
				
				@Override
				public Void callWithConnection(DominClient connection) {
					
					if (Player.INSTANCE.getHand().size() > 4) {
						Player.INSTANCE.discardAll();
						Player.INSTANCE.draw(4);
					}
					
					return null;
				}
				
			}, true, this.getImageName()));
		} else {
			player.addCoins(2);
		}
		
	}

}
