package com.domin.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.domin.card.Card;
import com.domin.card.CardLocation;
import com.domin.card.CardType;
import com.domin.card.seaside.Island;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.net.common.ChatPacket;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.SoundManager;

public enum Player {

	INSTANCE;	
	
	private int id;
	private String username;
	private boolean readyState;
	private boolean waiting;
	
	private int turnNumber;
	private int latestVictoryCount;
	private Map<Integer, Integer> victoryVelocityMap;
	
	private int actions;
	private int buys;
	private int coins;
	private int potions;
	
	private boolean hasBought;
	private boolean unaffected;	
	private boolean blockedAttack;
	
	private StringProperty actionProperty;
	private StringProperty buyProperty;
	private StringProperty coinProperty;
	private StringProperty potionProperty;
	
	private IntegerProperty actionsToSpendProperty;
	private IntegerProperty buysToSpendProperty;
	private IntegerProperty coinsToSpendProperty;
	private IntegerProperty potionsToSpendProperty;
	
	private IntegerProperty gainToSpendProperty;
	
	private IntegerProperty reducedCostProperty;
	
	private int gainCoins;
	private int gainPotions;
	private int exactGain;
	private CardType gainType;
	private boolean gaining;
	private CardLocation gainLoc;
	
	private Hand hand;
	private Deck deck;
	private DiscardPile discard;
	private PlayedCards playedCards;
	private DurationCards durationCards;
	
	private Card lastPlayedCard;
	
	private List<Card> setAsideCards;
	private List<Card> islandCards;
	private List<Card> boughtCards;
	
	private Map<String, Integer> statusEffectMap;
	
	private DominClient connection;
	
	private Player() {
		this.statusEffectMap = new HashMap<String, Integer>();
	}
	
