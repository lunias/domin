package com.domin.card;

import java.io.Serializable;

import javafx.scene.image.Image;

import com.domin.net.DominClient;
import com.domin.player.Player;

public abstract class Card implements Serializable, Comparable<Card>, Cloneable {	
		
	private static final long serialVersionUID = 1L;
	
	private String name;
	private CardSet set;
	private CardType[] types;
	private CardCost cost;	
	
	private String imageName;
	
	public Card(String name, CardSet set, CardType[] types, CardCost cost) {
		this.name = name;
		this.set = set;		
		this.types = types;
		this.cost = cost;		
		
		this.imageName = "img/" + name.replaceAll(" ", "_") + ".jpg";	

	}
	
	public String getName() {
		return name;
	}
	
	public CardType[] getTypes() {
		return types;
	}
	
	public boolean isOfType(CardType type) {
		for (CardType ct : types) {
			if (ct == type) {
				return true;
			}
		}
		return false;
	}
	
	public CardSet getSet() {
		return set;
	}
	
	public CardCost getCost() {
		return cost;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public Image getImage() {
		try {
			return new Image(imageName);
		} catch (IllegalArgumentException iae) {
			System.out.println("Could not find card image for " + imageName);
		}
		return null;
	}
	
	public Image getImage(double x, double y) {
		try {
			return new Image(imageName, x, y, true, true);			
		} catch (IllegalArgumentException iae) {
			System.out.println("Could not find card image for " + imageName);
		}
		return null;
	}
	
	@Override
	public int compareTo(Card other) {		
		int cmp = cost.compareTo(other.getCost());
		if (cmp != 0) {
			return cmp;
		}
		return name.compareTo(other.getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	public int givesActions() {
		return 0;
	}
	
	public int givesBuys() {
		return 0;
	}
	
	public int givesCards() {
		return 0;
	}
	
	public int givesCoins() {
		return 0;
	}
	
	public int givesPotions() {
		return 0;
	}
	
	public int givesVictory() {
		return 0;
	}
	
	public boolean mentionsTrash() {
		return false;
	}
	
	public boolean mentionsCurse() {
		return false;
	}
	
	public void play(Player player, DominClient connection) {
		// Pass
	}
	
	public void durationEffect(Player player, DominClient connection) {
		// Pass
	}
	
	public void endOfTurnEffect(Player player, DominClient connection) {
		// Pass
	}
	
	public void reactionEffect(Player player, DominClient connection) {
		// Pass
	}
	
	public void gainEffect(Player player, DominClient connection) {
		// Pass
	}
	
	public void trashEffect(Player player, DominClient connection) {
		// Pass
	}

	@Override
	public Card clone() {
		try {
			return (Card) super.clone();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
