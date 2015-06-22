package com.domin.player;

import java.io.Serializable;

public class PlayerState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final STATE state;
	private final int playerID;
	private final String username;

	public enum STATE {
		ACTION, BUY, CLEANUP, ACTION_RESPONSE, WAIT, CONNECT, DISCONNECT, READY, WAIT_FOR_READY, ACTIVE
	};
	
	public PlayerState(STATE state, int playerID) {		
		this.state = state;
		this.playerID = playerID;
		this.username = "";		
	}
	
	public PlayerState(STATE state, int playerID, String username) {
		this.state = state;
		this.playerID = playerID;
		this.username = username;
	}
	
	public STATE getState() {
		return state;
	}
	
	public int getPlayerID() {
		return playerID;
	}
	
	public String getUsername() {
		return username;
	}
	
}
