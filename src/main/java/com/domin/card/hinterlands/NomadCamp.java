package com.domin.card.hinterlands;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class NomadCamp extends Card {

	private static final long serialVersionUID = 1L;

	public NomadCamp() {
		super("Nomad Camp", CardSet.HINTERLANDS, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);		
		player.addBuys(1);
		player.addCoins(2);
		
	}

	public void gainEffect(Player player, DominClient connection) {
		
		player.setGainLoc(CardLocation.TOP_OF_DECK);
		
	}
	
}
