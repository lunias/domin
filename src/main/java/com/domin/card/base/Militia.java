package com.domin.card.base;

import java.util.List;

import com.domin.card.Card;
import com.domin.card.CardCost;
import com.domin.card.CardLocation;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.net.AttackCallablePacket;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.ui.util.DominCallable;
import com.domin.ui.util.StageManager;

public class Militia extends Card {

	private static final long serialVersionUID = 1L;

	public Militia() {
		super("Militia", CardSet.BASE, new CardType[] {CardType.ACTION, CardType.ATTACK}, new CardCost(4));
	}

	@Override
	public int givesCoins() {
		return 2;
	}
	
	@Override
	public void play(Player player, DominClient connection) {
		player.removeActions(1);
		player.addCoins(2);

		connection.send(new AttackCallablePacket(new DominCallable<Void>() {

			private static final long serialVersionUID = Militia.serialVersionUID;

			@Override
			public Void callWithConnection(DominClient connection) {

				int numDiscard = Player.INSTANCE.getHand().size() - 3;

				if (numDiscard > 0) {

					List<Card> discardCards = StageManager.INSTANCE
							.createCardChoiceView(Player.INSTANCE, "Discard",
									"Discard down to three cards",
									CardLocation.HAND, numDiscard, false,
									Militia.this.getImage());

					for (Card card : discardCards) {
						Player.INSTANCE.discard(card);
						connection.send(new CardEventPacket(card,
								CardEventType.ATTACK_DISCARD));
					}
				}

				return null;
			}

		}, true, this.getImageName()));

	}
}
