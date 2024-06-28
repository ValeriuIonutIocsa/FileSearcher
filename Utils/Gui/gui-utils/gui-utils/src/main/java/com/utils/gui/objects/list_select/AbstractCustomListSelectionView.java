package com.utils.gui.objects.list_select;

import java.util.List;

import com.utils.annotations.ApiMethod;
import com.utils.data_types.table.TableColumnData;
import com.utils.data_types.table.TableRowData;
import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.tables.table_view.CustomTableView;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

abstract class AbstractCustomListSelectionView<
		TableRowDataTLeft extends TableRowData,
		TableRowDataTRight extends TableRowData>
		extends AbstractCustomControl<GridPane> {

	private final double leftSizeRatio;

	private final CustomTableView<TableRowDataTLeft> customTableViewLeft;
	private final CustomTableView<TableRowDataTRight> customTableViewRight;
	private final Button buttonMoveToRight;
	private final Button buttonMoveAllToRight;
	private final Button buttonMoveToLeft;
	private final Button buttonMoveAllToLeft;

	AbstractCustomListSelectionView(
			final double leftSizeRatio,
			final List<TableRowDataTLeft> leftItemList,
			final List<TableRowDataTRight> rightItemList) {

		this.leftSizeRatio = leftSizeRatio;

		customTableViewLeft = createCustomTableViewLeft(leftItemList);
		customTableViewRight = createCustomTableViewRight(rightItemList);
		buttonMoveToRight = createButton(">", event -> moveToRight());
		buttonMoveAllToRight = createButton(">>", event -> moveAllToRight());
		buttonMoveToLeft = createButton("<", event -> moveToLeft());
		buttonMoveAllToLeft = createButton("<<", event -> moveAllToLeft());
		updateButtons();
	}

	protected CustomTableView<TableRowDataTLeft> createCustomTableViewLeft(
			final List<TableRowDataTLeft> leftTableRowDataList) {

		final TableColumnData[] tableColumnDataArrayLeft = getTableColumnDataArrayLeft();
		final CustomTableView<TableRowDataTLeft> customTableViewLeft =
				new CustomTableView<>(tableColumnDataArrayLeft, false, true, true, false, true, 0);

		customTableViewLeft.getSelectionModel().getSelectedCells()
				.addListener(new ListChangeListenerListSelectionView<>(this));

		customTableViewLeft.setItems(leftTableRowDataList);
		return customTableViewLeft;
	}

	protected abstract TableColumnData[] getTableColumnDataArrayLeft();

	protected CustomTableView<TableRowDataTRight> createCustomTableViewRight(
			final List<TableRowDataTRight> rightTableRowDataList) {

		final TableColumnData[] tableColumnDataArrayRight = getTableColumnDataArrayRight();
		final CustomTableView<TableRowDataTRight> customTableViewRight =
				new CustomTableView<>(tableColumnDataArrayRight, false, true, true, false, true, 0);

		customTableViewRight.getSelectionModel().getSelectedCells()
				.addListener(new ListChangeListenerListSelectionView<>(this));

		customTableViewRight.setItems(rightTableRowDataList);
		return customTableViewRight;
	}

	protected abstract TableColumnData[] getTableColumnDataArrayRight();

	private static Button createButton(
			final String text,
			final EventHandler<ActionEvent> eventHandler) {

		final Button button = BasicControlsFactories.getInstance().createButton(text);
		button.setMaxWidth(Double.MAX_VALUE);
		button.setOnAction(eventHandler);
		button.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 12));
		return button;
	}

	@Override
	protected GridPane createRoot() {

		final GridPane gridPaneRoot = LayoutControlsFactories.getInstance().createGridPane();

		gridPaneRoot.getColumnConstraints().add(
				LayoutControlsFactories.getInstance().createPercentageColumnConstraints(leftSizeRatio));
		gridPaneRoot.getColumnConstraints().add(
				LayoutControlsFactories.getInstance().createPercentageColumnConstraints(1 - leftSizeRatio));

		final VBox vBoxLeft = createVBoxLeft();
		GuiUtils.addToGridPane(gridPaneRoot, vBoxLeft, 0, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.ALWAYS, Priority.NEVER, 0, 0, 0, 0);

		final VBox vBoxRight = createVBoxRight();
		GuiUtils.addToGridPane(gridPaneRoot, vBoxRight, 1, 0, 1, 1,
				Pos.CENTER_LEFT, Priority.ALWAYS, Priority.NEVER, 0, 0, 0, 0);

		return gridPaneRoot;
	}

	private VBox createVBoxLeft() {

		final VBox vBoxLeft = LayoutControlsFactories.getInstance().createVBox();

		addLeftHeader(vBoxLeft);

		final HBox hBoxLeft = createHBoxLeft();
		GuiUtils.addToVBox(vBoxLeft, hBoxLeft,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxLeft;
	}

	/**
	 * @param vBoxLeft
	 *            vBoxLeft
	 */
	@ApiMethod
	protected void addLeftHeader(
			final VBox vBoxLeft) {
	}

	private HBox createHBoxLeft() {

		final HBox hBoxLeft = LayoutControlsFactories.getInstance().createHBox();

		GuiUtils.addToHBox(hBoxLeft, customTableViewLeft,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		final VBox vBoxButtons = createVBoxButtons();
		GuiUtils.addToHBox(hBoxLeft, vBoxButtons,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxLeft;
	}

	private VBox createVBoxRight() {

		final VBox vBoxRight = LayoutControlsFactories.getInstance().createVBox();

		addRightHeader(vBoxRight);

		GuiUtils.addToVBox(vBoxRight, customTableViewRight,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		return vBoxRight;
	}

	/**
	 * @param vBoxRight
	 *            vBoxRight
	 */
	@ApiMethod
	protected void addRightHeader(
			final VBox vBoxRight) {
	}

	private VBox createVBoxButtons() {

		final VBox vBoxButtons = LayoutControlsFactories.getInstance().createVBox();

		GuiUtils.addToVBox(vBoxButtons, buttonMoveToRight,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveAllToRight,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveToLeft,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		GuiUtils.addToVBox(vBoxButtons, buttonMoveAllToLeft,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 7, 0);

		return vBoxButtons;
	}

	abstract void moveToRight();

	abstract void moveAllToRight();

	abstract void moveToLeft();

	abstract void moveAllToLeft();

	void updateButtons() {

		buttonMoveToRight.setDisable(customTableViewLeft.getSelectionModel().getSelectedIndices().isEmpty());
		buttonMoveAllToRight.setDisable(customTableViewLeft.getItems().isEmpty());
		buttonMoveToLeft.setDisable(customTableViewRight.getSelectionModel().getSelectedIndices().isEmpty());
		buttonMoveAllToLeft.setDisable(customTableViewRight.getItems().isEmpty());
		getRoot().requestFocus();
	}

	@ApiMethod
	public CustomTableView<TableRowDataTLeft> getCustomTableViewLeft() {
		return customTableViewLeft;
	}

	@ApiMethod
	public CustomTableView<TableRowDataTRight> getCustomTableViewRight() {
		return customTableViewRight;
	}

	Button getButtonMoveToRight() {
		return buttonMoveToRight;
	}

	Button getButtonMoveAllToRight() {
		return buttonMoveAllToRight;
	}

	Button getButtonMoveToLeft() {
		return buttonMoveToLeft;
	}

	Button getButtonMoveAllToLeft() {
		return buttonMoveAllToLeft;
	}
}
