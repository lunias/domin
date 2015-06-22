package com.domin.ui.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.domin.card.Card;
import com.domin.card.CardSet;
import com.domin.card.CardType;
import com.domin.card.alchemy.Alchemist;
import com.domin.card.alchemy.Apothecary;
import com.domin.card.alchemy.Apprentice;
import com.domin.card.alchemy.Familiar;
import com.domin.card.alchemy.Herbalist;
import com.domin.card.alchemy.PhilosophersStone;
import com.domin.card.alchemy.Potion;
import com.domin.card.alchemy.Transmute;
import com.domin.card.alchemy.University;
import com.domin.card.alchemy.Vineyard;
import com.domin.card.base.Adventurer;
import com.domin.card.base.Bureaucrat;
import com.domin.card.base.Cellar;
import com.domin.card.base.Chancellor;
import com.domin.card.base.Chapel;
import com.domin.card.base.Copper;
import com.domin.card.base.CouncilRoom;
import com.domin.card.base.Curse;
import com.domin.card.base.Duchy;
import com.domin.card.base.Estate;
import com.domin.card.base.Feast;
import com.domin.card.base.Festival;
import com.domin.card.base.Gardens;
import com.domin.card.base.Gold;
import com.domin.card.base.Laboratory;
import com.domin.card.base.Library;
import com.domin.card.base.Market;
import com.domin.card.base.Militia;
import com.domin.card.base.Mine;
import com.domin.card.base.Moat;
import com.domin.card.base.Moneylender;
import com.domin.card.base.Province;
import com.domin.card.base.Remodel;
import com.domin.card.base.Silver;
import com.domin.card.base.Smithy;
import com.domin.card.base.Village;
import com.domin.card.base.Witch;
import com.domin.card.base.Woodcutter;
import com.domin.card.base.Workshop;
import com.domin.card.cornucopia.Fairgrounds;
import com.domin.card.cornucopia.FarmingVillage;
import com.domin.card.cornucopia.Hamlet;
import com.domin.card.cornucopia.Harvest;
import com.domin.card.cornucopia.HuntingParty;
import com.domin.card.cornucopia.Menagerie;
import com.domin.card.cornucopia.Remake;
import com.domin.card.darkages.Beggar;
import com.domin.card.darkages.Feodum;
import com.domin.card.darkages.Forager;
import com.domin.card.darkages.Fortress;
import com.domin.card.darkages.Hovel;
import com.domin.card.darkages.Ironmonger;
import com.domin.card.darkages.JunkDealer;
import com.domin.card.darkages.PoorHouse;
import com.domin.card.darkages.Rats;
import com.domin.card.darkages.Sage;
import com.domin.card.darkages.Scavenger;
import com.domin.card.darkages.Squire;
import com.domin.card.darkages.Storeroom;
import com.domin.card.darkages.Vagrant;
import com.domin.card.hinterlands.BorderVillage;
import com.domin.card.hinterlands.Cache;
import com.domin.card.hinterlands.Cartographer;
import com.domin.card.hinterlands.Crossroads;
import com.domin.card.hinterlands.Embassy;
import com.domin.card.hinterlands.Highway;
import com.domin.card.hinterlands.JackOfAllTrades;
import com.domin.card.hinterlands.Mandarin;
import com.domin.card.hinterlands.NomadCamp;
import com.domin.card.hinterlands.Oasis;
import com.domin.card.hinterlands.SilkRoad;
import com.domin.card.hinterlands.SpiceMerchant;
import com.domin.card.hinterlands.Stables;
import com.domin.card.intrigue.Baron;
import com.domin.card.intrigue.Bridge;
import com.domin.card.intrigue.Conspirator;
import com.domin.card.intrigue.Coppersmith;
import com.domin.card.intrigue.Courtyard;
import com.domin.card.intrigue.Duke;
import com.domin.card.intrigue.GreatHall;
import com.domin.card.intrigue.Harem;
import com.domin.card.intrigue.Ironworks;
import com.domin.card.intrigue.MiningVillage;
import com.domin.card.intrigue.Minion;
import com.domin.card.intrigue.Nobles;
import com.domin.card.intrigue.Pawn;
import com.domin.card.intrigue.Scout;
import com.domin.card.intrigue.SecretChamber;
import com.domin.card.intrigue.ShantyTown;
import com.domin.card.intrigue.Steward;
import com.domin.card.intrigue.Torturer;
import com.domin.card.intrigue.TradingPost;
import com.domin.card.intrigue.Upgrade;
import com.domin.card.intrigue.WishingWell;
import com.domin.card.prosperity.Bank;
import com.domin.card.prosperity.City;
import com.domin.card.prosperity.Colony;
import com.domin.card.prosperity.CountingHouse;
import com.domin.card.prosperity.Expand;
import com.domin.card.prosperity.Forge;
import com.domin.card.prosperity.Loan;
import com.domin.card.prosperity.Mountebank;
import com.domin.card.prosperity.Platinum;
import com.domin.card.prosperity.Rabble;
import com.domin.card.prosperity.Venture;
import com.domin.card.prosperity.WorkersVillage;
import com.domin.card.seaside.Bazaar;
import com.domin.card.seaside.Caravan;
import com.domin.card.seaside.Cutpurse;
import com.domin.card.seaside.Explorer;
import com.domin.card.seaside.FishingVillage;
import com.domin.card.seaside.GhostShip;
import com.domin.card.seaside.Haven;
import com.domin.card.seaside.Island;
import com.domin.card.seaside.Lighthouse;
import com.domin.card.seaside.Lookout;
import com.domin.card.seaside.MerchantShip;
import com.domin.card.seaside.PearlDiver;
import com.domin.card.seaside.Salvager;
import com.domin.card.seaside.SeaHag;
import com.domin.card.seaside.Tactician;
import com.domin.card.seaside.TreasureMap;
import com.domin.card.seaside.Treasury;
import com.domin.card.seaside.Warehouse;
import com.domin.card.seaside.Wharf;
import com.domin.player.Player;

