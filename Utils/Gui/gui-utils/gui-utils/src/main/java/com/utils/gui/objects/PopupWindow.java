package com.utils.gui.objects;

import com.utils.gui.data.Dimensions;
import com.utils.gui.screens.ScreenUtils;
import com.utils.gui.stages.StageUtils;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PopupWindow extends Stage {

	public PopupWindow(
			final Scene parentScene,
			final Screen parentScreen,
			final Modality modality,
			final String title,
			final Dimensions dimensions,
			final Parent parent) {

		getIcons().addAll(((Stage) parentScene.getWindow()).getIcons());
		setTitle(title);

		if (dimensions != null) {
			dimensions.set(this);
		} else {
			sizeToScene();
		}

		initModality(modality);
		if (modality == Modality.NONE) {
			setAlwaysOnTop(true);
		}

		final Scene scene = new Scene(parent);
		scene.getStylesheets().addAll(parentScene.getStylesheets());

		setOnShown(event -> {

			final Screen screen;
			if (parentScreen != null) {
				screen = parentScreen;
			} else {
				screen = ScreenUtils.computeScreen(parentScene);
			}
			if (screen != null) {
				StageUtils.centerOnScreen(this, screen);
			} else {
				StageUtils.centerOnScreen(this);
			}

			if (dimensions == null) {

				setMinHeight(100);
				setMinWidth(200);
			}
			scene.getRoot().requestFocus();
		});
		setScene(scene);
	}
}
