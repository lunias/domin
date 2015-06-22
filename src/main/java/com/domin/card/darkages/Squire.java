package com.domin.card.darkages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardCost;
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

public class Squire extends Card {

	private static final long serialVersionUID = 1L;

	public Squire() {
		super("Squire", CardSet.DARK_AGES, new CardType[] {CardType.ACTION}, new CardCost(2));
	}
	
	public void play(Player player, DominClient connection) {
		
		player.removeActions(1);
		player.addCoins(1);
		
		String[] choices = {"+2 Actions", "+2 Buys", "Gain a Silver"};
		
		List<Integer> selectedList = StageManager.INSTANCE.createChoiceView(player, "Choice", "Choose an option:", choices, 1, false);
		
		for (Integer choice : selectedList) {
			switch(choice) {
			case 0:
				player.addActions(2);
				break;
			case 1:
				player.addBuys(2);
				break;
			case 2:
				Card card = new Silver();
				StringProperty cardCount = DominGameScreen.supplyCardMap.get(card);
				if (Integer.parseInt(cardCount.get()) > 0) {
					player.getDiscardPile().add(card);
					connection.send(new CardEventPacket(card, CardEventType.GAIN));
					connection.send(new SupplyModPacket(card, -1));
				}
				break;
			default:
				System.out.println("Invalid choice for Squire.");
			}
		}		
		
	}
	
	public void trashEffect(Player player, DominClient connection) {
		
		List<Card> attackCards = new ArrayList<Card>();
		
		for (Entry<Card, StringProperty> entry : DominGameScreen.supplyCardMap.entrySet()) {
			
			if (entry.getKey().isOfType(CardType.ATTACK) && !"0".equals(entry.getValue().get())) {
				attackCards.add(entry.getKey());
			}
			
		}
		
		List<Card> toGain = StageManager.INSTANCE.createCardChoiceView(player, "Gain", "Gain an Attack Card", attackCards, 1, false, this.getImage());
		
		for (Card card : toGain) {
			connection.send(new CardEventPacket(card, CardEventType.GAIN));
			connection.send(new SupplyModPacket(card, -1));					
			Player.INSTANCE.getDiscardPile().add(card.clone());
		}
		
	}

}
