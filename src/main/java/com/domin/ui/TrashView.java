package com.domin.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.TilePaneBuilder;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.domin.card.Card;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;

public class TrashView extends Stage {
	
	private IntegerProperty numColumnsProperty;
	private BooleanProperty magnifyVisibleProperty;
	private ObjectProperty<Image> magnifyImageProperty;
	
	private ObservableList<Card> trashList;
	
	private static final int CARD_WIDTH = 190;
	
	private static TilePane cardPane;
	
	public TrashView(ObservableList<Card> trashList) {		
		
		this.setTitle("Trash");
		
		this.numColumnsProperty = new SimpleIntegerProperty();
		this.magnifyVisibleProperty = new SimpleBooleanProperty();
		this.magnifyImageProperty = new SimpleObjectProperty<Image>();
		
		this.trashList = trashList;
		
		this.widthProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				numColumnsProperty.set(newValue.intValue() / CARD_WIDTH);
			}
			
		});
		
		Stage primaryStage = StageManager.INSTANCE.getStage();
		double primaryWidth = primaryStage.getWidth();
		double primaryHeight = primaryStage.getHeight();
		
		this.setWidth(primaryWidth * .75);
		this.setHeight(primaryHeight * .6);
		
		this.setX(primaryStage.getX() + (primaryWidth - this.getWidth()) / 2);
		this.setY(primaryStage.getY() + (primaryHeight - this.getHeight()) / 2);
		
		this.initStyle(StageStyle.UTILITY);
		
		this.setOnCloseRequest(
				new EventHandler<WindowEvent>() {
					
					@Override
					public void handle(WindowEvent event) {
						StageManager.INSTANCE.close(StageType.TrashView);
					}
					
				});
		
		this.setScene(createScene());
		
		trashList.addListener(new ListChangeListener<Card>() {

			@Override
			public void onChanged(
					javafx.collections.ListChangeListener.Change<? extends Card> c) {
				
				List<ImageView> cardList = new ArrayList<ImageView>();
				for (Card card : c.getList()) {
					cardList.add(createCardImageView(card));
				}
				
				cardPane.getChildren().setAll(cardList);				
			}
			
		});
	}
	
	private Scene createScene() {
		
		ImageView magnifyLayer;
		
		StackPane stackPane = StackPaneBuilder.create()
				.children(createCardImageNode(),
						  magnifyLayer = ImageViewBuilder.create()
						  				 .visible(false)						  
						  				 .build()
						 )	
						.build();
		
		magnifyLayer.imageProperty().bind(magnifyImageProperty);
		magnifyLayer.visibleProperty().bind(magnifyVisibleProperty);
		
		return new Scene(stackPane);
	}
	
	private Node createCardImageNode() {		
		
		ScrollPane scrollNode = ScrollPaneBuilder.create()
									.content(cardPane = TilePaneBuilder.create()
									.hgap(5)
									.vgap(2)
									.build()
								)
								.build();
		
		for (final Card card : trashList) {
			
			cardPane.getChildren().add(createCardImageView(card));
			
		}
		
		cardPane.prefColumnsProperty().bind(this.numColumnsProperty);
		
		return scrollNode;				
	}
	
	private ImageView createCardImageView(final Card card) {
		
		ImageView cardImageView;
		
		cardImageView = ImageViewBuilder.create()
						.image(card.getImage())
						.preserveRatio(true)
						.smooth(true)
						.fitWidth(CARD_WIDTH)
						.cursor(Cursor.HAND)
						.build();
		
		cardImageView.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override				
			public void handle(MouseEvent event) {
				magnifyImageProperty.set(card.getImage());
				magnifyVisibleProperty.set(true);
			}
			
		});
					
		cardImageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent event) {
				magnifyVisibleProperty.set(false);
			}
			
		});
		
		return cardImageView;
	}

}
