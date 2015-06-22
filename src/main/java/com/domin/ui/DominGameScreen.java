package com.domin.ui;

import static com.domin.player.PlayerState.STATE.ACTION;
import static com.domin.player.PlayerState.STATE.ACTIVE;
import static com.domin.player.PlayerState.STATE.WAIT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.CustomMenuItemBuilder;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuButtonBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.SeparatorBuilder;
import javafx.scene.control.Slider;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.control.ToolBar;
import javafx.scene.control.ToolBarBuilder;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.TilePaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import com.domin.card.Card;
import com.domin.card.CardType;
import com.domin.card.base.Province;
import com.domin.card.prosperity.Colony;
import com.domin.net.CardEventPacket;
import com.domin.net.DominClient;
import com.domin.net.SupplyModPacket;
import com.domin.net.common.ChatPacket;
import com.domin.player.CardEventType;
import com.domin.player.Player;
import com.domin.player.PlayerState;
import com.domin.ui.util.CardManager;
import com.domin.ui.util.SoundManager;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;

public class DominGameScreen extends Scene {

	private static final int OK = 0;
	private static final int CANCEL = 1;
	private static final int SUPPLY_CARD_WIDTH = 90;
	private static final int HAND_CARD_WIDTH = 150;
	private static final int PLAYED_CARD_WIDTH = 60;	
	
	private static TextArea chatLog;
	private static TextField chatField;
	private static HBox handNode;
	private static Text deckCount;
	private static Text discardCount;
	private static HBox supplyNode;
	private static HBox basicSupplyNode;
	private static ScrollPane playedNode;
	private static TilePane playedCardPane;
	
	private static Button actionButton;
	private static Button buyButton;
	private static Button coinButton;
	private static Button potionButton;
	private static Button endButton;
	
	private static IntegerProperty handSpaceProperty;
	private static IntegerProperty playedCardsColumnsProperty;
	private static ObjectProperty<Image> magnifyImageProperty;
	private static BooleanProperty magnifyVisibleProperty;
	private static BooleanProperty inactiveProperty;
	
	private static DropShadow dropShadow;
	
	private static DominClient connection;
	
	private static ChangeListener<String> chatListener;
	private static ChangeListener<PlayerState> playerStateListener;
	private static ChangeListener<Boolean> startListener;
	
	public static Map<Card, StringProperty> supplyCardMap;
	private static Map<Card, StackPane> supplyCardNodeMap;
	
	private static ObservableList<Card> trashList;
	private static StringProperty trashCountProp;	
	