public enum CardManager {

	INSTANCE;
	
	private Map<String, Card> cardMap;
	
	private boolean usePotions = false;
	private boolean useRuins = false;
	private boolean useColonies = false;
	private boolean useShelters = false;
	
	private CardManager() {
		
		this.cardMap = new HashMap<String, Card>();
		
		// TREASURE
		cardMap.put("Copper", new Copper());
		cardMap.put("Silver", new Silver());
		cardMap.put("Gold", new Gold());
		cardMap.put("Platinum", new Platinum());
		
		// VICTORY
		cardMap.put("Estate", new Estate());
		cardMap.put("Duchy", new Duchy());
		cardMap.put("Province", new Province());
		cardMap.put("Colony", new Colony());
		cardMap.put("Curse", new Curse());
		
		// BASE
		cardMap.put("Cellar", new Cellar());
		cardMap.put("Chapel", new Chapel());
		cardMap.put("Moat", new Moat());
		cardMap.put("Chancellor", new Chancellor());
		cardMap.put("Village", new Village());
		cardMap.put("Woodcutter", new Woodcutter());
		cardMap.put("Workshop", new Workshop());
		cardMap.put("Bureaucrat", new Bureaucrat());
		cardMap.put("Feast", new Feast());
		cardMap.put("Gardens", new Gardens());
		cardMap.put("Militia", new Militia());
		cardMap.put("Moneylender", new Moneylender());
		cardMap.put("Remodel", new Remodel());
		cardMap.put("Smithy", new Smithy());
		            // SPY
            		// THIEF
		//cardMap.put("Throne Room", new ThroneRoom()); // TODO
		cardMap.put("Council Room", new CouncilRoom());
		cardMap.put("Festival", new Festival());
		cardMap.put("Laboratory", new Laboratory());
		cardMap.put("Library", new Library());		
		cardMap.put("Market", new Market());
		cardMap.put("Mine", new Mine());
		cardMap.put("Witch", new Witch());
		cardMap.put("Adventurer", new Adventurer());
		
		// INTRIGUE
		cardMap.put("Courtyard", new Courtyard());
		cardMap.put("Pawn", new Pawn());
		cardMap.put("Secret Chamber", new SecretChamber());
		cardMap.put("Great Hall", new GreatHall());
					// MASQUERADE
		cardMap.put("Shanty Town", new ShantyTown());
		cardMap.put("Steward", new Steward());
					// SWINDLER
		cardMap.put("Wishing Well", new WishingWell());
		cardMap.put("Baron", new Baron());
		cardMap.put("Bridge", new Bridge());
		cardMap.put("Conspirator", new Conspirator());
		cardMap.put("Coppersmith", new Coppersmith());
		cardMap.put("Ironworks", new Ironworks());
		cardMap.put("Mining Village", new MiningVillage());
		cardMap.put("Scout", new Scout());
		cardMap.put("Duke", new Duke());
		cardMap.put("Minion", new Minion());
					// SABOTEUR
		cardMap.put("Torturer", new Torturer());
		cardMap.put("Trading Post", new TradingPost());		
					// TRIBUTE
		cardMap.put("Upgrade", new Upgrade());		
		cardMap.put("Harem", new Harem());		
		cardMap.put("Nobles", new Nobles());
		
		// SEASIDE
		            // EMBARGO
		cardMap.put("Haven", new Haven());
		cardMap.put("Lighthouse", new Lighthouse());
					// NATIVE VILLAGE
		cardMap.put("Pearl Diver", new PearlDiver());
		//cardMap.put("Ambassador", new Ambassador()); // TODO
		cardMap.put("Fishing Village", new FishingVillage());
		cardMap.put("Lookout", new Lookout());
					// SMUGGLERS
		cardMap.put("Warehouse", new Warehouse());
		cardMap.put("Caravan", new Caravan());
		cardMap.put("Cutpurse", new Cutpurse());
		cardMap.put("Island", new Island());
		//cardMap.put("Navigator", new Navigator()); // TODO
					// PIRATE SHIP
		cardMap.put("Salvager", new Salvager());
		cardMap.put("Sea Hag", new SeaHag());
		cardMap.put("Treasure Map", new TreasureMap());
		cardMap.put("Bazaar", new Bazaar());
		cardMap.put("Explorer", new Explorer());
		cardMap.put("Ghost Ship", new GhostShip());
		cardMap.put("Merchant Ship", new MerchantShip());
					// OUTPOST
		cardMap.put("Tactician", new Tactician());
		cardMap.put("Treasury", new Treasury());
		cardMap.put("Wharf", new Wharf());
				
		// ALCHEMY
		cardMap.put("Trasmute", new Transmute());
		cardMap.put("Vineyard", new Vineyard());
		cardMap.put("Apothecary", new Apothecary());
		cardMap.put("Herbalist", new Herbalist());
		cardMap.put("University", new University());
		cardMap.put("Alchemist", new Alchemist());
		cardMap.put("Familiar", new Familiar());
		cardMap.put("Philosopher's Stone", new PhilosophersStone());
		cardMap.put("Apprentice", new Apprentice());
		cardMap.put("Potion", new Potion());
		
		// PROSPERITY
		cardMap.put("Loan", new Loan());
		cardMap.put("Worker's Village", new WorkersVillage());
		cardMap.put("City", new City());
		cardMap.put("Counting House", new CountingHouse());
		cardMap.put("Mountebank", new Mountebank());
		cardMap.put("Rabble", new Rabble());
		cardMap.put("Venture", new Venture());
		cardMap.put("Bank", new Bank());
		cardMap.put("Expand", new Expand());
		cardMap.put("Forge", new Forge());
		
		// CORNUCOPIA
		cardMap.put("Hamlet", new Hamlet());
		cardMap.put("Menagerie", new Menagerie());
		cardMap.put("Farming Village", new FarmingVillage());
		// cardMap.put("Remake", new Remake()); // TODO
		cardMap.put("Harvest", new Harvest());
		cardMap.put("Hunting Party", new HuntingParty());
		cardMap.put("Fairgrounds", new Fairgrounds());
		
		// Hinterlands
		cardMap.put("Crossroads", new Crossroads());
		cardMap.put("Oasis", new Oasis());
		cardMap.put("Jack Of All Trades", new JackOfAllTrades());
		cardMap.put("Nomad Camp", new NomadCamp());
		cardMap.put("Silk Road", new SilkRoad());
		cardMap.put("Spice Merchant", new SpiceMerchant());
		cardMap.put("Cache", new Cache());
		cardMap.put("Cartographer", new Cartographer());
		// cardMap.put("Embassy", new Embassy()); // TODO
		cardMap.put("Highway", new Highway());
		cardMap.put("Mandarin", new Mandarin());
		cardMap.put("Stables", new Stables());
		cardMap.put("Border Village", new BorderVillage());
		
		// Dark Ages
		cardMap.put("Hovel", new Hovel());
		// cardMap.put("Necropolis", new Necropolis());
		// cardMap.put("Overgrown Estate", new OvergrownEstate());
		cardMap.put("Poor House", new PoorHouse());
		cardMap.put("Beggar", new Beggar());
		cardMap.put("Squire", new Squire());
		cardMap.put("Vagrant", new Vagrant());
		cardMap.put("Forager", new Forager());
		cardMap.put("Sage", new Sage());
		cardMap.put("Storeroom", new Storeroom());
		cardMap.put("Feodum", new Feodum());
		cardMap.put("Fortress", new Fortress());
		cardMap.put("Ironmonger", new Ironmonger());
		cardMap.put("Rats", new Rats());
		cardMap.put("Scavenger", new Scavenger());
		cardMap.put("Junk Dealer", new JunkDealer());
		
	}
	
