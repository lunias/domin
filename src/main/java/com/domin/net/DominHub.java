package com.domin.net;

import static com.domin.player.PlayerState.STATE.ACTION;
import static com.domin.player.PlayerState.STATE.ACTIVE;
import static com.domin.player.PlayerState.STATE.CONNECT;
import static com.domin.player.PlayerState.STATE.DISCONNECT;
import static com.domin.player.PlayerState.STATE.READY;
import static com.domin.player.PlayerState.STATE.WAIT;
import static com.domin.player.PlayerState.STATE.WAIT_FOR_READY;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.domin.net.common.ChatPacket;
import com.domin.net.common.ForwardedMessage;
import com.domin.net.common.Hub;
import com.domin.player.CardEventType;
import com.domin.player.PlayerState;
import com.domin.ui.util.CardManager;
import com.domin.util.Util;

public class DominHub extends Hub {

	private int currentPlayerID;
	private int currentPlayerIndex;

	private Map<Integer, String> usernameMap;
	private Map<Integer, Boolean> readyMap;
	private List<Integer> playerIDList;
	
	private List<VictoryPacket> victoryPacketList;
	
	private boolean canProgress;
	private int doneCount;
	private int endCount;
	
	private static final int POKE_TIME = 10000;

	public DominHub(int port) throws IOException {
		super(port);
		this.currentPlayerID = 0;
		
		this.usernameMap = new HashMap<Integer, String>(5);
		this.readyMap = new HashMap<Integer, Boolean>(5);
		this.playerIDList = new ArrayList<Integer>(5);
		
		this.victoryPacketList = new ArrayList<VictoryPacket>(5);
		
		this.canProgress = false;
		this.doneCount = 0;
		this.endCount = 0;
	}

	protected boolean extraHandshake(int playerID, ObjectInputStream in,
			ObjectOutputStream out) throws IOException {
		try {
			HandshakePacket connectionData = (HandshakePacket) in.readObject();
			usernameMap.put(playerID, connectionData.getUsername());
			return connectionData.getVersion() == Util.VERSION;
		} catch (ClassNotFoundException e) {
			throw new IOException("DominHub could not read username");
		}		
		
	}

	protected void playerConnected(int playerID) {
		if (playerID > 0) {
			sendExistingPlayerInfoPacket(playerID);
			readyMap.put(playerID, false);
		} else {
			readyMap.put(playerID, true);
		}

		playerIDList.add(playerID);

		if (playerIDList.size() == 5) {
			shutdownServerSocket();
		}

		sendState(CONNECT, playerID, usernameMap.get(playerID));

		for (Integer pID : playerIDList) {
			if (pID != playerID) {
				sendToOne(pID, new ChatPacket(usernameMap.get(playerID) + " connected.", "Server"));
			}
		}

	}

	protected void playerDisconnected(int playerID) {
		if (playerID == 0) {
			this.shutDownHub();
		} else {
			sendState(DISCONNECT, playerID);
			sendToAll(new ChatPacket(usernameMap.get(playerID) + " disconnected.", "Server"));
			removePlayer(playerID);
			
			if (playerID == currentPlayerID) {
				nextPlayer();
				currentPlayerIndex = playerIDList.indexOf(currentPlayerID);
				
				sendToAll(new CardEventPacket(null, CardEventType.CLEAR));
				sendToAll(new ChatPacket(usernameMap.get(currentPlayerID)
						+ " is now up.", "Server"));
				sendState(ACTION, WAIT);
				
			} else if (playerReadyCheck()) {
				sendState(READY, currentPlayerID);
			}
		}
	}
	
	private void removePlayer(int playerID) {
		playerIDList.remove(new Integer(playerID));
		readyMap.remove(playerID);
		usernameMap.remove(playerID);
	}