	public DominGameScreen(final DominClient connection) {
		
		super(buildRoot(connection), StageManager.INSTANCE.getSceneWidth(), StageManager.INSTANCE.getSceneHeight());	
		
		this.getStylesheets().add("css/gamescreen.css");
		
		this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (!chatField.isFocused()) {
					if (!inactiveProperty.get() && event.getCode() == KeyCode.SPACE) {
						List<Card> playedTreasures = Player.INSTANCE.playAllTreasures();
						for (Card card : playedTreasures) {
							connection.send(new CardEventPacket(card, CardEventType.PLAY));		
						}						
						event.consume();
					} else if (!inactiveProperty.get() && event.getCode() == KeyCode.E) {
						endTurn();
						event.consume();
					}	
				} else {
					if (event.getCode() == KeyCode.ESCAPE) {
						playedCardPane.requestFocus();
					}
				}
			}
			
		});
		
		addListeners();
		
	}
	
	private static Parent buildRoot(final DominClient connection) {
		
		DominGameScreen.connection = connection;		
		
		handSpaceProperty = new SimpleIntegerProperty();
		playedCardsColumnsProperty = new SimpleIntegerProperty();
		magnifyImageProperty = new SimpleObjectProperty<Image>();
		magnifyVisibleProperty = new SimpleBooleanProperty();
		
		inactiveProperty = new SimpleBooleanProperty();
		inactiveProperty.set(true);
		
		dropShadow = new DropShadow();
		dropShadow.setOffsetY(3.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setColor(Color.BLACK);
		dropShadow.setBlurType(BlurType.GAUSSIAN);	
		
		supplyCardMap = new HashMap<Card, StringProperty>();
		supplyCardNodeMap = new HashMap<Card, StackPane>();		
		
		trashList = FXCollections.<Card> observableArrayList();
		StageManager.INSTANCE.initTrashList(trashList);
		
		trashCountProp = new SimpleStringProperty();
		
		StageManager.INSTANCE.getStage().widthProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				updateHandSpacing(newValue.intValue());
				updatePlayedSpacing(newValue.intValue());
			}
			
		});
		
		return BorderPaneBuilder.create().top(createMenuBar())
										 .center(createCenterNode())
										 .build();
	}
	
	private static MenuBar createMenuBar() {

		MenuBar menuBar = MenuBarBuilder
				.create()
				.menus(MenuBuilder
						.create()
						.text("_File")
						.items(MenuItemBuilder
								.create()
								.text("Exit")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+Q"))
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent e) {
										if (StageManager.INSTANCE
												.createDialog(
														ModalDialog.Type.CONFIRM,
														"Confirm Exit",
														"Are you sure you want to exit Domin?") == OK) {
											connection.disconnect();
											Platform.exit();
										}
									}
								}).build()).build(),
						MenuBuilder
								.create()
								.text("_Network")
								.items(MenuItemBuilder
										.create()
										.text("Disconnect")
										.onAction(
												new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent event) {
														connection.disconnect();
														// TODO removeListeners();
														StageManager.INSTANCE
																.setScene(new DominConnectScreen());
													}

												}).build()).build(),
						MenuBuilder
								.create()
								.text("_Options")
								.items(MenuItemBuilder.create()
										.text("Randomizer Settings")
										.onAction(new EventHandler<ActionEvent>() {
											@Override
											public void handle(ActionEvent event) {
												StageManager.INSTANCE.show(StageType.RandomizerSettings);
											}											
										})
										.build(),
										MenuItemBuilder.create()
												.text("View Card Library")
												.onAction(new EventHandler<ActionEvent>() {
													@Override
													public void handle(
															ActionEvent event) {
														StageManager.INSTANCE.show(StageType.CardLibraryView);
													}
													
												})
												.build()).build(),
						MenuBuilder
								.create()
								.text("_Help")
								.items(MenuItemBuilder.create().text("About")
										.build()).build()).build();
		return menuBar;
	}
	
	private static StackPane createCenterNode() {
		HBox topBox;
		ImageView magnifyLayer;
		
		StackPane centerNode = StackPaneBuilder.create()
				.children(VBoxBuilder.create()
				          	.children(topBox = HBoxBuilder.create()
				          	                   	.spacing(10)
				          	                   	.padding(new Insets(10, 10, 10, 10))
				          	                   	.children(createPlayedNode(), createChatNode())
				          	                   	.build(),
				          	                   	createBasicSupplyNode(),
				          	                   	createSupplyNode(),
				          	                   	createToolBarNode(),
				          	                   	createHandNode()
				          			)
				          	.build(),
				          	magnifyLayer = ImageViewBuilder.create()
				          	.visible(false)
				          	.build()
						)
				.build();							
		
		VBox.setVgrow(topBox, Priority.ALWAYS);
		
		magnifyLayer.imageProperty().bind(magnifyImageProperty);
		magnifyLayer.visibleProperty().bind(magnifyVisibleProperty);
		
		magnifyLayer.setEffect(dropShadow);
		
		return centerNode;
	}

	private static ScrollPane createPlayedNode() {
		playedNode = ScrollPaneBuilder.create()
				.content(
						playedCardPane = TilePaneBuilder.create()
								.hgap(5)
								.vgap(2)
								.build()
						)
				.id("played-area")
				.build();
		
		HBox.setHgrow(playedNode, Priority.ALWAYS);
		
		playedCardPane.prefColumnsProperty().bind(playedCardsColumnsProperty);
		playedCardsColumnsProperty.set(8);
		
		return playedNode;
	}
	
	private static StackPane createPlayedCardNode(Card card, CardEventType eventType) {				
		ImageView cardImageView;
		Text playType;
		
		StackPane playedCardPane = StackPaneBuilder.create()
				.children(cardImageView = ImageViewBuilder.create()
									.image(card.getImage())
									.preserveRatio(true)
									.smooth(true)
									.fitWidth(PLAYED_CARD_WIDTH)
									.build(),
							playType = TextBuilder.create()
							      .text(eventType.getAbbrev())
							      .font(Font.font(null, FontWeight.BOLD, 24))
							      .id("played-card-type")
							      .build()
						  )
				.build();	
		
		playType.setEffect(dropShadow);
		
		addMagnifyListener(playedCardPane, card);
		
		return playedCardPane;
	}
	
	private static VBox createChatNode() {
		
		VBox chatNode = VBoxBuilder.create()
					  .spacing(10)
					  .padding(new Insets(10, 10, 10, 10))
					  .children(
							  chatLog = TextAreaBuilder.create()
							          .text("Server: Game start.\n")
							          .editable(false)
							          .disable(false)
							          .focusTraversable(false)
							          .wrapText(true)
							          .build(),
							  chatField = TextFieldBuilder.create()
							  		  .minHeight(25)
							          .build()
							  )
			.build();
		
		VBox.setVgrow(chatLog, Priority.ALWAYS);
		
		chatField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					String said = chatField.getText().trim();
					if (!said.isEmpty()) {
						connection.send(new ChatPacket(said, connection
								.getUsername()));
						chatField.setText("");
					}
				}
			}
			
		});		
		
		return chatNode;
	}
	
	private static HBox createBasicSupplyNode() {
		//HBox basicSupplyNode;
		StackPane trashPane;
		Text trashCount;
		
		HBox basicSupplyBox = HBoxBuilder.create()
							.padding(new Insets(10, 10, 10, 10))
							.children(basicSupplyNode = HBoxBuilder.create()
							                            	.spacing(10)
							                            	.build(),
									  trashPane = StackPaneBuilder.create()
									  							  .cursor(Cursor.HAND)
									  							  .onMouseClicked(new EventHandler<MouseEvent>() {

																	@Override
																	public void handle(
																			MouseEvent event) {
																		if (event.getButton() == MouseButton.PRIMARY) {
																			StageManager.INSTANCE.show(StageType.TrashView);																			
																		}
																	}
									  								  
									  							  })
									  							  .children(ImageViewBuilder.create()
															  				.image(new Image("img/Trash.jpg"))
															  				.preserveRatio(true)
															  				.smooth(true)
															  				.fitWidth(SUPPLY_CARD_WIDTH)								
															  				.build(),
															  				trashCount = TextBuilder.create()
															  				                        .font(Font.font(null, FontWeight.BOLD, 36))
															  				                        .id("trash-count")															  				
															  				                        .build()
															  	   )
									                              .build()									  							                            	
							          )
							.id("basic-supply-area")
							.build();
		
		trashCount.setEffect(dropShadow);
		trashCount.textProperty().bind(trashCountProp);
		trashCountProp.set("0");
		
		HBox.setHgrow(basicSupplyNode, Priority.ALWAYS);
		
		// List<Card> supplyPiles = CardManager.INSTANCE.getBasicSupplyPiles();
		// 
		// for (Card card : supplyPiles) {
		// 	basicSupplyNode.getChildren().add(createSupplyCardNode(card));
		// }		
		
		return basicSupplyBox;
	}
	
	private static HBox createSupplyNode() {
		supplyNode = HBoxBuilder.create()
							 .spacing(10)
							 .padding(new Insets(10, 10, 10, 10))
							 .id("supply-area")
							 .build();		
		
		return supplyNode;
	}
	
	private static StackPane createSupplyCardNode(final Card card) {
		ImageView cardImageView;
		Text cardCount;
		
		StackPane supplyCardPane = StackPaneBuilder.create()
									.onMouseClicked(new EventHandler<MouseEvent>() {

										@Override
										public void handle(MouseEvent event) {
											if (event.getButton() == MouseButton.PRIMARY &&
													!inactiveProperty.get() &&
													Integer.parseInt(supplyCardMap.get(card).get()) > 0 &&
													(Player.INSTANCE.getBuys() > 0 || Player.INSTANCE.isGaining()) &&
													Player.INSTANCE.canAfford(card)) {
												
												if (Player.INSTANCE.isGaining()) {
													Player.INSTANCE.gain(card);
												} else {
													Player.INSTANCE.buy(card);	
												}
																								
											}
										}
										
									})
									.cursor(Cursor.HAND)
								   	.children(cardImageView = ImageViewBuilder.create()
										                          	.image(card.getImage())
										                          	.preserveRatio(true)
										                          	.smooth(true)
										                          	.fitWidth(SUPPLY_CARD_WIDTH)
										                          	.id("supply-card-image-empty")
										                          	.build(),
										      cardCount = TextBuilder.create()
							      			  	          	.font(Font.font(null, FontWeight.BOLD, 36))							      			  	          
							      			  	          	.id("supply-count-positive")
							      			  	          	.build()
								 )
						.build();		
		
		cardCount.setEffect(dropShadow);		
		
		addMagnifyListener(supplyCardPane, card);					
		
		int numPlayers = connection.getNumPlayers();
		String victoryCount = numPlayers == 2 ? "8" : "12";
		
		StringProperty cardCountProp = new SimpleStringProperty();
		if (card.isOfType(CardType.BASIC)) {
			switch (card.getName()) {
			case "Copper":
				cardCountProp.set(String.valueOf((numPlayers < 5 ? 60 : 120) - numPlayers * 7));
				break;
			case "Silver":
				cardCountProp.set(numPlayers < 5 ? "40" : "80");
				break;
			case "Gold":
				cardCountProp.set(numPlayers < 5 ? "30" : "60");
				break;
			case "Platinum":
				cardCountProp.set("12");
				break;
			case "Potion":
				cardCountProp.set("16");
				break;
			case "Estate":
				cardCountProp.set(victoryCount);
				break;
			case "Duchy":
				cardCountProp.set(victoryCount);
				break;
			case "Province":
				cardCountProp.set(numPlayers < 5 ? victoryCount : "15");
				break;
			case "Colony":
				cardCountProp.set("12");
				break;
			case "Curse":
				cardCountProp.set(String.valueOf(10 * (numPlayers - 1)));
				break;
			}
		} else if (card.isOfType(CardType.VICTORY)) {
			cardCountProp.set(victoryCount);	
		} else {
			cardCountProp.set("10");
		}
		
		cardCount.textProperty().bind(cardCountProp);
		supplyCardMap.put(card, cardCountProp);
		
		supplyCardNodeMap.put(card, supplyCardPane);
		
		return supplyCardPane;
	}
	
	private static ToolBar createToolBarNode() {
		MenuButton matButton;
		Slider volumeSlider;
		Region spring;
		
		ToolBar buttonBar = ToolBarBuilder.create()
							.items(HBoxBuilder.create()
									.spacing(10)
									.children(actionButton = ButtonBuilder.create()
									  			.text("Actions: 0")
									  			.build(),
									  		  buyButton = ButtonBuilder.create()
									  		    .text("Buys: 0")
									  		    .build(),
									  		  coinButton = ButtonBuilder.create()
									  		    .text("Coins: 0")
									  		    .onMouseClicked(new EventHandler<MouseEvent>() {

									  		    	@Override
									  		    	public void handle(MouseEvent event) {
									  		    		if (event.getButton() == MouseButton.PRIMARY && !inactiveProperty.get()) {
									  		    			List<Card> playedTreasures = Player.INSTANCE.playAllTreasures();
									  		    			for (Card card : playedTreasures) {
									  		    				connection.send(new CardEventPacket(card, CardEventType.PLAY));		
									  		    			}
									  		    		}
									  		    	}
									  		    	
									  		    })
									  		    .build(),
									  		  potionButton = ButtonBuilder.create()
									  		    .text("Potions: 0")
									  		    .onMouseClicked(new EventHandler<MouseEvent>() {
									  		    	
									  		    	@Override
									  		    	public void handle(MouseEvent event) {
									  		    		if (event.getButton() == MouseButton.PRIMARY && !inactiveProperty.get()) {
									  		    			List<Card> playedTreasures = Player.INSTANCE.playAllTreasures();
									  		    			for (Card card : playedTreasures) {
									  		    				connection.send(new CardEventPacket(card, CardEventType.PLAY));		
									  		    			}
									  		    		}												
									  		    	}
									  		    	
									  		    })
									  		    .build(),
									  		  SeparatorBuilder.create()
									  		    .build(),									  
									  		  matButton = MenuButtonBuilder.create()
									  		    .text("Mats")
									  		    .items(MenuBuilder.create()
									  		    		.text("Native Village")
									  		    		.items(CustomMenuItemBuilder.create()
									  		    				.content(createMat())
									  		    				.hideOnClick(false)
									  		    				.id("mat")
									  		    				.build()
									  		    			  )
									  		    		.build(),
									  		    		MenuBuilder.create()
									  		    		  .text("Trade Route")
									  		    		  .items(CustomMenuItemBuilder.create()
									  		    				  .content(createMat())
									  		    				  .hideOnClick(false)
									  		    				  .id("mat")
									  		    				  .build()
									  		    				)
									  		    		  .build()
									  		    		)									    
									  		    		.build(),
									  		    		endButton = ButtonBuilder.create()
									  		    		.text("End Turn")
									  		    		.onMouseClicked(new EventHandler<MouseEvent>() {
									  		    			
									  		    			@Override
									  		    			public void handle(MouseEvent event) {
									  		    				if (event.getButton() == MouseButton.PRIMARY && !inactiveProperty.get()) {
									  		    					endTurn();												
									  		    				}
									  		    			}
									  		    			
									  		    		})
									  		    .build()
											)
											.build(),
											spring = RegionBuilder.create()
											  .build(),
											volumeSlider = SliderBuilder.create()
											  .min(0)
											  .max(1.5)
											  .value(.2)
											  .tooltip(new Tooltip("Volume"))
											  .build()
									)
									.build();
		
		StageManager.INSTANCE.getVolumeProp().bind(volumeSlider.valueProperty());
		
		HBox.setHgrow(spring, Priority.ALWAYS);
						
		return buttonBar;
	}
	
	private static TilePane createMat() {
		List<ImageView> cardImages = new ArrayList<ImageView>();
		int i = 0;
		for (Card card : CardManager.INSTANCE.getCardMap().values()) {
			cardImages.add(ImageViewBuilder.create()
							.image(card.getImage(PLAYED_CARD_WIDTH, 120))
							.build());
			if (++i == 15) {
				break;
			}
		}
		
		return TilePaneBuilder.create()
				.prefColumns(5)
				.hgap(5)
				.vgap(5)
				.children(cardImages)
				.build();
	}
	
	private static HBox createHandNode() {		
		ImageView deckNode;
		
		HBox handBox = HBoxBuilder.create()
							.spacing(10)
							.padding(new Insets(10, 10, 10, 10))
							.children(handNode = HBoxBuilder.create()
													.spacing(10)
													.padding(new Insets(0, 10 , 0, 10))
													.build(),
									  StackPaneBuilder.create()
									  	.children(deckNode = ImageViewBuilder.create()
									  				.image(new Image("img/Back.jpg"))
									  				.preserveRatio(true)
									  				.smooth(true)
									  				.fitWidth(HAND_CARD_WIDTH)								
									  				.build(),
									  			  VBoxBuilder.create()
									  			  .spacing(70)
									  			  .alignment(Pos.CENTER)
									  			  .children(
									  					  deckCount = TextBuilder.create()									  					  
									  					  .text("5")
									  					  .font(Font.font(null, FontWeight.BOLD, 36))
									  					  .id("deck-count")
									  					  .build(),
									  					  discardCount = TextBuilder.create()
									  					  .text("0")
									  					  .font(Font.font(null, FontWeight.BOLD, 36))
									  					  .id("discard-count")
									  					  .build()
									  					  )
									  				.build()
									  			  )
									  	.build()
									 )
							.id("hand-area")
							.build();
		
		deckCount.setEffect(dropShadow);
		discardCount.setEffect(dropShadow);		
		
		HBox.setHgrow(handNode, Priority.ALWAYS);		
		
		handNode.spacingProperty().bind(handSpaceProperty);		
		
		return handBox;		
	}
	
	private static ImageView createHandCardNode(final Card card) {
		ImageView cardImageView = ImageViewBuilder.create()
									.onMouseClicked(new EventHandler<MouseEvent>() {

										@Override
										public void handle(MouseEvent event) {
											if (event.getButton() == MouseButton.PRIMARY && !inactiveProperty.get() && Player.INSTANCE.canPlay(card)) {
												if (!card.isOfType(CardType.ACTION)) {
													Player.INSTANCE.removeActions(Player.INSTANCE.getActions());
												}
												connection.send(new CardEventPacket(card, CardEventType.PLAY));												
												Player.INSTANCE.play(card);
											}																								
										}
										
									})
									.image(card.getImage())
									.preserveRatio(true)
									.smooth(true)
									.fitWidth(HAND_CARD_WIDTH)
									.cursor(Cursor.HAND)
									.build();
		
		addMagnifyListener(cardImageView, card);
		
		return cardImageView;		
	}
	
	private static void updateHandSpacing(int stageWidth) {
		int handSize = Player.INSTANCE.getHand().size();
		
		if (handSize == 0) {
			return;
		}
		
		int handSpacing = (handSize + 1) * 10;
		int handWidth = handSize * HAND_CARD_WIDTH + handSpacing;				
		int delta = stageWidth - (handWidth + HAND_CARD_WIDTH + 20);
		
		int spacing = delta / handSize - 1;		
		if (spacing > 0) {
			spacing = 10;
		}
		
		handSpaceProperty.set(spacing);					
	}
	
	private static void updatePlayedSpacing(int stageWidth) {
		int maxColumns = ((int)(playedNode.getWidth()) / (PLAYED_CARD_WIDTH + 10));
		
		playedCardsColumnsProperty.set(maxColumns);
	}
	
	private static void addMagnifyListener(Node node, final Card card) {		
		
		node.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					magnifyImageProperty.set(card.getImage());
					magnifyVisibleProperty.set(true);
				}
			}
			
		});		
		
		node.setOnMouseReleased(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				magnifyVisibleProperty.set(false);
			}
			
		});		
	}
	
	private static boolean isGameOver() {
		int pilesDown = 0;
		int pilesToDown = connection.getNumPlayers() > 4 ? 4 : 3;
		for (Entry<Card, StringProperty> entry : supplyCardMap.entrySet()) {
			if (Integer.parseInt(entry.getValue().get()) == 0) {
				if (entry.getKey() instanceof Province || entry.getKey() instanceof Colony) {
					return true;
				}
				
				if (++pilesDown == pilesToDown) {
					return true;
				}
			}
		}
		return false;
	}
	
	private static void endTurn() {
		
		if (Player.INSTANCE.isGaining()) {
			return;
		}
		
		List<Card> playedCardsCopy = new ArrayList<Card>(Player.INSTANCE.getPlayedCards().getAsList());
		for (Card card : playedCardsCopy) {
			card.endOfTurnEffect(Player.INSTANCE, connection);
		}
		
		inactiveProperty.set(true);
		
		Player.INSTANCE.cleanup();
		connection.send(new CardEventPacket(null, CardEventType.CLEAR));
		if (isGameOver()) {
			connection.send("end-game");										
		} else {
			connection.send("end-turn");	
		}
	}
	
	private static void addListeners() {
		connection.getChatLogProp().addListener(
				chatListener = new ChangeListener<String>() {
					
					@Override
					public void changed(
							ObservableValue<? extends String> observable,
							String oldValue, String newValue) {
						chatLog.appendText(newValue);
					}
					
				});
		
		connection.getStartProp().addListener(
				startListener = new ChangeListener<Boolean>() {

					@Override
					public void changed(
							ObservableValue<? extends Boolean> observable,
							Boolean oldValue, Boolean newValue) {

						Player.INSTANCE.initPlayer(connection);						
						
						// TODO needs optimization
						Player.INSTANCE.getHand().getAsList().addListener(new ListChangeListener<Card>() {

							@Override
							public void onChanged(
									javafx.collections.ListChangeListener.Change<? extends Card> c) {
								List<ImageView> cardList = new ArrayList<ImageView>();
								
								for (Card card : c.getList()) {									
									cardList.add(createHandCardNode(card));
								}					
								
								handNode.getChildren().setAll(cardList);
								updateHandSpacing((int)StageManager.INSTANCE.getStage().getWidth());
							}
							
						});
						
						actionButton.textProperty().bind(Player.INSTANCE.getActionProperty());
						buyButton.textProperty().bind(Player.INSTANCE.getBuyProperty());
						coinButton.textProperty().bind(Player.INSTANCE.getCoinProperty());
						potionButton.textProperty().bind(Player.INSTANCE.getPotionProperty());
						
						deckCount.textProperty().bind(Player.INSTANCE.getDeck().getSizeProperty());
						discardCount.textProperty().bind(Player.INSTANCE.getDiscardPile().getSizeProperty());											
						
						Player.INSTANCE.draw(5);
						
						Player.INSTANCE.getCoinsToSpendProperty().addListener(new ResourceChangeListener());
						
						Player.INSTANCE.getPotionsToSpendProperty().addListener(new ResourceChangeListener());
						
						Player.INSTANCE.getReducedCostProperty().addListener(new ResourceChangeListener());
						
						Player.INSTANCE.getBuysToSpendProperty().addListener(new ChangeListener<Number>() {

							@Override
							public void changed(
									ObservableValue<? extends Number> observable,
									Number oldValue, Number newValue) {
								
								// No more buys; no options.
								if (Player.INSTANCE.getBuys() == 0) {
									for (Entry<Card, StackPane> entry : supplyCardNodeMap.entrySet()) {
										entry.getValue().getChildren().get(0).setId("supply-card-image-empty");
									}
									
								// Turn start; enable zero costs basically.
								} else if (Player.INSTANCE.getBuys() == 1) {
									for (Entry<Card, StackPane> entry : supplyCardNodeMap.entrySet()) {
										
										StringProperty cardCount = supplyCardMap.get(entry.getKey());
										if (Integer.parseInt(cardCount.get()) == 0) {
											continue;
										}									
									
										if (Player.INSTANCE.canAfford(entry.getKey())) {
											entry.getValue().getChildren().get(0).setId("supply-card-image");
										} else {
											entry.getValue().getChildren().get(0).setId("supply-card-image-empty");
										}

									}
								}
							}
							
						});
						
						Player.INSTANCE.getGainToSpendProperty().addListener(new ChangeListener<Number>() {

							@Override
							public void changed(
									ObservableValue<? extends Number> observable,
									Number oldValue, Number newValue) {
								
									for (Entry<Card, StackPane> entry : supplyCardNodeMap.entrySet()) {
										
										StringProperty cardCount = supplyCardMap.get(entry.getKey());
										if (Integer.parseInt(cardCount.get()) == 0) {
											continue;
										}									
									
										// Can afford while gaining or can afford once gain is done
										if (Player.INSTANCE.canAfford(entry.getKey()) && (Player.INSTANCE.getBuys() > 0 || Player.INSTANCE.isGaining())) {
											entry.getValue().getChildren().get(0).setId("supply-card-image");
										} else {
											entry.getValue().getChildren().get(0).setId("supply-card-image-empty");
										}

									}							
							}
							
						});
						
					}
					
					class ResourceChangeListener implements ChangeListener<Number> {

						@Override
						public void changed(ObservableValue<? extends Number> observable,
								Number newValue, Number oldValue) {
							
							for (Entry<Card, StackPane> entry : supplyCardNodeMap.entrySet()) {
								
								StringProperty cardCount = supplyCardMap.get(entry.getKey());
								if (Integer.parseInt(cardCount.get()) == 0) {
									continue;
								}
								
								// Resource changed; can afford?
								if (Player.INSTANCE.getBuys() > 0 && Player.INSTANCE.canAfford(entry.getKey())) {
									entry.getValue().getChildren().get(0).setId("supply-card-image");
								} else {
									entry.getValue().getChildren().get(0).setId("supply-card-image-empty");
								}
							}
						}
						
					}

				});
		
		connection.getSupplyCardList().addListener(new ListChangeListener<Card>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Card> c) {
				List<StackPane> supplyCardList = new ArrayList<StackPane>();
				
				for (Card card : c.getList()) {										
					supplyCardList.add(createSupplyCardNode(card));
				}
				
				supplyNode.getChildren().setAll(supplyCardList);
			}
			
		});
		
		connection.getBasicSupplyCardList().addListener(new ListChangeListener<Card>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Card> c) {
				List<StackPane> basicSupplyCardList = new ArrayList<StackPane>();				
				
				for (Card card : c.getList()) {
					basicSupplyCardList.add(createSupplyCardNode(card));
				}
				
				basicSupplyNode.getChildren().setAll(basicSupplyCardList);
			}
			
		});
		
		connection.getSupplyModProp().addListener(new ChangeListener<SupplyModPacket>() {

			@Override
			public void changed(
					ObservableValue<? extends SupplyModPacket> observable,
					SupplyModPacket oldValue, SupplyModPacket newValue) {
				
				Card card = newValue.getCard();
				int mod = newValue.getMod();
				
				StringProperty cardCount = supplyCardMap.get(card);
				int newCount = Integer.parseInt(cardCount.get()) + mod;				
				
				if (newCount >= 0) {
					cardCount.set(String.valueOf(newCount));					
				}
				
				if (newCount == 0) {
					StackPane cardPane = supplyCardNodeMap.get(card);
					cardPane.getChildren().get(0).setId("supply-card-image-empty");
					cardPane.getChildren().get(1).setId("supply-count-zero");
				}
				
				if (!inactiveProperty.get() && Player.INSTANCE.getBuys() == 0 && !Player.INSTANCE.isGaining()) {
					endTurn();
				}

			}
			
		});
		
		connection.getCardEventProp().addListener(new ChangeListener<CardEventPacket>() {

			@Override
			public void changed(
					ObservableValue<? extends CardEventPacket> observable,
					CardEventPacket oldValue, CardEventPacket newValue) {
				
				Card card = newValue.getCard();
				CardEventType eventType = newValue.getEventType();
				
				if (eventType == CardEventType.CLEAR) {
					playedCardPane.getChildren().clear();
				} else {
					if (eventType == CardEventType.TRASH) {
						trashList.add(card);
						trashCountProp.set(String.valueOf(trashList.size()));
					} else if (eventType == CardEventType.UNTRASH) {
						trashList.remove(card);
						trashCountProp.set(String.valueOf(trashList.size()));						
					}
					playedCardPane.getChildren().add(createPlayedCardNode(card, eventType));					
				}								
			}
			
		});
		
		connection.getStateProp().addListener(
				playerStateListener = new ChangeListener<PlayerState>() {

					@Override
					public void changed(
							ObservableValue<? extends PlayerState> observable,
							PlayerState oldValue, PlayerState newValue) {
						
						PlayerState.STATE state = newValue.getState();

						if (state == ACTION) {
							// notify player of turn start
							SoundManager.ALERT.play();
							Player.INSTANCE.displayDurationCards();
							Player.INSTANCE.addActions(1);
							Player.INSTANCE.addBuys(1);
							Player.INSTANCE.waiting(false);
							inactiveProperty.set(false);
						} else if (state == ACTIVE) {
							SoundManager.ACTIVE.play();
							Player.INSTANCE.waiting(false);
							inactiveProperty.set(false);
						} else if (state == WAIT) {
							Player.INSTANCE.waiting(true);
							inactiveProperty.set(true);
						}
					}

				});	
	}
	
}