	public Map<String, Card> getCardMap() {
		return cardMap;
	}
	
	public List<Card> getCardsInSet(CardSet set) {
		List<Card> cardList = new ArrayList<Card>();
		
		for (Card card : cardMap.values()) {
			if (card.getSet() == set) {
				cardList.add(card);
			}
		}
		
		Collections.sort(cardList);
		
		return cardList;
	}
	
	public List<Card> getBasicSupplyPiles() {
		List<Card> supplyList = new ArrayList<Card>();
		
		supplyList.add(cardMap.get("Copper"));
		supplyList.add(cardMap.get("Silver"));
		supplyList.add(cardMap.get("Gold"));		
		
		if (useColonies) {
			supplyList.add(cardMap.get("Platinum"));
		}
		
		if (usePotions) {
			supplyList.add(cardMap.get("Potion"));	
		}
		
		supplyList.add(cardMap.get("Estate"));
		supplyList.add(cardMap.get("Duchy"));
		supplyList.add(cardMap.get("Province"));
		
		if (useColonies) {
			supplyList.add(cardMap.get("Colony"));
		}
		
		supplyList.add(cardMap.get("Curse"));		
		
		return supplyList;
	}
	
	public List<Card> allRandomSupplyPiles(int numPiles) {
		
		List<Card> cardList = new ArrayList<Card>(cardMap.values());				
		Collections.shuffle(cardList);
		
		List<Card> supplyList = new ArrayList<Card>(numPiles);
		
		for (int i = 0; supplyList.size() < numPiles; i++) {
			if (!cardList.get(i).isOfType(CardType.BASIC)) {
				supplyList.add(cardList.get(i));				
			}
		}		
		
		return supplyList;
	}
	
