package com.domin.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.domin.card.Card;
import com.domin.card.CardType;
import com.domin.net.common.ChatPacket;
import com.domin.net.common.Client;
import com.domin.net.common.ForwardedMessage;
import com.domin.player.Player;
import com.domin.player.PlayerState;
import com.domin.ui.DominConnectScreen;
import com.domin.ui.DominGameScreen;
import com.domin.ui.util.SoundManager;
import com.domin.ui.util.StageManager;
import com.domin.util.Util;

public class DominClient extends Client {
	
    private String host;
    private ObjectProperty<PlayerState> stateProp;
    private ObjectProperty<ExistingPlayerInfoPacket> existingInfoProp;
    private StringProperty chatLogProp;
    private BooleanProperty startProp;
    private ObjectProperty<SupplyModPacket> supplyModProp;
    private ObjectProperty<CardEventPacket> cardEventProp;
    
    private int numPlayers;
    
    private ObservableList<Card> supplyCardList;
    private ObservableList<Card> basicSupplyCardList;
    
    private boolean serverState;
    private boolean isLookingAtVictory;

    public DominClient(String host, int port, String username, double version)
            throws IOException {
        super(host, port, username);        
        
        this.host = host;
        this.stateProp = new SimpleObjectProperty<PlayerState>();
        this.chatLogProp = new SimpleStringProperty();
        this.startProp = new SimpleBooleanProperty();
        this.existingInfoProp = new SimpleObjectProperty<ExistingPlayerInfoPacket>();
        this.supplyModProp = new SimpleObjectProperty<SupplyModPacket>();
        this.cardEventProp = new SimpleObjectProperty<CardEventPacket>();
        
        this.numPlayers = 0;
        
        supplyCardList = FXCollections.<Card> observableArrayList();
        basicSupplyCardList = FXCollections.<Card> observableArrayList();
        
        this.serverState = this.getID() == 0;        
        
        this.isLookingAtVictory = false;
    }

    protected void extraHandshake(ObjectInputStream in, ObjectOutputStream out)
            throws IOException {    	
        out.writeObject(new HandshakePacket(username, Util.VERSION));
        out.flush();
    }

    protected void messageReceived(final Object message) {
        Platform.runLater(new Runnable() {
            public void run() {

                // Unpack forwarded messages
            	int senderId = -1;
                Object readableMessage;
                
                if (message instanceof ForwardedMessage) {
                	
                	senderId = ((ForwardedMessage) message).senderID;
                	readableMessage = ((ForwardedMessage) message).message;
                	
                } else {
                	
                	readableMessage = message;
                	
                }

                // Process message
                if (readableMessage instanceof PlayerState) {
                	
                    stateProp.set((PlayerState) readableMessage);
                    
                } else if (readableMessage instanceof ChatPacket) {
                	
                    chatLogProp.set(((ChatPacket) readableMessage)
                            .getUsername()
                            + ": "
                            + ((ChatPacket) readableMessage).getMessage()
                            + "\n");
                    
                } else if ("start".equals(readableMessage)) {
                	
                	StageManager.INSTANCE.setScene(new DominGameScreen(DominClient.this));                	
                    startProp.set(true);                    
                    
                } else if (readableMessage instanceof ExistingPlayerInfoPacket) {
                	
                	existingInfoProp.set((ExistingPlayerInfoPacket) readableMessage);
                	
                } else if (readableMessage instanceof SupplyPacket) {
                	
                	supplyCardList.setAll(((SupplyPacket) readableMessage).getSupplyList());
                	basicSupplyCardList.setAll(((SupplyPacket) readableMessage).getBasicSupplyList());
                	
                } else if (readableMessage instanceof SupplyModPacket) {
                	
                	supplyModProp.set((SupplyModPacket) readableMessage);
                	
                } else if (readableMessage instanceof CardEventPacket) {
                	
                	cardEventProp.set((CardEventPacket) readableMessage);
                	
                } else if (readableMessage instanceof CallablePacket) {
                	
                	((CallablePacket) readableMessage).call();
                	
                } else if (readableMessage instanceof AttackCallablePacket) {                	
                	
                	// TODO move reaction logic so that you react to attacks, not attack effects
                	
                	// can reveal same action unlimited times per the rules
                	List<Card> reactionCards = Player.INSTANCE.getCardsOfTypeInHand(CardType.REACTION);
                	                	
					while (reactionCards.size() != 0) {
						List<Card> chosenReaction = StageManager.INSTANCE
								.createCardChoiceView(
										Player.INSTANCE,
										"React",
										"Play a Reaction?",
										reactionCards,
										1,
										true,
										((AttackCallablePacket) readableMessage)
												.getAttackCardImage());
						if (chosenReaction.size() == 0) {
							break;
						}
						chosenReaction.get(0).reactionEffect(Player.INSTANCE,
								DominClient.this);
						reactionCards = Player.INSTANCE
								.getCardsOfTypeInHand(CardType.REACTION);
					}

					if (!Player.INSTANCE.isUnaffected()
							&& !Player.INSTANCE.blockedAttack()) {
						SoundManager.ACTIVE.play();
						((AttackCallablePacket) readableMessage)
								.callWithConnection(DominClient.this);
					}

					Player.INSTANCE.setBlockedAttack(false);

					send("done");
                	
                } else if (readableMessage instanceof Integer) {
                	
                	numPlayers = ((Integer) readableMessage).intValue();
                	
                } else if ("end-game".equals(readableMessage)) {
                	
                	send(new VictoryPacket(Player.INSTANCE.generateVictoryMap(), Player.INSTANCE.getVictoryVelocityMap(), username));
                	
                } else if (readableMessage instanceof VictoryDisplayPacket) {

					isLookingAtVictory = true;

					StageManager.INSTANCE.createVictoryTotalView(
							((VictoryDisplayPacket) readableMessage)
									.getVictoryPacketList(), DominClient.this);

					disconnect();
					StageManager.INSTANCE.setScene(new DominConnectScreen());
                	
                }
            }
        });
    }

    protected void serverShutdown(final String message) {
        Platform.runLater(new Runnable() {
            public void run() {
            	if (!isLookingAtVictory) {
            		StageManager.INSTANCE.closeActiveChoiceStage();            	
            		StageManager.INSTANCE.setScene(new DominConnectScreen());
            		StageManager.INSTANCE.createDialog("Server Shutdown", "Disconnected from server.");
            	}
            }
        });
    }
    
    public boolean isServer() {
    	return serverState;
    }

    public ObjectProperty<PlayerState> getStateProp() {
        return stateProp;
    }
    
    public ObjectProperty<ExistingPlayerInfoPacket> getExistingInfoProp() {
    	return existingInfoProp;
    }
    
    public ObjectProperty<SupplyModPacket> getSupplyModProp() {
    	return supplyModProp;
    }
    
    public ObjectProperty<CardEventPacket> getCardEventProp() {
    	return cardEventProp;
    }

    public StringProperty getChatLogProp() {
        return chatLogProp;
    }
    
    public BooleanProperty getStartProp() {
        return startProp;
    }

    public String getHost() {
        return host;
    }

    public String getUsername() {
        return username;
    }
    
    public int getNumPlayers() {
    	return numPlayers;
    }
    
    public ObservableList<Card> getSupplyCardList() {
    	return supplyCardList;
    }

    public ObservableList<Card> getBasicSupplyCardList() {
    	return basicSupplyCardList;
    }
}
