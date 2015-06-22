package com.domin.card.seaside;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.base.Gold;
import com.domin.card.base.Province;
import com.domin.card.base.Silver;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.StageManager;

public class Explorer extends Card {

	private static final long serialVersionUID = 1L;

	public Explorer() {
		super("Explorer", CardSet.SEASIDE, new CardType[] {CardType.ACTION}, new CardCost(5));
	}

	public void play(Player player, DominClient connection) {
					
		player.removeActions(1);
		
		List<Card> provinceList = new ArrayList<Card>();
		for (Card card : player.getHand().getAsList()) {
			if (card instanceof Province) {
				provinceList.add(card);
			}
		}
		
		provinceList = StageManager.INSTANCE.createCardChoiceView(player, "Choice", "Reveal a Province?", provinceList, 1, true);
		
		Card toGain;
		
		if (provinceList.size() > 0) {
			toGain = new Gold();
			StringProperty cardCount = DominGameScreen.supplyCardMap.get(toGain);
			if (Integer.parseInt(cardCount.get()) > 0) {
				player.getHand().add(toGain);	
				connection.send(new CardEventPacket(toGain, CardEventType.GAIN));
				connection.send(new SupplyModPacket(toGain, -1));
			}
			return;
		}
		
		toGain = new Silver();
		StringProperty cardCount = DominGameScreen.supplyCardMap.get(toGain);
		if (Integer.parseInt(cardCount.get()) > 0) {
			player.getHand().add(toGain);
			connection.send(new CardEventPacket(toGain, CardEventType.GAIN));
			connection.send(new SupplyModPacket(toGain, -1));
		}
		
	}
	
}
