package com.utils.gui.factories;

import com.utils.annotations.ApiMethod;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;

public final class LayoutControlsFactoryImpl implements LayoutControlsFactory {

	@Override
	@ApiMethod
	public Pane createPane() {
		return createPane(true);
	}

	@Override
	@ApiMethod
	public Pane createPane(
			final boolean emptyBackground) {

		final Pane pane = new Pane();
		pane.setBackground(Background.EMPTY);
		return pane;
	}

	@Override
	@ApiMethod
	public HBox createHBox() {
		return createHBox(true);
	}

	@Override
	@ApiMethod
	public HBox createHBox(
			final boolean emptyBackground) {

		final HBox hBox = new HBox();
		if (emptyBackground) {
			hBox.setBackground(Background.EMPTY);
		}
		return hBox;
	}

	@Override
	@ApiMethod
	public VBox createVBox() {
		return createVBox(true);
	}

	@Override
	@ApiMethod
	public VBox createVBox(
			final boolean emptyBackground) {

		final VBox vBox = new VBox();
		if (emptyBackground) {
			vBox.setBackground(Background.EMPTY);
		}
		return vBox;
	}

	@Override
	@ApiMethod
	public GridPane createGridPane() {
		return createGridPane(true);
	}

	@Override
	@ApiMethod
	public GridPane createGridPane(
			final boolean emptyBackground) {

		final GridPane gridPane = new GridPane();
		if (emptyBackground) {
			gridPane.setBackground(Background.EMPTY);
		}
		return gridPane;
	}

	/**
	 * @param ratio
	 *            a double value between 0 and 1
	 * @return a ColumnConstraints object to be added to a GridPane
	 */
	@Override
	@ApiMethod
	public ColumnConstraints createPercentageColumnConstraints(
			final double ratio) {

		final ColumnConstraints columnConstraints = new ColumnConstraints();
		final double percent = ratio * 100.0;
		columnConstraints.setPercentWidth(percent);
		return columnConstraints;
	}

	/**
	 * @param ratio
	 *            a double value between 0 and 1
	 * @return a ColumnConstraints object to be added to a GridPane
	 */
	@Override
	@ApiMethod
	public RowConstraints createPercentageRowConstraints(
			final double ratio) {

		final RowConstraints rowConstraints = new RowConstraints();
		final double percent = ratio * 100.0;
		rowConstraints.setPercentHeight(percent);
		return rowConstraints;
	}

	@Override
	@ApiMethod
	public StackPane createStackPane(
			final Pos pos,
			final Node... nodes) {

		final StackPane stackPane = new StackPane(nodes);
		stackPane.setBackground(Background.EMPTY);
		stackPane.setAlignment(pos);
		return stackPane;
	}

	@Override
	@ApiMethod
	public FlowPane createFlowPane() {

		return createFlowPane(true);
	}

	@Override
	@ApiMethod
	public FlowPane createFlowPane(
			final boolean emptyBackground) {

		final FlowPane flowPane = new FlowPane();
		if (emptyBackground) {
			flowPane.setBackground(Background.EMPTY);
		}
		return flowPane;
	}

	@Override
	@ApiMethod
	public TilePane createTilePane() {
		return createTilePane(true);
	}

	@Override
	@ApiMethod
	public TilePane createTilePane(
			final boolean emptyBackground) {

		final TilePane tilePane = new TilePane();
		if (emptyBackground) {
			tilePane.setBackground(Background.EMPTY);
		}
		return tilePane;
	}

	@Override
	@ApiMethod
	public ScrollPane createScrollPane(
			final Node content) {

		final ScrollPane scrollPane = new ScrollPane();
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		if (content != null) {
			scrollPane.setContent(content);
		}
		return scrollPane;
	}

	@Override
	@ApiMethod
	public TitledPane createTitledPane(
			final String title,
			final String... styleClassElements) {

		final TitledPane titledPane = new TitledPane();
		if (styleClassElements != null) {
			titledPane.getStyleClass().addAll(styleClassElements);
		}
		titledPane.setText(title);
		return titledPane;
	}

	@Override
	@ApiMethod
	public TabPane createTabPaneNoHeaders() {

		final TabPane tabPane = createTabPane();
		tabPane.getStylesheets().add("com/utils/gui/factories/tab_pane_no_headers.css");
		tabPane.getStyleClass().add("tab-pane-no-headers");
		return tabPane;
	}

	@Override
	@ApiMethod
	public TabPane createTabPane() {

		final TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		return tabPane;
	}
}
