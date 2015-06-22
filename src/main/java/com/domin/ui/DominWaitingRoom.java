package com.domin.ui;

import static com.domin.player.PlayerState.STATE.CONNECT;
import static com.domin.player.PlayerState.STATE.DISCONNECT;
import static com.domin.player.PlayerState.STATE.READY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import com.domin.net.DominClient;
import com.domin.net.ExistingPlayerInfoPacket;
import com.domin.net.common.ChatPacket;
import com.domin.player.Player;
import com.domin.player.PlayerState;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;

public class DominWaitingRoom extends Scene {

	private static final int OK = 0;
	private static final int CANCEL = 1;

	private static final String READY_STYLE = "-fx-background-color: orange;"
			+ "-fx-text-fill: rgb(50, 50, 50)";

	private static FlowPane playerPane;
	private static Button actionButton;
	private static TextArea chatLog;

	private static DominClient connection;

	private static Timeline waitingAnimation;

	private Map<Integer, Label> playerLabelMap;

	private static ChangeListener<ExistingPlayerInfoPacket> existingPlayerInfoListener;
	private static ChangeListener<PlayerState> playerStateListener;
	private static ChangeListener<String> chatListener;
	private static ChangeListener<Boolean> startListener;

	static {
		waitingAnimation = new Timeline();
		waitingAnimation.setCycleCount(Timeline.INDEFINITE);
		waitingAnimation.getKeyFrames().add(
				new KeyFrame(Duration.seconds(1),
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								if (actionButton.getText().length() > 10) {
									actionButton.setText("Waiting.");
								} else {
									actionButton.setText(actionButton.getText()
											+ ".");
								}
							}

						}));
	}

	public DominWaitingRoom(final DominClient connection) {

		super(buildRoot(connection), StageManager.INSTANCE.getSceneWidth(),
				StageManager.INSTANCE.getSceneHeight());

		this.getStylesheets().add("css/waitingroomscreen.css");
		
		StageManager.INSTANCE.getStage().setOnCloseRequest(
				new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent event) {
						connection.disconnect();
						Platform.exit();
					}

				});

		DominWaitingRoom.waitingAnimation.stop();

		this.playerLabelMap = new HashMap<Integer, Label>(5);

		addListeners();

	}

	private static Parent buildRoot(final DominClient connection) {

		DominWaitingRoom.connection = connection;

		return BorderPaneBuilder.create().top(createMenuBar())
				.center(createCenterNode()).build();
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
														removeListeners();
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
											public void handle(ActionEvent arg0) {
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

	private static VBox createCenterNode() {

		final TextField chatField;
		final VBox chatArea;

		VBox centerNode = VBoxBuilder
				.create()
				.spacing(10)
				.padding(new Insets(10, 10, 10, 10))
				.children(
						VBoxBuilder
								.create()
								.children(
										playerPane = FlowPaneBuilder.create()
												.hgap(10).vgap(10)
												.minHeight(200)
												.alignment(Pos.CENTER).build())
								.build(),
						chatArea = VBoxBuilder
								.create()
								.children(
										actionButton = ButtonBuilder
												.create()
												.text(connection.isServer() ? "Start Game"
														: "Ready")
												.maxWidth(Double.MAX_VALUE)
												.minHeight(50).build(),
										chatLog = TextAreaBuilder
												.create()
												.text("Server: "
														+ connection
																.getUsername()
														+ "! Welcome to Domin!\n")
												.editable(false).disable(false)
												.focusTraversable(false)
												.wrapText(true).build(),
										chatField = TextFieldBuilder.create()
												.build()).build()).build();

		VBox.setVgrow(chatArea, Priority.ALWAYS);
		VBox.setVgrow(chatLog, Priority.ALWAYS);

		actionButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (connection.isServer()) {
					connection.send("start");

				} else {
					actionButton.setText("Waiting");
					actionButton.setDisable(true);
					waitingAnimation.playFromStart();

					connection.send("ready");
				}
			}

		});

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

		return centerNode;
	}

	private void addListeners() {
		
		connection
				.getExistingInfoProp()
				.addListener(
						existingPlayerInfoListener = new ChangeListener<ExistingPlayerInfoPacket>() {

							@Override
							public void changed(
									ObservableValue<? extends ExistingPlayerInfoPacket> observable,
									ExistingPlayerInfoPacket oldValue,
									ExistingPlayerInfoPacket newValue) {
								
								List<Integer> playerIdList = newValue.getPlayerIdList();
								
								for (Integer pID : playerIdList) {									
									
									String username = newValue.getUsername(pID);
									boolean isReady = newValue.isReady(pID);									
									
									createPlayerLabel(pID, username, isReady);
								}
							}

						});

		connection.getStateProp().addListener(
				playerStateListener = new ChangeListener<PlayerState>() {

					@Override
					public void changed(
							ObservableValue<? extends PlayerState> observable,
							PlayerState oldValue, PlayerState newValue) {

						int playerID = newValue.getPlayerID();
						String username = newValue.getUsername();
						PlayerState.STATE state = newValue.getState();

						if (connection.isServer()) {
							actionButton.setDisable(state != READY);
						}

						if (state == DISCONNECT) {
							removePlayerLabel(playerID);
						} else if (state == CONNECT) {
							createPlayerLabel(playerID, username, playerID == 0);
						} else {
							playerLabelMap.get(playerID).setStyle(
									READY_STYLE);
						}
					}

				});

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
						removeListeners();
					}

				});
	}

	private static void removeListeners() {
		connection.getExistingInfoProp().removeListener(existingPlayerInfoListener);
		connection.getStateProp().removeListener(playerStateListener);
		connection.getChatLogProp().removeListener(chatListener);
		connection.getStartProp().removeListener(startListener);
	}

	private void removePlayerLabel(int playerID) {

		playerPane.getChildren().remove(playerLabelMap.remove(playerID));
	}

	private void createPlayerLabel(int playerId, String username, boolean isReady) {

		Label playerLabel = new Label(username);
		playerLabel.setAlignment(Pos.CENTER);
		playerLabel.setMinSize(100, 75);
		playerLabel.setId("player-label");

		if (isReady) {
			playerLabel.setStyle(READY_STYLE);
		}

		playerPane.getChildren().add(playerLabel);
		playerLabelMap.put(playerId, playerLabel);
	}

}
