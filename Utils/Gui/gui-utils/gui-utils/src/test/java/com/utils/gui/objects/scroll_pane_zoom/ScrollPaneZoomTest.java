package com.utils.gui.objects.scroll_pane_zoom;

import org.junit.jupiter.api.Test;

import com.utils.concurrency.ThreadUtils;
import com.utils.gui.AbstractCustomApplicationTest;
import com.utils.gui.GuiUtils;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineJoin;

class ScrollPaneZoomTest extends AbstractCustomApplicationTest {

	@Test
	void testLayout() {

		final StackPane zoomTarget = new StackPane(createStar(), createCurve());
		zoomTarget.setPrefHeight(256);
		zoomTarget.setPrefWidth(256);

		final GridPane gridPaneRoot = new GridPane();

		final ScrollPaneZoom scrollPaneZoom =
				new ScrollPaneZoom(zoomTarget, true, true, true, true);
		scrollPaneZoom.getRoot().setPrefViewportWidth(256);
		scrollPaneZoom.getRoot().setPrefViewportHeight(256);
		GuiUtils.addToGridPane(gridPaneRoot, scrollPaneZoom.getRoot(), 0, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.NEVER, 50, 50, 50, 50);

		GuiUtils.run(() -> {

			final StackPane stackPaneContainer = getStackPaneContainer();
			stackPaneContainer.getChildren().add(gridPaneRoot);
		});
		ThreadUtils.trySleep(Long.MAX_VALUE);
	}

	private static SVGPath createCurve() {

		final SVGPath ellipticalArc = new SVGPath();
		ellipticalArc.setContent("M10,150 A15 15 180 0 1 70 140 A15 25 180 0 0 130 130 A15 55 180 0 1 190 120");
		ellipticalArc.setStroke(Color.LIGHTGREEN);
		ellipticalArc.setStrokeWidth(4);
		ellipticalArc.setFill(null);
		return ellipticalArc;
	}

	private static SVGPath createStar() {

		final SVGPath star = new SVGPath();
		star.setContent("M100,10 L100,10 40,180 190,60 10,60 160,180 z");
		star.setStrokeLineJoin(StrokeLineJoin.ROUND);
		star.setStroke(Color.BLUE);
		star.setFill(Color.DARKBLUE);
		star.setStrokeWidth(4);
		return star;
	}
}
