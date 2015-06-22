package com.domin.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ProgressBarBuilder;
import javafx.scene.control.SeparatorBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.TabBuilder;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPaneBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import com.domin.card.Card;
import com.domin.card.CardType;
import com.domin.net.DominClient;
import com.domin.net.VictoryPacket;
import com.domin.ui.util.StageManager;

public class VictoryTotalView extends Stage {

	private List<VictoryPacket> victoryPacketList;
	private DominClient connection;
	
	private boolean detailsPopulated;
	private boolean velocityPopulated;	
	
	private static final int CARD_WIDTH = 70;

	public VictoryTotalView(List<VictoryPacket> victoryPacketList,
			DominClient connection) {
		
		this.victoryPacketList = victoryPacketList;
		this.connection = connection;

		this.detailsPopulated = false;
		this.velocityPopulated = false;
		
		Collections.sort(victoryPacketList);
		
		this.setResizable(false);

		this.setWidth(950);
		this.setHeight(550);

		Stage primaryStage = StageManager.INSTANCE.getStage();
		double primaryWidth = primaryStage.getWidth();
		double primaryHeight = primaryStage.getHeight();

		this.setX(primaryStage.getX() + (primaryWidth - this.getWidth()) / 2);
		this.setY(primaryStage.getY() + (primaryHeight - this.getHeight()) / 2);

		this.initModality(Modality.APPLICATION_MODAL);
		this.initStyle(StageStyle.UTILITY);

		this.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent event) {
				event.consume();
			}

		});

	}

	public int showDialog() {
		setTitle("Victory Totals");

		this.setScene(createScene());

		this.showAndWait();

		return 1;
	}

	private Scene createScene() {

		GridPane victoryGrid;
		Text winnerMessage;
		ImageView magnifyLayer;
		TabPane tabPane;				
		
		StackPane stackPane = StackPaneBuilder.create()
				.children(tabPane = TabPaneBuilder.create()
									.build(),
						  magnifyLayer = ImageViewBuilder.create()
						  			.visible(false)
						  			.build()
				)
		.build();
		
		VBox basicNode = VBoxBuilder
				.create()
				.padding(new Insets(10, 10, 10, 10))
				.spacing(10)
				.alignment(Pos.CENTER)
				.children(winnerMessage = TextBuilder.create()
				            .font(Font.font(null, FontWeight.BOLD, 36))
							.id("winner-message")
						    .build(),
						  victoryGrid = GridPaneBuilder.create()
						    .vgap(20)
						    .hgap(10)
						    .padding(new Insets(10, 10, 10, 10))
						    .alignment(Pos.CENTER)
							.build(),
						  createDoneButton()
						 )
				.build();
		
		final GridPane detailGrid;
		
		VBox detailedNode = VBoxBuilder
				.create()
				.padding(new Insets(10, 10, 10, 10))
				.spacing(10)
				.alignment(Pos.CENTER)
				.children(detailGrid = GridPaneBuilder.create()
						      .vgap(10)
						      .hgap(10)
						      .padding(new Insets(10, 10, 10, 10))
						      .alignment(Pos.CENTER)
						      .build(),
						  createDoneButton()
						)
				.build();					
		
		final VBox velocityNode = VBoxBuilder
				.create()
				.padding(new Insets(10, 10, 10, 10))
				.spacing(10)
				.alignment(Pos.CENTER)
				.build();
		
		tabPane.getTabs().add(TabBuilder.create()
								.closable(false)
								.text("Progress")
								.content(basicNode)
								.build());
		
		final Tab detailTab;
		tabPane.getTabs().add(detailTab = TabBuilder.create()
								.closable(false)
								.text("Table")
								.content(detailedNode)
								.build());
		
		detailTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (detailTab.isSelected()) {
					if (!detailsPopulated) {
						populateDetailsTab(detailGrid);
						detailsPopulated = true;
					}
				}
			}
			
		});
		
		final Tab velocityTab;
		tabPane.getTabs().add(velocityTab = TabBuilder.create()
								.closable(false)
								.text("Velocity")
								.content(velocityNode)
								.build());
		
		velocityTab.setOnSelectionChanged(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				if (velocityTab.isSelected()) {
					if (!velocityPopulated) {
						AreaChart<Number, Number> velocityChart;
						velocityNode.getChildren().add(velocityChart = createVelocityChart());
						velocityNode.getChildren().add(createDoneButton());
						VBox.setVgrow(velocityChart, Priority.ALWAYS);
						velocityPopulated = true;
					}
				}
			}
			
		});
		
		int winnerTotal = victoryPacketList.get(0).getVictoryTotal();
		List<String> winnerNameList = new ArrayList<String>();
		
		int row = 0;
		for (VictoryPacket vp : victoryPacketList) {
			
			String username = vp.getUsername();
			int victoryTotal = vp.getVictoryTotal();
			
			if (victoryTotal == winnerTotal) {
				winnerNameList.add(username);
			}
			
			victoryGrid.add(TextBuilder.create()
					           .text(username)
					           .font(Font.font(null, FontWeight.BOLD, 24))
					           .id("player-name")
							   .build()
					, 0, row);
			
			victoryGrid.add(ProgressBarBuilder.create()
							   .progress(victoryTotal / (double) winnerTotal)
							   .minHeight(30)
							   .minWidth(300)
							   .id("progress-bar")
							   .build()
					, 1, row);
			
			victoryGrid.add(TextBuilder.create()
					           .text(String.valueOf(victoryTotal))
					           .font(Font.font(null, FontWeight.BOLD, 24))
					           .id("player-total")
							   .build()
					, 2, row);			
			
			row++;			
			
		}		
		
		StringBuilder sb = new StringBuilder();
		if (winnerNameList.size() > 1) {
			for (String username : winnerNameList) {
				sb.append(username + " & ");
			}
			String winners = sb.toString();
			winnerMessage.setText(winners.substring(0, winners.length() - 3) + " Tie!");
		} else {
			winnerMessage.setText(winnerNameList.get(0) + " Wins!");
		}		

		VBox.setVgrow(victoryGrid, Priority.ALWAYS);
		VBox.setVgrow(detailGrid, Priority.ALWAYS);
		
		Scene scene = new Scene(stackPane);
		scene.getStylesheets().add("css/victoryview.css");
		
		return scene;

	}
	
	private void populateDetailsTab(GridPane detailGrid) {		
		
		List<Card> victoryCardsInGame = new ArrayList<Card>();
		for (Card card : DominGameScreen.supplyCardMap.keySet()) {
			if (card.isOfType(CardType.VICTORY) || "Curse".equals(card.getName())) {
				victoryCardsInGame.add(card);
			}
		}
		
		Collections.sort(victoryCardsInGame);
		
		ColumnConstraints columnAlignConstraint = new ColumnConstraints();
		columnAlignConstraint.setHalignment(HPos.LEFT);
		detailGrid.getColumnConstraints().add(columnAlignConstraint);
		
		RowConstraints rowAlignConstraint = new RowConstraints();
		rowAlignConstraint.setValignment(VPos.BOTTOM);
		detailGrid.getRowConstraints().add(rowAlignConstraint);
		
		// header {blank, victory images, total label}
		columnAlignConstraint.setHalignment(HPos.CENTER);
		int row = 0;
		int col = 1;		
		for (Card victoryCard : victoryCardsInGame) {			
			detailGrid.getColumnConstraints().add(columnAlignConstraint);			
			detailGrid.add(createMiniCardView(victoryCard), col++, row);
		}
		
		detailGrid.getColumnConstraints().add(columnAlignConstraint);
		
		detailGrid.add(TextBuilder.create()						
						.text("Point Totals")
						.id("text-label")
						.build()
				, col, row);
		
		// player rows {username, num of card, point total}
		int[] cardTotals = new int[victoryCardsInGame.size()];
		
		int sumVictoryTotals = 0;
		row = 1;
		for (VictoryPacket vp : victoryPacketList) {
		
			col = 0;
			String username = vp.getUsername();
			int victoryTotal = vp.getVictoryTotal();
			Map<Card, Integer> cardMap = vp.getVictoryCardMap();
			
			Text countText;
			
			detailGrid.add(TextBuilder.create()
							.text(username)
							.id("text-label")
							.build()
					, col, row);
			
			for (Card victoryCard : victoryCardsInGame) {
				
				int count = cardMap.containsKey(victoryCard) ? cardMap.get(victoryCard) : 0;				
				detailGrid.add(countText = TextBuilder.create()
								.text(String.valueOf(count))
								.id("number")
								.build()
						, ++col, row);
				
				cardTotals[col - 1] = cardTotals[col - 1] + count;
				
			}
			
			detailGrid.add(TextBuilder.create()							
							.text(String.valueOf(victoryTotal))
							.id("number")
							.build()
					, ++col, row++);
			
			sumVictoryTotals += victoryTotal;
		}
		
		// separator
		detailGrid.add(SeparatorBuilder.create()
						.orientation(Orientation.HORIZONTAL)
						.valignment(VPos.CENTER)
						.build()
					, 0, row++, victoryCardsInGame.size() + 2, 1);
		
		// card totals {total label, card totals, sum of card totals}
		int sumCardTotals = 0;
		col = 0;
		detailGrid.add(TextBuilder.create()
						.text("Card Totals")
						.id("text-label")
						.build()
				, col, row);	
		
		for (Integer cardTotal : cardTotals) {
			
			detailGrid.add(TextBuilder.create()
							.text(String.valueOf(cardTotal))
							.id("number")
							.build()
					, ++col, row);

			sumCardTotals += cardTotal;
		}
		
		detailGrid.add(TextBuilder.create()
						.text(sumCardTotals + " (" + sumVictoryTotals + ")")
						.id("number")
						.build()
				, ++col, row);
		
	}
	
	private Button createDoneButton() {
		return ButtonBuilder.create()
				.text("Done")
				.onMouseClicked(new EventHandler<MouseEvent>() {
					
					@Override
					public void handle(MouseEvent event) {
						if (event.getButton() == MouseButton.PRIMARY) {
							connection.disconnect();
							close();
							// TODO removeListeners();
							StageManager.INSTANCE
							.setScene(new DominConnectScreen());
						}
						
					}
				})
				.build();	
	}
	
	private ImageView createMiniCardView(Card card) {
		return ImageViewBuilder.create()
				.image(card.getImage())
				.preserveRatio(true)
				.smooth(true)
				.fitWidth(CARD_WIDTH)
				.cursor(Cursor.HAND)
				.build();
	}
	
	private AreaChart<Number, Number> createVelocityChart() {				
		
		final NumberAxis turnAxis = new NumberAxis();
		turnAxis.setLabel("Turn");
		turnAxis.setLowerBound(1);
		turnAxis.setTickUnit(1);
		
		final NumberAxis victoryAxis = new NumberAxis();
		victoryAxis.setLabel("Victory Total");
		victoryAxis.setTickUnit(5);
		
		final AreaChart<Number, Number> vvc = new AreaChart<Number, Number>(turnAxis, victoryAxis);				
		vvc.setTitle("Victory Velocity");				
		
		for (VictoryPacket vp : victoryPacketList) {
			
			Map<Integer, Integer> vvm = vp.getVictoryVelocityMap();
			
			Series<Number, Number> seriesPlayer = new XYChart.Series<Number, Number>();
			seriesPlayer.setName(vp.getUsername());
			
			for (Entry<Integer, Integer> entry : vvm.entrySet()) {
				
				int turn = entry.getKey();
				int victory = entry.getValue();
				
				seriesPlayer.getData().add(new XYChart.Data<Number, Number>(turn, victory));
				
			}
			
			vvc.getData().add(seriesPlayer);
			
		}		
				
		return vvc;
	}

}