	protected void messageReceived(final int playerID, final Object message) {

		if (message instanceof ChatPacket) {

			sendToAll(new ForwardedMessage(playerID, message));
			
		} else if (message instanceof CallablePacket) {
			
			for (Integer pID : playerIDList) {
				if (pID != playerID) {
					sendToOne(pID, new ForwardedMessage(playerID, message));
				}
			}
		} else if (message instanceof AttackCallablePacket) {
			
			sendState(WAIT, playerID);
			
			new Thread() {
				
				public void run() {
					boolean poke;
					int sleepTime;
					doneCount = 0;
					
					for (Integer pID : playerIDList) {
						
						if (pID != playerID) {

							if (((AttackCallablePacket) message).isSimultaneuous()) {
								sendToOne(pID, new ForwardedMessage(playerID, message));
							} else {
								poke = false;
								canProgress = false;
								sleepTime = 0;
								sendToOne(pID, new ForwardedMessage(playerID, message));
								
								while(!canProgress) {
									try {
										sleep(500);
										if (!poke && (sleepTime += 500) > POKE_TIME) {
											poke = true;
											sendToAll(new ChatPacket("We're waiting on you " + usernameMap.get(pID) + "...", "Server"));
										}
									} catch (InterruptedException ie) { }
								}	
							}
						}
					}
					
					if (((AttackCallablePacket) message).isSimultaneuous()) {
						poke = false;
						sleepTime = 0;											
						
						while(doneCount != playerIDList.size() - 1) {
							try {
								sleep(500);
								if (!poke && (sleepTime += 500) > POKE_TIME) {
									poke = true;
									if (doneCount != playerIDList.size()) {
										sendToAll(new ChatPacket("Still waiting on " + (playerIDList.size() - 1 - doneCount) + " player(s)...", "Server"));										
									}
								}
							} catch(InterruptedException ie) { }
						}
					}
					
					sendToOne(currentPlayerID, new ChatPacket("Done waiting for attack reactions. You may continue.", "Server"));
					sendState(ACTIVE, WAIT);
					
				}
				
			}.start();

		} else if (message instanceof SupplyModPacket) {			
			
			sendToAll(new ForwardedMessage(playerID, message));
			
		} else if (message instanceof CardEventPacket) {
			
			sendToAll(new ForwardedMessage(playerID, message));
			
		} else if ("ready".equals(message)) {

			readyMap.put(playerID, true);
			if (playerReadyCheck()) {
				sendState(READY, playerID);
			} else {
				sendState(WAIT_FOR_READY, playerID);
			}

		} else if ("start".equals(message)) {

			shutdownServerSocket();
			
			Random roller = new Random(System.nanoTime());
			currentPlayerIndex = roller.nextInt(playerIDList.size());
			currentPlayerID = playerIDList.get(currentPlayerIndex);
			
			// number of players
			sendToAll(new Integer(playerIDList.size()));
			
			// start
			sendToAll(new ForwardedMessage(currentPlayerID, message));			
			
			// supply
			sendToAll(new SupplyPacket(
					CardManager.INSTANCE.rollSupplyPiles(10),
					CardManager.INSTANCE.getBasicSupplyPiles()));
			
			// chat
			sendToAll(new ChatPacket(usernameMap.get(currentPlayerID) + " plays first.", "Server"));			
			
			// player states
			sendState(ACTION, WAIT);
			
		} else if ("end-turn".equals(message)) {
			
			sendToAll(new ChatPacket(usernameMap.get(currentPlayerID)
					+ " ended their turn. " + usernameMap.get(nextPlayer())
					+ " is now up.", "Server"));
			sendState(ACTION, WAIT);
			
		} else if ("done".equals(message)) {
			
			canProgress = true;			
			doneCount++;
			
		} else if ("end-game".equals(message)) {
			
			sendToAll(new ChatPacket("Game over!", "Server"));
			sendToAll(new ForwardedMessage(currentPlayerID, message));
			
		} else if (message instanceof VictoryPacket) {

			victoryPacketList.add((VictoryPacket) message);

			if (++endCount == playerIDList.size()) {
				sendToAll(new VictoryDisplayPacket(victoryPacketList));
			}
			
		}
	}

	private void sendState(PlayerState.STATE activePlayerState,
			PlayerState.STATE inactivePlayerState) {

		this.resetOutput();

		sendToOne(currentPlayerID, new PlayerState(activePlayerState, currentPlayerID));

		for (Integer pID : playerIDList) {
			if (pID != currentPlayerID) {
				sendToOne(pID, new PlayerState(inactivePlayerState, pID));
			}
		}
	}

	private void sendState(PlayerState.STATE playerState, int playerID) {
		this.resetOutput();
		sendToAll(new PlayerState(playerState, playerID));
	}
	
	private void sendState(PlayerState.STATE playerState, int playerID, String username) {
		this.resetOutput();
		sendToAll(new PlayerState(playerState, playerID, username));
	}
	
	private void sendExistingPlayerInfoPacket(int playerID) {
		this.resetOutput();
		sendToOne(playerID, new ExistingPlayerInfoPacket(new ArrayList<Integer>(playerIDList), usernameMap, readyMap));		
	}
	
	private int nextPlayer() {
		return currentPlayerID = playerIDList
				.get((++currentPlayerIndex % playerIDList.size()));
	}

	private int prevPlayer() {
		if (currentPlayerIndex == 0) {
			currentPlayerIndex = playerIDList.size() - 1;
			return playerIDList.get(playerIDList.size() - 1);
		}
		return playerIDList.get(--currentPlayerIndex);
	}

	private boolean playerReadyCheck() {
		if (playerIDList.size() < 2) {
			return false;
		}

		for (Boolean isReady : readyMap.values()) {
			if (!isReady) {
				return false;
			}
		}
		return true;
	}

}
