package com.utils.gui.charts.division;

import java.util.ArrayList;
import java.util.List;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.charts.division.data.PercentageDivisionElement;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.string.StrUtils;

import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class VBoxPercentageDivision extends AbstractCustomControl<VBox> {

	private final List<PercentageDivisionElement> elements;

	public VBoxPercentageDivision(
			final List<PercentageDivisionElement> elements) {

		this.elements = elements;
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactories.getInstance().createVBox();

		final List<Node> colorNodes = new ArrayList<>();
		final GridPane gridPaneColors = createGridPaneColors(colorNodes);
		GuiUtils.addToVBox(vBoxRoot, gridPaneColors,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		final Pane pane = new Pane();
		pane.setPrefHeight(36);
		GuiUtils.addToVBox(vBoxRoot, pane,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		final List<Node> descriptionNodes = new ArrayList<>();
		final HBox hBoxDescription = createHBoxDescription(descriptionNodes);
		GuiUtils.addToVBox(vBoxRoot, hBoxDescription,
				Pos.CENTER_LEFT, Priority.NEVER, 20, 0, 0, 0);

		final int elementCount = elements.size();
		for (int i = 0; i < elementCount; i++) {

			final Node colorNode = colorNodes.get(i);
			final Node descriptionNode = descriptionNodes.get(i);

			final Line line = new Line();
			line.setStrokeWidth(1.0);
			line.setStroke(Color.BLACK);
			line.setStrokeLineCap(StrokeLineCap.BUTT);
			line.getStrokeDashArray().setAll(3.0, 3.0);

			line.startXProperty().bind(Bindings.createDoubleBinding(() -> {
				final Bounds bounds = pane.sceneToLocal(
						colorNode.localToScene(colorNode.getBoundsInLocal()));
				return bounds.getMinX() + bounds.getWidth() / 2;
			}, colorNode.boundsInLocalProperty(), descriptionNode.boundsInLocalProperty()));
			line.startYProperty().bind(Bindings.createDoubleBinding(() -> {
				final Bounds bounds = pane.sceneToLocal(
						colorNode.localToScene(colorNode.getBoundsInLocal()));
				return bounds.getMaxY() + 3;
			}, colorNode.boundsInLocalProperty(), descriptionNode.boundsInLocalProperty()));

			line.endXProperty().bind(Bindings.createDoubleBinding(() -> {
				final Bounds bounds = pane.sceneToLocal(
						descriptionNode.localToScene(descriptionNode.getBoundsInLocal()));
				return bounds.getMinX() + bounds.getWidth() / 2;
			}, colorNode.boundsInLocalProperty(), descriptionNode.boundsInLocalProperty()));
			line.endYProperty().bind(Bindings.createDoubleBinding(() -> {
				final Bounds bounds = pane.sceneToLocal(
						descriptionNode.localToScene(descriptionNode.getBoundsInLocal()));
				return bounds.getMinY() - 3;
			}, colorNode.boundsInLocalProperty(), descriptionNode.boundsInLocalProperty()));

			pane.getChildren().add(line);
		}

		return vBoxRoot;
	}

	private GridPane createGridPaneColors(
			final List<Node> colorNodes) {

		final GridPane gridPaneColors = LayoutControlsFactories.getInstance().createGridPane();
		final int elementCount = elements.size();
		for (int i = 0; i < elementCount; i++) {

			final PercentageDivisionElement element = elements.get(i);
			final double percentage = element.percentage();
			final ColumnConstraints percentageConstraints =
					LayoutControlsFactories.getInstance().createPercentageColumnConstraints(percentage);
			gridPaneColors.getColumnConstraints().add(percentageConstraints);

			final Region region = new Region();
			region.setMinHeight(25);
			final Color color = element.color();
			region.setBackground(new Background(new BackgroundFill(color, null, null)));
			GuiUtils.addToGridPane(gridPaneColors, region, i, 0, 1, 1,
					Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 0, 0, 0, 0);
			colorNodes.add(region);
		}

		return gridPaneColors;
	}

	private HBox createHBoxDescription(
			final List<Node> descriptionNodes) {

		final HBox hBoxDescription = LayoutControlsFactories.getInstance().createHBox();

		for (final PercentageDivisionElement element : elements) {

			final String name = element.name();
			final double percentage = element.percentage();
			final String text = name + System.lineSeparator() +
					StrUtils.doubleToPercentageString(percentage, 2);
			final Label labelDescription = BasicControlsFactories.getInstance().createLabel(text, "fontSize10");
			GuiUtils.addToHBox(hBoxDescription, labelDescription,
					Pos.CENTER, Priority.ALWAYS, 0, 0, 0, 0);
			descriptionNodes.add(labelDescription);
		}

		return hBoxDescription;
	}
}