	public List<Card> rollSupplyPiles(int numPiles) {
		
		// clean up persistent state
		Player.INSTANCE.disableAllStatusEffects();
		usePotions = false;
		useRuins = false;
		useColonies = false;
		useShelters = false;
		
		List<Card> supplyList = generateSupplyPiles(numPiles);
		
		if (supplyList == null || supplyList.size() != numPiles) {
			System.out.println("Generation of supply failed! Creating random set.");
			supplyList = allRandomSupplyPiles(numPiles);
		}
		
		if (supplyList.get(0).getSet() == CardSet.PROSPERITY) {
			useColonies = true;
		}
		
		if (supplyList.get(supplyList.size() - 1).getSet() == CardSet.DARK_AGES) {
			useShelters = true;
		}
		
		for (Card card : supplyList) {
			if (card.getCost().getPotions() > 0) {
				usePotions = true;				
			}			
			
			if (card.isOfType(CardType.LOOTER)) {
				useRuins = true;
			}
		}		
		
		Collections.sort(supplyList);		
		
		return supplyList;		
	}
	
	private List<Card> generateSupplyPiles(int numPiles) {
		
		Properties randomizerProperties = Settings.readRandomizerSettings();
		if (randomizerProperties == null) {
			return null;
		}
		
		List<Card> cardList = new ArrayList<Card>(cardMap.values());
		Collections.shuffle(cardList);					
		
		// Set Filtering
		List<CardSet> setList = new ArrayList<CardSet>(CardSet.values().length);
		for (CardSet cardSet : CardSet.values()) {
			if (Boolean.valueOf(randomizerProperties.getProperty(cardSet.toString().toLowerCase(), "true"))) {
				setList.add(cardSet);
			}
		}		
		cardList = applySetFilter(cardList, setList);
		
		//Cost Limit Filtering
		Map<Integer, int[]> costMap = new HashMap<Integer, int[]>();
		int[] minMax;
		for (int i = 0; i < 11; i++) {
			minMax = new int[2];
			minMax[0] = 0;
			minMax[1] = 9;
			
			try {
				minMax[0] = Integer.parseInt(randomizerProperties.getProperty("min_" + i + "_cost", "0"));				
			} catch (NumberFormatException nfe) { }
			
			try {
				minMax[1] = Integer.parseInt(randomizerProperties.getProperty("max_" + i + "_cost", "9"));
			} catch (NumberFormatException nfe) { }			
			
			costMap.put(i, minMax);
		}
		cardList = applyCostLimitFilter(cardList, costMap, numPiles);
		
				
		return cardList;
	}
	
