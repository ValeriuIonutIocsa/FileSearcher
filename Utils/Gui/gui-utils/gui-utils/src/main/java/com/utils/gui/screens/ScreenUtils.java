package com.utils.gui.screens;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.utils.annotations.ApiMethod;

import javafx.geometry.Rectangle2D;
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

		final Rectangle2D bounds = new Rectangle2D(x, y, width, height);
		final List<Screen> screenList = new ArrayList<>(Screen.getScreens());
		screenList.sort(Comparator.comparing(
				aScreen -> computeIntersectionArea(aScreen.getBounds(), bounds), Comparator.reverseOrder()));
		return screenList.getFirst();
	}

	private static double computeIntersectionArea(
			final Rectangle2D a,
			final Rectangle2D b) {

		double intersectionArea = 0;
		final double x = Math.max(a.getMinX(), b.getMinX());
		final double y = Math.max(a.getMinY(), b.getMinY());
		final double w = Math.min(a.getMaxX(), b.getMaxX()) - x;
		final double h = Math.min(a.getMaxY(), b.getMaxY()) - y;
		if (w > 0 && h > 0) {
			intersectionArea = w * h;
		}
		return intersectionArea;
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
