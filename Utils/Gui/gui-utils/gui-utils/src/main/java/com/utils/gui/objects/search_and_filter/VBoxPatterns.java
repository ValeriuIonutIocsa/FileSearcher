package com.utils.gui.objects.search_and_filter;

import java.util.ArrayList;
import java.util.List;

import com.utils.gui.AbstractCustomControl;
import com.utils.gui.GuiUtils;
import com.utils.gui.alerts.CustomAlertWarning;
import com.utils.gui.factories.BasicControlsFactories;
import com.utils.gui.factories.LayoutControlsFactories;
import com.utils.string.regex.custom_patterns.CustomPatterns;
import com.utils.string.regex.custom_patterns.patterns.CustomPattern;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class VBoxPatterns extends AbstractCustomControl<VBox> {

	private final EventHandler<ActionEvent> textFieldEventHandler;

	private final List<List<HBoxPattern>> andHBoxPatternListList;
	private final HBoxRegexType hBoxRegexType;

	public VBoxPatterns(
			final EventHandler<ActionEvent> textFieldEventHandler) {

		this.textFieldEventHandler = textFieldEventHandler;

		andHBoxPatternListList = new ArrayList<>();
		hBoxRegexType = new HBoxRegexType();
	}

	@Override
	protected VBox createRoot() {

		final VBox vBoxRoot = LayoutControlsFactories.getInstance().createVBox();
		vBoxRoot.getStylesheets().add("com/utils/gui/objects/search_and_filter/patterns.css");

		final GridPane gridPanePatternTable = createGridPanePatternTable();
		final ScrollPane scrollPaneSearchPatterns =
				LayoutControlsFactories.getInstance().createScrollPane(gridPanePatternTable);
		GuiUtils.addToVBox(vBoxRoot, scrollPaneSearchPatterns,
				Pos.CENTER_LEFT, Priority.ALWAYS, 0, 0, 0, 0);

		GuiUtils.addToVBox(vBoxRoot, hBoxRegexType.getRoot(),
				Pos.CENTER_LEFT, Priority.NEVER, 0, 0, 0, 4);

		return vBoxRoot;
	}

	private GridPane createGridPanePatternTable() {

		final GridPane gridPanePatternTable = new GridPane();
		gridPanePatternTable.setPadding(new Insets(3));

		createHBoxPatternsRow(gridPanePatternTable, 0, true, false);

		createHBoxPatternsRow(gridPanePatternTable, 2, false, false);

		createHBoxPatternsRow(gridPanePatternTable, 4, false, true);

		return gridPanePatternTable;
	}

	private void createHBoxPatternsRow(
			final GridPane gridPanePatternTable,
			final int row,
			final boolean firstRow,
			final boolean lastRow) {

		final List<HBoxPattern> andHBoxPatternList = new ArrayList<>();
		andHBoxPatternListList.add(andHBoxPatternList);

		createHBoxPattern(firstRow, gridPanePatternTable, 0, row, andHBoxPatternList);
		gridPanePatternTable.add(createLabelOperation(FilterType.AND), 1, row);
		createHBoxPattern(false, gridPanePatternTable, 2, row, andHBoxPatternList);
		gridPanePatternTable.add(createLabelOperation(FilterType.AND), 3, row);
		createHBoxPattern(false, gridPanePatternTable, 4, row, andHBoxPatternList);

		if (!lastRow) {

			final int rowPlusOne = row + 1;
			gridPanePatternTable.add(createLabelOperation(FilterType.OR), 0, rowPlusOne);
			gridPanePatternTable.add(createLabelOperation(FilterType.OR), 2, rowPlusOne);
			gridPanePatternTable.add(createLabelOperation(FilterType.OR), 4, rowPlusOne);
		}
	}

	private void createHBoxPattern(
			final boolean focus,
			final GridPane gridPanePatternTable,
			final int col,
			final int row,
			final List<HBoxPattern> hBoxPatternsRow) {

		final HBoxPattern hBoxPattern = new HBoxPattern(focus, textFieldEventHandler);
		GuiUtils.addToGridPane(gridPanePatternTable, hBoxPattern.getRoot(), col, row, 1, 1,
				Pos.CENTER_LEFT, Priority.NEVER, Priority.ALWAYS, 3, 3, 3, 3);
		hBoxPatternsRow.add(hBoxPattern);
	}

	private static StackPane createLabelOperation(
			final FilterType filterType) {

		final Label labelOperation =
				BasicControlsFactories.getInstance().createLabel(filterType.name(), "label-operation");
		final StackPane stackPaneLabelOperation =
				LayoutControlsFactories.getInstance().createStackPane(Pos.CENTER, labelOperation);
		if (filterType == FilterType.AND) {
			GridPane.setMargin(stackPaneLabelOperation, new Insets(3, 7, 3, 7));
		} else {
			GridPane.setMargin(stackPaneLabelOperation, new Insets(3));
		}
		return stackPaneLabelOperation;
	}

	public CustomPatterns createCustomPatterns() {

		CustomPatterns customPatterns = null;
		final int patternTypeIndex = hBoxRegexType.getPatternType();
		if (patternTypeIndex < 0) {
			new CustomAlertWarning("No pattern type!",
					"In order to use patterns, you have to choose a pattern type" +
							" from the three pattern types above!").showAndWait();

		} else {
			final List<List<CustomPattern>> andPatternListList = new ArrayList<>();
			for (final List<HBoxPattern> andHBoxPatternList : andHBoxPatternListList) {

				final List<CustomPattern> andPatternList = new ArrayList<>();
				for (final HBoxPattern hBoxPattern : andHBoxPatternList) {

					final CustomPattern pattern = hBoxPattern.createPattern(patternTypeIndex);
					if (pattern != null) {
						andPatternList.add(pattern);
					}
				}
				if (!andPatternList.isEmpty()) {
					andPatternListList.add(andPatternList);
				}
			}
			customPatterns = new CustomPatterns(andPatternListList);
		}
		return customPatterns;
	}
}
