package com.domin.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFieldBuilder;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.domin.card.CardSet;
import com.domin.ui.util.Settings;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;

public class RandomizerSettings extends Stage {
	
	private static final String ERROR_STYLE = "-fx-background-color: red,linear-gradient(to bottom, derive(red,60%) 5%,derive(red,90%) 40%)";
	
	private List<CheckBox> setList;
	private List<TextField> minList;
	private List<TextField> maxList;
	private List<CheckBox> otherList;

	public RandomizerSettings() {
		
		this.setList = new ArrayList<CheckBox>();
		this.minList = new ArrayList<TextField>();
		this.maxList = new ArrayList<TextField>();
		this.otherList = new ArrayList<CheckBox>();
		
		this.setTitle("Randomizer Settings");
		
		this.setResizable(false);
		
		this.setWidth(300);
		this.setHeight(600);			
		
		Stage primaryStage = StageManager.INSTANCE.getStage();
		
		double yLoc = primaryStage.getY() + (primaryStage.getHeight() / 2) - 300;
		double xLoc = primaryStage.getX() + (primaryStage.getWidth() / 2) - 150;
		this.setX(xLoc);
		this.setY(yLoc);
		
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				StageManager.INSTANCE.close(StageType.RandomizerSettings);
			}
			
		});
		
		this.setScene(createScene());
		
	}
	
	public Scene createScene() {
		
		final GridPane gridPane;
		final HBox buttonBox;
		
		VBox rootNode = VBoxBuilder.create()
				.padding(new Insets(10, 10, 10, 10))
				.children(gridPane = GridPaneBuilder.create()
									.vgap(5)
									.hgap(15)
									.padding(new Insets(5, 5, 5, 5))
									.build(),
						  buttonBox = HBoxBuilder.create()
									  .alignment(Pos.BOTTOM_CENTER)
									  .children(ButtonBuilder.create()
									          .text("Save")
									          .onAction(new EventHandler<ActionEvent>() {

												@Override
												public void handle(ActionEvent event) {
													saveSettings();
													StageManager.INSTANCE.close(StageType.RandomizerSettings);
												}
									        	  
									          })
									          .build()
									 )
									 .build()
				)
				.build();
		
		VBox.setVgrow(buttonBox, Priority.ALWAYS);
		
		gridPane.add(new Label("Sets: "), 0, 0);
		
		int col = 0;
		int row = 1;
		for (CardSet set : CardSet.values()) {
			CheckBox setCheck;
			gridPane.add(setCheck = CheckBoxBuilder.create()
					.selected(true)
					.text(set.toString())
					.build(), col, row);
			col = ++col % 2;
			if (col == 0) {
				row++;
			}
			setList.add(setCheck);
		}
		
		gridPane.add(new Label("Card Limits: "), 0, ++row);
		
		gridPane.add(HBoxBuilder.create()
								.alignment(Pos.CENTER)
								.spacing(55)
								.children(new Label("Min"),
										new Label("Max")
										)
								.build(), 0, ++row, 2, 1);
		
		for (int i = 0; i < 11; i++) {
			final TextField min;
			final TextField max;
			gridPane.add(HBoxBuilder.create()
					.spacing(10)
					.alignment(Pos.CENTER)
					.children(min = TextFieldBuilder.create()
							.text("0")
							.prefColumnCount(1)
							.build(),
							new Label(i + " Cost"),
							max = TextFieldBuilder.create()
							.text("9")
							.prefColumnCount(1)
							.build()
							)
							.build(), 0, ++row, 2, 1);
			
			addNumberValidator(min);
			addNumberValidator(max);
			
			minList.add(min);
			maxList.add(max);
		}
		
		row += 5;
		gridPane.add(new Label("Other Options: "), 0, ++row);

		CheckBox[] optChecks = new CheckBox[3];
		
		gridPane.add(optChecks[0] = CheckBoxBuilder.create()
				                    .text("3 - 5 Alchemy Cards")
				                    .build(),
				     0, ++row);
		gridPane.add(optChecks[1] = CheckBoxBuilder.create()
				                    .text("If Attack, Require Counter")
						            .build(),
			         0, ++row);		
		gridPane.add(optChecks[2] = CheckBoxBuilder.create()
				                    .text("No Attack Cards")
				                    .build(),
				     0, ++row);
		
		for (CheckBox optCheck : optChecks) {
			otherList.add(optCheck);
		}		
		
		loadSettings();
		
		return new Scene(rootNode);
		
	}
	
	private void saveSettings() {
		
		List<Boolean> setToggleList = new ArrayList<Boolean>();
		List<Integer> minIntList = new ArrayList<Integer>();
		List<Integer> maxIntList = new ArrayList<Integer>();
		List<Boolean> otherToggleList = new ArrayList<Boolean>();
		
		for (CheckBox cb : setList) {
			setToggleList.add(cb.isSelected());
		}
		
		for (TextField tf : minList) {
			int min = 0;
			try {
				min = Integer.parseInt(tf.getText());
			} catch (NumberFormatException nfe) { }
			minIntList.add(min);
		}
		
		for (TextField tf : maxList) {
			int max = 9;
			try {
				max = Integer.parseInt(tf.getText());
			} catch (NumberFormatException nfe) { }			
			maxIntList.add(max);
		}

		
		for (CheckBox cb : otherList) {
			otherToggleList.add(cb.isSelected());
		}
		
		Settings.writeRandomizerSettings(setToggleList, minIntList, maxIntList, otherToggleList);
	}
	
	private void loadSettings() {
		Properties randomizerProperties = Settings.readRandomizerSettings();
		if (randomizerProperties != null) {
			
			int i = 0;	
			for (CheckBox cb : setList) {
				cb.setSelected(Boolean.parseBoolean(randomizerProperties.getProperty(CardSet.values()[i++].toString().toLowerCase(), "true")));
			}
			
			i = 0;
			for (TextField tf : minList) {
				tf.setText(randomizerProperties.getProperty("min_" + i++ + "_cost", "0"));
			}
			
			i = 0;
			for (TextField tf : maxList) {
				tf.setText(randomizerProperties.getProperty("max_" + i++ + "_cost", "9"));
			}
			
			i = 0;
			for (CheckBox cb : otherList) {
				cb.setSelected(Boolean.parseBoolean(randomizerProperties.getProperty("other_" + i++, "false")));
			}
			
		}
	}
	
	private void addNumberValidator(final TextField field) {
		
		field.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				field.clear();
				if (event.getCharacter().matches("[^0-9]")) {
					field.setStyle(ERROR_STYLE);
					event.consume();
				} else {
					field.setStyle("");
				}
			}
			
		});
	}
	
}
