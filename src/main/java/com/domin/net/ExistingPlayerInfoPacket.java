package com.domin.net;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ExistingPlayerInfoPacket implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	List<Integer> playerIdList;
	Map<Integer, String> usernameMap;
	Map<Integer, Boolean> readyMap;
	
	public ExistingPlayerInfoPacket(List<Integer> playerIdList, Map<Integer, String> usernameMap, Map<Integer, Boolean> readyMap) {
		this.playerIdList = playerIdList;
		this.usernameMap = usernameMap;
		this.readyMap = readyMap;
	}
	
	public List<Integer> getPlayerIdList() {
		return playerIdList;
	}

	public String getUsername(int pID) {
		String username = usernameMap.get(pID);
		return username != null ? username : "";
	}
	
	public boolean isReady(int pID) {		
		Boolean readyState = readyMap.get(pID);
		return readyState != null ? readyState : false;
	}
	
}
