package com.utils.gui.objects;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.log.progress.ProgressIndicator;
import com.utils.string.StrUtils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;

public class ProgressIndicatorBar extends AbstractCustomControl<StackPane> implements ProgressIndicator {

	private final ProgressBar progressBar;
	private final Label progressBarLabel;

	public ProgressIndicatorBar() {

		progressBar = new ProgressBar();
		progressBar.setPrefHeight(24);
		progressBar.setMinHeight(24);
		progressBar.setPrefWidth(Double.MAX_VALUE);
		progressBar.setProgress(0);

		progressBarLabel = BasicControlsFactories.getInstance()
				.createLabel("", "label-progress-indicator-bar");
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPane =
				LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER);

		stackPane.getStylesheets().add("com/utils/gui/objects/progress_indicator_bar.css");

		stackPane.setPrefWidth(150);
		stackPane.setPrefHeight(24);
		stackPane.setMinHeight(24);

		stackPane.getChildren().setAll(progressBar, progressBarLabel);

		return stackPane;
	}

	@Override
	public void update(
			final double value) {

		GuiUtils.run(() -> {

			final double progress;
			final String progressString;
			if (value <= 0 || Double.isNaN(value)) {

				progress = 0;
				progressString = "";

			} else {
				progress = value;
				progressString = StrUtils.doubleToPercentageString(value, 2);
			}

			progressBar.setProgress(progress);
			progressBarLabel.setText(progressString);
		});
	}
}
