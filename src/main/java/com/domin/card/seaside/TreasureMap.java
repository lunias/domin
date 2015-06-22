package com.domin.card.seaside;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Gold;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class TreasureMap extends Card {

	private static final long serialVersionUID = 1L;

	public TreasureMap() {
		super("Treasure Map", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(4));
	}

	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		player.trashImmediately(this);
		
		List<Card> treasureMaps = new ArrayList<Card>();
		for (Card card : player.getHand().getAsList()) {
			if (card instanceof TreasureMap) {
				treasureMaps.add(card);	
			}
		}
		
		List<Card> trashedCards = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash another Treasure Map", treasureMaps, 1, false);
		
		for (Card card : trashedCards) {
			player.trash(card);
			
			card = new Gold();				
			StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
			int goldLeft = Integer.parseInt(cardCount.get());
			for(int i = 0; i < 4 && goldLeft > 0; i++) {
				card = new Gold();				
				player.getDeck().addToTop(card);		
				connection.send(new CardEventPacket(card, CardEventType.GAIN));
				connection.send(new SupplyModPacket(card, -1));
				goldLeft--;
			}			
			return;
		}
		
	}
	
}
