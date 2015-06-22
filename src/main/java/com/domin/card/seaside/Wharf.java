package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Wharf extends Card {

	private static final long serialVersionUID = 1L;

	public Wharf() {
		super("Wharf", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.draw(2);
		player.addBuys(1);
		
	}	
		
	@Override
	public void durationEffect(Player player, DominClient connection) {		
		
		player.draw(2);
		player.addBuys(1);
		
	}		
	
}