	public void initPlayer(DominClient connection) {
		this.id = connection.getID();
		this.username = connection.getUsername();
		this.connection = connection;
		
		this.readyState = false;
		this.waiting = false;
		
		this.turnNumber = 1;
		this.latestVictoryCount = 3;
		this.victoryVelocityMap = new LinkedHashMap<Integer, Integer>();
		this.victoryVelocityMap.put(1, 3);
		
		this.actions = 0;
		this.buys = 0;
		this.coins = 0;
		this.potions = 0;
		this.gainCoins = 0;
		this.gainPotions = 0;
		this.exactGain = 0;
		
		this.hasBought = false;
		this.unaffected = false;
		this.gaining = false;
		
		this.gainType = null;
		this.gainLoc = CardLocation.DISCARD;
		
		this.actionProperty = new SimpleStringProperty();
		this.buyProperty = new SimpleStringProperty();
		this.coinProperty = new SimpleStringProperty();
		this.potionProperty = new SimpleStringProperty();
		
		this.actionsToSpendProperty = new SimpleIntegerProperty();
		this.buysToSpendProperty = new SimpleIntegerProperty();
		this.coinsToSpendProperty = new SimpleIntegerProperty();
		this.potionsToSpendProperty = new SimpleIntegerProperty();
		
		this.gainToSpendProperty = new SimpleIntegerProperty();
		
		this.reducedCostProperty = new SimpleIntegerProperty();
		
		this.actionProperty.set("Actions: 0");
		this.buyProperty.set("Buys: 0");
		this.coinProperty.set("Coins: 0");
		this.potionProperty.set("Potions: 0");
		
		this.actionsToSpendProperty.set(0);
		this.buysToSpendProperty.set(0);
		this.coinsToSpendProperty.set(0);
		this.potionsToSpendProperty.set(0);
		
		this.gainToSpendProperty.set(0);
		
		this.reducedCostProperty.set(0);
		
		this.hand = new Hand();
		this.deck = new Deck();
		this.discard = new DiscardPile();
		this.playedCards = new PlayedCards();
		this.durationCards = new DurationCards();
		
		this.lastPlayedCard = null;
		
		this.setAsideCards = new ArrayList<Card>();
		this.islandCards = new ArrayList<Card>();
		this.boughtCards = new ArrayList<Card>();
		
		this.statusEffectMap.clear();
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getActions() {
		return actions;
	}
	
	public int getBuys() {
		return buys;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getPotions() {
		return potions;
	}
	
	public int getGainCoins() {
		return gainCoins;
	}
	
	public int getGainPotions() {
		return gainPotions;
	}
	
	public int getExactGain() {
		return exactGain;
	}
	
	public CardType getGainType() {
		return gainType;
	}
	
	public Hand getHand() {
		return hand;
	}
	
	public Deck getDeck() {
		return deck;
	}
	
	public DiscardPile getDiscardPile() {
		return discard;
	}
	
	public PlayedCards getPlayedCards() {
		return playedCards;
	}
	
	public Card getLastPlayedCard() {
		return lastPlayedCard;
	}
	
	public void setLastPlayedCard(Card card) {
		lastPlayedCard = card;
	}
	
	public DurationCards getDurationCards() {
		return durationCards;
	}
	
	public List<Card> getSetAsideCards() {
		return setAsideCards;
	}
	
	public List<Card> getBoughtCards() {
		return boughtCards;
	}
	
	public void addIslandCard(Card card) {
		islandCards.add(card);
	}
	
	public Map<String, Integer> getStatusEffectMap() {
		return statusEffectMap;
	}
	
	public void enableStatusEffect(String cardName) {
		statusEffectMap.put(cardName, 1);
	}
	
	public void disableStatusEffect(String cardName) {
		statusEffectMap.put(cardName, 0);
	}
	
	public void incrementStatusEffectValue(String cardName) {
		statusEffectMap.put(cardName, getStatusEffectValue(cardName) + 1);
	}
	
	public int getStatusEffectValue(String cardName) {
		Integer value = statusEffectMap.get(cardName);
		return value == null ? 0 : value;
	}
	
	public void disableAllStatusEffects() {
		statusEffectMap.clear();
	}
	
	public void incrementReducedCostProperty() {
		reducedCostProperty.set(reducedCostProperty.get() + 1);
	}
	
	public Card play(int index) {
		Card card = hand.getAsList().get(index);		
		return play(card);
	}
	
	public Card play(Card card) {
		return play(card, false);
	}
	
	public Card play(Card card, boolean isCopy) {
		if (!isCopy) {
			hand.remove(card);
			
			// TODO copies will not activate duration effect
			if (card.isOfType(CardType.DURATION)) {
				durationCards.add(card);
			} else if (card instanceof Island) {
				addIslandCard(card);
			} else {
				playedCards.add(card);	
			}			
		}
		
		lastPlayedCard = card;
		
		card.play(this, connection);		

		return card;
	}
	
	public void trash(Card card) {
		hand.remove(card);
		connection.send(new CardEventPacket(card, CardEventType.TRASH));
		card.trashEffect(this, connection);
	}
	
	public void trashImmediately(Card card) {
		playedCards.remove(card);
		connection.send(new CardEventPacket(card, CardEventType.TRASH));		
	}
	
	public List<Card> playAllTreasures() {
		
		List<Card> playedList = new ArrayList<Card>();
		
		if (hasBought || gaining) {
			return playedList;
		}
		
		Iterator<Card> handIterator = hand.getAsList().iterator();
		while(handIterator.hasNext()) {
			Card card = handIterator.next();
			if (!card.isOfType(CardType.ACTION) && card.isOfType(CardType.TREASURE)) {
				playedList.add(card);
				handIterator.remove();
				playedCards.add(card);
				card.play(this, connection);				
			}
		}		
		
		removeActions(actions);
		
		return playedList;
	}
	
	public void displayDurationCards() {
		for (Card card : durationCards.removeAll()) {
			card.durationEffect(this, connection);
			discard.add(card);
			connection.send(new CardEventPacket(card, CardEventType.DURATION));		
		}
		
		for (Card card : setAsideCards) {
			hand.add(card);
		}
		
		setAsideCards.clear();		
		
		reducedCostProperty.set(0);
		disableAllStatusEffects();
	}	
	
	public Card peekAtDeck(boolean top) {
		Card peeked = top ? deck.peek() : deck.peekLast();		
		if (peeked != null) {
			return peeked;
		} else if (shuffleDiscardAddToDeck()) {
			return peekAtDeck(top);
		}
		return null;
	}
	
	public List<Card> peekAtDeck(int numToPeek, boolean top) {
		List<Card> peekList = new ArrayList<Card>();
		for (int i = 0; i < numToPeek; i++) {
			Card peeked = peekAtDeck(top);
			if (peeked == null) {
				break;
			}
			peekList.add(peeked);
		}
		return peekList;
	}
	
	public Card popDeck() {
		Card popped = deck.removeFirst();
		if (popped != null) {
			return popped;
		} else if (shuffleDiscardAddToDeck()) {
			return popDeck();
		}
		return null;
	}
	
	public List<Card> popDeck(int numToPop) {
		List<Card> popList = new ArrayList<Card>();		
		Card card;
		while((card = peekAtDeck(true)) != null) {
			popList.add(card);
			deck.removeFirst();
			if (popList.size() == numToPop) {
				return popList;
			}
		}
		/* TODO is this needed?
		if (shuffleDiscardAddToDeck()) {
			popDeck(popList.size() - numToPop);
		}*/
		
		return popList;
	}
	
	public List<Card> popUntil(CardType cardType, int numToFind) {
		List<Card> popList = new ArrayList<Card>();
		int found = 0;
		Card card;
		while((card = peekAtDeck(true)) != null) {			
			popList.add(card);
			deck.removeFirst();
			if (card.isOfType(cardType)) {
				found++;
			}
			if (found == numToFind) {
				return popList;
			}
		}
		if (shuffleDiscardAddToDeck()) {
			popUntil(cardType, numToFind - found);
		}
		return popList;
	}
	
	public List<Card> popUntil(List<CardType> cardTypeList) {
		List<Card> popList = new ArrayList<Card>();		
		Card card;
		while((card = peekAtDeck(true)) != null) {			
			popList.add(card);
			deck.removeFirst();
			for (CardType type : cardTypeList) {
				if (card.isOfType(type)) {
					return popList;
				}
			}
		}
		if (shuffleDiscardAddToDeck()) {
			popUntil(cardTypeList);
		}
		return popList;		
	}
	
	public List<Card> popUntilMoreThan(int cardCost) {
		List<Card> popList = new ArrayList<Card>();		
		Card card;
		while((card = peekAtDeck(true)) != null) {			
			popList.add(card);
			deck.removeFirst();
			if (card.getCost().getCoins() > cardCost) {
				return popList;
			}
		}
		if (shuffleDiscardAddToDeck()) {
			popUntilMoreThan(cardCost);
		}
		return popList;		
	}	
	
	public boolean draw() {
		Card drawn = deck.draw();
		if (drawn != null) {
			hand.add(drawn);
			return true;
		} else if (shuffleDiscardAddToDeck()) {
			return draw();
		}
		return false;
	}
	
	public void draw(int numCards) {
		for (int i = 0; i < numCards; i++) {
			if (!draw()) {
				break;
			}
		}
	}
	
	public void gain(Card card) {
		gaining = false;
		gainType = null;
		removeExactGain(card.getCost().getCoins());
		removeGainPotions(card.getCost().getPotions());
		removeGainCoins(card.getCost().getCoins());
		connection.send(new CardEventPacket(card, CardEventType.GAIN));
		connection.send(new ChatPacket(username + " gained a " + card + ".", "Server"));
		
		card.gainEffect(this, connection);
		switch(gainLoc) {
		case DISCARD: 
			discard.add(card.clone());
			break;
		case HAND:
			hand.add(card.clone());
			break;
		case TOP_OF_DECK:
			deck.addToTop(card.clone());
		default:
			System.out.println("Unrecognized CardLocation");
		}		
		
		connection.send(new SupplyModPacket(card, -1));
		
		if (getStatusEffectValue("Ironworks") == 1) {
			
			if (card.isOfType(CardType.ACTION)) {
				addActions(1);				
			}
			if (card.isOfType(CardType.TREASURE)) {
				addCoins(1);				
			}
			if (card.isOfType(CardType.VICTORY)) {
				draw();
			}
			
			disableStatusEffect("Ironworks");
		}
		
		gainLoc = CardLocation.DISCARD;
		
		connection.resetOutput();
	}
	
	public void buy(Card card) {
		SoundManager.COIN.play();				
		boughtCards.add(card);
		hasBought = true;
		removeCoins(card.getCost().getCoins());
		removePotions(card.getCost().getPotions());
		removeBuys(1);
		connection.send(new CardEventPacket(card, CardEventType.BUY));
		connection.send(new ChatPacket(username + " bought a " + card + ".", "Server"));
		
		card.gainEffect(this, connection);	
		switch(gainLoc) {
		case DISCARD: 
			discard.add(card.clone());
			break;
		case HAND:
			hand.add(card.clone());
			break;
		case TOP_OF_DECK:
			deck.addToTop(card.clone());
		default:
			System.out.println("Unrecognized CardLocation");
		}	
		
		connection.send(new SupplyModPacket(card, -1));
		
		gainLoc = CardLocation.DISCARD;		
		
		connection.resetOutput();
	}
	
	public void cleanup() {
		discard.addAll(playedCards.removeAll());
		discardAll();				
		
		updateVictoryVelocityMap();
		
		removeActions(actions);
		removeBuys(buys);
		removeCoins(coins);
		removePotions(potions);		
				
		hasBought = false;
		gaining = false;
		gainType = null;
		gainLoc = CardLocation.DISCARD;
		gainCoins = 0;
		gainPotions = 0;
		exactGain = 0;
		
		boughtCards.clear();
		
		draw(5);
	}
	
	public boolean shuffleDiscardAddToDeck() {
		SoundManager.SHUFFLE.play();
		deck.addAll(discard.getShuffledList());
		discard.clear();
		return deck.size() > 0;
	}
	
	public boolean shuffleDiscardIntoDeck() {
		discard.addAll(deck.removeAll());
		return shuffleDiscardAddToDeck();
	}
	
	public void discard(int index) {
		discard.add(hand.remove(index));
	}
	
	public void discard(Card card) {
		hand.remove(card);
		discard.add(card);
	}
	
	public Card discardTopCard() {
		Card card = deck.removeFirst();
		if (card != null) {
			discard.add(card);	
		}
		return card;
	}
	
	public void discardAll() {
		discard.addAll(hand.removeAll());
	}
	
	public void discardDeck() {
		discard.addAll(deck.removeAll());
	}
	
	public void addToTopOfDeck(Card card) {
		deck.addToTop(card);
	}
	
	public boolean canAfford(Card card) {
		if (exactGain > 0) {
			return canAfford(card, exactGain);
		}
		
		int coinsToSpend = gaining ? gainCoins : coins;
		int potionsToSpend = gaining ? gainPotions : potions;
		
		int coinsCost = card.getCost().getCoins();
		int potionsCost = card.getCost().getPotions();		
		
		boolean hasEnoughResources = coinsToSpend >= coinsCost && potionsToSpend >= potionsCost; 
		boolean isCorrectType = gaining && gainType != null ? card.isOfType(gainType) : true; 
		
		return hasEnoughResources && isCorrectType;
	}
	
	public boolean canAfford(Card card, int maxMin) {
		int coinsCost = card.getCost().getCoins();
		int potionsCost = card.getCost().getPotions();
		
		return coinsCost == maxMin && potionsCost <= gainPotions;
	}
	
	public boolean canPlay(Card card) {
		return !gaining && !hasBought && (actions > 0 || !card.isOfType(CardType.ACTION));
	}
	
	public boolean canReact() {
		for (Card card : hand.getAsList()) {
			if (card.isOfType(CardType.REACTION)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Card> getCardsOfTypeInHand(CardType type) {
		return hand.getCardsOfType(type);
	}
	
	public Map<Integer, Integer> getVictoryVelocityMap() {
		return victoryVelocityMap;
	}
	
	public void updateVictoryVelocityMap() {		
		int victoryTotal = 0;
		
		victoryTotal += getVictoryInList(deck.getAsList());
		victoryTotal += getVictoryInList(discard.getAsList());
		victoryTotal += getVictoryInList(islandCards);
		victoryTotal += getVictoryInList(setAsideCards);		
		
		if (latestVictoryCount != victoryTotal) {
			victoryVelocityMap.put(turnNumber, victoryTotal);
			latestVictoryCount = victoryTotal;
		}
		
		turnNumber++;
	}	
	
	private int getVictoryInList(List<Card> cardList) {
		int victoryTotal = 0;
		for (Card card : cardList) {
			victoryTotal += card.givesVictory();
		}
		return victoryTotal;
	}
	
	public Map<Card, Integer> generateVictoryMap() {
		Map<Card, Integer> victoryMap = new HashMap<Card, Integer>();
		
		deck.addAll(discard.getAsList());
		deck.addAll(hand.getAsList());
		deck.addAll(islandCards);
		deck.addAll(setAsideCards);
		
		discard.clear();
		hand.clear();
		
		for (Card card : deck.getAsList()) {
			if (card.isOfType(CardType.VICTORY) || "Curse".equals(card.getName())) {
				int count  = victoryMap.containsKey(card) ? victoryMap.get(card) : 0;
				victoryMap.put(card, count + 1);
			}
		}
		
		return victoryMap;
	}
	
	public int addActions(int numActions) {
		actions += numActions;
		actionProperty.set("Actions: " + actions);
		actionsToSpendProperty.set(actions);
		return actions;
	}
	
	public int addBuys(int numBuys) {
		buys += numBuys;
		buyProperty.set("Buys: " + buys);
		buysToSpendProperty.set(buys);
		return buys;
	}
	
	public int addCoins(int numCoins) {
		coins += numCoins;
		coinProperty.set("Coins: " + coins);
		coinsToSpendProperty.set(coins);
		return coins;
	}
	
	public int addPotions(int numPotions) {
		potions += numPotions;
		potionProperty.set("Potions: " + potions);
		potionsToSpendProperty.set(potions);
		return potions;
	}
	
	public boolean isGainPossible() {
		for (Entry<Card, StringProperty> entry : DominGameScreen.supplyCardMap.entrySet()) {
			int count = Integer.parseInt(entry.getValue().get());
			if (count != 0 && canAfford(entry.getKey())) {
				return true;
			}
		}
		
		disableStatusEffect("Ironworks");
		
		return false;
	}	
	
	public int addGain(int numGain, int numGainPotions) {
		gainCoins += numGain;
		gainPotions += numGainPotions;
		gaining = true;
		if (isGainPossible()) {
			gainToSpendProperty.set(gainCoins);	
		} else {
			gainCoins = 0;
			gainPotions = 0;
			gaining = false;
		}		
		return gainCoins;
	}
	
	public int addExactGain(int numGain, int numGainPotions) {		
		exactGain += numGain;
		gainPotions += numGainPotions;
		gaining = true;
		if (isGainPossible()) {
			gainToSpendProperty.set(exactGain);	
		} else {
			exactGain = 0;
			gainPotions = 0;
			gaining = false;
		}		
		return exactGain;
	}
	
	public void setGainType(CardType gainType) {
		this.gainType = gainType;
	}
	
	public void setGainLoc(CardLocation gainLoc) {
		this.gainLoc = gainLoc;
	}
	
	public int removeActions(int numActions) {
		actions -= numActions;
		actionProperty.set("Actions: " + actions);
		actionsToSpendProperty.set(actions);
		return actions;
	}
	
	public int removeBuys(int numBuys) {		
		buys -= numBuys;
		buyProperty.set("Buys: " + buys);
		buysToSpendProperty.set(buys);
		return buys;
	}
	
	public int removeCoins(int numCoins) {
		coins -= numCoins;
		coinProperty.set("Coins: " + coins);
		coinsToSpendProperty.set(coins);
		return coins;
	}
	
	public int removePotions(int numPotions) {
		potions -= numPotions;
		potionProperty.set("Potions: " + potions);
		potionsToSpendProperty.set(potions);
		return potions;
	}
	
	public int removeGainCoins(int numGain) {
		gainCoins -= numGain;
		gainToSpendProperty.set(gainCoins);
		return gainCoins;
	}
	
	public int removeGainPotions(int numGainPotions) {
		gainPotions -= numGainPotions;
		return gainPotions;
	}
	
	public int removeExactGain(int numGain) {
		exactGain -= numGain;
		return exactGain;
	}
	
	public boolean setUnaffected(boolean unaffectedState) {
		return unaffected = unaffectedState;
	}
	
	public boolean setBlockedAttack(boolean blockState) {
		return blockedAttack = blockState;
	}
	
	public boolean isReady() {
		return readyState;
	}
	
	public boolean isGaining() {
		return gaining;
	}
	
	public boolean isUnaffected() {
		return unaffected;
	}
	
	public boolean blockedAttack() {
		return blockedAttack;
	}
	
	public boolean isWaiting() {
		return waiting;
	}
	
	public void ready(boolean readyState) {
		this.readyState = readyState;
	}
	
	public void waiting(boolean waitingState) {
		this.waiting = waitingState;
	}
	
	public StringProperty getActionProperty() {
		return actionProperty;
	}
	
	public StringProperty getBuyProperty() {
		return buyProperty;	
	}
	
	public StringProperty getCoinProperty() {
		return coinProperty;
	}
	
	public StringProperty getPotionProperty() {
		return potionProperty;
	}
	
	public IntegerProperty getActionsToSpendProperty() {
		return actionsToSpendProperty;
	}
	
	public IntegerProperty getBuysToSpendProperty() {
		return buysToSpendProperty;
	}
	
	public IntegerProperty getCoinsToSpendProperty() {
		return coinsToSpendProperty;
	}
	
	public IntegerProperty getPotionsToSpendProperty() {
		return potionsToSpendProperty;
	}
	
	public IntegerProperty getGainToSpendProperty() {
		return gainToSpendProperty;
	}
	
	public IntegerProperty getReducedCostProperty() {
		return reducedCostProperty;
	}	
	
	@Override
	public String toString() {
		return username + " ID: " + id + " Ready? " + readyState;
	}
	
}
