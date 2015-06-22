package com.domin.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import com.domin.player.Player;
import com.domin.ui.util.StageManager;

public class ChoiceView extends Stage {

	private Player player;
	private String[] choices;
	private boolean may = false;
	private int toSelect = -1;
	private String labelText = "";
	
	private Image cardImage = null;
	
	private List<Integer> selectedChoices;
	
	public ChoiceView(Player player) {
		
		this.player = player;
		
		this.setResizable(false);
		
		Stage primaryStage = StageManager.INSTANCE.getStage();
		double primaryWidth = primaryStage.getWidth();
		double primaryHeight = primaryStage.getHeight();
		
		this.setWidth(500);
		this.setHeight(520);
		
		this.setX(primaryStage.getX() + (primaryWidth - this.getWidth()) / 2);
		this.setY(primaryStage.getY() + (primaryHeight - this.getHeight()) / 2);
				
		this.initModality(Modality.APPLICATION_MODAL);
		this.initStyle(StageStyle.UTILITY);
		
		this.selectedChoices = new ArrayList<Integer>();
		
		this.setOnCloseRequest(
				new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent event) {
						if (toSelect == -1) {
							ChoiceView.this.close();
						} else {
							event.consume();
						}
					}

				});
				
	}
	
		public List<Integer> showDialog(String title, String labelText, String[] choices, int toSelect, boolean may, Image cardImage) {
			this.cardImage = cardImage;
			
			return showDialog(title, labelText, choices, toSelect, may);
		}
	
	public List<Integer> showDialog(String title, String labelText, String[] choices, int toSelect, boolean may) {
		setTitle(title);
		
		this.labelText = labelText;
		this.choices = choices;		
		this.toSelect = toSelect;
		this.may = may;		
		
		this.setScene(createScene());			
		
		this.showAndWait();
		
		return selectedChoices;
	}
	
	private Scene createScene() {
		Image bigCardImage = cardImage != null ? cardImage : player.getLastPlayedCard().getImage();
		
		VBox vBox;
		
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
								            		     vBox = VBoxBuilder.create()								    			
								            		     .children(createChoicePane())
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
		
		HBox.setHgrow(vBox, Priority.ALWAYS);
		
		return new Scene(rootNode);
	}
	
	private VBox createChoicePane() {
		
		VBox choicePane = VBoxBuilder.create()
				                     .build();
		
		for (int i = 0; i < choices.length; i++) {
			final Button choiceButton;
			final int index = i;
			
			choicePane.getChildren().add(choiceButton = ButtonBuilder.create()					                                  
					                                  	.text(choices[i])
					                                  	.maxWidth(Double.MAX_VALUE)
					                                  	.minHeight(50)
					                                  	.build()
					                     );
			
			choiceButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					if (event.getButton() == MouseButton.PRIMARY && toSelect != 0) {
						selectedChoices.add(index);
						choiceButton.setDisable(true);

						if (--toSelect == 0) {
							ChoiceView.this.close();
						}
					}
				}
				
			});
		}
		
		return choicePane;		
	}
	
	private List<Button> createButtons() {
		List<Button> buttonList = new ArrayList<Button>();
		
		if (toSelect == -1) {
			buttonList.add(ButtonBuilder.create()
					.text("Done")
					.onAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							ChoiceView.this.close();
						}
						
					})
					.build());			
		}
		
		if (may) {
			buttonList.add(ButtonBuilder.create()
					.text("Pass")
					.onAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent event) {
							selectedChoices.clear();
							ChoiceView.this.close();
						}
						
					})
					.build());
		}
		
		return buttonList;
	}	
}
