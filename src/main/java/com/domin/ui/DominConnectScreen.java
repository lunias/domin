package com.domin.ui;

import java.io.IOException;
import java.util.Properties;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.AccordionBuilder;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuBarBuilder;
import javafx.scene.control.MenuBuilder;
import javafx.scene.control.MenuItemBuilder;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TitledPaneBuilder;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RegionBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

import com.domin.net.DominClient;
import com.domin.net.DominHub;
import com.domin.ui.util.CardManager;
import com.domin.ui.util.Settings;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;
import com.domin.util.Util;

public class DominConnectScreen extends Scene {
	
	private static Button actionButton;
	private static Text statusText;
	private static TextField joinHost;
	
	private static final int OK = 0;
	
	private static String defaultHost = "127.0.0.1";
	private static String defaultPort = "53768";
	private static String defaultUsername = "";
	
	private static boolean joining;
	
	private static boolean isServer;	

	public DominConnectScreen() {
		super(buildRoot(), StageManager.INSTANCE.getSceneWidth(),
				StageManager.INSTANCE.getSceneHeight());
		
		this.getStylesheets().add("css/connectscreen.css");

		this.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					actionButton.fire();
					event.consume();
				}
			}

		});

		isServer = false;
		joining = false;
	}
	
	private static Parent buildRoot() {
		StageManager.INSTANCE.closeGameScreens();
		
		Properties connectionSettings = Settings.readConnectionSettings();
		if (connectionSettings != null) {
			defaultHost = connectionSettings.getProperty("host", "127.0.0.1");
			defaultPort = connectionSettings.getProperty("port", "53768");
			defaultUsername = connectionSettings.getProperty("username", "");
		}
		
		return BorderPaneBuilder.create()
				.top(createMenuBar())
				.center(createCenterNode())
				.bottom(createBottomNode())
				.build();
	}
	
	private static MenuBar createMenuBar() {
		MenuBar menuBar = MenuBarBuilder
				.create()
				.menus(MenuBuilder
						.create()
						.text("_File")
						.items(MenuItemBuilder.create()
								.text("Exit")
								.accelerator(
										KeyCombination.keyCombination("Ctrl+Q"))
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										if (StageManager.INSTANCE
												.createDialog(
														ModalDialog.Type.CONFIRM,
														"Confirm Exit",
														"Are you sure you want to exit Domin?") == OK) {
											Platform.exit();
										}
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
											public void handle(ActionEvent event) {
												StageManager.INSTANCE.show(StageType.CardLibraryView);
											}
											
										})
										.build())
								.build(),
						MenuBuilder
								.create()
								.text("_Help")
								.items(MenuItemBuilder.create().text("About")
										.build()).build()).build();
		return menuBar;
	}	
	
	private static Node createCenterNode() {
		final TextField username;
		
		final Accordion accordion;
		
		final TitledPane joinPane;
		final GridPane joinGrid;
		final TextField joinPort;
		
		final GridPane hostGrid;
		final TextField hostPort;				
		
		final Region spring = new Region();
		VBox.setVgrow(spring, Priority.ALWAYS);
		
		FlowPane centerNode = FlowPaneBuilder.create()
				.alignment(Pos.CENTER)
				.children(VBoxBuilder.create()
						.spacing(5)
						.alignment(Pos.CENTER)
						.children(TextBuilder.create()
								.text("Dominion")
								.id("title-text")
								.build(),
								RegionBuilder.create()
								.prefHeight(200)
								.minHeight(Region.USE_PREF_SIZE)
								.maxHeight(Region.USE_PREF_SIZE)
								.build(),
								HBoxBuilder.create()
								.spacing(5)
								.alignment(Pos.CENTER)
								.children(new Label("Username: "),
										username = new TextField(defaultUsername))
										.build(),
										accordion = AccordionBuilder.create()
										.panes(joinPane = TitledPaneBuilder.create()
												.text("Join")
												.focusTraversable(false)
												.content(joinGrid = GridPaneBuilder.create()
												          .vgap(5)
												          .padding(new Insets(5, 5, 5, 5))
												          .build()
														)
												.build(),
												TitledPaneBuilder.create()
												.text("Host")
												.focusTraversable(false)
												.content(hostGrid = GridPaneBuilder.create()
												         .vgap(5)
												         .padding(new Insets(5, 5, 5, 5))
												         .build()
														)
												.build()
												)								
										.build(),
										spring,
										actionButton = ButtonBuilder.create()
										.text("Join")
										.build(),
										statusText = TextBuilder.create()
										.id("status-text")
										.build()
								)
						.build()
						)
				.build();
		
		joinHost = new TextField(defaultHost);
		joinPort = new TextField(defaultPort);
		
		joinGrid.add(new Label("Host Address: "), 0, 0);
		joinGrid.add(joinHost, 1, 0);
		joinGrid.add(new Label("Port: "), 0, 1);
		joinGrid.add(joinPort, 1, 1);
		
		hostPort = new TextField(defaultPort);
		
		hostGrid.add(new Label("Port:                "), 0, 0);
		hostGrid.add(hostPort, 1, 0);
		
		Region strut = new Region();
		strut.setPrefHeight(20);
		strut.setMinHeight(Region.USE_PREF_SIZE);
		strut.setMaxHeight(Region.USE_PREF_SIZE);
		hostGrid.add(strut, 0, 1);
		
		accordion.setExpandedPane(joinPane);
		joinPane.setCollapsible(false);
		accordion.expandedPaneProperty().addListener(new ChangeListener<TitledPane>() {

			@Override
			public void changed(
					ObservableValue<? extends TitledPane> observable,
					final TitledPane oldValue, final TitledPane newValue) {
				
				if (newValue != null) {
					if (newValue.getText().equals("Host")) {
						actionButton.setText("Host");
						isServer = true;
					} else {
						actionButton.setText("Join");
						isServer = false;
					}
					
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							newValue.setCollapsible(false);
						}
						
					});
				}
				
				if (oldValue != null) {
					oldValue.setCollapsible(true);
				}				
			}
			
		});
		
		actionButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (joining) {
					return;
				}
				
				statusText.setFill(Color.FIREBRICK);							
				
				final String usernameString = username.getText().trim();

				if (usernameString.isEmpty()) {
					statusText.setText("Username required");
					username.requestFocus();
					return;
				}
				
				final String hostPortString = hostPort.getText().trim();
				final String joinHostString = joinHost.getText().trim();
				final String joinPortString = joinPort.getText().trim();
				
				int port = -1;
				
				if (isServer) {
					statusText.setText("Creating Server...");
					try {
						port = Integer.parseInt(hostPortString);
					} catch (NumberFormatException nfe) { }
					
					if (port < 0) {
						statusText.setText("Illegal port number");
						hostPort.requestFocus();
						return;
					}
										
					try {
						new DominHub(port);
					} catch (Exception e) {
						statusText.setText("Cannot listen on port " + port);
						hostPort.requestFocus();
						return;
					}
					
					clientConnection("127.0.0.1", port, usernameString);
					
				} else {
					statusText.setText("Establishing Connection...");
					try {
						port = Integer.parseInt(joinPortString);
					} catch (NumberFormatException nfe) { }
					
					if (joinHostString.isEmpty()) {
						statusText.setText("Host address required");
						joinHost.requestFocus();
						return;
					} else if (port < 0) {
						statusText.setText("Illegal port number");
						joinPort.requestFocus();
						return;
					}
					
					joining = true;
					clientConnection(joinHostString, port, usernameString);
				}
			}
			
		});		
		
		return centerNode;
	}
	
	private static Node createBottomNode() {
		return HBoxBuilder.create()
				          .alignment(Pos.BOTTOM_RIGHT)
				          .padding(new Insets(10, 10, 10, 10))
				          .children(TextBuilder.create()
				                        .text(CardManager.INSTANCE.getCardCount() + " cards enabled (" + Util.VERSION + " alpha)")
				                        .build()
				                    )
					      .build();
				
				

	}
	
	private static void clientConnection(final String host, final int port, final String username) {
		new Thread() {
			
			public void run() {
				try {
					
					Settings.writeConnectionSettings(host, port, username);
					
					final DominClient client = new DominClient(host, port, username, Util.VERSION);
					Platform.runLater(new Runnable() {

						@Override
						public void run() {					
							StageManager.INSTANCE.setScene(new DominWaitingRoom(client));
							joining = false;
						}
						
					});
				} catch (IOException ioe) {
					statusText.setText("Could not connect to server");
					joinHost.requestFocus();
					joining = false;
				}
			}
			
		}.start();
	}	
	
}