	private List<Card> applySetFilter(List<Card> cardList, List<CardSet> setList) {
		
		List<Card> filteredList = new ArrayList<Card>(cardList.size());
		for (Card card : cardList) {
			if (setList.contains(card.getSet()) && !card.isOfType(CardType.BASIC)) {
				filteredList.add(card);
			}
		}
		return filteredList;
	}
	
	private List<Card> applyCostLimitFilter(List<Card> cardList, Map<Integer, int[]> costMap, int numPiles) {
		
		List<Card> filteredList = new ArrayList<Card>(numPiles);
		
		int[] mins = new int[costMap.size()];
		int[] deltas = new int[costMap.size()];
		for (Entry<Integer, int[]> entry : costMap.entrySet()) {
			mins[entry.getKey()] = entry.getValue()[0];
			deltas[entry.getKey()] = entry.getValue()[1] - entry.getValue()[0];
		}		
		
		int cardCost;
		int i = numPiles;
		fillmins: for (Card card : cardList) {
			cardCost = card.getCost().getCoins();
			if (mins[cardCost] > 0) {
				filteredList.add(card);
				mins[cardCost]--;
				if (--i == 0) {
					return filteredList;
				}
			} else {
				for (Integer left : mins) {
					if (left > 0) {
						continue fillmins;
					}
				}
				break fillmins;
			}
		}
		
		cardList.removeAll(filteredList);					
		
		for (Card card : cardList) {
			cardCost = card.getCost().getCoins();			
			if (deltas[cardCost] > 0) {
				filteredList.add(card);
				deltas[cardCost]--;
				if (--i == 0) {
					return filteredList;
				}
			}
		}
		
		return filteredList;
	}	
	
	public int getCardCount() {
		return cardMap.size();
	}
	
}
