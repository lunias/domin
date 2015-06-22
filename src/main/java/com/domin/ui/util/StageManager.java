package com.domin.ui.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.domin.card.Card;
import com.domin.card.CardLocation;
import com.domin.net.DominClient;
import com.domin.net.VictoryPacket;
import com.domin.player.Player;
import com.domin.ui.CardChoiceView;
import com.domin.ui.CardLibraryView;
import com.domin.ui.ChoiceView;
import com.domin.ui.ModalDialog;
import com.domin.ui.RandomizerSettings;
import com.domin.ui.TrashView;
import com.domin.ui.VictoryTotalView;

public enum StageManager {

	INSTANCE;

	private Stage primaryStage;
	private Map<StageType, Stage> stagesOpen;
	private Stage activeChoiceStage;
	
	private ObservableList<Card> trashList;
	
	private DoubleProperty volumeProp;

	private StageManager() {
		this.stagesOpen = new HashMap<StageType, Stage>();
		volumeProp = new SimpleDoubleProperty(.2);
	}

	public void init(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	public void initTrashList(ObservableList<Card> trashList) {
		this.trashList = trashList;
	}
	
	public ObservableList<Card> getTrashList() {
		return trashList;
	}

	public void setScene(Scene scene) {
		primaryStage.setScene(scene);
	}

	public int createDialog(String title, String message) {
		ModalDialog dialog = new ModalDialog(ModalDialog.Type.INFO,
				primaryStage);
		dialog.setTitleText(title);
		dialog.setMessageText(message);
		return dialog.showDialog();
	}

	public int createDialog(ModalDialog.Type type, String title, String message) {
		ModalDialog dialog = new ModalDialog(type, primaryStage);
		dialog.setTitleText(title);
		dialog.setMessageText(message);
		return dialog.showDialog();
	}

	public List<Card> createCardChoiceView(Player player, String title,
			String label, CardLocation cardLoc, int toSelect, boolean may) {
		activeChoiceStage = new CardChoiceView(player);
		return ((CardChoiceView) activeChoiceStage).showDialog(title, label,
				cardLoc, toSelect, may);
	}

	public List<Card> createCardChoiceView(Player player, String title,
			String label, CardLocation cardLoc, int toSelect, boolean may,
			Image cardImage) {
		activeChoiceStage = new CardChoiceView(player);
		return ((CardChoiceView) activeChoiceStage).showDialog(title, label,
				cardLoc, toSelect, may, cardImage);
	}
	
	public List<Card> createCardChoiceView(Player player, String title,
			String label, List<Card> cardList, int toSelect, boolean may) {
		activeChoiceStage = new CardChoiceView(player);
		return ((CardChoiceView) activeChoiceStage).showDialog(title, label,
				cardList, toSelect, may);
	}
	
	public List<Card> createCardChoiceView(Player player, String title,
			String label, List<Card> cardList, int toSelect, boolean may, Image cardImage) {
		activeChoiceStage = new CardChoiceView(player);
		return ((CardChoiceView) activeChoiceStage).showDialog(title, label,
				cardList, toSelect, may, cardImage);
	}	

	public List<Integer> createChoiceView(Player player, String title,
			String label, String[] choices, int toSelect, boolean may) {
		activeChoiceStage = new ChoiceView(player);
		return ((ChoiceView) activeChoiceStage).showDialog(title, label,
				choices, toSelect, may);
	}
	
	public List<Integer> createChoiceView(Player player, String title,
			String label, String[] choices, int toSelect, boolean may, Image cardImage) {
		activeChoiceStage = new ChoiceView(player);
		return ((ChoiceView) activeChoiceStage).showDialog(title, label,
				choices, toSelect, may, cardImage);
	}	
	
	public int createVictoryTotalView(List<VictoryPacket> victoryPacketList, DominClient connection) {
		activeChoiceStage = new VictoryTotalView(victoryPacketList, connection);
		return ((VictoryTotalView) activeChoiceStage).showDialog();
	}

	public void closeActiveChoiceStage() {
		if (activeChoiceStage != null) {
			activeChoiceStage.close();
		}
	}

	public Stage getStage() {
		return primaryStage;
	}

	public double getSceneWidth() {
		return primaryStage.getScene() != null ? primaryStage.getScene()
				.getWidth() : 1100;
	}

	public double getSceneHeight() {
		return primaryStage.getScene() != null ? primaryStage.getScene()
				.getHeight() : 600;
	}

	public void closeGameScreens() {
		for (StageType stageType : stagesOpen.keySet()) {
			if (stageType == StageType.TrashView) {
				close(stageType);
			}
		}
	}
	
	public void close(StageType type) {
		Stage openStage = stagesOpen.get(type);
		if (openStage != null) {
			openStage.close();
			stagesOpen.remove(type);
		}
	}

	public void show(StageType type) {
		Stage toShow = stagesOpen.get(type);
		if (toShow != null) {
			toShow.toFront();
		} else {
			final Stage newStage;
			switch (type) {
			case RandomizerSettings:
				newStage = new RandomizerSettings();
				break;
			case CardLibraryView:
				newStage = new CardLibraryView();
				break;
			case TrashView:
				newStage = new TrashView(trashList);
				break;
			default:
				System.out.println("Unrecognized StageType");
				return;
			}

			stagesOpen.put(type, newStage);

			newStage.show();
		}
	}
	
	public DoubleProperty getVolumeProp() {
		return volumeProp;
	}

}
