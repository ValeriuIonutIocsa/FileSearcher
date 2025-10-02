package com.utils.gui.objects.messages;

import com.utils.data_types.table.messages.TableRowDataMessage;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.help.StackPaneHelp;
import com.utils.gui.icons.ImagesGuiUtils;
import com.utils.gui.objects.tables.tree_table.CustomTreeTableCell;
import com.utils.log.messages.MessageType;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

class CustomTreeTableCellMessages extends CustomTreeTableCell<TableRowDataMessage, Object> {

	private final UserActionRequiredHelper userActionRequiredHelper;
	private String text;

	private StackPane stackPane;

	CustomTreeTableCellMessages(
			final UserActionRequiredHelper userActionRequiredHelper) {

		this.userActionRequiredHelper = userActionRequiredHelper;
	}

	@Override
	public void startEdit() {

		super.startEdit();

		final TableRowDataMessage tableRowDataMessage = getRowData();
		final boolean notCategory = tableRowDataMessage != null && !tableRowDataMessage.isCategory();
		if (notCategory) {

			final HBox hBoxTextArea = LayoutControlsFactories.getInstance().createHBox();

			final Rectangle statusRectangle = createStatusRectangle(tableRowDataMessage);
			GuiUtils.addToHBox(hBoxTextArea, statusRectangle,
					Pos.CENTER_LEFT, Priority.NEVER, 0, 5, 0, 0);

			final TextArea textArea = BasicControlsFactories.getInstance().createTextArea(text);
			textArea.setEditable(false);
			final double cellHeight = getHeight();
			textArea.setPrefHeight(cellHeight);
			final double cellWidth = getWidth();
			textArea.setPrefWidth(cellWidth);
			GuiUtils.addToHBox(hBoxTextArea, textArea,
					Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

			setGraphic(hBoxTextArea);
		}
	}

	@Override
	public void cancelEdit() {

		super.cancelEdit();

		final TableRowDataMessage tableRowDataMessage = getRowData();
		final boolean notCategory = tableRowDataMessage != null && !tableRowDataMessage.isCategory();
		if (notCategory) {
			setGraphic(stackPane);
		}
	}

	@Override
	public void setText(
			final StackPane stackPane,
			final Object item) {

		text = item.toString();
		this.stackPane = stackPane;

		final TableRowDataMessage tableRowDataMessage = getRowData();
		final boolean category = tableRowDataMessage != null && tableRowDataMessage.isCategory();
		if (category) {

			final MessageType messageType = tableRowDataMessage.getType();
			if (messageType == MessageType.APPROVAL || messageType == MessageType.INFO) {
				createUserActionRequiredControl(stackPane, tableRowDataMessage, item, null);

			} else if (messageType == MessageType.WARNING) {
				createUserActionRequiredControl(stackPane, tableRowDataMessage, item,
						"start thinking about possible optimizations");

			} else if (messageType == MessageType.CRITICAL) {
				createUserActionRequiredControl(stackPane, tableRowDataMessage, item,
						"immediate action required");
			}

		} else {
			final Label label = new Label(text);

			final Tooltip tooltip = BasicControlsFactories.getInstance().createTooltip(text);
			setTooltip(tooltip);

			final Rectangle statusRectangle = createStatusRectangle(tableRowDataMessage);
			label.setGraphic(statusRectangle);

			final Pos textAlignment = getTextAlignmentValue();
			GuiUtils.addToStackPane(stackPane, label, textAlignment, 1, 1, 1, 1);
		}
	}

	private void createUserActionRequiredControl(
			final StackPane stackPane,
			final TableRowDataMessage tableRowDataMessage,
			final Object item,
			final String userActionRequiredText) {

		final HBox hBoxUserActionRequired = LayoutControlsFactories.getInstance().createHBox();

		final String messageCategory = item.toString();
		final Label label = BasicControlsFactories.getInstance()
				.createLabel(messageCategory, "boldFontSize9");

		final Tooltip tooltip = BasicControlsFactories.getInstance().createTooltip(messageCategory);
		setTooltip(tooltip);

		final Rectangle statusRectangle = createStatusRectangle(tableRowDataMessage);
		label.setGraphic(statusRectangle);
		GuiUtils.addToHBox(hBoxUserActionRequired, label,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		final ImageView imageViewArrow = new ImageView(ImagesGuiUtils.IMAGE_LONG_ARROW);
		GuiUtils.addToHBox(hBoxUserActionRequired, imageViewArrow,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);
		if (userActionRequiredText == null) {
			imageViewArrow.setVisible(false);
		}

		final Label labelUserActionRequired = BasicControlsFactories.getInstance()
				.createLabel(userActionRequiredText, "boldFontSize9");
		GuiUtils.addToHBox(hBoxUserActionRequired, labelUserActionRequired,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 7);
		if (userActionRequiredText == null) {
			labelUserActionRequired.setVisible(false);
		}

		final StackPaneHelp stackPaneHelp = new StackPaneHelp(mouseEvent -> {
			if (GuiUtils.isLeftClick(mouseEvent)) {
				userActionRequiredHelper.displayHelp(messageCategory);
			}
			mouseEvent.consume();
			getTreeTableView().getSelectionModel().clearSelection();
		});
		if (userActionRequiredText == null) {
			stackPaneHelp.getRoot().setVisible(false);
		}
		GuiUtils.addToHBox(hBoxUserActionRequired, stackPaneHelp.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 3);

		final Pos textAlignmentValue = getTextAlignmentValue();
		GuiUtils.addToStackPane(stackPane, hBoxUserActionRequired, textAlignmentValue, 3, 1, 1, 1);
	}

	private static Rectangle createStatusRectangle(
			final TableRowDataMessage tableRowDataMessage) {

		Rectangle statusRectangle = null;
		if (tableRowDataMessage != null) {

			final MessageType type = tableRowDataMessage.getType();
			if (type == MessageType.APPROVAL) {
				statusRectangle = SplitPaneMessages.createStatusRectangleOK(15);
			} else if (type == MessageType.CRITICAL) {
				statusRectangle = SplitPaneMessages.createStatusRectangleCritical(15);
			} else if (type == MessageType.WARNING) {
				statusRectangle = SplitPaneMessages.createStatusRectangleWarning(15);
			} else if (type == MessageType.INFO) {
				statusRectangle = SplitPaneMessages.createStatusRectangleInfo(15);
			}
		}
		return statusRectangle;
	}
}
