package com.domin.card.seaside;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.player.Player;

public class Lighthouse extends Card {

	private static final long serialVersionUID = 1L;

	public Lighthouse() {
		super("Lighthouse", CardSet.SEASIDE, new CardType[] {CardType.ACTION, CardType.DURATION}, new CardCost(2));
	}
	
	@Override
	public int givesActions() {
		return 1;
	}
	
	@Override
	public int givesCoins() {
		return 1;
	}

	@Override
	public void play(Player player, DominClient connection) {
		player.addCoins(1);
		player.setUnaffected(true);
	}
	
	@Override
	public void durationEffect(Player player, DominClient connection) {
		player.addCoins(1);
		player.setUnaffected(false);
	}
	
}
