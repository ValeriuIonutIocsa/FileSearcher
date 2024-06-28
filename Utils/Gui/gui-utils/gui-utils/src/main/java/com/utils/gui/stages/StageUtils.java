package com.utils.gui.stages;

import com.utils.annotations.ApiMethod;
import com.utils.gui.screens.ScreenUtils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class StageUtils {

	private StageUtils() {
	}

	@ApiMethod
	public static void centerOnScreen(
			final Stage primaryStage) {

		final Screen widestScreen = ScreenUtils.computeWidestScreen();
		centerOnScreen(primaryStage, widestScreen);
	}

	@ApiMethod
	public static void centerOnScreen(
			final Stage primaryStage,
			final Screen screen) {

		final Rectangle2D screenBounds = screen.getBounds();
		primaryStage.setX(screenBounds.getMinX() +
				(screenBounds.getWidth() - primaryStage.getWidth()) / 2);
		primaryStage.setY(screenBounds.getMinY() +
				(screenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}
}
