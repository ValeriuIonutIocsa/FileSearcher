package com.utils.gui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;

import com.sun.javafx.application.PlatformImpl;
import com.utils.annotations.ApiMethod;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.version.VersionDependentMethods;
import com.utils.io.ResourceFileUtils;
import com.utils.log.Logger;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public final class GuiUtils {

	private static boolean platformStarted;

	private GuiUtils() {
	}

	@ApiMethod
	public static void startPlatform() {

		if (!platformStarted) {
			PlatformImpl.startup(() -> {
			});
			platformStarted = true;
		}
	}

	@ApiMethod
	public static void stopPlatform() {

		if (platformStarted) {
			PlatformImpl.exit();
		}
	}

	@ApiMethod
	public static void setAppIcon(
			final Stage stage,
			final Image image) {

		if (image != null) {

			stage.getIcons().clear();
			stage.getIcons().add(image);
		}
	}

	@ApiMethod
	public static Image createImageFromResourceFile(
			final String imageName) {

		Image image = null;
		try (InputStream inputStream = ResourceFileUtils.resourceFileToInputStream(imageName)) {

			if (inputStream == null) {
				Logger.printWarning("failed to find resource file " + imageName);
			} else {
				image = new Image(inputStream);
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to create image from resource file " + imageName);
			Logger.printThrowable(throwable);
		}
		return image;
	}

	@ApiMethod
	public static void setStyleCss(
			final Scene scene,
			final String... styleCssResourcePathStrings) {

		scene.getStylesheets().clear();
		scene.getStylesheets().addAll(styleCssResourcePathStrings);
	}

	@ApiMethod
	public static void setupCustomTooltipBehavior() {
		VersionDependentMethods.setupCustomTooltipBehavior();
	}

	@ApiMethod
	public static ListView<?> getComboBoxListView(
			final ComboBox<?> comboBox) {
		return VersionDependentMethods.getComboBoxListView(comboBox);
	}

	@ApiMethod
	public static String getFxBackgroundColorString(
			final Color color) {
		return "-fx-background-color: " + colorRbgFormat(color);
	}

	@ApiMethod
	public static String colorRbgFormat(
			final Color color) {
		return "rgba(" + (int) (255 * color.getRed()) + ", " + (int) (255 * color.getGreen()) + ", " +
				(int) (255 * color.getBlue()) + ", " + color.getOpacity() + ")";
	}

	@ApiMethod
	public static void setDefaultBorder(
			final Parent parent) {

		parent.getStylesheets().add("com/utils/gui/default_border.css");
		parent.getStyleClass().add("border-default");
	}

	@ApiMethod
	public static String computeTextInputControlPathString(
			final TextInputControl textInputControl) {

		final String path = textInputControl.getText();
		return computeTextInputControlPathString(path);
	}

	@ApiMethod
	public static String computeTextInputControlPathString(
			final String pathParam) {

		String path = pathParam;
		if (StringUtils.isNotBlank(path)) {

			if (path.startsWith("\"") && path.endsWith("\"")) {
				path = path.substring(1, path.length() - 1);
			}
			if (path.startsWith("\\") && !path.startsWith("\\\\")) {
				path = path.substring(1);
			}
			path = path.trim();
		}
		return path;
	}

	@ApiMethod
	public static IndexedCell<?> getControlIndexedCell(
			final Control control,
			final int index) {
		return VersionDependentMethods.getControlIndexedCell(control, index);
	}

	@ApiMethod
	public static void updateEmptyCell(
			final IndexedCell<?> indexedCell) {

		indexedCell.setText("");
		indexedCell.setGraphic(null);
		indexedCell.setContextMenu(null);
	}

	@ApiMethod
	public static void addToStackPane(
			final StackPane stackPane,
			final Node node,
			final Pos pos,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		if (node != null) {

			StackPane.setAlignment(node, pos);
			StackPane.setMargin(node, new Insets(top, right, bottom, left));
			stackPane.getChildren().add(node);
		}
	}

	@ApiMethod
	public static StackPane addToVBox(
			final VBox vBox,
			final Node node,
			final Pos pos,
			final Priority priority,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		StackPane stackPane = null;
		if (node != null) {

			stackPane = LayoutControlsFactories.getInstance().createStackPane(pos, node);
			vBox.getChildren().add(stackPane);
			VBox.setVgrow(stackPane, priority);
			stackPane.setPadding(new Insets(top, right, bottom, left));
		}
		return stackPane;
	}

	@ApiMethod
	public static StackPane addToHBox(
			final HBox hBox,
			final Node node,
			final Pos pos,
			final Priority priority,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		StackPane stackPane = null;
		if (node != null) {

			stackPane = LayoutControlsFactories.getInstance().createStackPane(pos, node);
			hBox.getChildren().add(stackPane);
			HBox.setHgrow(stackPane, priority);
			stackPane.setPadding(new Insets(top, right, bottom, left));
		}
		return stackPane;
	}

	@ApiMethod
	public static StackPane addToGridPane(
			final GridPane gridPane,
			final Node node,
			final int col,
			final int row,
			final int colSpan,
			final int rowSpan,
			final Pos pos,
			final Priority vGrowPriority,
			final Priority hGrowPriority,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		StackPane stackPane = null;
		if (node != null) {

			stackPane = LayoutControlsFactories.getInstance().createStackPane(pos, node);
			gridPane.add(stackPane, col, row, colSpan, rowSpan);
			GridPane.setVgrow(stackPane, vGrowPriority);
			GridPane.setHgrow(stackPane, hGrowPriority);
			stackPane.setPadding(new Insets(top, right, bottom, left));
		}
		return stackPane;
	}

	@ApiMethod
	public static void addToFlowPane(
			final FlowPane flowPane,
			final Node node,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		if (node != null) {

			flowPane.getChildren().add(node);
			FlowPane.setMargin(node, new Insets(top, right, bottom, left));
		}
	}

	@ApiMethod
	public static void addToTilePane(
			final TilePane tilePane,
			final Node node,
			final Pos pos,
			final double top,
			final double right,
			final double bottom,
			final double left) {

		if (node != null) {

			tilePane.getChildren().add(node);
			TilePane.setAlignment(node, pos);
			TilePane.setMargin(node, new Insets(top, right, bottom, left));
		}
	}

	@ApiMethod
	public static boolean isDoubleClick(
			final MouseEvent mouseEvent) {
		return isLeftClick(mouseEvent) && mouseEvent.getClickCount() == 2;
	}

	@ApiMethod
	public static boolean isLeftClick(
			final MouseEvent mouseEvent) {
		return mouseEvent.getButton() == MouseButton.PRIMARY;
	}

	@ApiMethod
	public static boolean isRightClick(
			final MouseEvent mouseEvent) {
		return mouseEvent.getButton() == MouseButton.SECONDARY;
	}

	@ApiMethod
	public static void run(
			final Runnable runnable) {

		try {
			if (!Platform.isFxApplicationThread()) {
				try {
					Platform.runLater(runnable);

				} catch (final Throwable throwable) {
					final Throwable cause = throwable.getCause();
					if (cause instanceof final Exception causeException) {
						throw causeException;
					} else {
						throw throwable;
					}
				}

			} else {
				runnable.run();
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to execute task in GUI thread");
			Logger.printThrowable(throwable);
		}
	}

	@ApiMethod
	public static void runAndWait(
			final Runnable runnable) {

		try {
			final FutureTask<?> futureTask = new FutureTask<>(runnable, null);
			final boolean fxApplicationThread = Platform.isFxApplicationThread();
			if (fxApplicationThread) {
				futureTask.run();

			} else {
				try {
					Platform.runLater(futureTask);
					futureTask.get();

				} catch (final Throwable throwable) {
					final Throwable cause = throwable.getCause();
					if (cause instanceof final Exception causeException) {
						throw causeException;
					} else {
						throw throwable;
					}
				}
			}

		} catch (final Throwable throwable) {
			Logger.printError("failed to execute task in GUI thread");
			Logger.printThrowable(throwable);
		}
	}

	@ApiMethod
	public static List<Node> getAllNodes(
			final Parent root) {

		final List<Node> nodes = new ArrayList<>();
		addAllDescendants(root, nodes);
		return nodes;
	}

	@ApiMethod
	public static void addAllDescendants(
			final Parent parent,
			final List<Node> nodes) {

		final List<Node> childrenUnmodifiable = parent.getChildrenUnmodifiable();
		for (final Node node : childrenUnmodifiable) {

			nodes.add(node);
			if (node instanceof final Parent childParent) {
				addAllDescendants(childParent, nodes);
			}
		}
	}
}
