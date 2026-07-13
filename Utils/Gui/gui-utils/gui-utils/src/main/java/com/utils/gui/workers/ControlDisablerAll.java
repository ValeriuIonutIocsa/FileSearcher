package com.utils.gui.workers;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class ControlDisablerAll implements ControlDisabler {

	@Override
	public void setControlsDisabled(
			final boolean b) {

		final Scene scene = AbstractGuiWorker.computeScene();
		if (scene != null) {

			final Parent root = scene.getRoot();
			for (final Node node : root.getChildrenUnmodifiable()) {
				node.setDisable(b);
			}
		}
	}
}
