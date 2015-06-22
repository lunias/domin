package com.domin.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPaneBuilder;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPaneBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.TilePaneBuilder;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.domin.card.Card;
import com.domin.card.CardSet;
import com.domin.ui.util.CardManager;
import com.domin.ui.util.StageManager;
import com.domin.ui.util.StageType;

public class CardLibraryView extends Stage {
	
	private Map<CardSet, Node> tabNodeMap;
	
	private IntegerProperty numColumnsProperty;
	private BooleanProperty magnifyVisibleProperty;
	private ObjectProperty<Image> magnifyImageProperty;
	
	private static final int CARD_WIDTH = 190;

	public CardLibraryView() {
		
		this.setTitle("Card Library");
		
		this.numColumnsProperty = new SimpleIntegerProperty();
		this.magnifyVisibleProperty = new SimpleBooleanProperty();
		this.magnifyImageProperty = new SimpleObjectProperty<Image>();
		
		this.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				numColumnsProperty.set(newValue.intValue() / CARD_WIDTH);
			}
			
		});		
		
		this.setWidth(StageManager.INSTANCE.getSceneWidth());
		this.setHeight(StageManager.INSTANCE.getSceneHeight());
		
		this.setMinWidth(800);
		this.setMinHeight(600);
		
		Stage primaryStage = StageManager.INSTANCE.getStage();
		
		double xLoc = primaryStage.getX() + (primaryStage.getWidth() / 2) - this.getWidth() / 2;
		double yLoc = primaryStage.getY() + (primaryStage.getHeight() / 2) - this.getHeight() / 2;
		this.setX(xLoc);
		this.setY(yLoc);
		
		this.tabNodeMap = new HashMap<CardSet, Node>();
		
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				StageManager.INSTANCE.close(StageType.CardLibraryView);
			}
			
		});		
		
		this.setScene(createScene());
		
	}
	
	public Scene createScene() {
		
		TabPane tabPane;
		ImageView magnifyLayer;
		
		StackPane stackPane = StackPaneBuilder.create()
				.children(tabPane = TabPaneBuilder.create()
				          .build(),
						  magnifyLayer = ImageViewBuilder.create()
						  .visible(false)						  
						  .build()
						  )	
				.build();	
		
		for (CardSet set : CardSet.values()) {
			List<Card> cardList = CardManager.INSTANCE.getCardsInSet(set);
			
			Node node;			
			tabPane.getTabs().add(TabBuilder.create()
					.closable(false)
					.text(set.toString())
					.content(node = createCardImageNode(cardList))
					.build());
			tabNodeMap.put(set, node);
		}
		
		magnifyLayer.imageProperty().bind(magnifyImageProperty);
		magnifyLayer.visibleProperty().bind(magnifyVisibleProperty);
		
		return new Scene(stackPane);
		
	}
	
	public Node createCardImageNode(List<Card> cardList) {
		
		final TilePane cardPane;
		
		ScrollPane tabNode = ScrollPaneBuilder.create()
				.content(cardPane = TilePaneBuilder.create()
						.hgap(5)
						.vgap(2)
						.build()
						)
				.build();
		
		for (final Card card : cardList) {
			final ImageView cardImageView;
			
			cardPane.getChildren().add(cardImageView = ImageViewBuilder.create()
													   .image(card.getImage())
													   .preserveRatio(true)
													   .smooth(true)
													   .fitWidth(CARD_WIDTH)
													   .cursor(Cursor.HAND)
										               .build()
					);
			
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
		}
		
		cardPane.prefColumnsProperty().bind(this.numColumnsProperty);
		
		return tabNode;
		
	}
	
}
