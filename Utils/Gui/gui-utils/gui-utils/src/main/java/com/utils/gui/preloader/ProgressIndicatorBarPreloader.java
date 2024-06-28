package com.utils.gui.preloader;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

class ProgressIndicatorBarPreloader extends AbstractCustomControl<StackPane> {

	ProgressIndicatorBarPreloader() {
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPane = LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER);

		final ProgressBar progressBar = new ProgressBar(-1);
		progressBar.setMinHeight(20);
		progressBar.setPrefWidth(Double.MAX_VALUE);
		progressBar.getStyleClass().add("green-accent");
		GuiUtils.addToStackPane(stackPane, progressBar, Pos.CENTER, 0, 0, 0, 0);

		final Label label = BasicControlsFactories.getInstance().createLabel("loading...", "bold");
		GuiUtils.addToStackPane(stackPane, label, Pos.CENTER, 0, 0, 0, 0);

		return stackPane;
	}
}
