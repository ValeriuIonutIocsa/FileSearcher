package com.utils.gui.objects.messages;

import java.util.ArrayList;
import java.util.List;

import com.utils.data_types.table.messages.MessagesTreeTableUtils;
import com.utils.data_types.table.messages.TableRowDataMessage;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.split_pane.CustomSplitPane;
import com.utils.gui.objects.tables.tree_table.CustomTreeTableView;
import com.utils.gui.objects.tables.tree_table.UnfilteredTreeItem;
import com.utils.log.messages.MessageType;
import com.utils.log.messages.Messages;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SplitPaneMessages extends AbstractCustomControl<SplitPane> {

	private final UserActionRequiredHelper userActionRequiredHelper;

	private final Label labelCriticalCount;
	private final Label labelWarningCount;
	private final Label labelInfoCount;
	private final CustomTreeTableView<TableRowDataMessage> customTreeTableView;

	public SplitPaneMessages(
			final UserActionRequiredHelper userActionRequiredHelper) {

		this.userActionRequiredHelper = userActionRequiredHelper;

		labelCriticalCount = BasicControlsFactories.getInstance().createLabel("", "bold");
		labelWarningCount = BasicControlsFactories.getInstance().createLabel("", "bold");
		labelInfoCount = BasicControlsFactories.getInstance().createLabel("", "bold");
		customTreeTableView = createCustomTreeTableView();
	}

	public void setTopControl(
			final Parent topControl) {

		final SplitPane splitPaneRoot = getRoot();
		splitPaneRoot.getItems().removeFirst();
		splitPaneRoot.getItems().addFirst(topControl);
		splitPaneRoot.setDividerPositions(0.7);
	}

	private CustomTreeTableView<TableRowDataMessage> createCustomTreeTableView() {

		final CustomTreeTableView<TableRowDataMessage> customTreeTableView =
				new CustomTreeTableView<>(TableRowDataMessage.DATA_INFO.columnsTable(),
						true, true, true, true, 0);
		customTreeTableView.setId("tree-table-view-messages");

		customTreeTableView.getColumnByName(TableRowDataMessage.MESSAGES_COLUMN_NAME)
				.setCellFactory(param -> new CustomTreeTableCellMessages(userActionRequiredHelper));

		return customTreeTableView;
	}

	@Override
	protected SplitPane createRoot() {

		final SplitPane splitPaneRoot = new CustomSplitPane(Orientation.VERTICAL);
		splitPaneRoot.getStylesheets().add("com/utils/gui/objects/messages/split_pane_messages.css");
		splitPaneRoot.setBackground(Background.EMPTY);
		splitPaneRoot.setPadding(new Insets(0));

		splitPaneRoot.getItems().add(new Region());

		final VBox vBoxBottom = createVBoxBottom();
		splitPaneRoot.getItems().add(vBoxBottom);

		splitPaneRoot.setDividerPositions(0.7);

		return splitPaneRoot;
	}

	private VBox createVBoxBottom() {

		final VBox vBoxBottom = LayoutControlsFactories.getInstance().createVBox();

		final HBox hBoxTop = createHBoxTop();
		GuiUtils.addToVBox(vBoxBottom, hBoxTop,
				Pos.CENTER_LEFT, Priority.NEVER, 5, 0, 5, 0);

		GuiUtils.addToVBox(vBoxBottom, customTreeTableView,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxBottom;
	}

	private HBox createHBoxTop() {

		final HBox hBoxTop = LayoutControlsFactories.getInstance().createHBox();

		GuiUtils.addToHBox(hBoxTop, labelCriticalCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxTop, labelWarningCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		GuiUtils.addToHBox(hBoxTop, labelInfoCount,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxTop;
	}

	public void setData(
			final Messages messages) {

		final int criticalCount = messages.computeMessageCount(MessageType.CRITICAL);
		labelCriticalCount.setText(criticalCount + " critical");
		if (criticalCount > 0) {
			final Rectangle statusRectangleCritical = createStatusRectangleCritical(15);
			labelCriticalCount.setGraphic(statusRectangleCritical);
		} else {
			labelCriticalCount.setGraphic(null);
		}

		final int warningCount = messages.computeMessageCount(MessageType.WARNING);
		labelWarningCount.setText(warningCount + " warning");
		if (warningCount > 0) {
			final Rectangle statusRectangleWarning = createStatusRectangleWarning(15);
			labelWarningCount.setGraphic(statusRectangleWarning);
		} else {
			labelWarningCount.setGraphic(null);
		}

		final int infoCount = messages.computeMessageCount(MessageType.INFO);
		labelInfoCount.setText(infoCount + " info");
		if (infoCount > 0) {
			final Rectangle statusRectangleInfo = createStatusRectangleInfo(15);
			labelInfoCount.setGraphic(statusRectangleInfo);
		} else {
			labelInfoCount.setGraphic(null);
		}

		final UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItemRoot =
				customTreeTableView.getUnfilteredTreeItemRoot();
		unfilteredTreeItemRoot.getChildrenList().clear();

		final List<TableRowDataMessage> tableRowDataMessageList = new ArrayList<>();
		MessagesTreeTableUtils.fillTableRowDataMessageList(messages, tableRowDataMessageList);
		fillTreeItemsRec(unfilteredTreeItemRoot, tableRowDataMessageList);

		customTreeTableView.setFilteredItems();
	}

	private static void fillTreeItemsRec(
			final UnfilteredTreeItem<TableRowDataMessage> parentUnfilteredTreeItem,
			final List<TableRowDataMessage> tableRowDataMessageList) {

		for (final TableRowDataMessage tableRowDataMessage : tableRowDataMessageList) {

			final UnfilteredTreeItem<TableRowDataMessage> unfilteredTreeItem =
					new UnfilteredTreeItem<>(tableRowDataMessage, true);
			parentUnfilteredTreeItem.getChildrenList().add(unfilteredTreeItem);

			final List<TableRowDataMessage> childTableRowDataMessageList =
					tableRowDataMessage.getChildTableRowDataMessageList();
			fillTreeItemsRec(unfilteredTreeItem, childTableRowDataMessageList);
		}
	}

	public static Rectangle createStatusRectangleOK(
			final int squareSideLength) {
		return createStatusRectangle(Color.LIGHTGREEN, squareSideLength);
	}

	public static Rectangle createStatusRectangleInfo(
			final int squareSideLength) {
		return createStatusRectangle(Color.LIGHTBLUE, squareSideLength);
	}

	public static Rectangle createStatusRectangleWarning(
			final int squareSideLength) {
		return createStatusRectangle(Color.YELLOW, squareSideLength);
	}

	public static Rectangle createStatusRectangleCritical(
			final int squareSideLength) {
		return createStatusRectangle(Color.RED, squareSideLength);
	}

	private static Rectangle createStatusRectangle(
			final Color color,
			final int squareSideLength) {

		final Rectangle rectangle = new Rectangle(squareSideLength, squareSideLength);
		rectangle.setFill(color);
		rectangle.setStroke(Color.BLACK);
		return rectangle;
	}
}
