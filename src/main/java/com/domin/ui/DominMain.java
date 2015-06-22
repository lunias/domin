package com.domin.ui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import com.domin.ui.util.StageManager;

public class DominMain extends Application {
	
	private Stage stage;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		stage = primaryStage;
		
		StageManager.INSTANCE.init(primaryStage);
		StageManager.INSTANCE.setScene(new DominConnectScreen());									
		
		stage.setTitle("Domin");
		stage.setMinWidth(1000);
		stage.setMinHeight(900);
		stage.show();		

	}
		
	@Override
	public void stop() {
		System.out.println("STOPPING");
	}	
	
} 