package com.domin.net;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import com.domin.card.Card;

public class VictoryPacket implements Serializable, Comparable<VictoryPacket> {

	private static final long serialVersionUID = 1L;

	private Map<Card, Integer> victoryCardMap;
	private Map<Integer, Integer> victoryVelocityMap;
	private String username;
	private int victoryTotal;	

	public VictoryPacket(Map<Card, Integer> victoryCardMap, Map<Integer, Integer> victoryVelocityMap, String username) {
		this.victoryCardMap = victoryCardMap;
		this.victoryVelocityMap = victoryVelocityMap;
		this.username = username;

		this.victoryTotal = 0;
		for (Entry<Card, Integer> entry : victoryCardMap.entrySet()) {
			victoryTotal += entry.getKey().givesVictory() * entry.getValue();
		}
	}

	public Map<Card, Integer> getVictoryCardMap() {
		return victoryCardMap;
	}

	public Map<Integer, Integer> getVictoryVelocityMap() {
		return victoryVelocityMap;
	}
	
	public String getUsername() {
		return username;
	}

	public int getVictoryTotal() {
		return victoryTotal;
	}

	@Override
	public int compareTo(VictoryPacket other) {
		return victoryTotal == other.victoryTotal ? 0
				: victoryTotal > other.victoryTotal ? -1 : 1;
	}

}
