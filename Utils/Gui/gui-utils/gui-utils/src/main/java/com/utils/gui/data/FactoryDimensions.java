package com.utils.gui.data;

import com.utils.annotations.ApiMethod;
import com.utils.gui.screens.ScreenUtils;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;

public final class FactoryDimensions {

	private FactoryDimensions() {
	}

	@ApiMethod
	public static Dimensions newInstanceRelativeToScreenSize(
			final Scene scene,
			final double prefWidthRatio,
			final double prefHeightRatio) {

		final double screenWidth;
		final double screenHeight;
		final Screen screen = ScreenUtils.computeScreen(scene);
		if (screen != null) {

			final Rectangle2D screenBounds = screen.getBounds();
			screenWidth = screenBounds.getWidth();
			screenHeight = screenBounds.getHeight();

		} else {
			screenWidth = 1_920;
			screenHeight = 1_080;
		}
		final double prefWidth = screenWidth * prefWidthRatio;
		final double prefHeight = screenHeight * prefHeightRatio;

		return new Dimensions(300, 200, -1, -1, prefWidth, prefHeight);
	}
}
