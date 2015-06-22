package com.domin.card.intrigue;

import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Silver;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class TradingPost extends Card {

	private static final long serialVersionUID = 1L;

	public TradingPost() {
		super("Trading Post", CardSet.INTRIGUE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}
	
	@Override
	public boolean mentionsTrash() {
		return true;
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		
		List<Card> trashList = StageManager.INSTANCE.createCardChoiceView(player, "Trash", "Trash 2 Cards?", CardLocation.HAND, 2, true);
		
		for (Card card : trashList) {
			player.trash(card);
		}
		
		if (trashList.size() == 2) {
			Card card = new Silver();
			StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
			if (Integer.parseInt(cardCount.get()) > 0) {
				player.getHand().add(card);		
				connection.send(new CardEventPacket(card, CardEventType.GAIN));
				connection.send(new SupplyModPacket(card, -1));
			}
		}
		
	}

}
