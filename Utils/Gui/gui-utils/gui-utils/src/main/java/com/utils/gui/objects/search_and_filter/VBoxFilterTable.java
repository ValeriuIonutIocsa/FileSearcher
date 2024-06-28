package com.utils.gui.objects.search_and_filter;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.gui.objects.HBoxWindowButtons;
import com.utils.string.regex.custom_patterns.CustomPatterns;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VBoxFilterTable extends AbstractCustomControl<VBox> {

	private final SearchAndFilterTable searchAndFilterTable;

	private final ComboBox<String> comboBoxFilterColumn;
	private final VBoxPatterns vBoxPatterns;
	private final ComboBox<FilterType> comboBoxFilterType;

	public VBoxFilterTable(
			final SearchAndFilterTable searchAndFilterTable,
			final int columnIndex) {

		this.searchAndFilterTable = searchAndFilterTable;

		comboBoxFilterColumn = FactoryComboBoxColumn.newInstance(searchAndFilterTable, columnIndex);
		vBoxPatterns = new VBoxPatterns(event -> filter());
		comboBoxFilterType = createComboBoxFilterType();
	}

	private static ComboBox<FilterType> createComboBoxFilterType() {

		final ComboBox<FilterType> comboBoxFilterType = BasicControlsFactories.getInstance().createComboBox();
		comboBoxFilterType.getItems().addAll(FilterType.values());
		comboBoxFilterType.getSelectionModel().selectFirst();
		return comboBoxFilterType;
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactories.getInstance().createVBox(false);

		final HBox hBoxFilterColumn = createHBoxFilterColumn();
		GuiUtils.addToVBox(vBoxRoot, hBoxFilterColumn,
				Pos.CENTER_LEFT, Priority.NEVER, 12, 7, 7, 11);

		final Label labelFilterPatterns =
				BasicControlsFactories.getInstance().createLabel("filter patterns:", "bold");
		GuiUtils.addToVBox(vBoxRoot, labelFilterPatterns,
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 11);

		GuiUtils.addToVBox(vBoxRoot, vBoxPatterns.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 0, 7);

		final HBox hBoxFilterType = createHBoxFilterType();
		GuiUtils.addToVBox(vBoxRoot, hBoxFilterType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 7, 11);

		final HBoxWindowButtons hBoxWindowButtons = new HBoxWindowButtons("Filter", this::filter);
		GuiUtils.addToVBox(vBoxRoot, hBoxWindowButtons.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 7, 7, 15, 0);

		return vBoxRoot;
	}

	private HBox createHBoxFilterColumn() {

		final HBox hBoxFilterColumn = LayoutControlsFactories.getInstance().createHBox();

		final Label labelSearchColumn =
				BasicControlsFactories.getInstance().createLabel("filter column:", "bold");
		GuiUtils.addToHBox(hBoxFilterColumn, labelSearchColumn,
				Pos.CENTER, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxFilterColumn, comboBoxFilterColumn,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 14);

		return hBoxFilterColumn;
	}

	private HBox createHBoxFilterType() {

		final HBox hBoxFilterType = LayoutControlsFactories.getInstance().createHBox();

		final Label labelDescription = BasicControlsFactories.getInstance().createLabel("logical operator " +
				"to apply between current filter and previously applied filters:", "bold");
		GuiUtils.addToHBox(hBoxFilterType, labelDescription,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 0);

		GuiUtils.addToHBox(hBoxFilterType, comboBoxFilterType,
				Pos.CENTER_LEFT, Priority.NEVER, 0, 7, 0, 7);

		return hBoxFilterType;
	}

	private void filter() {

		final int filterColumnIndex =
				comboBoxFilterColumn.getSelectionModel().getSelectedIndex();

		final CustomPatterns customPatterns = vBoxPatterns.createCustomPatterns();
		if (customPatterns != null) {

			if (customPatterns.checkEmptyPatterns()) {
				new CustomAlertWarning("empty filter patterns",
						"All the search patterns above are empty." +
								" It is redundant to perform a search.").showAndWait();

			} else {
				final FilterType filterType = comboBoxFilterType.getSelectionModel().getSelectedItem();
				searchAndFilterTable.filterTable(filterType, filterColumnIndex, customPatterns);

				getRoot().getScene().getWindow().hide();
			}
		}
	}
}
