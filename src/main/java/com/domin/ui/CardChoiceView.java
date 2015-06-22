package com.domin.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.FlowPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.domin.card.Card;
import com.domin.card.CardLocation;
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class CardChoiceView extends Stage {
	
	private Player player;	
	
	private List<Card> returnCardList;
	private List<Card> sourceList;
	
	private String labelText = "";
	private int toSelect = -1;
	private int canSelect = 0;
	private boolean may = false;
	private Image cardImage = null;
	
	private Button passButton = null;
	
	public CardChoiceView(Player player) {
		
		this.player = player;		
		
		this.setResizable(false);
		
		Stage primaryStage = StageManager.INSTANCE.getStage();
		double primaryWidth = primaryStage.getWidth();
		double primaryHeight = primaryStage.getHeight();
		
		this.setWidth(primaryWidth * .75);
		this.setHeight(primaryHeight * .6);
		
		this.setX(primaryStage.getX() + (primaryWidth - this.getWidth()) / 2);
		this.setY(primaryStage.getY() + (primaryHeight - this.getHeight()) / 2);
				
		this.initModality(Modality.APPLICATION_MODAL);
		this.initStyle(StageStyle.UTILITY);
		
		this.returnCardList = new ArrayList<Card>();
		
		this.setOnCloseRequest(
				new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						if (toSelect == -1) {
							CardChoiceView.this.close();
						} else {
							event.consume();
						}
					}
					
				});
	}
		
	public List<Card> showDialog(String title, String labelText, CardLocation cardLoc, int toSelect, boolean may, Image cardImage) {
		this.cardImage = cardImage;
		
		return showDialog(title, labelText, cardLoc, toSelect, may);
	}
	
	public List<Card> showDialog(String title, String labelText, List<Card> sourceList, int toSelect, boolean may, Image cardImage) {
		this.cardImage = cardImage;
		
		return showDialog(title, labelText, sourceList, toSelect, may);
	}
	
	public List<Card> showDialog(String title, String labelText, CardLocation cardLoc, int toSelect, boolean may) {
		List<Card> sourceList = null;		
		
		switch(cardLoc) {
		case HAND:
			sourceList = player.getHand().getAsList();
			break;
		case DECK:
			sourceList = player.getDeck().getAsList();
			break;
		case DISCARD:
			sourceList = player.getDiscardPile().getAsList();
			break;
		case TRASH:
			return returnCardList;
		case PLAYED:
			sourceList = player.getPlayedCards().getAsList();
			break;
		default:
			System.out.println("Unrecognized CardLocation in CardChoiceView.");
			return returnCardList;
		}
		
		return showDialog(title, labelText, sourceList, toSelect, may);
	}
	
	public List<Card> showDialog(String title, String labelText, List<Card> sourceList, int toSelect, boolean may) {
		this.setTitle(title);
		
		if (sourceList.isEmpty()) {
			return returnCardList;
		}
		
		this.labelText = labelText;
		this.sourceList = sourceList;
		this.toSelect = toSelect;		
		this.may = may;
		
		this.canSelect = sourceList.size();
		
		if (sourceList.size() == 0) {
			return returnCardList;
		}
		
		this.setScene(createScene());
		
		this.showAndWait();		
		
		return returnCardList;
	}
	
	private Scene createScene() {
		Image bigCardImage = cardImage != null ? cardImage : player.getLastPlayedCard().getImage();
		
		ScrollPane scrollPane;		
		
		VBox rootNode = VBoxBuilder.create()
						.padding(new Insets(10, 10, 10, 10))
						.spacing(10)
						.alignment(Pos.CENTER)
						.children(LabelBuilder.create()
											  .text(labelText)
											  .font(Font.font(null, FontWeight.BOLD, 24))
											  .build(),
								    HBoxBuilder.create()
								    		   .spacing(10)
								               .children(ImageViewBuilder.create()
								            		     .image(bigCardImage)
								            		     .preserveRatio(true)
								            		     .smooth(true)
								            		     .fitHeight(400)
								            		     .build(),
								            		     scrollPane = ScrollPaneBuilder.create()								    			
								            		     .content(createCardImagePane(sourceList))
								            		     .build()
								            		   )
								               .build(),
									HBoxBuilder.create()
									.alignment(Pos.CENTER)
									.spacing(100)
									.children(createButtons())
									.build()
								)
						.build();
		
		HBox.setHgrow(scrollPane, Priority.ALWAYS);
		
		return new Scene(rootNode);
	}
	
	private FlowPane createCardImagePane(List<Card> cardList) {
		FlowPane cardImagePane = FlowPaneBuilder.create()
				                 .build();
		
		for (final Card card : cardList) {
			final ImageView cardImageView;
			
			cardImagePane.getChildren().add(cardImageView = ImageViewBuilder.create()
															.image(card.getImage())
															.preserveRatio(true)
															.smooth(true)
															.fitWidth(100)
															.cursor(Cursor.HAND)
															.build());			
			
			cardImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getButton() == MouseButton.PRIMARY && toSelect != 0) {
						returnCardList.add(card);
						cardImageView.setVisible(false);
						
						if (passButton != null) {
							passButton.setText("Done");	
						}

						if (--toSelect == 0 || --canSelect == 0) {
							CardChoiceView.this.close();
						}
					}					
				}
				
			});
		}
		
		return cardImagePane;
	}
	
	private List<Button> createButtons() {
		List<Button> buttonList = new ArrayList<Button>();
		
		if (toSelect == -1) {
			buttonList.add(ButtonBuilder.create()
					.text("Done")
					.onAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							CardChoiceView.this.close();
						}
						
					})
					.build());	
		}
		
		if (may) {
			buttonList.add(passButton = ButtonBuilder.create()
					.text("Pass")
					.onAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {							
							CardChoiceView.this.close();
						}
						
					})
					.build());
		}
		
		return buttonList;
	}
	
}