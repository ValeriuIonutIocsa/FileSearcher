package com.utils.gui.factories;

import com.utils.annotations.ApiMethod;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public interface LayoutControlsFactory {

	@ApiMethod
	Pane createPane();

	@ApiMethod
	Pane createPane(
			boolean emptyBackground);

	@ApiMethod
	HBox createHBox();

	@ApiMethod
	HBox createHBox(
			boolean emptyBackground);

	@ApiMethod
	VBox createVBox();

	@ApiMethod
	VBox createVBox(
			boolean emptyBackground);

	@ApiMethod
	GridPane createGridPane();

	@ApiMethod
	GridPane createGridPane(
			boolean emptyBackground);

	@ApiMethod
	ColumnConstraints createPercentageColumnConstraints(
			double ratio);

	@ApiMethod
	RowConstraints createPercentageRowConstraints(
			double ratio);

	@ApiMethod
	StackPane createStackPane(
			Pos pos,
			Node... nodes);

	@ApiMethod
	FlowPane createFlowPane();

	@ApiMethod
	FlowPane createFlowPane(
			boolean emptyBackground);

	@ApiMethod
	TilePane createTilePane();

	@ApiMethod
	TilePane createTilePane(
			boolean emptyBackground);

	@ApiMethod
	ScrollPane createScrollPane(
			Node content);

	@ApiMethod
	TitledPane createTitledPane(
			String title,
			String... styleClassElements);

	@ApiMethod
	TabPane createTabPaneNoHeaders();

	@ApiMethod
	TabPane createTabPane();
}
