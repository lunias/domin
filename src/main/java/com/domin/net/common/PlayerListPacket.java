package com.domin.net.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.domin.player.Player;

public class PlayerListPacket implements Serializable {

	private static final long serialVersionUID = 5106508508942868823L;
	
	private Map<Integer, Player> playerMap;

	public PlayerListPacket(Map<Integer, Player> playerMap) {
		
		this.playerMap = playerMap;
		
	}
	
	public List<Player> getList() {
		
		return new ArrayList<Player>(playerMap.values());
		
	}
	
	public Map<Integer, Player> getMap() {
		
		return playerMap;
		
	}
	
}
