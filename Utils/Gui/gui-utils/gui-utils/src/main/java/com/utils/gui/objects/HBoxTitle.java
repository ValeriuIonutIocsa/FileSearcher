package com.utils.gui.objects;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class HBoxTitle extends AbstractCustomControl<HBox> {

	private final String title;
	private final String titleLabelId;
	private final double titleMinWidth;

	public HBoxTitle(
			final String title,
			final String titleLabelId,
			final double titleMinWidth) {

		this.title = title;
		this.titleLabelId = titleLabelId;
		this.titleMinWidth = titleMinWidth;
	}

	@Override
	protected HBox createRoot() {

		final HBox hBoxRoot = LayoutControlsFactories.getInstance().createHBox();

		final Label labelLocal = BasicControlsFactories.getInstance().createLabel(title, titleLabelId);
		labelLocal.setMinWidth(titleMinWidth);
		GuiUtils.addToHBox(hBoxRoot, labelLocal,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 7);

		final Separator separator = BasicControlsFactories.getInstance().createSeparator(Orientation.VERTICAL);
		GuiUtils.addToHBox(hBoxRoot, separator,
				Pos.CENTER, Priority.NEVER, 0, 3, 0, 5);

		return hBoxRoot;
	}
}
