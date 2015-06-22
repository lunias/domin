package com.domin.card;

import java.io.Serializable;

import com.domin.player.Player;

public class CardCost implements Serializable, Comparable<CardCost> {

	private static final long serialVersionUID = 1L;
	
	private int coins;
	private int potions;
	
	public CardCost(int coins) {
		this.coins = coins;
	}
	
	public CardCost(int coins, int potions) {
		this.coins = coins;
		this.potions = potions;
	}
	
	public int getCoins() {
		int moddedCoins = coins;
		moddedCoins -= Player.INSTANCE.getStatusEffectValue("Bridge");		
		moddedCoins -= Player.INSTANCE.getStatusEffectValue("Highway");
		
		return moddedCoins > 0 ? moddedCoins : 0;
	}
	
	public int getPotions() {
		return potions;
	}

	@Override
	public int compareTo(CardCost other) {
		if (coins == other.getCoins()) {
			if (potions > other.getPotions()) {
				return 1;
			} else if (other.getPotions() > potions) {
				return -1;
			}
			return 0;
		} else if (coins > other.getCoins()) {
			return 1;
		}
		return -1;
	}
	
}
