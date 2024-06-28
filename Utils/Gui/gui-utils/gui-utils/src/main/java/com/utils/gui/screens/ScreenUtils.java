package com.utils.gui.screens;

import java.util.List;

import com.utils.annotations.ApiMethod;

import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Window;

public final class ScreenUtils {

	private ScreenUtils() {
	}

	@ApiMethod
	public static Screen computeScreen(
			final Scene scene) {

		final Window window = scene.getWindow();
		final double x = window.getX();
		final double y = window.getY();
		final double width = window.getWidth();
		final double height = window.getHeight();
		return computeScreen(x, y, width, height);
	}

	@ApiMethod
	public static Screen computeScreen(
			final double x,
			final double y,
			final double width,
			final double height) {

		final Screen screen;
		final List<Screen> screenList = Screen.getScreensForRectangle(
				x + width / 2, y + height / 2, 1, 1);
		if (!screenList.isEmpty()) {
			screen = screenList.getFirst();
		} else {
			screen = computeWidestScreen();
		}
		return screen;
	}

	@ApiMethod
	public static Screen computeWidestScreen() {

		Screen widestScreen = Screen.getPrimary();
		double maxScreenWidth = widestScreen.getVisualBounds().getWidth();

		final List<Screen> screenList = Screen.getScreens();
		for (final Screen screen : screenList) {

			final double screenWidth = screen.getVisualBounds().getWidth();
			if (screenWidth > maxScreenWidth) {

				widestScreen = screen;
				maxScreenWidth = screenWidth;
			}
		}
		return widestScreen;
	}
}
