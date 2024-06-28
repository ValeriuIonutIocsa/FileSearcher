package com.utils.gui.help;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.icons.ImagesGuiUtils;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class StackPaneHelp extends AbstractCustomControl<StackPane> {

	private final EventHandler<? super MouseEvent> eventHandler;

	public StackPaneHelp(
			final EventHandler<? super MouseEvent> eventHandler) {

		this.eventHandler = eventHandler;
	}

	@Override
	protected StackPane createRoot() {

		final StackPane stackPaneRoot =
				LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER_LEFT);
		stackPaneRoot.getStylesheets().add("com/utils/gui/help/stack_pane_help.css");
		stackPaneRoot.getStyleClass().add("hoverOn");
		stackPaneRoot.setMaxWidth(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMaxHeight(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMinWidth(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setMinHeight(Double.NEGATIVE_INFINITY);
		stackPaneRoot.setPrefWidth(-1);
		stackPaneRoot.setPrefHeight(-1);

		final Image imageHelp = ImagesGuiUtils.IMAGE_HELP;
		final ImageView imageViewHelp = new ImageView(imageHelp);
		GuiUtils.addToStackPane(stackPaneRoot, imageViewHelp,
				Pos.CENTER_LEFT, 3, 3, 3, 3);

		stackPaneRoot.setOnMouseClicked(eventHandler);

		return stackPaneRoot;
	}
}
