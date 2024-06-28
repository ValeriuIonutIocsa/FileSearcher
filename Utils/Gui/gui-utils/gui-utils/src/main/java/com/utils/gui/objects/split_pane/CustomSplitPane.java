package com.utils.gui.objects.split_pane;

import java.util.Arrays;

import com.utils.gui.GuiUtils;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;

public class CustomSplitPane extends SplitPane {

	private boolean rendered;
	private double[] positions;

	public CustomSplitPane(
			final Orientation orientation) {

		setOrientation(orientation);
	}

	@Override
	protected void layoutChildren() {

		if (!rendered) {
			if (positions != null) {
				super.setDividerPositions(positions);
			}
		}

		super.layoutChildren();

		if (!rendered) {
			if (positions == null) {
				final double[] dividerPositions = getDividerPositions();
				positions = Arrays.copyOf(dividerPositions, dividerPositions.length);
			}
			super.setDividerPositions(positions);

			int dividerIndex = 0;
			for (final Node node : super.getChildrenUnmodifiable()) {

				final Class<? extends Node> nodeClass = node.getClass();
				final String nodeClassSimpleName = nodeClass.getSimpleName();
				if ("ContentDivider".equals(nodeClassSimpleName)) {

					final int finalDividerIndex = dividerIndex;
					node.setOnMouseClicked(mouseEvent -> dividerMouseClicked(mouseEvent, finalDividerIndex));
					dividerIndex++;
				}
			}
		}

		rendered = true;
	}

	private void dividerMouseClicked(
			final MouseEvent mouseEvent,
			final int dividerIndex) {

		final boolean doubleClick = mouseEvent.getClickCount() >= 2;
		if (doubleClick) {

			final boolean leftClick = GuiUtils.isLeftClick(mouseEvent);
			final boolean rightClick = GuiUtils.isRightClick(mouseEvent);
			if (leftClick || rightClick) {

				final double[] dividerPositions = getDividerPositions();
				final double dividerPosition = dividerPositions[dividerIndex];
				double position = positions[dividerIndex];
				if (Math.abs(dividerPosition - position) < 0.001) {

					if (leftClick) {
						position = 0;
					} else {
						position = 1;
					}
				}
				setDividerPosition(dividerIndex, position);
			}
		}
	}

	@Override
	public void setDividerPositions(
			final double... positions) {

		super.setDividerPositions(positions);
		this.positions = positions;
	}

	public static void splitEvenly(
			final SplitPane splitPane) {

		final int itemCount = splitPane.getItems().size();
		final double[] dividerPositions = new double[itemCount - 1];
		for (int i = 1; i < itemCount; i++) {
			dividerPositions[i - 1] = (double) i / itemCount;
		}
		splitPane.setDividerPositions(dividerPositions);
	}
}
