package com.utils.gui.workers;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ControlDisablerAll implements ControlDisabler {

	private final Scene scene;

	public ControlDisablerAll(
			final Scene scene) {

		this.scene = scene;
	}

	@Override
	public void setControlsDisabled(
			final boolean b) {

		if (scene != null) {

			final Parent root = scene.getRoot();
			for (final Node node : root.getChildrenUnmodifiable()) {
				node.setDisable(b);
			}
		}
	}
}
